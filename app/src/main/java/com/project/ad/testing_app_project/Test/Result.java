package com.project.ad.testing_app_project.Test;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.project.ad.testing_app_project.R;
import com.project.ad.testing_app_project.Starting;

import org.w3c.dom.Text;

public class Result extends AppCompatActivity implements View.OnClickListener {

    TextView textView_result;
    String  numberOfAnswers;
    Button finish;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textView_result = (TextView) findViewById(R.id.textView_result);
        textView_result.setTypeface(Typeface.create("casual", Typeface.BOLD));
        finish = (Button) findViewById(R.id.btn_finish);

        //getting number of correct answers from quiz.class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            numberOfAnswers = extras.getString("num_correctAnswers");
        }

        //initialing colors for background animation
        int color1 = ContextCompat.getColor(this, R.color.red);
        int color2 = ContextCompat.getColor(this, R.color.blue);
        int color3 = ContextCompat.getColor(this, R.color.green);
        int color4 = ContextCompat.getColor(this, R.color.grey);
        int color5 = ContextCompat.getColor(this, R.color.yellow);

        //animation start
        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), color1, color2, color3, color4, color5);
        colorAnimation.setDuration(10000);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                findViewById(R.id.activity_result).setBackgroundColor((int) valueAnimator.getAnimatedValue());
            }
        });
        colorAnimation.setRepeatCount(Animation.INFINITE);
        colorAnimation.start();

        textView_result.setText("You have " + numberOfAnswers + " correct answers");
        finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == finish){
            startActivity(new Intent(getApplicationContext(), Starting.class));
        }

    }
}
