package tfsapps.barragefighter;

/**
 * Created by FURUKAWA on 2017/11/03.
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class Bullet extends BaseObject {

    private Paint paint = new Paint();
    private static final float SIZE = 50f;
//    private static final float SIZE = 90f;
    private int bullet_index = 0;
    private int bullet_speed = 0;

//    private static final float SIZE = 15f;

    public final float alignX;
    public final Bitmap bitmap;

    public Bullet(Bitmap bitmap, float alignX, Rect rect, int index, int hp, int speed) {
        this.bitmap = bitmap;
        this.alignX = alignX;
        this.setHitpoint(hp);
        yPosition = rect.centerY();
        xPosition = rect.centerX();
        bullet_index = index;
        bullet_speed = speed;
        switch (bullet_index){
            default:
            case 1:
                xPosition = xPosition-(this.bitmap.getWidth()/2);
                yPosition = yPosition-45;
                break;
            case 2:
                xPosition = xPosition-(this.bitmap.getWidth()/2)-70;
                yPosition = yPosition-30;
                break;
            case 3:
                xPosition = xPosition-(this.bitmap.getWidth()/2)+70;
                yPosition = yPosition-30;
                break;
            case 4:
                xPosition = xPosition-(this.bitmap.getWidth()/2)-140;
                yPosition = yPosition-15;
                break;
            case 5:
                xPosition = xPosition-(this.bitmap.getWidth()/2)+140;
                yPosition = yPosition-15;
                break;
            case 6:
                xPosition = xPosition-(this.bitmap.getWidth()/2)-210;
                break;
            case 7:
                xPosition = xPosition-(this.bitmap.getWidth()/2)+210;
                break;
        }
        paint.setColor(Color.RED);
    }

    @Override
    public void move(float x, float y) {
        yPosition -= 1 * (MOVE_WEIGHT+bullet_speed);
//        xPosition += 1 * MOVE_WEIGHT;
//        xPosition += alignX * MOVE_WEIGHT;
    }

    @Override
    public Type getType() {
        return Type.Bullet;
    }

    @Override
    public boolean isHit(BaseObject object, Bitmap bmp) {
//        double temp;
        if (object.getType() == Type.Bullet) {
            return false;
        }
        return calcDistance(this, object, this.bitmap, bmp);
//        Log.v(">>>>bullet ishit", "temp=" + temp + ", SIZE="+ SIZE);
//        return (temp < SIZE);
//        return (calcDistance(this, object) < SIZE);
//      return false;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.bitmap, xPosition, yPosition, paint);
/*        switch (bullet_index){
            default:
            case 1:canvas.drawBitmap(this.bitmap, xPosition-(this.bitmap.getWidth()/2), yPosition-30, paint);
                break;
            case 2:canvas.drawBitmap(this.bitmap, xPosition-(this.bitmap.getWidth()/2)-100, yPosition-15, paint);
                break;
            case 3:canvas.drawBitmap(this.bitmap, xPosition-(this.bitmap.getWidth()/2)+100, yPosition-15, paint);
                break;
            case 4:canvas.drawBitmap(this.bitmap, xPosition-(this.bitmap.getWidth()/2)-200, yPosition, paint);
                break;
            case 5:canvas.drawBitmap(this.bitmap, xPosition-(this.bitmap.getWidth()/2)+200, yPosition, paint);
                break;
        }
        */
//        Log.v(">>>>bullet", "x=" + this.xPosition + ", x2="+ (xPosition-(this.bitmap.getWidth()/2)));

//      canvas.drawCircle(xPosition, yPosition, SIZE, paint);
    }
}
