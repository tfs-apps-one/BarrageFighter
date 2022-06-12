package tfsapps.barragefighter;

/**
 * Created by FURUKAWA on 2017/11/03.
 */
import android.content.Context;
import android.graphics.*;
import android.view.*;

public class ShootMenu extends View {

    Paint paint;

    public ShootMenu(Context context /* database */) {
        super(context);

        paint = new Paint();
    }

    public boolean Draw(Canvas canvas) {

        /* ステータス */
        canvas.drawColor(Color.WHITE);

        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        canvas.drawText("＜ミサイル＞\n　ヒット値:" + 90 + "\n　貫通値:" + 1, 0, 100, paint);
        canvas.drawText("＜レーザー＞\n　ヒット値:" + 90 + "\n　貫通値:" + 1, 0, 200, paint);
        /* レベルアップ */

        return true;
    }
}