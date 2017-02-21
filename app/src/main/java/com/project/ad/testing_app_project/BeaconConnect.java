package com.project.ad.testing_app_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
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
import com.project.ad.testing_app_project.Test.Quiz;
import com.project.ad.testing_app_project.Test.Quiz_question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BeaconConnect extends AppCompatActivity {

    private static final Map<String, List<String>> PLACES_BY_BEACONS;

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

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public FirebaseAuth firebaseAuth;
    FirebaseUser user;

    private BeaconManager beaconManager;
    private Region region;
    Beacon nearestBeacon;
    String proximity;
    String group;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_connect);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

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
        connectBeacon();
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
        if (Objects.equals(proximity, "IMMEDIATE") || Objects.equals(proximity, "NEAR")) {

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


}
