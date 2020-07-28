package com.projectwild.shared.utils;

public class Vector2 {

    private double x, y;

    public Vector2() {
        this(0);
    }

    public Vector2(double xy) {
        this(xy, xy);
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2 vector) {
        set(vector.getX(), vector.getY());
    }

    public void set(double xy) {
        set(xy, xy);
    }

    public void set(double x, double y) {
        setX(x);
        setY(y);
    }

    public Vector2 setX(double x) {
        this.x = x;
        return this;
    }

    public Vector2 setY(double y) {
        this.y = y;
        return this;
    }

    public Vector2 changeX(double x) {
        this.x += x;
        return this;
    }

    public Vector2 changeY(double y) {
        this.y += y;
        return this;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getXInt() {
        return (int) x;
    }

    public int getYInt() {
        return (int) y;
    }

    public Vector2 copy() {
        return new Vector2(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", x, y);
    }

}
