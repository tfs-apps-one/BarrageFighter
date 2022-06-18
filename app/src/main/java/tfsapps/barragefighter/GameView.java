package tfsapps.barragefighter;

/**
 * Created by FURUKAWA on 2017/11/03.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    //    private static final int MISSILE_LAUNCH_WEIGHT = 50;
    private static final int MISSILE_LAUNCH_WEIGHT = 150;
    private static final int OBJECT_LAUNCH_WEIGHT = 150;
    private static final long FPS = 60;

    private static final float SCORE_TEXT_SIZE = 60.0f;

    private static final long VIBRATION_LENGTH_HIT_MISSILE = 100;
    private static final long VIBRATION_LENGTH_HIT_DROID = 1000;
    private static final long SCORE_LEVEL = 200;
//    private static final long SCORE_LEVEL = 100;

    private Boss boss;
    private Boss bossbom;
    private Droid droid;
    private City city;
    private final List<BaseObject> missileList = new ArrayList<BaseObject>();
    private final List<BaseObject> bulletList = new ArrayList<BaseObject>();
    private final List<BaseObject> objectList = new ArrayList<BaseObject>();
    private final List<BaseObject> object2List = new ArrayList<BaseObject>();

    private final Paint paintScore = new Paint();

    static private int score;
    static public boolean game_pause = false;

    private final Random rand = new Random(System.currentTimeMillis());
    private BackGround background;
    private ShootMenu shootmenu;

    private long bulletcount;   /* 弾丸の表示間隔 */
    private long endcount;       /* 終了の間隔 */

    private MediaPlayer sound1;
    private MediaPlayer sound2;
    private MediaPlayer sound3;
    private MediaPlayer soundboss;
    private MediaPlayer bomboss1;
    private MediaPlayer bomboss2;
    private MediaPlayer bombgm1;
    private MediaPlayer bombgm2;
    private MediaPlayer bombgm3;
    private MediaPlayer bombgm4;
    private MediaPlayer bombgm5;
    private MediaPlayer bombgm6;
    private MediaPlayer bombgm7;
    private MediaPlayer bombgm8;
    private MediaPlayer bombgm9;
    private MediaPlayer bombgm10;
