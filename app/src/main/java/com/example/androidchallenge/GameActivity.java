package com.example.androidchallenge;


import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private GameView gameView;
    private SensorManager sm;
    final Handler handler = new Handler();
    Runnable runnable;
    int valueTest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        gameView = new GameView(this);

        setViewTouchListener();

        setContentView(gameView);
    }

    private void setViewTouchListener(){
        gameView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                startRunnableIncrease();
                handler.postDelayed(runnable, 100);
                return true;
            }
        });
    }
    public void startRunnableIncrease(){
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!gameView.isPressed()){
                    startRunnableDecrease();
                    return;
                } else{
                    increasePowerValue();
                }
                handler.postDelayed(runnable, 100);
            }
        };
    }
    public void startRunnableDecrease(){
        runnable = new Runnable() {
            @Override
            public void run() {
                if(valueTest <= 0) return;
                decreasePowerValue();
                handler.postDelayed(runnable, 100);
            }

        };
        handler.postDelayed(runnable, 100);
    }

    private void increasePowerValue(){
        if(valueTest < 30){
            valueTest ++;
        }
        System.out.println("value : "+valueTest);
    }

    private void decreasePowerValue(){
        valueTest --;
        System.out.println("value decrease: "+valueTest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensor = sensorEvent.sensor.getType();
        float[] values = sensorEvent.values;

        synchronized (this) {
            if (sensor == Sensor.TYPE_ACCELEROMETER){
                gameView.getPlayer().setSpeedX(-3 * values[0]);
                gameView.getPlayer().setSpeedY(3 * values[1]);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}

