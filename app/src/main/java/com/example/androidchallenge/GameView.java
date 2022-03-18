package com.example.androidchallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.androidchallenge.threads.GameDrawThread;
import com.example.androidchallenge.threads.GameUpdateThread;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private final GameDrawThread threadDraw = new GameDrawThread(getHolder(), this);
    private final GameUpdateThread threadUpdate = new GameUpdateThread(getHolder(), this);

    private final int SCREEN_WIDTH = this.getResources().getDisplayMetrics().widthPixels;
    private final int SCREEN_HEIGHT = this.getResources().getDisplayMetrics().heightPixels;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255,0,0));
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("draw test", SCREEN_WIDTH - 20, 60, paint);
    }

    public void update() {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        threadDraw.setRunning(true);
        threadDraw.start();
        threadUpdate.setRunning(true);
        threadUpdate.start();
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
                threadUpdate.setRunning(false);
                threadUpdate.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }
}
