package edu.unc.sjyan.simonsays;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.plattysoft.leonids.ParticleSystem;

/**
 * Created by brianluong on 5/2/16.
 */
public class TypeGameAnimation extends View {

    private int incorrectColor = Color.argb(127, 228, 54, 54);
    private int correctColor = Color.argb(200, 77, 228, 54);

    public TypeGameAnimation(Context con) {
        super(con);
    }
    public TypeGameAnimation(Context con, AttributeSet att){
        super(con, att);
    }
    public TypeGameAnimation(Context con, AttributeSet att, int s){
        super(con, att, s);
    }
    public TypeGameAnimation(Context con, AttributeSet att, int s1, int s2){
        super(con, att, s1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        double percentCorrect = TypeGameActivity.getPercentCorrect();
        Paint correctPaint = new Paint();
        Paint incorrectPaint = new Paint();
        correctPaint.setColor(correctColor);
        incorrectPaint.setColor(incorrectColor);

        canvas.drawRect(
                0,
                0,
                (float) percentCorrect * this.getWidth(),
                this.getHeight(),
                correctPaint);
        canvas.drawRect(
                (float) percentCorrect * this.getWidth(),
                0,
                this.getWidth(),
                this.getHeight(),
                incorrectPaint);

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setTextSize(50);
        Log.v("", TypeGameActivity.randomString);
        canvas.drawText(percentCorrect == 100.0 ? "CONGRATS" : TypeGameActivity.randomString, 75, 75, p);
    }
}
