package edu.unc.sjyan.simonsays;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.plattysoft.leonids.ParticleSystem;

public class FinalActivity extends AppCompatActivity {

    ShareDialog shareDialog;
    CallbackManager callbackManager;
    TextView score;
    int finalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_final);
        getSupportActionBar().hide();

        // for Facebook sharing
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        finalTime = getIntent().getExtras().getInt("time");
        score = (TextView) findViewById(R.id.time_finished);
        score.setText("in " + finalTime + " seconds");

        promptAnimation();
    }

    public void promptAnimation() {
        new ParticleSystem(this, 50, R.drawable.nirjon , 3000)
                .setSpeedRange(0.2f, 0.5f)
                .oneShot(findViewById(R.id.nirjon_end), 500);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.again:
                Intent intent = new Intent(this, WaitingRoom.class);
                startActivity(intent);
                break;
            case R.id.share:
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Can you beat me?")
                        .setContentDescription("I won in " + finalTime + " seconds")
                        .setContentUrl(Uri.parse("http://www.nirjonsays.com"))
                        .build();
                shareDialog.show(linkContent);
                break;
        }
    }
}
