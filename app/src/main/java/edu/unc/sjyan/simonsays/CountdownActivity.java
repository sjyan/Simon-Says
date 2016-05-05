package edu.unc.sjyan.simonsays;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by brianluong on 5/2/16.
 */
public class CountdownActivity extends AppCompatActivity {

    Vibrator vibr;
    Class nextActivity;
    ArrayList<Integer> doneActivities;
    static Timer timer;
    int time;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        getSupportActionBar().hide();

        doneActivities = new ArrayList<>();
        for(int i = 1; i <= 4; i++) {
            doneActivities.add(i);
        }

        vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        startTimer();
    }

    // Generate random permutation for activity sequence instead of
    // check off list every loaded activity
    public void shuffleOrder(ArrayList<Integer> a) {
        java.util.Collections.shuffle(a);
    }

    public void decideNext(int which) {
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

    }

    public void handleIntent() {
        Intent intent = new Intent(this, nextActivity);

        // send timer and queued activities info
        int seconds = 0;
        Log.v("Head of activity stack", doneActivities.get(0).toString());
        doneActivities.remove(0);
        Log.v("Next activity in stack", doneActivities.get(0).toString());
        intent.putIntegerArrayListExtra("done", doneActivities);
        intent.putExtra("time", seconds);
        Log.v("This activity is", this.getClass().toString());
        Log.v("Starting activity", nextActivity.toString());
        startActivity(intent);
    }

    protected void startTimer() {
        timer = new Timer();
        time = 4;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                time--;
                Log.v("TT", Integer.toString(time));
                mHandler.obtainMessage().sendToTarget();
                if (time == 0) {
                    this.cancel();
                    shuffleOrder(doneActivities);
                    decideNext(doneActivities.get(0));
                    handleIntent();

                }
            }
        }, 0, 3000);
    }


    /*
    public void handler(int time) {
        TextView v = (TextView) findViewById(R.id.mockCountDownText);
        v.setText(Integer.toString(time));
    }
    */

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            TextView v = (TextView) findViewById(R.id.mockCountDownText);
            vibr.vibrate(100);
            v.setText(Integer.toString(time));
            YoYo.with(Techniques.Shake).playOn(v);
        }
    };

}