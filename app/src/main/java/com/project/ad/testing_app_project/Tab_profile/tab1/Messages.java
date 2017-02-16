package com.project.ad.testing_app_project.Tab_profile.tab1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.ad.testing_app_project.R;
import com.project.ad.testing_app_project.Registration.MainActivity;
import com.project.ad.testing_app_project.Test.Quiz_question;
import java.util.ArrayList;

public class Messages extends AppCompatActivity {

    ListView lv_messages;
    public FirebaseAuth firebaseAuth;
    public DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user;
    ArrayList<String> groups = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        lv_messages = (ListView) findViewById(R.id.listView_groupMessages);
        Query ref = databaseReference.child("userInformation").child(user.getUid()).child("messages");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Quiz_question quiz_question = dataSnapshot.getValue(Quiz_question.class);
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Log.w("QuickQuiz", postSnapshot.getKey());
                    groups.add(postSnapshot.getKey());
                    final ArrayAdapter arrayAdapter = new ArrayAdapter(Messages.this, android.R.layout.simple_list_item_1, groups);
                    lv_messages.setAdapter(arrayAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                searchGroup();
            }
        });
        lv_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),ChatRoom.class);
                intent.putExtra("group_name",((TextView)view).getText().toString() );
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addGroup) {
            searchGroup();
        }
        if (id == R.id.action_logout) {
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchGroup(){
        AlertDialog.Builder alert = new AlertDialog.Builder(Messages.this);
        alert.setTitle("Group Search");
        alert.setMessage("Input Group");
        final EditText input = new EditText(Messages.this);
        alert.setView(input);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String result = input.getText().toString();
                Query ref = databaseReference.child("messages").child("groups").child(result);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Quiz_question quiz_question = dataSnapshot.getValue(Quiz_question.class);
                        Log.w("QuickQuiz", quiz_question.getGroup());
                        groups.add(quiz_question.getGroup());
                        final ArrayAdapter arrayAdapter = new ArrayAdapter(Messages.this, android.R.layout.simple_list_item_1, groups);
                        lv_messages.setAdapter(arrayAdapter);
                        databaseReference.child("userInformation").child(user.getUid()).child("messages").child(result).child("group").setValue(result);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
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
