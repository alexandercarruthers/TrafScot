package com.example.trafscot.Models;

/**
 * Alexander Carruthers - S1828301
 */
public class GeoPoint {
    public double x;
    public double y;

    public GeoPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Location:" + x + "," + y;
    }

}
