package com.project.ad.testing_app_project.Tab_profile.tab1;

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
public class ChatRoom extends AppCompatActivity {


    Button button;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseUser user;
    public String courseTitle;
    public Intent intent;

    String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //getting PIN number from "Starting" class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupName = extras.getString("group_name");
            Log.w("Group Name", groupName);
        }

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.editText);
                FirebaseDatabase.getInstance().getReference().child("messages").child("groups").child(groupName).child("chat").push()
                        .setValue(new Message(input.getText().toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference().child("messages").child("groups").child(groupName).child("chat");



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