/*    private MediaPlayer bombgm11;
    private MediaPlayer bombgm12;
    private MediaPlayer bombgm13;
    private MediaPlayer bombgm14;
    private MediaPlayer bombgm15;*/

    private Bitmap bulletBmp;
    private Bitmap droidBmp;
    private Bitmap missileBmp;
    private Bitmap bossBmp;
    private Bitmap bossbomBmp;
    private Bitmap objectBmp;
    private Bitmap object2Bmp;
    private Bitmap cityBmp;
    private static final int PLAY_OPENING = 0;
    private static final int PLAY_ENEMY = 1;
    private static final int PLAY_BOSS = 2;
    private static final int PLAY_ENDING = 3;
    private static int stage = 11;  /* デフォルトステージ１－１からスタート */
    private static int game_state = 0;
        /* 0:オープニング、1:雑魚、2:ボス、3:エンディング */

    static private MyOpenHelper dbhp;
    static private int bullet_hp = 1;
    static private int bullet_type = 1;
    static private int bullet_num = 1;
    static private int bullet_speed = 1;
    static private int bullet_interval = 1;
    static private int droid_level = 1;

    static private int enemy_hp = 1;
    static private int enemy_speed = 0;
    static private int boss_hp = 11;
    static private int boss_speed = 0;

    static private int droid_update = 0;

    public interface Callback {
        public void onGameOver(int point, int score, int stage, int finish);
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private Handler handler;

    private final Vibrator vibrator;

    private int game_mode = 0;

/*----------------------------------------------------------
	各種データの初期セット、解放	INITIAL END　
-----------------------------------------------------------*/

    /* メモリ初期化処理 */
    public void GameInitial()
    {
        /* データベースから */
        SQLiteDatabase db = dbhp.getReadableDatabase();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" stage");
        sql.append(" ,score");
        sql.append(" ,bullet_hp");
        sql.append(" ,bullet_type");
        sql.append(" ,bullet_num");
        sql.append(" ,bullet_interval");
        sql.append(" ,bullet_speed");
        sql.append(" ,dr_lv");
        sql.append(" FROM gameinfo;");
        try{
            Cursor cursor = db.rawQuery(sql.toString(), null);
            //TextViewに表示
            StringBuilder text = new StringBuilder();
            if (cursor.moveToNext()){
                stage = cursor.getInt(0);
//              score = cursor.getInt(1);
                score = 0;
                bullet_hp = cursor.getInt(2);
                bullet_type = cursor.getInt(3);
                bullet_num = cursor.getInt(4);
                bullet_interval = cursor.getInt(5);
                bullet_speed = cursor.getInt(6);
                droid_level =  cursor.getInt(7);
            }
        }finally{
            db.close();
        }


        /* 変数初期化 */
        game_state = PLAY_OPENING;
        bulletcount = 0;   /* 弾丸の表示間隔 */
        endcount = 0;       /* 弾丸の表示間隔 */

        /* サウンド関連 */
        if (sound1==null)   sound1 = MediaPlayer.create(getContext(), R.raw.sound1);
        if (sound2==null)   sound2 = MediaPlayer.create(getContext(), R.raw.sound2);
        if (sound3==null)   sound3 = MediaPlayer.create(getContext(), R.raw.sound3);
        if (soundboss==null)  soundboss = MediaPlayer.create(getContext(), R.raw.boss);
        if (bomboss1==null)  bomboss1 = MediaPlayer.create(getContext(), R.raw.bom_boss);
        if (bomboss2==null)  bomboss2 = MediaPlayer.create(getContext(), R.raw.bom_boss);
        if (bombgm1==null)  bombgm1 = MediaPlayer.create(getContext(), R.raw.bom);
        if (bombgm2==null)  bombgm2 = MediaPlayer.create(getContext(), R.raw.bom);
        if (bombgm3==null)  bombgm3 = MediaPlayer.create(getContext(), R.raw.bom);
        if (bombgm4==null)  bombgm4 = MediaPlayer.create(getContext(), R.raw.bom);
        if (bombgm5==null)  bombgm5 = MediaPlayer.create(getContext(), R.raw.bom);
        if (bombgm6==null)  bombgm6 = MediaPlayer.create(getContext(), R.raw.bom);
        if (bombgm7==null)  bombgm7 = MediaPlayer.create(getContext(), R.raw.bom);
        if (bombgm8==null)  bombgm8 = MediaPlayer.create(getContext(), R.raw.bom);
        if (bombgm9==null)  bombgm9 = MediaPlayer.create(getContext(), R.raw.bom);
        if (bombgm10==null)  bombgm10 = MediaPlayer.create(getContext(), R.raw.bom);
/*        if (bombgm11==null)  bombgm11 = MediaPlayer.create(getContext(), R.raw.bom);
        if (bombgm12==null)  bombgm12 = MediaPlayer.create(getContext(), R.raw.bom);
        if (bombgm13==null)  bombgm13 = MediaPlayer.create(getContext(), R.raw.bom);
        if (bombgm14==null)  bombgm14 = MediaPlayer.create(getContext(), R.raw.bom);
        if (bombgm15==null)  bombgm15 = MediaPlayer.create(getContext(), R.raw.bom);*/

        /* キャラクター関連 */
        if(droidBmp == null){
            switch (droid_level)
            {
                case 1: droidBmp = BitmapFactory.decodeResource(getResources(), R.drawable.plain1); break;
                case 2: droidBmp = BitmapFactory.decodeResource(getResources(), R.drawable.plain2); break;
                case 3: droidBmp = BitmapFactory.decodeResource(getResources(), R.drawable.plain3); break;
                case 4: droidBmp = BitmapFactory.decodeResource(getResources(), R.drawable.plain4); break;
            }
        }

        if(bossbomBmp == null)  bossbomBmp = BitmapFactory.decodeResource(getResources(), R.drawable.bossbom);


        if (game_mode == 1)         //ストーリー
        {
            if (background == null) background = new BackGround(getContext(), R.drawable.free);
            enemy_hp = 1;
            enemy_speed = -1;
            boss_hp = 1;
            boss_speed = 1;
            if (missileBmp == null)
                missileBmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy6);
            if (bossBmp == null)
                bossBmp = BitmapFactory.decodeResource(getResources(), R.drawable.boss1);
        }
        else {
            /* 背景メニュー関連 */
            if (stage / 10 == 4) {
                if (background == null) background = new BackGround(getContext(), R.drawable.dosei);
                enemy_hp = 4;
                enemy_speed = 2;
                boss_hp = stage * 3;
                boss_speed = 2;
                if (missileBmp == null)
                    missileBmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy4);
                if (bossBmp == null)
                    bossBmp = BitmapFactory.decodeResource(getResources(), R.drawable.boss4);
            } else if (stage / 10 == 1) {
                if (background == null) background = new BackGround(getContext(), R.drawable.sky);
                enemy_hp = 1;
                enemy_speed = -1;
                boss_hp = stage;
                boss_speed = -1;
                if (missileBmp == null)
                    missileBmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy1);
                if (bossBmp == null)
                    bossBmp = BitmapFactory.decodeResource(getResources(), R.drawable.boss1);
                if (objectBmp == null)
                    objectBmp = BitmapFactory.decodeResource(getResources(), R.drawable.cloud1);
                if (object2Bmp == null)
                    object2Bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cloud2);
            } else if (stage / 10 == 2) {
                if (background == null) background = new BackGround(getContext(), R.drawable.earth);
                enemy_hp = 2;
                enemy_speed = 0;
                boss_hp = stage * 2;
                boss_speed = -1;
                if (missileBmp == null)
                    missileBmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy2);
                if (bossBmp == null)
                    bossBmp = BitmapFactory.decodeResource(getResources(), R.drawable.boss2);
            } else if (stage / 10 == 3) {
                if (background == null) background = new BackGround(getContext(), R.drawable.space);
                enemy_hp = 3;
                enemy_speed = 1;
                boss_hp = stage * 2;
                boss_speed = 1;
                if (missileBmp == null)
                    missileBmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy3);
                if (bossBmp == null)
                    bossBmp = BitmapFactory.decodeResource(getResources(), R.drawable.boss3);
            } else {
                if (background == null) background = new BackGround(getContext(), R.drawable.gala);
                enemy_hp = (stage / 10) + 2;
                enemy_speed = (stage / 10) - 2;
                boss_hp = stage * (stage / 10 - 1);
                boss_speed = 2;
                if (missileBmp == null)
                    missileBmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy5);
                if (bossBmp == null)
                    bossBmp = BitmapFactory.decodeResource(getResources(), R.drawable.boss5);
            }
        }

        /* 弾丸の種類 */
        switch (bullet_hp)
        {
            default:
                if (bulletBmp == null)  bulletBmp = BitmapFactory.decodeResource(getResources(), R.drawable.gun5);
                break;
            case 1:
                if (bulletBmp == null)  bulletBmp = BitmapFactory.decodeResource(getResources(), R.drawable.gun1);
                break;
            case 2:
                if (bulletBmp == null)  bulletBmp = BitmapFactory.decodeResource(getResources(), R.drawable.gun2);
                break;
            case 3:
                if (bulletBmp == null)  bulletBmp = BitmapFactory.decodeResource(getResources(), R.drawable.gun3);
                break;
            case 4:
                if (bulletBmp == null)  bulletBmp = BitmapFactory.decodeResource(getResources(), R.drawable.gun4);
                break;
        }

    }
    /* メモリの解放と初期化処理（後始末） */
    public void GameEnding() {
        sound1.reset();  sound1.release(); sound1 = null;
        sound2.reset();  sound2.release(); sound2 = null;
        sound3.reset();  sound3.release(); sound3 = null;
        soundboss.reset();   soundboss.release(); soundboss = null;
        bombgm1.reset(); bombgm1.release(); bombgm1 = null;
        bombgm2.reset(); bombgm2.release(); bombgm2 = null;
        bombgm3.reset(); bombgm3.release(); bombgm3 = null;
        bombgm4.reset(); bombgm4.release(); bombgm4 = null;
        bombgm5.reset(); bombgm5.release(); bombgm5 = null;
        bombgm6.reset(); bombgm6.release(); bombgm6 = null;
        bombgm7.reset(); bombgm7.release(); bombgm7 = null;
        bombgm8.reset(); bombgm8.release(); bombgm8 = null;
        bombgm9.reset(); bombgm9.release(); bombgm9 = null;
        bombgm10.reset(); bombgm10.release(); bombgm10 = null;
/*        bombgm11.reset(); bombgm11.release(); bombgm11 = null;
        bombgm12.reset(); bombgm12.release(); bombgm12 = null;
        bombgm13.reset(); bombgm13.release(); bombgm13 = null;
        bombgm14.reset(); bombgm14.release(); bombgm14 = null;
        bombgm15.reset(); bombgm15.release(); bombgm15 = null;*/
        bomboss1.reset(); bomboss1.release(); bomboss1 = null;
        bomboss2.reset(); bomboss2.release(); bomboss2 = null;
        bulletBmp = null;
        droidBmp = null;
        missileBmp = null;
        bossBmp = null;
        background = null;
    }


