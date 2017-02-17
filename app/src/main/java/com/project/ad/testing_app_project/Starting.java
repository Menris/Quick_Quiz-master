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

    private static final Map<String, List<String>> PLACES_BY_BEACONS;

    // TODO: replace "<major>:<minor>" strings to match your own beacons.
    static {
        Map<String, List<String>> placesByBeacons = new HashMap<>();
        placesByBeacons.put("50850:18329", new ArrayList<String>() {{
            add("FIRST");
        }});
        /*placesByBeacons.put("7013:23859", new ArrayList<String>() {{
            add("SECOND");
        }});*/
        PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
    }

    private List<String> placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return Collections.emptyList();
    }

    private BeaconManager beaconManager;
    private Region region;
    Beacon nearestBeacon;
    String proximity;
    String group;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_starting);
        //beacon connection
        beaconManager = new BeaconManager(this);
        beaconManager.setBackgroundScanPeriod(5000, 10000);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(com.estimote.sdk.Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    nearestBeacon = list.get(0);
                    List<String> places = placesNearBeacon(nearestBeacon);

                    Log.e("Current Proximity - ", String.valueOf(computeProximity(nearestBeacon)));
                    proximity = String.valueOf(computeProximity(nearestBeacon));
                    Log.d("Airport", "Nearest places: " + places);
                }
            }
        });
        region = new com.estimote.sdk.Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
        //end of beacon connection

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        btn_manageGroup = (Button) findViewById(R.id.btn_manageGroup);
        btn_beaconConnect = (Button) findViewById(R.id.btn_beaconConnect);
        enterPIN = (Button) findViewById(R.id.btn_enter_pin);

        int color1 = ContextCompat.getColor(this, R.color.red);
        int color2 = ContextCompat.getColor(this, R.color.blue);
        int color3 = ContextCompat.getColor(this, R.color.green);
        int color4 = ContextCompat.getColor(this, R.color.grey);
        int color5 = ContextCompat.getColor(this, R.color.yellow);


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

        tv_profile = (TextView) findViewById(R.id.textView_profile);
        pinNumber = (EditText) findViewById(R.id.editText_pin);

        btn_manageGroup.setOnClickListener(this);
        btn_beaconConnect.setOnClickListener(this);
        pinNumber.setOnClickListener(this);
        tv_profile.setOnClickListener(this);
        enterPIN.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
    }

    public static double computeAccuracy(Beacon beacon) {
        if (beacon.getRssi() == 0) {
            return -1.0D;
        }

        double ratio = beacon.getRssi() / beacon.getMeasuredPower();
        double rssiCorrection = 0.96D + Math.pow(Math.abs(beacon.getRssi()), 3.0D) % 10.0D / 150.0D;

        if (ratio <= 1.0D) {
            return Math.pow(ratio, 9.98D) * rssiCorrection;
        }
        return (0.103D + 0.89978D * Math.pow(ratio, 7.71D)) * rssiCorrection;
    }

    public static Utils.Proximity proximityFromAccuracy(double accuracy) {
        if (accuracy < 0.0D) {
            return Utils.Proximity.UNKNOWN;
        }
        if (accuracy < 0.5D) {
            return Utils.Proximity.IMMEDIATE;
        }
        if (accuracy <= 3.0D) {
            return Utils.Proximity.NEAR;
        }
        return Utils.Proximity.FAR;
    }


    public static Utils.Proximity computeProximity(Beacon beacon) {
        return proximityFromAccuracy(computeAccuracy(beacon));
    }

    private void connectBeacon() {
        dialog = ProgressDialog.show(Starting.this, "", "Loading...", true);
        if (Objects.equals(proximity, "IMMEDIATE")) {

            DatabaseReference groupRef = databaseReference.child("userInformation").child(user.getUid());
            groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Quiz_question userInfo = dataSnapshot.getValue(Quiz_question.class);
                    group = userInfo.getGroup();
                    onPause();
                    startBeacon();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //delay on connection
                    connectBeacon();
                }
            }, 2000);
        }
    }

    private void startBeacon() {

        DatabaseReference groupRef = databaseReference.child("beacon").child("groups").child(group);
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Quiz_question userPIN = dataSnapshot.getValue(Quiz_question.class);
                Intent intent = new Intent(getApplicationContext(), Quiz.class);
                intent.putExtra("PIN", userPIN.getQuizID());
                startActivity(intent);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    @Override
    public void onClick(View view) {

        if (view == enterPIN) {
            success_pin = false;
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

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
            connectBeacon();
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
