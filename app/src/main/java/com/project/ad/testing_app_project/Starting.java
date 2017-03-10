package com.project.ad.testing_app_project;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Nearable;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.ad.testing_app_project.Tab_profile.My_Profile;
import com.project.ad.testing_app_project.Tab_profile.tab2.Check_quiz;
import com.project.ad.testing_app_project.Test.Quiz;
import com.project.ad.testing_app_project.Test.Quiz_question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Starting extends Activity implements View.OnClickListener {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref = databaseReference.child("Tests");
    public FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView tv_profile;
    Button enterPIN;
    Button btn_manageGroup;
    Button btn_beaconConnect;
    EditText pinNumber;
    Boolean success_pin;

    String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_starting);

        //Firebase things
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //Entering PIN number
        pinNumber = (EditText) findViewById(R.id.editText_pin);
        //button to go to quiz activity with entered PIN
        enterPIN = (Button) findViewById(R.id.btn_enter_pin);
        //go to Beacon Connection activity
        btn_beaconConnect = (Button) findViewById(R.id.btn_beaconConnect);
        //button for teachers allows to give access through beacon to exact group
        btn_manageGroup = (Button) findViewById(R.id.btn_manageGroup);
        //textView go to profile
        tv_profile = (TextView) findViewById(R.id.textView_profile);

        //showing btn_manageGroup button only for teachers
        Query info = databaseReference.child("userInformation").child(user.getUid());
        info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Quiz_question quiz_question = dataSnapshot.getValue(Quiz_question.class);
                Log.e("Role ", quiz_question.getRole());
                role = quiz_question.getRole();

                if (Objects.equals(role, "teachers")) {
                    btn_manageGroup.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //initialing colors for background animation
        int color1 = ContextCompat.getColor(this, R.color.red);
        int color2 = ContextCompat.getColor(this, R.color.blue);
        int color3 = ContextCompat.getColor(this, R.color.green);
        int color4 = ContextCompat.getColor(this, R.color.grey);
        int color5 = ContextCompat.getColor(this, R.color.yellow);

        //animation start
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), color1, color2, color3, color4, color5);
        colorAnimation.setDuration(10000);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                findViewById(R.id.activity_starting).setBackgroundColor((int) valueAnimator.getAnimatedValue());
            }
        });
        colorAnimation.setRepeatCount(Animation.INFINITE);
        colorAnimation.start();

        btn_manageGroup.setOnClickListener(this);
        btn_beaconConnect.setOnClickListener(this);
        pinNumber.setOnClickListener(this);
        tv_profile.setOnClickListener(this);
        enterPIN.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
    }


    @Override
    public void onClick(View view) {
        //on ENTER PIN button click
        if (view == enterPIN) {
            //bool value for checking PIN correction
            success_pin = false;
            //checking if PIN exist in database
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //checking equality with PIN numbers
                        if (pinNumber.getText().toString().equals(child.getKey())) {
                            Log.w("MyApp", "WORKING");
                            Log.w("MyPin", pinNumber.getText().toString());

                            Intent intent = new Intent(getApplicationContext(), Quiz.class);
                            intent.putExtra("PIN", pinNumber.getText().toString());
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
                    Log.w("The read failed: ", "My ERRRRRRRRRRRRRRoR");
                }
            });

        }

        if (view == tv_profile) {
            startActivity(new Intent(this, My_Profile.class));
        }

        if (view == btn_beaconConnect) {
            Intent intent = new Intent(getApplicationContext(), BeaconConnect.class);
            startActivity(intent);
        }

        if (view == btn_manageGroup) {

            AlertDialog.Builder alert = new AlertDialog.Builder(Starting.this);
            alert.setTitle("Group Search");
            alert.setMessage("Input Group");
            Context context = getApplicationContext();
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText inputPIN = new EditText(context);
            inputPIN.setSingleLine(true);
            inputPIN.setHint("PIN");
            inputPIN.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
            inputPIN.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            layout.addView(inputPIN);

            final EditText inputGroup = new EditText(context);
            inputGroup.setHint("Group");
            inputGroup.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
            inputGroup.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            layout.addView(inputGroup);

            alert.setView(layout);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final String resultPIN = inputPIN.getText().toString();
                    final String resultGroup = inputGroup.getText().toString();

                    databaseReference.child("beacon").child("groups").child(resultGroup).child("group").setValue(resultGroup);
                    databaseReference.child("beacon").child("groups").child(resultGroup).child("quizID").setValue(resultPIN);
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            alert.show();
        }

    }
}