/*----------------------------------------------------------
	サウンド関連 SOUND
-----------------------------------------------------------*/

    /* ヒット音 */
    public void bomSoundPlay()
    {
        if (bombgm1.isPlaying()==false)     bombgm1.start();
        else if(bombgm2.isPlaying()==false) bombgm2.start();
        else if(bombgm3.isPlaying()==false) bombgm3.start();
        else if(bombgm4.isPlaying()==false) bombgm4.start();
        else if(bombgm5.isPlaying()==false) bombgm5.start();
        else if(bombgm6.isPlaying()==false) bombgm6.start();
        else if(bombgm7.isPlaying()==false) bombgm7.start();
        else if(bombgm8.isPlaying()==false) bombgm8.start();
        else if(bombgm9.isPlaying()==false) bombgm9.start();
        else if(bombgm10.isPlaying()==false) bombgm10.start();
/*        else if(bombgm11.isPlaying()==false) bombgm11.start();
        else if(bombgm12.isPlaying()==false) bombgm12.start();
        else if(bombgm13.isPlaying()==false) bombgm13.start();
        else if(bombgm14.isPlaying()==false) bombgm14.start();
        else if(bombgm15.isPlaying()==false) bombgm15.start();*/

    }

    /* ＰＬＡＹ中のサウンド */
    public void gameSoundPlay() {

        if (sound1 == null || sound2 == null || sound3 == null || soundboss == null)
        {
            return;
        }

        if (game_state == PLAY_ENDING) {
            if (sound1.isPlaying() == true) sound1.pause();
            if (sound2.isPlaying() == true) sound2.pause();
            if (sound3.isPlaying() == true) sound3.pause();
            if(soundboss.isPlaying() == true)  soundboss.pause();

            if(bomboss1.isPlaying() == false) {
                bomboss1.start();
            }
            else if(bomboss2.isPlaying() == false) {
                bomboss2.start();
            }
        }
        else if (game_state == PLAY_BOSS) {
            if (sound1.isPlaying() == true) sound1.pause();
            if (sound2.isPlaying() == true) sound2.pause();
            if (sound3.isPlaying() == true) sound3.pause();

            if (soundboss.isPlaying() == false) {
                soundboss.setLooping(true);
                soundboss.start();
            }
        }
        else {
            if (stage/10 == 4){
                if(sound1.isPlaying() == true)  sound1.pause();
                if(sound2.isPlaying() == true)  sound2.pause();
                if(soundboss.isPlaying() == true)  soundboss.pause();

                if (sound3.isPlaying() == false) {
                    sound3.setLooping(true);
                    sound3.start();
                }
            }
            else if (stage/10 == 1 || stage/10 == 2){
                if(sound2.isPlaying() == true)  sound2.pause();
                if(sound3.isPlaying() == true)  sound3.pause();
                if(soundboss.isPlaying() == true)  soundboss.pause();

                if (sound1.isPlaying() == false) {
                    sound1.setLooping(true);
                    sound1.start();
                }
            }
            else {
                if(sound1.isPlaying() == true)  sound1.pause();
                if(sound3.isPlaying() == true)  sound3.pause();
                if(soundboss.isPlaying() == true)  soundboss.pause();

                if (sound2.isPlaying() == false) {
                    sound2.setLooping(true);
                    sound2.start();
                }
            }
        }
    }

    /* サウンドの後始末 */
    public void endSound() {
        if (sound1 != null) {
            if (sound1.isPlaying() == true) sound1.stop();
        }
        if (sound2 != null) {
            if (sound2.isPlaying() == true) sound2.stop();
        }
        if (sound3 != null) {
            if (sound3.isPlaying() == true) sound3.stop();
        }
        if (soundboss != null) {
            if (soundboss.isPlaying() == true) soundboss.stop();
        }
    }



