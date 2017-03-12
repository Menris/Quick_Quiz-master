package com.project.ad.testing_app_project.Tab_profile.tab1;

import android.content.Intent;
import android.os.Handler;
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

public class StudentResult_innerGroups extends AppCompatActivity {

    public FirebaseAuth firebaseAuth;
    FirebaseUser user;
    public DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String PIN;
    Integer numberOfStudents = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result_inner_groups);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //getting PIN number from "Starting" class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            PIN = extras.getString("PIN_result1");
            Log.w("Amir", PIN);
        }

        Query ref = databaseReference.child("userInformation").child(user.getUid()).child("teacherQuizes").child(PIN).child("groups");
        ListView listView = (ListView) findViewById(R.id.listview_groupList);


        FirebaseListAdapter<Quiz_question> adapter = new FirebaseListAdapter<Quiz_question>(this, Quiz_question.class, android.R.layout.simple_list_item_2, ref) {
            @Override
            protected void populateView(View view, Quiz_question person, int position) {
                //Populate the item

                final TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                DatabaseReference countStudentsRef = databaseReference.child("userInformation").child(user.getUid()).child("teacherQuizes").child(PIN).child("groups").child(person.getGroup()).child("userNames");
                countStudentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.e("My students number", dataSnapshot.getChildrenCount() + "");
                            numberOfStudents = (int) dataSnapshot.getChildrenCount();
                            text2.setText(numberOfStudents.toString() +  " - number of students passed quiz");
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Log.w("Groups", person.getGroup());
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setText(person.getGroup());

            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Quiz_question item = (Quiz_question) adapterView.getItemAtPosition(i);
                String pin = item.getGroup();
                final Intent intent = new Intent(getApplicationContext(),StudentResult_innerUserNames.class);
                intent.putExtra("Group_result", pin );
                intent.putExtra("PIN_result", PIN );
                startActivity(intent);
            }
        });

    }
}
