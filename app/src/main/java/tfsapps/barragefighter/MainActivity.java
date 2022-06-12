package tfsapps.barragefighter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.app.AlertDialog;
import android.content.DialogInterface;

//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
*/
public class MainActivity extends Activity implements GameView.Callback {
//public class MainActivity extends Activity implements GameView.Callback ,RewardedVideoAdListener {

    static final boolean DEBUG = false;
    private GameView gameView;
    private MediaPlayer bgmover;
    private MediaPlayer bgmclear;
    private MediaPlayer bgmwait;
    public MyOpenHelper helper;
    /* サブ画面 */
    private Button Menu;    /* メニューへ戻る */
    private Button Lvup;    /* レベルアップボタン */
    private CheckBox CB_UnitLv;
    private CheckBox CB_Attack;
    private CheckBox CB_BulletNum;
    private CheckBox CB_BulletInterval;
    private CheckBox CB_BulletSpeed;
    private TextView SubInfo;
    private TextView SubInfo2;
    private ImageView ImgView;
    private ImageView Imgttl1;
    private ImageView Imgttl2;
    private ImageView present;

    final int UNIT_LV_MAX = 4;
    final int ATTACK_MAX = 15;
    final int BULLET_NUM = 7;
    final int BULLET_INT = 7;
    final int BULLET_SP = 7;
    private int gamepoint = 0;
    private int gamestage = 11;
    private int gamescore = 0;
    private int attack = 1;
    private int bullet_type = 1;
    private int bullet_num = 1;
    private int bullet_interval = 1;
    private int bullet_speed = 1;
    private int unit_level = 1;
    /*
    private AdView mAdView;
    // 動画
    private RewardedVideoAd mRewardedVideoAd;
    private static final String AD_UNIT_ID = "ca-app-pub-4924620089567925/4042148683";
    private static final String APP_ID = "ca-app-pub-4924620089567925~3539585801";
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bgmover = MediaPlayer.create(this, R.raw.gameover);
        bgmclear = MediaPlayer.create(this, R.raw.clear);
        Imgttl2 = (ImageView) findViewById(R.id.titleimage2);
        Imgttl1 = (ImageView) findViewById(R.id.titleimage1);
        present = (ImageView) findViewById(R.id.play);
        Imgttl2.setImageResource(R.drawable.plain3);
        Imgttl1.setImageResource(R.drawable.plain4);
        present.setImageResource(R.drawable.present);

//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        //動画リワード
        /*
        MobileAds.initialize(this, APP_ID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
         */
    }
/*
    // 動画
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(AD_UNIT_ID,new AdRequest.Builder().build());
    }

 */
    public void onPlay(View view){
        /*
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }

         */
    }

    /* ステータス表示 */
    public void MainDisp()
    {
        setContentView(R.layout.activity_main);
        Imgttl2 = (ImageView) findViewById(R.id.titleimage2);
        Imgttl1 = (ImageView) findViewById(R.id.titleimage1);
        present = (ImageView) findViewById(R.id.play);

        Imgttl2.setImageResource(R.drawable.plain3);
        Imgttl1.setImageResource(R.drawable.plain4);
        present.setImageResource(R.drawable.present);

        if (bgmwait != null) {
            if (bgmwait.isPlaying() == false) {
                bgmwait.setLooping(true);
                bgmwait.start();
            }
        }
    }


    /* ゲームの初期設定ｓ */
    public void GameInitRoad() {
        int data = 0;
        int data2 = 0;
        int data3 = 0;

        SQLiteDatabase db = helper.getReadableDatabase();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" stage");
        sql.append(" ,score");
        sql.append(" ,point");
        sql.append(" FROM gameinfo;");
        try {
            Cursor cursor = db.rawQuery(sql.toString(), null);
            //TextViewに表示
            StringBuilder text = new StringBuilder();
            if (cursor.moveToNext()) {
                data = cursor.getInt(0);
                data2 = cursor.getInt(1);
                data3 = cursor.getInt(2);
//                gamestage = cursor.getInt(0);
//                gamepoint = cursor.getInt(1);
            }
        } finally {
            db.close();
        }

        db = helper.getWritableDatabase();
