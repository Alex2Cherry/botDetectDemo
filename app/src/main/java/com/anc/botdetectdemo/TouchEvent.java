package com.anc.botdetectdemo;

import android.view.MotionEvent;

public class TouchEvent {
    private int type;

    private long time;

    private int x;

    private int y;

    private float pressure;

    public TouchEvent(MotionEvent event) {
        type = event.getActionMasked();
        time = System.currentTimeMillis();
        x = (int)event.getRawX();
        y = (int)event.getRawY();
        pressure = event.getPressure();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }
}
