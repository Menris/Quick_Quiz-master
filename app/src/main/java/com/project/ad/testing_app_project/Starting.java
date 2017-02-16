package com.project.ad.testing_app_project;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ad.testing_app_project.Tab_profile.My_Profile;
import com.project.ad.testing_app_project.Test.Quiz;

public class Starting extends Activity implements View.OnClickListener{

    TextView tv_profile;
    Button enterPIN;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref = databaseReference.child("Tests");
    EditText pinNumber;



    Boolean success_pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_starting);



        enterPIN = (Button) findViewById(R.id.btn_enter_pin);

        int color1 = ContextCompat.getColor(this,R.color.red);
        int color2 = ContextCompat.getColor(this,R.color.blue);
        int color3 = ContextCompat.getColor(this,R.color.green);
        int color4 = ContextCompat.getColor(this,R.color.grey);
        int color5 = ContextCompat.getColor(this,R.color.yellow);



        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),color1,color2,color3,color4,color5);
        colorAnimation.setDuration(10000);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                findViewById(R.id.activity_starting).setBackgroundColor((int) valueAnimator.getAnimatedValue());
            }
        });
        colorAnimation.setRepeatCount(Animation.INFINITE);
        colorAnimation.start();

        tv_profile = (TextView) findViewById(R.id.textView_profile);
        pinNumber = (EditText) findViewById(R.id.editText_pin);

        pinNumber.setOnClickListener(this);
        tv_profile.setOnClickListener(this);
        enterPIN.setOnClickListener(this);

        }

    @Override
    public void onClick(View view) {

        if (view == enterPIN){
            success_pin = false;
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {

                        if (pinNumber.getText().toString().equals(child.getKey())){
                            Log.w("MyApp","WORKING");
                            Log.w("MyPin",pinNumber.getText().toString());

                            Intent intent = new Intent(getApplicationContext(), Quiz.class);
                            intent.putExtra("PIN",pinNumber.getText().toString());
                            startActivity(intent);
                            success_pin = true;

                        }
                    }
                    if (success_pin == false) {
                        Log.w("AMIR", "wrong PIN");
                        pinNumber.setError("Your PIN number is incorrect");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("The read failed: " ,"My ERRRRRRRRRRRRRRoR");
                }
            });

        }

        if (view == tv_profile){
            startActivity(new Intent(this, My_Profile.class));
        }
    }
}
