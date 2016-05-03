package edu.unc.sjyan.simonsays;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShakeActivity extends AppCompatActivity implements SensorEventListener {

    boolean firstActivity;
    ArrayList<String> doneActivities;
    Class nextActivity;
    int seconds;
    TextView time;
    Timer t;
    SensorManager sm;
    private Sensor s;
    private List<Sensor> l;
    ProgressBar pb;
    float progress;
    TextView percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        doneActivities = getIntent().getStringArrayListExtra("done");
        for(String s : doneActivities) {
            Log.v("done activities", s);
        }
        seconds = getIntent().getExtras().getInt("time");
        time = (TextView) findViewById(R.id.time);
        t = new Timer();
        manageTime();

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        l = sm.getSensorList(Sensor.TYPE_ALL);
        s = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sm.registerListener(this, s, 1000000);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        percent = (TextView) findViewById(R.id.percent);
        progress = 0;
    }

    public void manageTime() {
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time.setText(seconds + "");
                        seconds++;
                    }
                });
            }
        }, 1000, 1000);
    }

    public void decideNext() {
        int which = (int) Math.floor(Math.random() * 4) + 1;
        switch(which) {
            case 1:
                nextActivity = StompActivity.class;
                break;
            case 2:
                nextActivity = ShakeActivity.class;
                break;
            case 3:
                nextActivity = TriviaActivity.class;
                break;
            case 4:
                nextActivity = TypeGameActivity.class;
                break;
        }

        if(doneActivities.size() == 0) {
            doneActivities.add(this.getClass().toString());
            firstActivity = true;
        } else {
            for (String s : doneActivities) {
                Log.v("Comparing", s + " vs. " + nextActivity.toString());
                if (nextActivity.toString().equals(s)) {
                    Log.v("Done already", s);
                    decideNext();
                } else {
                    Log.v("Decided activity", nextActivity.toString());
                    return;
                }
            }
        }
    }

    public void handleIntent() {
        Intent intent = new Intent(this, nextActivity);
        if(!firstActivity) doneActivities.add(this.getClass().toString());
        intent.putStringArrayListExtra("done", doneActivities);
        intent.putExtra("time", seconds);
        startActivity(intent);
    }

        @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        updateProgress(x, y, z);
    }

    public void updateProgress(float x, float y, float z) {
        progress += 0.1 * calculateRotationRate(x, y, z);
        pb.setProgress((int) progress);
        percent.setText((int) progress + "%");
        Log.v("Shake progress", "" + progress);

        if(((int) progress) >= 100) {
            sm.unregisterListener(this);
            if(doneActivities.size() >= 4) {
                Intent intent = new Intent(this, FinalActivity.class);
                intent.putExtra("time", seconds);
                startActivity(intent);
            } else {
                decideNext();
                handleIntent();
            }
        }
    }

    // if not in the list of completed activities, start that activity

    private float calculateRotationRate(float x, float y, float z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}