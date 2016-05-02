package edu.unc.sjyan.simonsays;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sm;
    private Sensor s;
    private List<Sensor> l;
    ProgressBar pb;
    float progress;
    TextView percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        l = sm.getSensorList(Sensor.TYPE_ALL);
        s = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sm.registerListener(this, s, 1000000);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        percent = (TextView) findViewById(R.id.percent);
        progress = 0;
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

        if(((int) progress) == 100) {
            // go to next activity!
        }
    }

    private float calculateRotationRate(float x, float y, float z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
