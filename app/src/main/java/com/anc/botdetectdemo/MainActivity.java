package com.anc.botdetectdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anc.botdetectdemo.json.IOUtils;
import com.anc.botdetectdemo.json.JsonArrayBuilder;
import com.anc.botdetectdemo.json.JsonObjectBuilder;
import com.anc.botdetectdemo.SensorEventRecorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    //TextView notice;
    ImageView mail, profile;
    private Deque<TouchEvent> mTouchEventQueue;
    private int mOldX;
    private int mOldY;

    private SensorManager mSensorManager;
    private Sensor mSensorAcc;
    private SensorEventRecorder mAccRecordor;
    private Sensor mSensorGyro;
    private SensorEventRecorder mGyroRecordor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTouchEventQueue = new ArrayDeque<>();
        mOldX = 0;
        mOldY = 0;

        //init touch
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(username.getText().toString(), "admin") && Objects.equals(password.getText().toString(), "admin")) {
                    Toast.makeText(MainActivity.this, "Record Paht: /sdcard/Download/touch-xxxx.txt", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Record Paht: /sdcard/Download/touch-xxxx.txt", Toast.LENGTH_LONG).show();
                }

                writeToFile();

                //sensor
                mSensorManager.unregisterListener(mAccRecordor, mSensorAcc);
                mSensorManager.unregisterListener(mGyroRecordor, mSensorGyro);
            }
        });

        //notice = findViewById(R.id.notice);
        //notice.setText("Life, thin and light-off time and time again \\n Frivolous tireless \\n one \\n I heard the echo, from the valleys and the heart \\n Open to the lonely soul of sickle harvesting \\n Repeat outrightly, but also repeat the well-being of \\n Eventually swaying in the desert oasis \\n I believe I am \\n Born as the bright summer flowers \\n Do not withered undefeated fiery demon rule \\n Heart rate and breathing to bear the load of the cumbersome \\n Bored \\n Two \\n I heard the music, from the moon and carcass \\n Auxiliary extreme aestheticism bait to capture misty \\n Filling the intense life, but also filling the pure \\n There are always memories throughout the earth \\n I believe I am \\n Died as the quiet beauty of autumn leaves \\n Sheng is not chaos, smoke gesture \\n Even wilt also retained bone proudly Qing Feng muscle \\n Occult \\n Three \\n I hear love, I believe in love \\n Love is a pool of struggling blue-green algae \\n As desolate micro-burst of wind \\n Bleeding through my veins \\n Years stationed in the belief \\n Four \\n I believe that all can hear \\n Even anticipate discrete, I met the other their own \\n Some can not grasp the moment \\n Left to the East to go West, the dead must not return to nowhere \\n See, I wear Zan Flowers on my head, in full bloom along the way all the way \\n Frequently missed some, but also deeply moved by wind, frost, snow or rain \\n Five \\n Prajna Paramita, soon as soon as \\n life be beautiful like summer flowers and death like autumn leaves \\n Also care about what has");

        mail = findViewById(R.id.imageView);
        mail.setImageResource(R.drawable.mail);

        profile = findViewById(R.id.imageView2);
        profile.setImageResource(R.drawable.logo);

        //init sensor event
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccRecordor = new SensorEventRecorder("acc");
        mSensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mAccRecordor, mSensorAcc, SensorManager.SENSOR_DELAY_NORMAL); // 根据频率调整

        mGyroRecordor = new SensorEventRecorder("gyro");
        mSensorGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(mGyroRecordor, mSensorGyro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //Log.e("DETECT_DEMO", "MainActivity-----------dispatchTouchEvent--------" + event.toString());
        record(event);
        return super.dispatchTouchEvent(event);
    }

    private void record(MotionEvent motionEvent) {
        final int action = motionEvent.getActionMasked();
        final int index = motionEvent.getActionIndex();

        if (index != 0 && motionEvent.getPointerId(index) != 0) {
            return;
        }

        if (motionEvent.getRawY() < 0 || motionEvent.getRawX() < 0) {
            return;
        }

        if (!mTouchEventQueue.isEmpty()
                && mTouchEventQueue.size() >= 64) {
            mTouchEventQueue.removeFirst();
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (mOldX == motionEvent.getRawX() && mOldY == motionEvent.getRawY()) {
                    break;
                }

                mTouchEventQueue.addLast(new TouchEvent(motionEvent));
                Log.e("DETECT_DEMO", mOldX + "," + mOldY + "----" + motionEvent.toString());

                mOldX = (int) motionEvent.getRawX();
                mOldY = (int) motionEvent.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                mTouchEventQueue.addLast(new TouchEvent(motionEvent));
                Log.e("DETECT_DEMO", mOldX + "," + mOldY + "----" + motionEvent.toString());

                mOldX = 0;
                mOldY = 0;
                break;

            default:
                Log.e("DETECT_DEMO", "onTouch: unKnow event:" + motionEvent.getAction());
                break;

        }
    }

    private String getScreemPix() {
        WindowManager mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return width + "*" + height;
    }

    private void writeToFile() {

        final List<TouchEvent> eventList = new ArrayList<>();

        eventList.addAll(mTouchEventQueue);
        mTouchEventQueue.clear();

        if (eventList.isEmpty()) {
            return;
        }

        final JsonArrayBuilder builder = new JsonArrayBuilder();
        for (TouchEvent event : eventList) {
            JsonObjectBuilder job = new JsonObjectBuilder()
                    .add("p", event.getType())
                    .add("i", event.getTime())
                    .add("x", event.getX())
                    .add("y", event.getY())
                    .add("f", event.getPressure());

            builder.add(job.toJson());
        }

        JsonObjectBuilder job = new JsonObjectBuilder()
                .add("ps", getScreemPix())
                .add("ts", builder.toJson())
                .add(mAccRecordor.getType(), mAccRecordor.getData())
                .add(mGyroRecordor.getType(), mGyroRecordor.getData());

        String dirPath = Environment.getExternalStorageDirectory().getPath() + "/Download"; // /sdcard/Download
        File dataDir = new File(dirPath);
        if (!dataDir.exists() && !dataDir.mkdir()) {
            Log.e("DETECT_DEMO", "Make dir failed: " + dirPath);
            return;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        String fileName = String.format(dirPath + "/detect-%s.txt", format.format(new Date()));
        try {
            String data = job.toJson().toString();
            Log.e("DETECT_DEMO", "json: " + data);
            IOUtils.writeToFile(new File(fileName), data.getBytes());
        } catch (IOException e) {
        }
    }

}
