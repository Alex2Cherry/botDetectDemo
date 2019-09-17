package com.anc.botdetectdemo;

import android.hardware.SensorEvent;

public class SensorData {

    private  long time;

    private float x;

    private float y;

    private float z;

    public SensorData(SensorEvent event) {
        time = System.currentTimeMillis();
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) { this.z = z; }

    public long getTime() { return time; }

    public void setTime(long time) { this.time = time; }
}
