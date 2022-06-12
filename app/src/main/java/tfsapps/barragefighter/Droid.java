package tfsapps.barragefighter;

/**
 * Created by FURUKAWA on 2017/11/03.
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;

public class Droid extends BaseObject {

    private Paint paint = new Paint();

    public final Bitmap bitmap;
    //    public final Rect rect;
    public Rect rect;
    final int move_weight = 10;
    public int horizon = 0;
    public int vertical = 0;
    public int mv_count = 0;
    public int bullet_num = 1;  /* 弾丸の数 */
    public int _left = 0;
    public int _top = 0;
    public int _right = 0;
    public int _bottom = 0;

    public Droid(Bitmap bitmap, int width, int height, int num) {
        this.bitmap = bitmap;

        this.setHitpoint(1);
        bullet_num = num;

        // 画面の下端中央の位置
/*        int left = (width - bitmap.getWidth()) / 2;
        int top = height - bitmap.getHeight();
        int right = left + bitmap.getWidth();
        int bottom = top + bitmap.getHeight();*/
        _left = (width - bitmap.getWidth()) / 2;
        _top = height - bitmap.getHeight() - 30;
        _right = _left + bitmap.getWidth();
        _bottom = _top + bitmap.getHeight() + 30;

        rect = new Rect(_left, _top, _right, _bottom);

        yPosition = rect.centerY();
        xPosition = rect.centerX();
//        Log.v(">>>>droid", "x=" + xPosition + ", y="+ yPosition + ", reX" + rect.centerX() + ", reY" + rect.centerY() + ", " + left + top + right + bottom);

    }

    @Override
    public void draw(Canvas canvas) {
        if (status == STATUS_NORMAL) {
            if (horizon == -1){
                _left -= move_weight;
                _right -= move_weight;
                mv_count++;
            }
            else if(horizon == 1) {
                _left += move_weight;
                _right += move_weight;
                mv_count++;
            }
            rect = new Rect(_left, _top, _right, _bottom);
            yPosition = rect.centerY();
            xPosition = rect.centerX();

            canvas.drawBitmap(bitmap, rect.left, rect.top, paint);

            if (mv_count >= 4)
            {
                horizon = 0;
                mv_count = 0;
//              yPosition = rect.centerY();
//              xPosition = rect.centerX();
            }
        }
    }

    @Override
    public boolean isAvailable(int width, int height) {
        return true;
    }

    @Override
    public void move(float x, float y) {
        float mx = x - xPosition;
        float my = y - yPosition;

        if (mv_count > 0)   return;

        if (mx <= -20) {
            horizon = -1;
        }
        else if (mx >= 20){
            horizon = 1;
        }
    }

    @Override
    public Type getType() {
        return Type.Droid;
    }

    @Override
    public boolean isHit(BaseObject object, Bitmap bmp) {
        if (object.getType() != Type.Missile && object.getType() != Type.Boss) {
            return false;
        }
        return calcDistance(this, object, this.bitmap, bmp);
//        return rect.contains(Math.round(object.xPosition), Math.round(object.yPosition));
    }
}