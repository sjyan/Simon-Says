package edu.unc.sjyan.simonsays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by brianluong on 5/2/16.
 */
public class TypeGameActivity extends AppCompatActivity {

    boolean firstActivity;
    ArrayList<Integer> doneActivities;
    Class nextActivity;
    int seconds;
    TextView time;
    Timer t;
    protected static String randomString = "";
    protected static String currentString = "";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_game);

        Log.v(this.getClass().toString(), "size: " + doneActivities + ", isEmpty; "
                + doneActivities.isEmpty());

        doneActivities = getIntent().getIntegerArrayListExtra("done");
        seconds = getIntent().getExtras().getInt("time");
        time = (TextView) findViewById(R.id.time);
        t = new Timer();
        manageTime();

        TextView titleTextView = (TextView) findViewById(R.id.typeGameTitleText);
        EditText answerEditText = (EditText) findViewById(R.id.typeGameEditText);
        TypeGameAnimation promptTextViewAnimation =
                (TypeGameAnimation) findViewById(R.id.typeGamePromptTextAnimation);

        YoYo.with(Techniques.Bounce).playOn(titleTextView);
        randomString = generateRandomString();
        promptTextViewAnimation.invalidate();
        answerEditText.addTextChangedListener(watcher);
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
        Log.v("This activity is", this.getClass().toString());
        Log.v("Next activity is", nextActivity.toString());
        intent.putIntegerArrayListExtra("done", doneActivities);
        intent.putExtra("time", seconds);
        startActivity(intent);
    }

    private String generateRandomString() {
        // String is of length 10-15
        int length = (int) (Math.random() * 6) + 10;
        String randomString = "";
        for (int i = 0; i < length; i++) {
            randomString += (char) (Math.random() * 94 + 33);
        }
        return randomString;
    }

    public Activity getThis() {
        return this;
    }
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            currentString = s.toString();
            TypeGameAnimation promptTextViewAnimation =
                    (TypeGameAnimation) findViewById(R.id.typeGamePromptTextAnimation);
            promptTextViewAnimation.invalidate();
            if (currentString.equals(randomString)) {
                if(doneActivities.isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), FinalActivity.class);
                    intent.putExtra("time", seconds);
                    startActivity(intent);
                } else {
                    decideNext(doneActivities.get(0));
                    handleIntent();
                }
                new ParticleSystem(getThis(), 50, R.drawable.confetti , 1000)
                        .setSpeedRange(0.2f, 0.5f)
                        .oneShot(findViewById(R.id.typeGamePromptTextAnimation), 400);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static double getPercentCorrect() {
        double charsCorrect = 0;
        for (int i = 0; i < (Math.min(randomString.length(), currentString.length())); i++) {
            if (randomString.charAt(i) == currentString.charAt((i))) {
                charsCorrect++;
            } else {
                break;
            }
        }
        return charsCorrect > 0 ? charsCorrect / randomString.length() : 0;
    }
}