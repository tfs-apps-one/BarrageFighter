package tfsapps.barragefighter;

/**
 * Created by FURUKAWA on 2017/11/03.
 */

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper
{
    private static final String TABLE = "gameinfo";
    public MyOpenHelper(Context context) {
        super(context, "GameDB", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE + "("
                + "stage integer,"
                + "score integer,"
                + "point integer,"
                + "item integer,"
                + "data1 integer,"
                + "data2 integer,"
                + "data3 integer,"
                + "data4 integer,"
                + "data5 integer,"
                + "bullet_hp integer,"
                + "bullet_type integer,"
                + "bullet_num integer,"
                + "bullet_interval integer,"
                + "bullet_speed integer,"
                + "dr_hp integer,"
                + "dr_lv integer,"
                + "dr_move integer,"
                + "dr_data1 integer,"
                + "dr_data2 integer,"
                + "dr_data3 integer,"
                + "dr_data4 integer,"
                + "dr_data5 integer);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
