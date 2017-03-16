package com.project.ad.testing_app_project.Tab_profile.tab1;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.ad.testing_app_project.R;
import com.project.ad.testing_app_project.Test.Quiz_question;

public class StudentResult_innerUserNames extends AppCompatActivity {

    public FirebaseAuth firebaseAuth;
    FirebaseUser user;
    public DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String PIN;
    String groupName;
    Integer numberOfStudents;
    TextView quizPIN, userGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result_inner_user_names);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        quizPIN = (TextView) findViewById(R.id.innerPIN);
        userGroup = (TextView) findViewById(R.id.innerGroup);

        //getting PIN number from "Starting" class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            PIN = extras.getString("PIN_result");
            groupName = extras.getString("Group_result");
            Log.w("Amir", PIN);
            quizPIN.setText(PIN);
            Log.w("Amir", groupName);
            userGroup.setText(groupName);
        }

        PIN = quizPIN.getText().toString();
        groupName = userGroup.getText().toString();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.graph);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), QuizStatistic.class);
                intent.putExtra("studentGroup", groupName);
                intent.putExtra("PIN", PIN);
                //intent.putExtra("numberOfStudents", numberOfStudents);
                startActivity(intent);
            }
        });

        DatabaseReference ref = databaseReference.child("userInformation").child(user.getUid()).child("teacherQuizes").child(PIN).child("groups").child(groupName).child("userNames");
        ListView listView = (ListView) findViewById(R.id.listview_userNamesList);
        FirebaseListAdapter<Quiz_question> adapter = new FirebaseListAdapter<Quiz_question>(this, Quiz_question.class, android.R.layout.simple_list_item_2, ref) {
            @Override
            protected void populateView(View view, Quiz_question person, int position) {
                //Populate the item
                Log.w("Amir", person.getName());
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setText(person.getName());
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text2.setText(person.getUserResult() + " correct answers");
            }
        };
        listView.setAdapter(adapter);
        listView.setEnabled(false);

    }

}

