package tfsapps.barragefighter;

/**
 * Created by FURUKAWA on 2017/11/03.
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;

public class City extends BaseObject {

    private static final int CITY_HEIGHT = 50;

    private final Paint paint = new Paint();

    public final Rect rect;

    public City(int width, int height) {

//        paint.setColor(Color.LTGRAY);

        this.setHitpoint(1);

        // 画面の下端全域
        int left = 0;
        int top = height - CITY_HEIGHT;
        int right = width;
        int bottom = height;
        rect = new Rect(left, top, right, bottom);

        yPosition = rect.centerY();
        xPosition = rect.centerX();
    }

    @Override
    public void draw(Canvas canvas) {
        if (status == STATUS_NORMAL) {
            canvas.drawRect(rect, paint);
        }
    }

    @Override
    public boolean isAvailable(int width, int height) {
        return true;
    }

    @Override
    public void move(float x, float y) {
    }

    @Override
    public Type getType() {
        return Type.City;
    }

    @Override
    public boolean isHit(BaseObject object, Bitmap bmp) {
        if (object.getType() != Type.Missile && object.getType() != Type.Boss) {
            return false;
        }

        return rect.contains(Math.round(object.xPosition), Math.round(object.yPosition));
    }
}