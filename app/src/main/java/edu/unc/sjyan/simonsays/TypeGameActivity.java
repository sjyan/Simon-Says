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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.plattysoft.leonids.ParticleSystem;

/**
 * Created by brianluong on 5/2/16.
 */
public class TypeGameActivity extends AppCompatActivity {

    protected static String randomString = "";
    protected static String currentString = "";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_game);
        TextView titleTextView = (TextView) findViewById(R.id.typeGameTitleText);
        EditText answerEditText = (EditText) findViewById(R.id.typeGameEditText);
        TypeGameAnimation promptTextViewAnimation = (TypeGameAnimation) findViewById(R.id.typeGamePromptTextAnimation);

        YoYo.with(Techniques.Bounce).playOn(titleTextView);
        randomString = generateRandomString();
        promptTextViewAnimation.invalidate();
        answerEditText.addTextChangedListener(watcher);
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
            TypeGameAnimation promptTextViewAnimation = (TypeGameAnimation) findViewById(R.id.typeGamePromptTextAnimation);
            promptTextViewAnimation.invalidate();
            if (currentString.equals(randomString)) {
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

    public void toShake(View v) {
        Button button = (Button)findViewById(R.id.shakeButton);
        if(button.getId()==R.id.shakeButton) {
            Intent intent = new Intent(this, MainActivity.class);
            Log.v("TEST BUTTON", "BUTTON WORKS");
            startActivity(intent);
        }
    }
}