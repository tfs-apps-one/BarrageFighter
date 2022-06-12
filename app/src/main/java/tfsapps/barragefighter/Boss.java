package tfsapps.barragefighter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by FURUKAWA on 2017/11/03.
 */

public class Boss extends BaseObject {

    private Paint paint = new Paint();

    private static final float SIZE = 256f;
    public Rect rect;
    public final float alignX;
    public final Bitmap bitmap;
    private MediaPlayer bgm;
    public int _left = 0;
    public int _top = 0;
    public int _right = 0;
    public int _bottom = 0;
    public int boss_speed = 0;

    public Boss(Bitmap bitmap, int fromX, float alignX, int fromY, int hp, int speed) {
        this.bitmap = bitmap;
        this.setHitpoint(hp);
        boss_speed = speed;
/*        _left = fromX;
        _top = fromY;
        _right = _left + bitmap.getWidth();
        _bottom = fromY + bitmap.getHeight();
        rect = new Rect(_left, _top, _right, _bottom);
        yPosition = rect.centerY();
        xPosition = rect.centerX();*/
        yPosition = fromY;
        xPosition = fromX;
        this.alignX = alignX;
    }

    @Override
    public void move(float x, float y) {
        yPosition += 1 * (MOVE_WEIGHT+boss_speed);
        _top += 1 * MOVE_WEIGHT;
        xPosition += alignX * MOVE_WEIGHT;
        _left += alignX * MOVE_WEIGHT;
    }

    @Override
    public Type getType() {
        return Type.Boss;
    }

    @Override
    public boolean isHit(BaseObject object, Bitmap bmp) {
        if (object.getType() == Type.Boss) {
            return false;
        }
        return (calcDistance(this, object, this.bitmap, bmp));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.bitmap, xPosition, yPosition, paint);
    }
}