package com.example.androidchallenge;


import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
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
                handler.postDelayed(runnable, 333);
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
                handler.postDelayed(runnable, 333);
            }
        };
    }
    public void startRunnableDecrease(){
        runnable = new Runnable() {
            @Override
            public void run() {
                if(gameView.getPlayer().getThrusterPower() <= 0) return;
                decreasePowerValue();
                handler.postDelayed(runnable, 333);
            }

        };
        handler.postDelayed(runnable, 100);
    }

    private void increasePowerValue(){
        if(gameView.getPlayer().getThrusterPower() <= 9){
            gameView.getPlayer().addThrusterPower(1);
        }
    }

    private void decreasePowerValue(){
        gameView.getPlayer().addThrusterPower(-1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Sensor mGeo = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sm.registerListener(this, mGeo, SensorManager.SENSOR_DELAY_NORMAL);
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
                gameView.getPlayer().updateSpeedAccelerometer(values[1]);
            }
            if (sensor == Sensor.TYPE_ROTATION_VECTOR) {
                gameView.getPlayer().updateSpeed(values[2]);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