/*----------------------------------------------------------
	描画処理　DRAW↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
-----------------------------------------------------------*/

    // スコア表示
    public GameView(Context context, MyOpenHelper helper, int mode) {
        super(context);

        handler = new Handler();
        dbhp = helper;
        game_mode = mode;

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (game_mode == 1) {
            paintScore.setColor(Color.BLUE);
        }
        else {
            paintScore.setColor(Color.RED);
        }
        paintScore.setTextSize(SCORE_TEXT_SIZE);
        paintScore.setAntiAlias(true);

        getHolder().addCallback(this);
    }

    //全体描画
    private void drawObject(Canvas canvas) {

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        if (game_pause == true)
        {
            endSound();
            GameEnding();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onGameOver(0, 0, 0, 2);
                }
            });
            return;
        }

        /* 背景処理（空） */
        background.BakDraw(canvas);
//      canvas.drawColor(Color.WHITE);  /* 背景白 */

        /* 戦闘機描画 */
        if (droid == null) {
            droid = new Droid(droidBmp, width, height, bullet_num);
        }
        else{
            droid_update ++;
            if (droid_update % 2 == 0) {
                droidBmp = BitmapFactory.decodeResource(getResources(), R.drawable.plain42);
            }
            else if (droid_update % 3 == 0) {
                droidBmp = BitmapFactory.decodeResource(getResources(), R.drawable.plain43);
            }
            else {
                droidBmp = BitmapFactory.decodeResource(getResources(), R.drawable.plain41);
            }
            droid.droidDisp(droidBmp);

        }

        if (city == null) {
            city = new City(width, height);
        }

        /* ゲームサウンド */
        gameSoundPlay();

        drawObjectList(canvas, missileList, width, height);

        drawObjectList(canvas, bulletList, width, height);

        drawObjectList(canvas, objectList, width, height);

        drawObjectList(canvas, object2List, width, height);

        /* ヒット判定 */
        boolean ishits = false;
        /* enemy */
        for (int i = 0; i < missileList.size(); i++) {
            BaseObject missile = missileList.get(i);

            /* ミサイル */
            for (int j = 0; j < bulletList.size(); j++) {
                BaseObject bullet = bulletList.get(j);

                if (bullet.isHit(missile, missileBmp)) {
                    missile.hit();
                    bullet.hit();
                    if(missile.isBroken()) {
                        ishits = true;
                        if (game_mode == 1) {       //ストーリー
                            score += 10;
                        }
                        else
                        {
                            int bai = stage / 10;
                            score += (10 * bai);
                            if ((stage > 14 && stage <= 19) || (stage > 24 && stage <= 29)) {
                                score += 5;
                            }
                        }
                    }
                }
            }
        }
        if (ishits)
        {
            bomSoundPlay();
            vibrator.vibrate(VIBRATION_LENGTH_HIT_MISSILE);
        }
        /* BOSS */
        ishits = false;
        if (game_state == PLAY_BOSS && boss != null) {
            for (int j = 0; j < bulletList.size(); j++) {
                BaseObject bullet = bulletList.get(j);

                if (boss.isHit(bullet, bulletBmp)) {
//                if(bullet.isHit(boss, bossBmp)){
                    boss.hit();
                    bullet.hit();
                    ishits = true;
                }
            }
        }
        if (ishits)
        {
            bomSoundPlay();
            vibrator.vibrate(VIBRATION_LENGTH_HIT_MISSILE);
        }


        /* 戦闘機や町にヒットした場合（ゲームオーバーの判定） */
        for (int i = 0; i < missileList.size(); i++) {
            BaseObject missile = missileList.get(i);

            if (!missile.isAvailable(width, height)) {
                continue;
            }
            boolean gameOver = false;

            if (droid.isHit(missile, missileBmp)) {
                missile.hit();
                droid.hit();
                gameOver = true;
            } else if (city.isHit(missile, missileBmp)) {
                missile.hit();
                city.hit();
                gameOver = true;
            }

            if (gameOver) {
                vibrator.vibrate(VIBRATION_LENGTH_HIT_DROID);
                GameEnding();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onGameOver((score/10), score, stage, 0);
                    }
                });
                return;
            }
        }
        if (game_state == PLAY_BOSS && boss != null)
        {
            boolean gameOver = false;

            if (boss.isAvailable(width, height)) {
                if (droid.isHit(boss, bossBmp)) {
                    boss.hit();
                    droid.hit();
                    gameOver = true;
                } else if (city.isHit(boss, bossBmp)) {
                    boss.hit();
                    city.hit();
                    gameOver = true;
                }
            }
            if (gameOver) {
                vibrator.vibrate(VIBRATION_LENGTH_HIT_DROID);
                GameEnding();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onGameOver((score/10), score, stage, 0);
                    }
                });
                return;
            }
        }

        /* エンディング */
        if (game_state == PLAY_ENDING)
        {
            if (bossbom != null)
            {
//              bossbom.move(0,0);
                bossbom.draw(canvas);
            }

            endcount++;
            if (endcount >= 40) {
                endcount = 0;
                bossbom = null;
                bomboss1.pause();
                bomboss2.pause();
//              score += (stage * 10);
                stage++; if(stage%10 == 0) stage++;
                GameEnding();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onGameOver((score/10), score, stage, 1);
                    }
                });
                return;
            }
        }
        /* BOSS */
        else if (game_state == PLAY_BOSS)
        {
            if (boss == null) {
                bossCreate();
            }
            boss.move(0,0);
            boss.draw(canvas);

            if (boss.isAvailable(width, height)== false)
            {
                float x, y;
                game_state = PLAY_ENDING;

                x = boss.xPosition;
                y = boss.yPosition;
                boss = null;
                drawObjectListDelete(canvas, missileList, width, height);
                drawObjectListDelete(canvas, bulletList, width, height);
                vibrator.vibrate(VIBRATION_LENGTH_HIT_DROID);

                if (bossbom == null)
                {
                    bossbom = new Boss(bossbomBmp, (int)x, 0.0f, (int)y, 1, 0);
                }
//              return;
            }
        }
        /* 通常戦闘 */
        else
        {
            // enemy作成
            if (game_mode == 1) {   //ストーリー
                if (rand.nextInt(MISSILE_LAUNCH_WEIGHT - 30) == 0) {
                    long count = (score / 200);
                    if (count <= 0) count = 1;
                    for (int i = 0; i < count; i++) {
                        launchMissile();
                    }
                }
            }
            else
            {
                if (rand.nextInt(MISSILE_LAUNCH_WEIGHT - (stage / 10) * 10) == 0) {
                    long count = (stage % 10);
                    if (stage / 10 == 1) count /= 3;
                    if (stage / 10 == 2) count /= 2;
                    if (count < 1) count = 1;
                    for (int i = 0; i < count; i++) {
                        launchMissile();
                    }
                }

                //オブジェクト作成　小さい
                if (rand.nextInt(OBJECT_LAUNCH_WEIGHT+80) == 0) {
                    launchObject();
                }
                //オブジェクト作成　大きい
                if (rand.nextInt(OBJECT_LAUNCH_WEIGHT*2) == 0) {
                    launchObject2();
                }
            }

        }

        /* 町 */
        city.draw(canvas);

        /* 戦闘機 */
        droid.draw(canvas);

        /* ミサイルの自動表示間隔 */
        if (game_state != PLAY_ENDING) {
            bulletcount++;
            if (bulletcount > 100 - bullet_interval*10) {
                bulletcount = 0;
                for (int i = 1; i <= droid.bullet_num; i++) {
                    fire(droid.xPosition, droid.yPosition, i);
                }
            }
        }

        // スコアの表示
        if (game_mode == 1)     //ストーリー
        {
            canvas.drawText(" SCORE: " + score, 0, SCORE_TEXT_SIZE, paintScore);
            if (score <= 1000) {
                enemy_hp = 1;
                enemy_speed = -1;
            }
            else if (score <= 2000)
            {
                enemy_hp = 2;
                enemy_speed = 0;
            }
            else if (score <= 3000)
            {
                enemy_hp = 3;
                enemy_speed = 1;
            }
            else if (score <= 4000)
            {
                enemy_hp = 4;
                enemy_speed = 2;
            }
            else if (score <= 5000)
            {
                enemy_hp = 5;
                enemy_speed = 3;
            }
            else if (score <= 6000)
            {
                enemy_hp = 6;
                enemy_speed = 4;
            }
            else
            {
                enemy_hp = 6;
                enemy_speed = 4;
            }
        }
        else {
            canvas.drawText("STAGE:" + stage / 10 + "-" + stage % 10 + "    SCORE: " + score, 0, SCORE_TEXT_SIZE, paintScore);

            if (game_state == PLAY_OPENING || game_state == PLAY_ENEMY) {
                if (score >= 100 * (stage - 10) * 2) {
                    {
                        game_state = PLAY_BOSS;
                    }
                }
            }
        }
    }


    /* オブジェクトの描画処理 */
    private static void drawObjectList(Canvas canvas, List<BaseObject> objectList, int width, int height) {
        for (int i = 0; i < objectList.size(); i++) {
            BaseObject object = objectList.get(i);
            if (object.isAvailable(width, height)) {
                object.move(0,0);
                object.draw(canvas);
            } else {
                objectList.remove(object);
                i--;
            }
        }
    }
    /* オブジェクト描画消滅 */
    private static void drawObjectListDelete(Canvas canvas, List<BaseObject> objectList, int width, int height) {
        for (int i = 0; i < objectList.size(); i++) {
            BaseObject object = objectList.get(i);
            objectList.remove(object);
            i--;
        }
    }

    /* ミサイルやレーザーの描画処理 */
    private void fire(float y, float x, int index) {
        float alignX = (x - droid.rect.centerX()) / Math.abs(y - droid.rect.centerY());
        Bullet bullet = new Bullet(bulletBmp, alignX, droid.rect, index, bullet_hp, bullet_speed);
        bulletList.add(0, bullet);
    }

    /* enemy描画処理 */
    private void launchMissile() {
        int fromX = rand.nextInt(getWidth()-missileBmp.getWidth());
        int toX = rand.nextInt(getWidth()-missileBmp.getWidth());
        int fromY = rand.nextInt(150);

        float alignX = (toX - fromX) / (float) getHeight();
        Missile missile = new Missile(missileBmp, fromX, alignX, fromY, enemy_hp, enemy_speed);
        missileList.add(missile);
    }

    /* BOSS描画処理 */
    private void bossCreate() {
        int fromX = rand.nextInt(getWidth()-bossBmp.getWidth());
        int toX = rand.nextInt(getWidth()-bossBmp.getWidth());
        int fromY = rand.nextInt(150);

        float alignX = (toX - fromX) / (float) getHeight();
        boss = new Boss(bossBmp, fromX, alignX, fromY, boss_hp, boss_speed);
    }

    /* オブジェクト描画処理（雲、隕石） */
    private void launchObject() {
        int fromX = rand.nextInt(getWidth()-objectBmp.getWidth());
        int toX = rand.nextInt(getWidth()-objectBmp.getWidth());
//        int fromY = rand.nextInt(150);
        int objsp = rand.nextInt(2);
        int fromY = 0;

//        float alignX = (toX - fromX) / (float) getHeight();
        float alignX = ((toX - fromX)/2) / (float) getHeight();
        MyObject myObject = new MyObject(objectBmp, fromX, alignX, fromY, enemy_hp, objsp);
        objectList.add(myObject);
    }
    /* オブジェクト描画処理（雲、隕石） */
    private void launchObject2() {
        int fromX = rand.nextInt(getWidth()-object2Bmp.getWidth());
        int toX = rand.nextInt(getWidth()-object2Bmp.getWidth());
//        int fromY = rand.nextInt(150);
        int objsp = rand.nextInt(2);
        int fromY = 0;

//        float alignX = (toX - fromX) / (float) getHeight();
        float alignX = ((toX - fromX)/2) / (float) getHeight();
        MyObject myObject = new MyObject(object2Bmp, fromX, alignX, fromY, enemy_hp, objsp);
        objectList.add(myObject);
    }


