package com.example.androidchallenge.threads;

import android.os.Handler;
import android.view.SurfaceHolder;

import com.example.androidchallenge.GameView;

public class GameUpdateThread extends Thread {

    private boolean running;

    private final SurfaceHolder surfaceHolder;
    private final GameView gameView;

    private final Handler mHandler = new Handler();

    public GameUpdateThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    @Override
    public void run() {
        if (running) {
            try {
                synchronized (surfaceHolder) {
                    this.gameView.update();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mHandler.postDelayed(this, 50);
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}