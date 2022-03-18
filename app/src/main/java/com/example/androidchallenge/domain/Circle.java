package com.example.androidchallenge.domain;

import android.graphics.PointF;

public class Circle {

    private final PointF center = new PointF();
    private final int radius;

    public Circle (float x, float y, int radius) {
        this.radius = radius;
        this.center.set(x, y);
    }

    public int getRadius() {
        return radius;
    }

    public PointF getCenter() {
        return center;
    }

    public void setCenter(float x, float y) {
        this.center.set(x, y);
    }

    public void addToCenter(float valX, float valY) { this.center.set(center.x + valX, center.y + valY); }
}
