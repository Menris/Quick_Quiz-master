package com.project.ad.testing_app_project.Tab_profile.tab1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.project.ad.testing_app_project.R;
import com.project.ad.testing_app_project.Tab_profile.tab2.Tab2_UserResult;

public class StudentsResult extends AppCompatActivity {


    public FirebaseAuth firebaseAuth;
    FirebaseUser user;
    public DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_result);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Query ref = databaseReference.child("userInformation").child(user.getUid()).child("teacherQuizes");
        ListView listView = (ListView) findViewById(R.id.listView_myQuizTitles);
        final FirebaseListAdapter<Tab2_UserResult> adapter = new FirebaseListAdapter<Tab2_UserResult>(this, Tab2_UserResult.class, android.R.layout.simple_list_item_2, ref) {
            @Override
            protected void populateView(View view, Tab2_UserResult person, int position) {
                //Populate the item

                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setText(person.getQuizID());
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text2.setText(person.getQuizTitle());
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tab2_UserResult item = (Tab2_UserResult) adapterView.getItemAtPosition(i);
                String pin = item.getQuizID();
                final Intent intent = new Intent(getApplicationContext(),StudentResult_innerGroups.class);
                intent.putExtra("PIN_result1", pin );
                startActivity(intent);
            }
        });


    }

}
