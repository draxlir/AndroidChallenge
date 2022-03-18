package com.example.androidchallenge.threads;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.example.androidchallenge.GameView;

public class GameDrawThread extends Thread {

    private boolean running;
    private Canvas canvas;

    private final SurfaceHolder surfaceHolder;
    private final GameView gameView;

    private final Handler mHandler = new Handler();

    public GameDrawThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    @Override
    public void run() {
        if (running) {
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder) {
                    this.gameView.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        mHandler.postDelayed(this, 50/3);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}