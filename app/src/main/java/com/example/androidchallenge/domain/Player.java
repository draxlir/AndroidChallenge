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

    // PROBABLY NOT USEFUL
    public float getThrusterPower() { return thrusterPower; }
}
