package tfsapps.barragefighter;

/**
 * Created by FURUKAWA on 2017/11/03.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;

public class Missile extends BaseObject {

    private Paint paint = new Paint();

    private static final float SIZE = 50f;
//    private static final float SIZE = 90f;
//  private static final float SIZE = 10f;

    public final float alignX;
    public final Bitmap bitmap;
    private MediaPlayer bgm;
    public int enemy_speed = 0;

    public Missile(Bitmap bitmap, int fromX, float alignX, int fromY, int hp, int speed) {
        this.bitmap = bitmap;
        this.setHitpoint(hp);
        yPosition = fromY;
        xPosition = fromX;
        this.alignX = alignX;
        enemy_speed = speed;

        paint.setColor(Color.BLUE);
    }

    @Override
    public void move(float x, float y) {
        yPosition += 1 * (MOVE_WEIGHT + enemy_speed);
        xPosition += alignX * MOVE_WEIGHT;
    }

    @Override
    public Type getType() {
        return Type.Missile;
    }

    @Override
    public boolean isHit(BaseObject object, Bitmap bmp) {
        if (object.getType() == Type.Missile) {
            return false;
        }
        return calcDistance(this, object, this.bitmap, bmp);
//        return (calcDistance(this, object) < SIZE);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.bitmap, xPosition, yPosition, paint);
//        canvas.drawCircle(xPosition, yPosition, SIZE, paint);
    }
}