//        if(gamestage == 0)
        if (data == 0) {
            long ret;
            /* 新規レコード追加 */
            ContentValues insertValues = new ContentValues();
            gamestage = 11;
            gamescore = 0;
            gamepoint = 0;
            insertValues.put("stage", 11);
            insertValues.put("score", 0);
            insertValues.put("point", 0);
            insertValues.put("item", 0);
            insertValues.put("data1", 0);
            insertValues.put("data2", 0);
            insertValues.put("data3", 0);
            insertValues.put("data4", 0);
            insertValues.put("data5", 0);
            insertValues.put("bullet_hp", 1);
            insertValues.put("bullet_type", 1);
            insertValues.put("bullet_num", 1);
            insertValues.put("bullet_interval", 1);
            insertValues.put("bullet_speed", 1);
            insertValues.put("dr_hp", 1);
            insertValues.put("dr_lv", 1);
            insertValues.put("dr_move", 1);
            insertValues.put("dr_data1", 0);
            insertValues.put("dr_data2", 0);
            insertValues.put("dr_data3", 0);
            insertValues.put("dr_data4", 0);
            insertValues.put("dr_data5", 0);
            try {
                ret = db.insert("gameinfo", null, insertValues);
            } finally {
                db.close();
            }
            if (ret == -1) {
                Toast.makeText(this, "Save Data Create.... ERROR", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Save Data Create.... OK", Toast.LENGTH_SHORT).show();
            }
        } else {
            gamestage = data;
            gamescore = data2;
            gamepoint = data3;
            Toast.makeText(this, "Save Data Loading...", Toast.LENGTH_SHORT).show();
        }

    }

    /* 清算処理 */
    public int GameCheck(int point, int score, int stage, int finish) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues value = new ContentValues();
        gamepoint += point;
        if (gamescore < score)  gamescore = score;  /* BEST */
        value.put("score", gamescore);
        value.put("point", gamepoint);
        if (finish == 1 && stage <= 89) {
            value.put("stage", stage);
        }
        int ret;
        try {
            ret = db.update("gameinfo", value, null, null);
        } finally {
            db.close();
        }
        if (ret == -1) {
            Toast.makeText(this, "Saving.... ERROR", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saving.... OK", Toast.LENGTH_SHORT).show();
        }
        if (stage > 89)
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this, R.style.MyAlertDialogStyleOK);
            ad.setCancelable(false);
            ad.setTitle("*** FANTASTIC ***");
            ad.setMessage("\n\n\n\n\nYou have cleared all the stages.\n\n\n\n");
            ad.setPositiveButton("OK", null);
            ad.show();
            return 0;
        }
        return 1;
    }

    /* ゲームオーバー処理 */
    @Override
    public void onGameOver(int point, int score, int stage, int finish) {

        gameView.stopDrawThread();

        /* ホームボタンや戻るボタンを押した時 */
        if (finish == 2) {
            MainDisp();
            return;
        }

        if (GameCheck(point, score, stage, finish) == 0)    /* ALL CLEAR */
        {
            MainDisp();
            return;
        }

        if (finish == 0) {
            bgmover.start();

//            Toast.makeText(this, "Game Over Score " + score, Toast.LENGTH_LONG).show();
            AlertDialog.Builder ad = new AlertDialog.Builder(this, R.style.MyAlertDialogStyleNG);
            ad.setCancelable(false);
            ad.setTitle("!!! GAME OVER !!!");
            ad.setMessage("\n\n\nReturn to the menu screen.\n\n" + " SCORE:" + score + "\n POINT:" + (score / 10) + "\n\n" +
                    "I recommend you to level up at points you earn.\n");
            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainDisp();
                }
            });
            ad.show();
        } else {
            bgmclear.start();

            new AlertDialog.Builder(this, R.style.MyAlertDialogStyleOK)
                    .setCancelable(false)
                    .setTitle("--- STAGE CLEAR ---")
                    .setMessage("\n\n\nDo you want to continue ?\nGo to the next stage.\n\n" + " SCORE:" + score + "\n POINT:" + (score / 10) + "\n\n")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            setContentView(gameView);
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainDisp();
                        }
                    })
                    .show();
        }
    }


    /*----------------------------------------------------------
        以下、メイン画面　ボタン関連の処理	BUTTON
    -----------------------------------------------------------*/
    /* ゲームスタート（ストーリー） */
    public void onGameStart(View view) {
        if (bgmwait.isPlaying() == true) {
            bgmwait.pause();
        }
        gameView = new GameView(this, helper, 0);
        gameView.setCallback(this);
        gameView.game_pause = false;
        setContentView(gameView);
    }

    /* ゲームスタート（スコア） */
    public void onScoreStart(View view) {
        if (bgmwait.isPlaying() == true) {
            bgmwait.pause();
        }
        gameView = new GameView(this, helper, 1);
        gameView.setCallback(this);
        gameView.game_pause = false;
        setContentView(gameView);
    }

    /* レベルアップ */
    public void onLevelUp(View view) {
        //サブ画面へ
        setScreenSub();
    }

    /* セーブデータクリア */
    public void allclear(){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        gamestage = 11; gamepoint = 0;  gamescore = 0;
        insertValues.put("stage", 11);
        insertValues.put("score", 0);
        insertValues.put("point", 0);
        insertValues.put("item", 0);
        insertValues.put("data1", 0);
        insertValues.put("data2", 0);
        insertValues.put("data3", 0);
        insertValues.put("data4", 0);
        insertValues.put("data5", 0);
        insertValues.put("bullet_hp", 1);
        insertValues.put("bullet_type", 1);
        insertValues.put("bullet_num", 1);
        insertValues.put("bullet_interval", 1);
        insertValues.put("bullet_speed", 1);
        insertValues.put("dr_hp", 1);
        insertValues.put("dr_lv", 1);
        insertValues.put("dr_move", 1);
        insertValues.put("dr_data1", 0);
        insertValues.put("dr_data2", 0);
        insertValues.put("dr_data3", 0);
        insertValues.put("dr_data4", 0);
        insertValues.put("dr_data5", 0);
        int ret;
        try {
            ret = db.update("gameinfo", insertValues, null, null);;
        } finally {
            db.close();
        }
        if (ret == -1){
            Toast.makeText(this, "Save Data Clear.... ERROR", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Save Data Clear.... OK", Toast.LENGTH_SHORT).show();
        }
    }
    public void onAllClear(View view){
        new AlertDialog.Builder(this, R.style.MyAlertDialogStyleNG)
                .setCancelable(false)
                .setTitle("--- ALL CLEAR OK ? ---")
                .setMessage("\n\n\n Do you really want to initialize \n [ALL SAVE DATA] ? \n\n\n")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        allclear();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .show();
    }



    /* ステージ */
    public void stageclear(){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
/*
        gamestage += 1;
        if ((gamestage % 10) == 0) gamestage++;
		if (gamestage > 79) gamestage=11;
*/

        gamestage = 11;

        insertValues.put("stage", gamestage);
        int ret;
        try {
            ret = db.update("gameinfo", insertValues, null, null);
        } finally {
            db.close();
        }
        if (ret == -1){
            Toast.makeText(this, "Stage Clear.... ERROR " + gamestage/10 + "-" + gamestage%10, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Stage Clear.... OK "+ gamestage/10 + "-" + gamestage%10, Toast.LENGTH_SHORT).show();
        }

    }
    public void onNextStage(View view){
        new AlertDialog.Builder(this, R.style.MyAlertDialogStyleNG)
                .setCancelable(false)
                .setTitle("--- STAGE CLEAR OK ? ---")
                .setMessage("\n\n\n Do you really want to initialize \n [STAGE DATA] ? \n\n\n")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        stageclear();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .show();
    }

    /* DB更新 */
    public void AppDBUpdated() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("point", gamepoint);
        int ret;
        try {
            ret = db.update("gameinfo", value, null, null);
        } finally {
            db.close();
        }
        if (ret == -1){
            Toast.makeText(this, "Saving.... ERROR ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saving.... OK ", Toast.LENGTH_SHORT).show();
        }

    }

    /*----------------------------------------------------------
        以下、イベント関連の処理	EVENT
    -----------------------------------------------------------*/
    @Override
    public void onStart() {
        super.onStart();
        /* データベース */
        helper = new MyOpenHelper(this);
        GameInitRoad();

        if (bgmwait == null)
        {
            bgmwait = MediaPlayer.create(this, R.raw.wait);
            if (bgmwait.isPlaying() == false) {
                bgmwait.setLooping(true);
                bgmwait.start();
            }
        }
        if (gameView != null)
        {
            gameView.game_pause = false;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //動画リワード
//        mRewardedVideoAd.resume(this);
    }

    public void pauseExec()
    {
        if (bgmwait != null)
        {
            if (bgmwait.isPlaying() == true) {
                bgmwait.pause();
            }
            bgmwait.reset();  bgmwait.release(); bgmwait = null;
        }
        if (gameView != null)
        {
            gameView.game_pause = true;
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        pauseExec();
        //動画リワード
//        mRewardedVideoAd.pause(this);
    }
    @Override
    public void onUserLeaveHint(){
        //ホームボタンが押された時や、他のアプリが起動した時に呼ばれる
        //戻るボタンが押された場合には呼ばれない
        pauseExec();
//        Toast.makeText(getApplicationContext(), "Home Button Good bye!" , Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                //戻るボタンが押された時の処理。
                pauseExec();
//                Toast.makeText(this, "Back button!" , Toast.LENGTH_SHORT).show();
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //動画リワード
//        mRewardedVideoAd.destroy(this);
    }
    /*
        動画リワード
    @Override
    public void onRewarded(RewardItem reward) {
        // Reward the user.
        int point = gamepoint;
        if (gamestage <= 19)         gamepoint += 30;
        else if (gamestage <= 29)    gamepoint += 100;
        else if (gamestage <= 39)    gamepoint += 300;
        else if (gamestage <= 49)    gamepoint += 700;
        else if (gamestage <= 59)    gamepoint += 1500;
        else                          gamepoint += 3000;

        //ユーザーレベルアップ
        Toast.makeText(this, "POINT GET!!：" + (point) + "  → " + (gamepoint), Toast.LENGTH_SHORT).show();
        AppDBUpdated();
    }
    @Override
    public void onRewardedVideoAdLeftApplication() {
//        Toast.makeText(this, "onRewardedVideoAdLeftApplication",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
//        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad " + errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
//        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
//        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
//        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }
     */

    /*----------------------------------------------------------
        以下、サブ画面関連の処理	SUB SCREEN
    -----------------------------------------------------------*/
    /* サブ画面の表示処理 */
    private void SubInfoDisp() {
        String drawtext = "";
        String drawtext2 = "";

        SubInfo = (TextView) findViewById(R.id.SubInfo);
        SubInfo2 = (TextView) findViewById(R.id.SubInfo2);
        ImgView = (ImageView) findViewById(R.id.UnitImage);

        /* データベースから */
        SQLiteDatabase db = helper.getReadableDatabase();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" score");
        sql.append(" ,point");
        sql.append(" ,bullet_hp");
        sql.append(" ,bullet_type");
        sql.append(" ,bullet_num");
        sql.append(" ,bullet_interval");
        sql.append(" ,bullet_speed");
        sql.append(" ,dr_lv");
        sql.append(" FROM gameinfo;");
        try {
            Cursor cursor = db.rawQuery(sql.toString(), null);
            //TextViewに表示
            StringBuilder text = new StringBuilder();
            if (cursor.moveToNext()) {
                gamescore = cursor.getInt(0);
                gamepoint = cursor.getInt(1);
                attack = cursor.getInt(2);
                bullet_type = cursor.getInt(3);
                bullet_num = cursor.getInt(4);
                bullet_interval = cursor.getInt(5);
                bullet_speed = cursor.getInt(6);
                unit_level = cursor.getInt(7);
            }
        } finally {
            db.close();
        }
        /*           12345678901234567890*/
        drawtext =  "  BEST SCORE         \n";
        drawtext += "  POINT              \n";
        drawtext += "   -Fighter type     \n";
        drawtext += "   -Attack           \n";
        drawtext += "   -Bullet Number    \n";
        drawtext += "   -Firing Interval  \n";
        drawtext += "   -Bullet Speed     \n";
        SubInfo.setText(drawtext);
        SubInfo.setTextColor(Color.BLUE);

        drawtext2 += ": " + String.valueOf(gamescore) + "\n";
        drawtext2 += ": " + String.valueOf(gamepoint) + "\n";
        if (unit_level == UNIT_LV_MAX) {
            drawtext2 += ": " + String.valueOf(unit_level) + " (Max 4)(Next --- )\n";
        }
        else{
            drawtext2 += ": " + String.valueOf(unit_level) + " (Max 4)(Next "+ String.valueOf(getNeedPoint(1,unit_level+1)) + ")\n";
        }
        if (attack == ATTACK_MAX){
            drawtext2 += ": " + String.valueOf(attack) + " (Max15)(Next --- )\n";
        }
        else {
            drawtext2 += ": " + String.valueOf(attack) + " (Max15)(Next "+ String.valueOf(getNeedPoint(2, attack+1)) + ")\n";
        }
        if (bullet_num == BULLET_NUM){
            drawtext2 += ": " + String.valueOf(bullet_num) + " (Max 7)(Next --- )\n";
        }
        else {
            drawtext2 += ": " + String.valueOf(bullet_num) + " (Max 7)(Next "+ String.valueOf(getNeedPoint(3, bullet_num+1)) + ")\n";
        }
        if (bullet_interval == BULLET_INT){
            drawtext2 += ": " + String.valueOf(bullet_interval) + " (Max 7)(Next --- )\n";
        }
        else {
            drawtext2 += ": " + String.valueOf(bullet_interval) + " (Max 7)(Next "+ String.valueOf(getNeedPoint(4, bullet_interval+1)) + ")\n";
        }
        if (bullet_speed == BULLET_SP){
            drawtext2 += ": " + String.valueOf(bullet_speed) + " (Max 7)(Next --- )\n";
        }
        else {
            drawtext2 += ": " + String.valueOf(bullet_speed) + " (Max 7)(Next "+ String.valueOf(getNeedPoint(5, bullet_speed+1)) + ")\n";
        }
        SubInfo2.setText(drawtext2);
        SubInfo2.setTextColor(Color.BLUE);

        switch (unit_level) {
            case 1:
                ImgView.setImageResource(R.drawable.plain1);
                break;
            case 2:
                ImgView.setImageResource(R.drawable.plain2);
                break;
            case 3:
                ImgView.setImageResource(R.drawable.plain3);
                break;
            case 4:
                ImgView.setImageResource(R.drawable.plain4);
                break;
        }
    }

    /* レベルアップ処理 */
    private int LevelUpDbUpdate(int flag, int data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues value = new ContentValues();
        int temppoint = 0;

        temppoint = gamepoint - getNeedPoint(flag, data);
        if (temppoint < 0)
        {
            return -2;
        }
        else
        {
            value.put("point", temppoint);
        }

        switch (flag) {
            case 1:
                value.put("dr_lv", data);
                break;

            case 2:
                value.put("bullet_hp", data);
                break;

            case 3:
                value.put("bullet_num", data);
                break;

            case 4:
                value.put("bullet_interval", data);
                break;

            case 5:
                value.put("bullet_speed", data);
                break;
        }

        int ret;
        try {
            ret = db.update("gameinfo", value, null, null);
            ;
        } finally {
            db.close();
        }
        if (ret == -1) {
            Toast.makeText(this, "Saving.... ERROR", Toast.LENGTH_SHORT).show();
            return 0;
        } else {
            AlertDialog.Builder ad = new AlertDialog.Builder(this, R.style.MyAlertDialogStyleOK);
            ad.setCancelable(false);
            ad.setTitle("--- LEVEL UP ---");
            ad.setMessage("\n\n\n\nLevel up is complete.\n\n\n\n");
            ad.setPositiveButton("OK", null);
            ad.show();

            Toast.makeText(this, "Saving.... OK", Toast.LENGTH_SHORT).show();
        }

        return 1;
    }

    private int getNeedPoint(int type, int nextlv)
    {
        if (type == 1)
        {
            switch (nextlv)
            {
                case 1: return 0;
                case 2: return 100;
                case 3: return 100;
                case 4: return 100;
                case 5: return 1000;
                case 6: return 5000;
                case 7: return 10000;
                default: return 20000;
            }
        }
        else
        {
            switch (nextlv)
            {
                case 1: return 0;
                case 2: return 40;
                case 3: return 100;
                case 4: return 500;
                case 5: return 1000;
                case 6: return 5000;
                case 7: return 10000;
                default: return 20000;
            }
        }
    }


    private void LevelUpDone(int flag)
    {
        int data;
        int ans = 0;
        switch (flag)
        {
            case 1:
                data = unit_level+1;
                if (data > UNIT_LV_MAX) {
                    ans = 0;
                }
                else if (getNeedPoint(flag, data) > gamepoint) {
                    ans = -1;
                }
                else{
                    ans = LevelUpDbUpdate(flag, data);
                }

                break;
            case 2:
                data = attack + 1;
                if (data > ATTACK_MAX) {
                    ans = 0;
                }
                else if (getNeedPoint(flag, data) > gamepoint) {
                    ans = -1;
                }
                else {
                    ans = LevelUpDbUpdate(flag, data);
                }
                break;

            case 3:
                data = bullet_num + 1;
                if (data > BULLET_NUM) {
                    ans = 0;
                }
                else if (getNeedPoint(flag, data) > gamepoint) {
                    ans = -1;
                }
                else {
                    ans = LevelUpDbUpdate(flag, data);
                }
                break;

            case 4:
                data = bullet_interval + 1;
                if (data > BULLET_INT) {
                    ans = 0;
                }
                else if (getNeedPoint(flag, data) > gamepoint) {
                    ans = -1;
                }
                else {
                    ans = LevelUpDbUpdate(flag, data);
                }
                break;

            case 5:
                data = bullet_speed + 1;
                if (data > BULLET_SP) {
                    ans = 0;
                }
                else if (getNeedPoint(flag, data) > gamepoint) {
                    ans = -1;
                }
                else {
                    ans = LevelUpDbUpdate(5, data);
                }
                break;
        }

        if (ans == 1)
        {
            setScreenSub();
        }
        else if (ans == -1)
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this, R.style.MyAlertDialogStyleNG);
            ad.setCancelable(false);
            ad.setTitle("!!! OPERATION IMPOSSIBLE !!!");
            ad.setMessage("\n\n\nPoints are not enough.\n\n\n\n");
            ad.setPositiveButton("OK", null);
            ad.show();
        }
        else
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this, R.style.MyAlertDialogStyleNG);
            ad.setCancelable(false);
            ad.setTitle("!!! IT IS THE MAXIMUM LEVEL !!!");
            ad.setMessage("\n\n\nA fighter plane can not be strengthened any more.\n\n\n\n");
            ad.setPositiveButton("OK", null);
            ad.show();
        }

    }
    private void LevelUpCheck()
    {
        int checknum = 0;
        if (CB_UnitLv.isChecked() == true)  checknum++;
        if (CB_Attack.isChecked() == true)  checknum++;
        if (CB_BulletNum.isChecked() == true)  checknum++;
        if (CB_BulletInterval.isChecked() == true)  checknum++;
        if (CB_BulletSpeed.isChecked() == true)  checknum++;

        if (checknum == 0)
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this, R.style.MyAlertDialogStyleNG);
            ad.setCancelable(false);
            ad.setTitle("!!! SELECTION MISTAKE !!!");
            ad.setMessage("\n\n\nPlease select checkbox.\n\n\n\n");
            ad.setPositiveButton("OK", null);
            ad.show();
        }
        else if (checknum == 1)
        {
            if (CB_UnitLv.isChecked() == true)  LevelUpDone(1);
            if (CB_Attack.isChecked() == true)  LevelUpDone(2);
            if (CB_BulletNum.isChecked() == true)  LevelUpDone(3);
            if (CB_BulletInterval.isChecked() == true)  LevelUpDone(4);
            if (CB_BulletSpeed.isChecked() == true)  LevelUpDone(5);
        }
        else
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this, R.style.MyAlertDialogStyleNG);
            ad.setCancelable(false);
            ad.setTitle("!!! SELECTION MISTAKE !!!");
            ad.setMessage("\n\n\nPlease select only one checkbox\n\n\n\n");
            ad.setPositiveButton("OK", null);
            ad.show();

        }
    }

    /* サブ画面 */
    private void setScreenSub(){
        setContentView(R.layout.activity_sub);

        CB_UnitLv = (CheckBox) findViewById(R.id.UnitLv);
        CB_Attack = (CheckBox) findViewById(R.id.Attack);
        CB_BulletNum = (CheckBox) findViewById(R.id.BulletNum);
        CB_BulletInterval = (CheckBox) findViewById(R.id.BulletInterval);
        CB_BulletSpeed = (CheckBox) findViewById(R.id.BulletSpeed);

        SubInfoDisp();

        Menu = (Button) findViewById(R.id.Menu);
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainDisp();
            }
        });

        Lvup = (Button) findViewById(R.id.LevelUp);
        Lvup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LevelUpCheck();
            }
        });

    }
}
/*
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
 */