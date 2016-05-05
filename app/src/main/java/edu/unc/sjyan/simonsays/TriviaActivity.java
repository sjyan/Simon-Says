package edu.unc.sjyan.simonsays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TriviaActivity extends AppCompatActivity {

    boolean firstActivity;
    ArrayList<Integer> doneActivities;
    Class nextActivity;
    int seconds;
    TextView time;
    Timer t;
    double timeOutStart;
    double timeOutEnd;
    boolean try1;
    double secs = 5000000000.0;
    double tester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        getSupportActionBar().hide();

        doneActivities = getIntent().getIntegerArrayListExtra("done");
        seconds = getIntent().getExtras().getInt("time");
        time = (TextView) findViewById(R.id.time);
        t = new Timer();
        manageTime();

        Log.v(this.getClass().toString(), "size: " + doneActivities + ", isEmpty; "
                + doneActivities.isEmpty());

        TextView question = (TextView)findViewById(R.id.triviaQuestion);
        try1=true;
        //call method to set a random question on activity creation
        randomQuestion(question);
        

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

    public void decideNext(int which) {
        switch (which) {
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
        Log.v("Head of activity stack", doneActivities.get(0).toString());
        doneActivities.remove(0);
        if(doneActivities.isEmpty()) {
            Log.v("Next activity is", "Final Activity");
        } else {
            Log.v("Next activity is", doneActivities.get(0).toString());
        }
        intent.putIntegerArrayListExtra("done", doneActivities);
        intent.putExtra("time", seconds);
        Log.v("This activity is", this.getClass().toString());
        Log.v("Starting activity", nextActivity.toString());
        startActivity(intent);
    }

    //selects a random question and sets the triviaQuestion text field
    public void randomQuestion(TextView tv) {
        Random r = new Random();
        int rand = r.nextInt(3 - 1) + 1;
        //use switch statement to select string based on value of rand
        switch(rand) {
            case(1):
                tv.setText(R.string.question1);
                break;
            case(2):
                tv.setText(R.string.question2);
                break;
            case(3):
                tv.setText(R.string.question3);
                break;
        }
        setChoices(rand);
    }

    //sets the answers
    public void setChoices(int rand) {
        RadioButton rb1 = (RadioButton)findViewById(R.id.rb1);
        RadioButton rb2 = (RadioButton)findViewById(R.id.rb2);
        RadioButton rb3 = (RadioButton)findViewById(R.id.rb3);
        RadioButton rb4 = (RadioButton)findViewById(R.id.rb4);
        switch(rand) {
            case(1):
                rb1.setText(R.string.q1a1);
                rb2.setText(R.string.q1a2);
                rb3.setText(R.string.q1a3);
                rb4.setText(R.string.q1a4);
                break;
            case(2):
                rb1.setText(R.string.q2a1);
                rb2.setText(R.string.q2a2);
                rb3.setText(R.string.q2a3);
                rb4.setText(R.string.q2a4);
                break;
            case(3):
                rb1.setText(R.string.q3a1);
                rb2.setText(R.string.q3a2);
                rb3.setText(R.string.q3a3);
                rb4.setText(R.string.q3a4);
                break;
        }
    }

    public void submit(View v) {
        RadioGroup rg = (RadioGroup)findViewById(R.id.radiogroup);
        int ans = rg.getCheckedRadioButtonId();
        timeOutStart = System.nanoTime();
        tester = 0;

        if(try1) {
            try1=false;
            switch (ans) {
                case (R.id.rb1):
                    Toast.makeText(getBaseContext(), "WRONG", Toast.LENGTH_SHORT).show();
                    timeOutEnd = System.nanoTime()+secs;
                    break;
                case (R.id.rb2):
                    Toast.makeText(getBaseContext(), "WRONG", Toast.LENGTH_SHORT).show();
                    timeOutEnd = System.nanoTime()+secs;
                    break;
                case (R.id.rb3):
                    Toast.makeText(getBaseContext(), "WRONG", Toast.LENGTH_SHORT).show();
                    timeOutEnd = System.nanoTime()+secs;
                    break;
                case (R.id.rb4):
                    timeOutEnd = System.currentTimeMillis();
                    if(doneActivities.isEmpty()) {
                        Intent intent = new Intent(this, FinalActivity.class);
                        intent.putExtra("time", seconds);
                        startActivity(intent);
                    } else {
                        decideNext(doneActivities.get(0));
                        handleIntent();
                    }
                    break;
            }
        } else {
            tester = System.nanoTime()-timeOutEnd;
            Log.v("TESTER VALUE", ""+tester);
            if(tester>0.0) {
                Log.v("Entered switch", "yay");
                switch (ans) {
                    case (R.id.rb1):
                        timeOutEnd = System.nanoTime()+secs;
                        Toast.makeText(getBaseContext(), "WRONG", Toast.LENGTH_SHORT).show();
                        break;
                    case (R.id.rb2):
                        Toast.makeText(getBaseContext(), "WRONG", Toast.LENGTH_SHORT).show();
                        timeOutEnd = System.nanoTime()+secs;
                        break;
                    case (R.id.rb3):
                        Toast.makeText(getBaseContext(), "WRONG", Toast.LENGTH_SHORT).show();
                        timeOutEnd = System.nanoTime()+secs;
                        break;
                    case (R.id.rb4):
                        timeOutEnd = System.nanoTime();
                        if(doneActivities.isEmpty()) {
                            Intent intent = new Intent(this, FinalActivity.class);
                            intent.putExtra("time", seconds);
                            startActivity(intent);
                        } else {
                            decideNext(doneActivities.get(0));
                            handleIntent();
                        }
                        break;
                }
            } else {
                Toast.makeText(getBaseContext(), "WAIT", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
