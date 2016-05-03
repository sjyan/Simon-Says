package edu.unc.sjyan.simonsays;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {

    TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        int finalTime = getIntent().getExtras().getInt("time");
        score = (TextView) findViewById(R.id.time_finished);
        score.setText(finalTime + "seconds");
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.again:
                break;
            case R.id.share:
                break;
        }
    }
}
