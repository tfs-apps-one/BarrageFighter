package tfsapps.barragefighter;

/**
 * Created by FURUKAWA on 2017/11/03.
 */


import android.content.Context;
import android.graphics.*;
import android.view.*;

public class BackGround extends View{

    public static Bitmap bmp; //???
    public static int dp_w;   //????????
    public static int dp_h;   //?????????
    public int img_w;         //????
    public int img_h;         //?????
    public static float mg_w; //??????????
    public static float mg_h; //???????????

    Paint paint = new Paint();

    //??????????????????????
    public BackGround(Context context, int resource_name){
        super(context);

        Matrix matrix = new Matrix();

        //??????bitmap???
        bmp = BitmapFactory.decodeResource(context.getResources(), resource_name);
        // WindowManager??
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        //Display????????
        Display dp = wm.getDefaultDisplay();
        //???????????
        dp_w = dp.getWidth();
        dp_h = dp.getHeight();
        //???????
        img_w = bmp.getWidth();
        img_h = bmp.getHeight();

        //????
        mg_w = (float)dp_w/img_w;
        mg_h = (float)dp_h/img_h;
        matrix.postScale(mg_w, mg_h);
        //??????
        bmp = Bitmap.createBitmap(bmp, 0, 0, img_w, img_h , matrix, true);
    }

    //    @Override
//    public void onDraw(Canvas canvas){
    public void BakDraw(Canvas canvas){
        //???
        canvas.drawColor(Color.WHITE);
        //??
        canvas.drawBitmap(bmp, 0, 0, paint);
    }
}