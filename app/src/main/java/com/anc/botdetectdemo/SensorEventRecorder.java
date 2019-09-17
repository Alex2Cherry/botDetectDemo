package com.anc.botdetectdemo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.anc.botdetectdemo.json.IOUtils;
import com.anc.botdetectdemo.json.JsonArrayBuilder;
import com.anc.botdetectdemo.json.JsonObjectBuilder;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;


public class SensorEventRecorder implements SensorEventListener {

    private Deque<SensorData> mAccdataQueue;

    private Boolean mIsContinue;

    private String mSensorType;

    public SensorEventRecorder(String sensorType) {
        mIsContinue = true;
        mSensorType = sensorType;
        mAccdataQueue = new ArrayDeque<>();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (!mIsContinue) {
            return;
        }

        if (!mAccdataQueue.isEmpty()
                && mAccdataQueue.size() >= 300) {
            mAccdataQueue.removeFirst();
        }

        mAccdataQueue.addLast(new SensorData(event));
        Log.e("DETECT_DEMO", "SensorEvent: " + event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    /*
    public void writeToFile() {
        Log.d("DETECT_DEMO", "SensorEventRecorder Write File: ");

        mIsContinue = false;

        final List<SensorData> eventList = new ArrayList<>();

        eventList.addAll(mAccdataQueue);
        mAccdataQueue.clear();

        if (eventList.isEmpty()) {
            return;
        }

        final JsonArrayBuilder builder = new JsonArrayBuilder();
        for (SensorData event : eventList) {
            JsonObjectBuilder job = new JsonObjectBuilder()
                    .add("t", event.getTime())
                    .add("x", event.getX())
                    .add("y", event.getY())
                    .add("z", event.getZ());

            builder.add(job.toJson());
        }

        JsonObjectBuilder job = new JsonObjectBuilder()
                .add("acc", builder.toJson());

        File dataDir = new File("/sdcard/Download");
        if (!dataDir.exists() && !dataDir.mkdir()) {
            Log.e("DETECT_DEMO", "Make dir failed: /sdcard/Download");
            return;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        String fileName = String.format("/sdcard/Download/sensor-%s.txt", format.format(new Date()));
        try {
            String data = job.toJson().toString();
            Log.e("DETECT_DEMO", "json: " + data);
            IOUtils.writeToFile(new File(fileName), data.getBytes());
        } catch (IOException e) {
        }
    }
    */

    public JSONArray getData() {
        mIsContinue = false;

        final List<SensorData> eventList = new ArrayList<>();

        eventList.addAll(mAccdataQueue);
        mAccdataQueue.clear();

        final JsonArrayBuilder builder = new JsonArrayBuilder();
        for (SensorData event : eventList) {
            JsonObjectBuilder job = new JsonObjectBuilder()
                    .add("t", event.getTime())
                    .add("x", event.getX())
                    .add("y", event.getY())
                    .add("z", event.getZ());

            builder.add(job.toJson());
        }

        return builder.toJson();
    }

    public String getType() {
        return mSensorType;
    }
}
