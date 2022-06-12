package tfsapps.barragefighter;

/**
 * Created by FURUKAWA on 2017/11/03.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;

public abstract class BaseObject {
    static final int STATUS_NORMAL = -1;
    static final int STATUS_DESTROYED = 0;
    int hitpoint;
    int status = STATUS_NORMAL;
    enum Type {
        City,
        Droid,
        Bullet,
        Missile,
        Boss,
        Object,
    }
    static final float MOVE_WEIGHT = 3.0f;
    //    static final float MOVE_WEIGHT = 3.0f;
    float yPosition;
    float xPosition;
    public abstract void draw(Canvas canvas);
    public boolean isAvailable(int width, int height) {
        if (yPosition < 0 || xPosition < 0 || yPosition > height || xPosition > width) {
            return false;
        }
        if (status == STATUS_DESTROYED) {
            return false;
        }
        return true;
    }
    public boolean isBroken() {
        if (status == STATUS_DESTROYED) {
            return true;
        }
        return false;
    }
    public abstract void move(float x, float y);
    public abstract Type getType();
    public abstract boolean isHit(BaseObject object, Bitmap bmp);
    static boolean calcDistance(BaseObject object1, BaseObject object2, Bitmap bmp1, Bitmap bmp2) {
        if(((object1.yPosition < object2.yPosition + bmp2.getHeight()) && (object1.yPosition + bmp1.getHeight() > object2.yPosition))
                && ((object1.xPosition+bmp1.getWidth()-bmp2.getWidth()/2 > object2.xPosition) && (object1.xPosition+bmp2.getWidth()/2  < object2.xPosition + bmp2.getWidth())))

/*        if(((object1.yPosition < object2.yPosition + bmp2.getHeight()) && (object1.yPosition + bmp1.getHeight() > object2.yPosition))
                && ((object1.xPosition + bmp1.getWidth() > object2.xPosition) && (object1.xPosition  < object2.xPosition + bmp2.getWidth())))*/
/*            if (Math.abs(object1.xPosition - object2.xPosition) < bmp1.getWidth() / 2 + bmp2.getWidth() / 2 //横の判定
                && Math.abs(object1.yPosition - object2.yPosition) < bmp1.getHeight() / 2 + bmp2.getHeight() / 2)//縦の判定*/
/*        if (Math.abs(object1.xPosition+bmp1.getWidth()/2 - object2.xPosition+bmp2.getWidth()/2) < bmp1.getWidth() / 2 + bmp2.getWidth() / 2 //横の判定
         && Math.abs(object1.yPosition+bmp1.getHeight()/2 - object2.yPosition+bmp2.getHeight()/2) < bmp1.getHeight() / 2 + bmp2.getHeight() / 2)//縦の判定*/
        {
            return true;
        } else {
            return false;
        }
    }
    /*    static double calcDistance(BaseObject object1, BaseObject object2) {
            float distX = object1.xPosition - object2.xPosition; float distY = object1.yPosition - object2.yPosition;
            return Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
            }
    */
    public void hit() {
        hitpoint = hitpoint -1;
        if (hitpoint <=0) {
            status = STATUS_DESTROYED;
        }
    }
    public int getHitpoint()
    {
        return hitpoint;
    }
    public void setHitpoint(int data)
    {
        hitpoint = data;
    }
}