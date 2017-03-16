package com.project.ad.testing_app_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.ad.testing_app_project.Registration.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends Activity {

    // Set Duration of the Splash Screen
    long Delay = 1500;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove the Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Get the view from splash_screen.xml
        setContentView(R.layout.activity_home);

        // Create a Timer
        Timer RunSplash = new Timer();

        // Task to do when the timer ends
        TimerTask ShowSplash = new TimerTask() {
            @Override
            public void run() {
                // Close SplashScreenActivity.class

                if (user != null) {
                    finish();
                    // Start MainActivity.class
                    Intent myIntent = new Intent(HomeActivity.this, Starting.class);
                    startActivity(myIntent);
                } else {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }



            }
        };

        // Start the timer
        RunSplash.schedule(ShowSplash, Delay);
    }
}
