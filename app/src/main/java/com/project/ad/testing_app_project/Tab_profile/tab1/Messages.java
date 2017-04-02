package com.project.ad.testing_app_project.Tab_profile.tab1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.Objects;

public class Messages extends AppCompatActivity {

    ListView lv_messages;
    public FirebaseAuth firebaseAuth;
    public DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user;
    ArrayList<String> groups = new ArrayList<String>();
    String role, group, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchGroup();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        DatabaseReference userRef = databaseReference.child("userInformation").child(user.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Quiz_question userInfo = dataSnapshot.getValue(Quiz_question.class);
                role = userInfo.getRole();

                if (Objects.equals(role, "teachers")) {
                    fab.setVisibility(View.VISIBLE);
                } else {
                    fab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lv_messages = (ListView) findViewById(R.id.listView_groupMessages);
        Query info = databaseReference.child("userInformation").child(user.getUid());

        info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Quiz_question quiz_question = dataSnapshot.getValue(Quiz_question.class);
                Log.w("Role ", quiz_question.getRole());
                role = quiz_question.getRole();
                group = quiz_question.getGroup();
                name = quiz_question.getName();
                addGroups();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void addGroups(){
        final Query ref;
         if (Objects.equals(role, "teachers")) {
             ref = databaseReference.child("userInformation").child(user.getUid()).child("messages");
             ref.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     Quiz_question quiz_question = dataSnapshot.getValue(Quiz_question.class);
                     for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                         Log.w("QuickQuizTeacher", postSnapshot.getKey());
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
         } else {
             ref = databaseReference.child("messages").child("groups").child(group).child("teachers");
             ref.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        // Quiz_question quiz_question = postSnapshot.getValue(Quiz_question.class);
                         Log.w("QuickQuizStudent", postSnapshot.child("name").getValue().toString());
                         String add = postSnapshot.child("name").getValue().toString();
                         groups.add(add);
                         final ArrayAdapter arrayAdapter = new ArrayAdapter(Messages.this, android.R.layout.simple_list_item_1, groups);
                         lv_messages.setAdapter(arrayAdapter);
                     }
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {
                     searchGroup();
                 }
             });
         }


        lv_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChatRoom.class);

                if (Objects.equals(role, "teachers")) {
                    intent.putExtra("teacher_name", name);
                    intent.putExtra("group_name", ((TextView)view).getText().toString());
                } else {
                    intent.putExtra("group_name", group);
                    intent.putExtra("teacher_name", ((TextView)view).getText().toString() );
                }
                intent.putExtra("role", role);
                startActivity(intent);
            }
        });
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
