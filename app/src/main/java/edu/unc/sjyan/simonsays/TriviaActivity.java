package edu.unc.sjyan.simonsays;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TriviaActivity extends AppCompatActivity {

    double timeOutStart;
    double timeOutEnd;
    boolean try1;
    double secs = 5000000000.0;
    double tester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        TextView question = (TextView)findViewById(R.id.triviaQuestion);
        try1=true;
        //call method to set a random question on activity creation
        randomQuestion(question);
        

    }

    //selects a random question and sets the triviaQuestion text field
    public void randomQuestion(TextView tv) {
        int rand = 1+(int)(Math.random()*((10-1)+1));

        //use switch statement to select string based on value of rand
        tv.setText(R.string.question1);

        setChoices();
    }

    //sets the answers
    public void setChoices() {
        RadioButton rb1 = (RadioButton)findViewById(R.id.rb1);
        RadioButton rb2 = (RadioButton)findViewById(R.id.rb2);
        RadioButton rb3 = (RadioButton)findViewById(R.id.rb3);
        RadioButton rb4 = (RadioButton)findViewById(R.id.rb4);

        rb1.setText(R.string.q1a1);
        rb2.setText(R.string.q1a2);
        rb3.setText(R.string.q1a3);
        rb4.setText(R.string.q1a4);
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
                    Toast.makeText(getBaseContext(), "CORRECT!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getBaseContext(), "CORRECT!", Toast.LENGTH_SHORT).show();
                        break;
                }
            } else {
                Toast.makeText(getBaseContext(), "WAIT", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
