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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.play:
                Intent intent = new Intent(this, WaitingRoom.class);
                startActivity(intent);
                break;
            case R.id.help:
                new AlertDialog.Builder(this)
                    .setTitle("What's this?")
                    .setMessage("Simon says? Forget Simon. This time Dr. Nirjon is doing the " +
                            "talking and this time he means business. Connect with your friends" +
                            " and/or strangers to play them in an intense round of tasks, and see" +
                            " who can do it the fastest!")
                    .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .show();
                break;
        }
    }
}
