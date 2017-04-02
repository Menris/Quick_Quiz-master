package com.project.ad.testing_app_project.Tab_profile.tab1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ad.testing_app_project.R;
import com.project.ad.testing_app_project.Tab_profile.tab2.Check_Quiz_multiple;
import com.project.ad.testing_app_project.Test.Quiz_question;

import java.util.Objects;

/**
 * Created by menri on 19.01.2017.
 */

public class Tab1_profile extends Fragment implements View.OnClickListener {


    public FirebaseAuth firebaseAuth;
    public DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user;

    String role;
    Button myQuizes;
    Button myMessages;
    TextView userName, userEmail, userGroup;



    public Tab1_profile () {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_profile, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        myQuizes = (Button) getActivity().findViewById(R.id.btn_myQuizes);
        myMessages = (Button) getActivity().findViewById(R.id.btn_messages);
        userName = (TextView) getActivity().findViewById(R.id.textView_userName);
        userEmail = (TextView) getActivity().findViewById(R.id.textView_userEmail);
        userGroup = (TextView) getActivity().findViewById(R.id.textView_myGroup);
        myQuizes.setVisibility(View.GONE);

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(getActivity(), "", "Loading...", true);

        DatabaseReference questionCountRef = databaseReference.child("userInformation").child(user.getUid());

        questionCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Quiz_question userInfo = dataSnapshot.getValue(Quiz_question.class);
                role = userInfo.getRole();

                userName.setText(userInfo.getName() + " ");
                userEmail.setText(user.getEmail() + " ");
                userGroup.setText(userInfo.getGroup() + " ");

                if (Objects.equals(role, "teachers")){
                    myQuizes.setVisibility(View.VISIBLE);
                } else {
                    myQuizes.setVisibility(View.GONE);
                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 500);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myQuizes.setOnClickListener(this);
        myMessages.setOnClickListener(this);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        if (view == myQuizes){
            Log.w("Amir", "Manage my quiz button is working");
            Intent intent = new Intent(getActivity(), StudentsResult.class);
            startActivity(intent);
        }
        if (view == myMessages) {
            Intent intent = new Intent(getActivity(), Messages.class);
            startActivity(intent);
        }
    }
}
