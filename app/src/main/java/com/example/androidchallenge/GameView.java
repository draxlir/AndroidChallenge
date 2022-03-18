package com.example.androidchallenge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;


import com.example.androidchallenge.domain.Entity;
import com.example.androidchallenge.domain.Player;
import com.example.androidchallenge.domain.Debris;
import com.example.androidchallenge.domain.Mars;
import com.example.androidchallenge.threads.GameDrawThread;
import com.example.androidchallenge.threads.GameUpdateThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private final GameDrawThread threadDraw = new GameDrawThread(getHolder(), this);
    private final GameUpdateThread threadUpdate = new GameUpdateThread(getHolder(), this);

    private final int SCREEN_WIDTH = this.getResources().getDisplayMetrics().widthPixels;
    private final int SCREEN_HEIGHT = this.getResources().getDisplayMetrics().heightPixels;

   private final Player player = new Player(
            (float) new Random().nextInt(SCREEN_WIDTH * 5 - Constants.PLAYER_RADIUS) + Constants.PLAYER_RADIUS,
            (float) new Random().nextInt(SCREEN_HEIGHT * 5 - Constants.PLAYER_RADIUS) + Constants.PLAYER_RADIUS,
            Constants.PLAYER_RADIUS
    );

    private final List<Debris> debris = new ArrayList<>();
    private Mars mars;

    private Bitmap scaled;

    private long startTime;
    private long timeSpend;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        //background
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background_space);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        scaled = Bitmap.createScaledBitmap(background, metrics.widthPixels,metrics.heightPixels, true);

        startTime = SystemClock.elapsedRealtime();

    }

    public void marsCreation() {
        Random rnd = new Random();
        int posX = rnd.nextInt((SCREEN_WIDTH * 5 -Constants.MARS_RADIUS) - Constants.MARS_RADIUS) + Constants.MARS_RADIUS;
        int posY = rnd.nextInt((SCREEN_HEIGHT * 5 -Constants.MARS_RADIUS) - Constants.MARS_RADIUS) + Constants.MARS_RADIUS;
        mars = new Mars((float) posX, (float) posY, Constants.MARS_RADIUS);
    }

    public void debrisCreation() {
        Random rnd = new Random();
        for(int i=0; i<Constants.DEBRIS_NUMBER; i++){
            int radius = rnd.nextInt(100-50) + 50;
            int posX = rnd.nextInt((SCREEN_WIDTH * 5 - radius) - radius) + radius;
            int posY = rnd.nextInt((SCREEN_HEIGHT * 5 - radius) - radius) + radius;
            debris.add(new Debris((float) posX, (float) posY, radius));
        }
    }

    private void drawBorders(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        Rect borders = new Rect(0, 0, SCREEN_WIDTH*5, SCREEN_HEIGHT*5);
        if(isBorderOnScreen(borders)) {
            canvas.drawRect(SCREEN_WIDTH/2 + borders.left - player.getX(),
                    SCREEN_HEIGHT/2 + borders.top - player.getY(),
                    SCREEN_WIDTH/2 +  borders.right - player.getX(),
                    SCREEN_HEIGHT/2 + borders.bottom - player.getY(),
                    paint);
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(scaled, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255,0,0));
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("draw test", SCREEN_WIDTH - 20, 60, paint);

        drawPlayer(canvas);
        drawDebris(canvas);
        drawMars(canvas);
        drawBorders(canvas);
    }

    private boolean isBorderOnScreen(Rect borders) {
        return borders.right < player.getX() + (SCREEN_WIDTH/2) ||
                borders.left > player.getX() - (SCREEN_WIDTH/2)  ||
                borders.bottom < player.getY() + (SCREEN_HEIGHT/2)  ||
                borders.top > player.getY() - (SCREEN_HEIGHT/2) ;
    }

    private boolean isEntityOnScreen(Entity entity) {
        return entity.getX() < player.getX() + (SCREEN_WIDTH/2) + entity.getRadius() &&
                entity.getX() > player.getX() - (SCREEN_WIDTH/2) - entity.getRadius() &&
                entity.getY() < player.getY() + (SCREEN_HEIGHT/2) + entity.getRadius() &&
                entity.getY() > player.getY() - (SCREEN_HEIGHT/2) - entity.getRadius();
    }

    public void drawDebris(Canvas canvas) {
        for(Debris debris1 : debris) {
            if (isEntityOnScreen(debris1)) {
                canvas.drawCircle(SCREEN_WIDTH/2 + debris1.getX() - player.getX(),SCREEN_HEIGHT/2 + debris1.getY() - player.getY(), debris1.getRadius(), debris1.getColor());
            }
        }
    }

    public void drawMars(Canvas canvas) {
        if(isEntityOnScreen(mars)) {
            canvas.drawCircle(SCREEN_WIDTH/2 + mars.getX() - player.getX(),SCREEN_HEIGHT/2 + mars.getY() - player.getY(), mars.getRadius(), mars.getColor());
        }
    }

    public void drawPlayer(Canvas canvas) {
        canvas.drawCircle(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, Constants.PLAYER_RADIUS, player.getColor());
    }

    public void update() {
        player.move();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        debrisCreation();
        marsCreation();

        threadDraw.setRunning(true);
        threadDraw.start();
        threadUpdate.setRunning(true);
        threadUpdate.start();
    }

    private int nearest(int minus, int plus, double pick) {
        return Math.abs(minus - pick) < Math.abs(plus - pick) ? minus : plus;
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // nothing
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry) {
            try {
                threadDraw.setRunning(false);
                threadDraw.join();
                threadUpdate .setRunning(false);
                threadUpdate.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void gameOver(){
        timeSpend = (SystemClock.elapsedRealtime() - startTime) / 1000;
        System.out.println(timeSpend);
        Intent intent = new Intent(getContext(), EndGameActivity.class);
        //mContext.startActivity(intent);

        //Todo pass the time to endgame activity
        /*SharedPreferences sharedPref = this.mContext.getSharedPreferences("settings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("score", score.getScore());
        editor.apply();*/
        //mContext.startActivity(intent);
    }
}
