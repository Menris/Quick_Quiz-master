package com.project.ad.testing_app_project.Test;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

        textView_result = (TextView) findViewById(R.id.textView_result);
        textView_result.setTypeface(Typeface.create("casual", Typeface.BOLD));
        finish = (Button) findViewById(R.id.btn_finish);

        //getting number of correct answers from quiz.class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            numberOfAnswers = extras.getString("num_correctAnswers");
        }

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
