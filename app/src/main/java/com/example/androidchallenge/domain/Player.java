package com.example.androidchallenge.domain;

import android.graphics.Color;

public class Player extends Entity {

    public float speedX;
    public float speedY;
    public float thrusterPower;

    public Player(float x, float y, int radius) {
        super(x, y, radius, Color.WHITE);
        this.speedY = 10;
        this.thrusterPower = 0;
    }

    public void addThrusterPower(float val) {
        thrusterPower = thrusterPower + val;
        thrusterPower = Math.min(Math.max(thrusterPower, 0), 9);
    }

    public void move() {
        //TODO Update speedX and speedY using Compass value and thrusterPower
        this.circle.addToCenter(speedX, speedY);
    }

    public float getSpeedX() { return speedX; }

    public float getSpeedY() { return speedY; }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    // PROBABLY NOT USEFUL
    public float getThrusterPower() { return thrusterPower; }

    public void updateSpeed(float azimuth) {
        float azimuthLevel = Math.abs(azimuth) % 0.5f;
        if (azimuth == 0) {
            speedX = 0;
            speedY = 10;
        } else if (azimuth == 0.5) {
            speedX = -10;
            speedY = 0;
        } else if (azimuth == 1 || azimuth == -1) {
            speedX = 0;
            speedY = -10;
        } else if (azimuth == -0.5) {
            speedX = 10;
            speedY = 0;
        } else if (azimuth < 0.5 && azimuth > 0) {
            speedX = 0 - azimuthLevel * 20;
            speedY = 10 - azimuthLevel * 20;
        }
        else if (azimuth > 0.5 && azimuth < 1) {
            speedX = -10 + azimuthLevel * 20;
            speedY = 0 - azimuthLevel * 20;
        } else if (azimuth < 0 && azimuth > -0.5) {
            speedX = 0 + azimuthLevel * 20;
            speedY = 10 - azimuthLevel * 20;
        } else if (azimuth < -0.5 && azimuth < -1) {
            speedX = 10 - azimuthLevel * 20;
            speedY = 0 - azimuthLevel * 20;
        }

        //accelerometer

        if (speedX < 0) {
            speedX += (Math.abs(speedX) / 10) * thrusterPower;
        }
        if (speedX >= 0) {
            speedX -= (Math.abs(speedX) / 10) * thrusterPower;
        }
        if (speedY < 0) {
            speedY += (Math.abs(speedY) / 10) * thrusterPower;
        }
        if (speedY >= 0) {
            speedY -= (Math.abs(speedY) / 10) * thrusterPower;
        }

    }

    public void updateSpeedAccelerometer(float accelerometerX) {
        speedX = speedX * (1 + accelerometerX/100);
    }
}