/*----------------------------------------------------------
	描画処理　DRAW ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
-----------------------------------------------------------*/


    /* タッチイベント（タップ処理）自機移動 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                droid.move(event.getX(), event.getY());
                Log.v("TAP>>>","xPos=" + event.getX() + ", yPos="+ event.getY());

                break;
        }
        return super.onTouchEvent(event);
    }



/*----------------------------------------------------------
	スレッド関連　TASK THREAD
-----------------------------------------------------------*/

    private DrawThread drawThread;

    /* ゲーム全体の描画間隔 */
    private class DrawThread extends Thread {
        private boolean isFinished;

        @Override
        public void run() {
            super.run();

            SurfaceHolder holder = getHolder();
            while (!isFinished) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    drawObject(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }

                try {
                    sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void startDrawThread() {
        stopDrawThread();

        drawThread = new DrawThread();
        GameInitial();
        game_state = PLAY_ENEMY;
        drawThread.start();

    }

    public boolean stopDrawThread() {
        if (drawThread == null) {
            return false;
        }

        drawThread.isFinished = true;
        drawThread = null;
        return true;
    }

    public void reStartDrawThread() {
        stopDrawThread();

        drawThread = new DrawThread();
        game_state = PLAY_OPENING;
        drawThread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDrawThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDrawThread();
    }
}