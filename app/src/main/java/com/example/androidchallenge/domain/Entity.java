package com.example.androidchallenge.domain;

import android.graphics.Paint;


public abstract class Entity {

    protected Paint color;
    protected Circle circle;

    public Entity(float x, float y, int radius, int color) {
        this.circle = new Circle(x, y, radius);
        this.color = new Paint();
        this.color.setColor(color);
    }

    public float getX() { return circle.getCenter().x; }

    public float getY() { return circle.getCenter().y; }

    public float getRadius() { return circle.getRadius(); }

    public Paint getColor() { return color; }
}
