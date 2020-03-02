package com.example.trafscot.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class GeoPoint implements Parcelable {
    public double x;
    public double y;

    public GeoPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    protected GeoPoint(Parcel in) {
        x = in.readDouble();
        y = in.readDouble();
    }

    public static final Creator<GeoPoint> CREATOR = new Creator<GeoPoint>() {
        @Override
        public GeoPoint createFromParcel(Parcel in) {
            return new GeoPoint(in);
        }

        @Override
        public GeoPoint[] newArray(int size) {
            return new GeoPoint[size];
        }
    };

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
        return "GeoPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(x);
        parcel.writeDouble(y);
    }
}
