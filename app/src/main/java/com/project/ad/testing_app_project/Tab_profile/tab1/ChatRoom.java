package com.project.ad.testing_app_project.Tab_profile.tab1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ad.testing_app_project.R;
import com.project.ad.testing_app_project.Test.Quiz_multiple;

import java.util.Objects;

public class ChatRoom extends AppCompatActivity {


    Button button;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseUser user;
    public String courseTitle;
    public Intent intent;

    String groupName, teacherName, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //getting PIN number from "Starting" class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupName = extras.getString("group_name");
            teacherName = extras.getString("teacher_name");
            role = extras.getString("role");
            Log.w("Group Name", groupName);
        }

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.editText);
                if (Objects.equals(input.getText().toString(), "")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                    builder.setTitle("Alert!")
                            .setMessage("Please, enter your message!")
                            .setCancelable(false)
                            .setNegativeButton("ОК",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    FirebaseDatabase.getInstance().getReference().child("messages").child("groups").child(groupName).child("teachers").child(teacherName).child("chat").push()
                            .setValue(new Message(input.getText().toString(),
                                    FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                    input.setText("");
                }

            }
        });
        if (Objects.equals(role, "teachers")) {
            databaseReference.child("messages").child("groups").child(groupName).child("teachers").child(teacherName).child("name").setValue(teacherName);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("messages").child("groups").child(groupName).child("teachers").child(teacherName).child("chat");


        ListView listMessages = (ListView) findViewById(R.id.listView);
        FirebaseListAdapter<Message> adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.list_chat_room, databaseReference) {
            @Override
            protected void populateView(View v, Message model, int position) {

                TextView textMessage, author, timeMessage;
                textMessage = (TextView) v.findViewById(R.id.tvMessage);
                author = (TextView) v.findViewById(R.id.tvUser);
                timeMessage = (TextView) v.findViewById(R.id.tvTimeMessage);

                textMessage.setText(model.getTextMessage());
                author.setText(model.getAuthor());
                timeMessage.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTimeMessage()));
            }
        };
        listMessages.setAdapter(adapter);


    }

}