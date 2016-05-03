package edu.unc.sjyan.simonsays;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ClipDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StompActivity extends AppCompatActivity {

    Thread playThread;
    Handler fadeHandler;
    boolean playing;
    TextView score;
    int stomped;
    Vibrator v;
    private RelativeLayout l;
    ObjectAnimator bgFadeIn;
    ObjectAnimator bgFadeOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stomp);

        l = (RelativeLayout) findViewById(R.id.stomp_layout);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        score = (TextView) findViewById(R.id.stomp_score);
        fadeHandler = new Handler();
        bgFadeIn = ObjectAnimator.ofObject(l, "backgroundColor",
                new ArgbEvaluator(), Color.WHITE, Color.RED);
        bgFadeOut = ObjectAnimator.ofObject(l, "backgroundColor",
                new ArgbEvaluator(), Color.RED, Color.WHITE);
        bgFadeIn.setDuration(250);
        bgFadeOut.setDuration(250);
        randomAppear();
        playThread.start();
    }

    public void randomAppear() {
        playing = true;
        playThread = new Thread(new Runnable() {
            public void run() {
                while(!Thread.currentThread().isInterrupted() && playing) {
                    // run on ui
                    int randDelay = (int) Math.floor(Math.random() * 5000) + 300;
                    runOnUiThread(new Runnable() {
                        int simulAppear = (int) Math.floor(Math.random() * 3) + 1;
                        @Override
                        public void run() {
                            determineSpawns(simulAppear);
                        }
                    });

                    try {
                        Thread.sleep(randDelay);
                        runOnUiThread(new Runnable() {
                            @Override
                        public void run() {
                                resetColors();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public void determineSpawns(int totalSpawns) {
        for (int i = 0; i < totalSpawns; i++) {
            int bugID = (int) Math.floor(Math.random() * 9) + 1;
            int iid = getResources().getIdentifier("bug" + Integer.toString(bugID),
                    "id", getPackageName());
            ImageButton bug = (ImageButton) findViewById(iid);

            determineIfBad(bug);
            bug.setVisibility(View.VISIBLE);
            long delay = (long) Math.floor(Math.random() * 2000) + 10;
            timerDelayRemoveView(delay, bug);
        }
    }

    public void determineIfBad(ImageButton b) {
        double probBad = Math.random();
        if(probBad < 0.33) {
            b.setColorFilter(Color.RED);
        }
    }

    public void resetColors() {
        for(int i = 1; i <= 9; i++) {
            int iid = getResources().getIdentifier("bug" + Integer.toString(i),
                    "id", getPackageName());
            ImageButton bug = (ImageButton) findViewById(iid);
            bug.clearColorFilter();
        }
    }

    public void timerDelayRemoveView(long time, final ImageButton b) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            public void run() {
                b.setVisibility(View.INVISIBLE);
            }
        }, time);
    }


    public boolean isBad(View v) {
        if(((ImageButton) v).getColorFilter() != null) {
            return true;
        }

        return false;
    }


    public void onClick(View v) {
        if(isBad(v)) {
            stomped--;
            score.setText(stomped + "/10");
            this.v.vibrate(100);
            v.setVisibility(View.INVISIBLE);
            bgFadeIn.start();
            bgFadeOut.start();
        } else {
            stomped++;
            score.setText(stomped + "/10");
            this.v.vibrate(100);
            v.setVisibility(View.INVISIBLE);
        }
    }

}
