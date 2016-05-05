package edu.unc.sjyan.simonsays;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    Class nextActivity;
    ArrayList<Integer> doneActivities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        doneActivities = new ArrayList<>();
        for(int i = 1; i <= 4; i++) {
            doneActivities.add(i);
        }
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

    // Generate random permutation for activity sequence instead of
    // check off list every loaded activity
    public void shuffleOrder(ArrayList<Integer> a) {
        java.util.Collections.shuffle(a);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.play:
                shuffleOrder(doneActivities);
                decideNext(doneActivities.get(0));
                handleIntent();
                break;
            case R.id.help:
                new AlertDialog.Builder(this)
                    .setTitle("What's this?")
                    .setMessage("The greatest game ever made")
                    .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .show();
        }
    }
}
