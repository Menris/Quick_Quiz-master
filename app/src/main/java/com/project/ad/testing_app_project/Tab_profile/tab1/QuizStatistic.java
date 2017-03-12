package com.project.ad.testing_app_project.Tab_profile.tab1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.project.ad.testing_app_project.R;
import com.project.ad.testing_app_project.Test.Quiz_question;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuizStatistic extends AppCompatActivity {


    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    //public ListView questionsList;
    public TextView quizTitle;
    Integer counter = 1;
    public String PIN, groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_statistic);

        //questionsList = (ListView) findViewById(R.id.listView_quizStatistic);
        quizTitle = (TextView) findViewById(R.id.textView_quizTitle);

        //getting PIN number from "Starting" class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            PIN = extras.getString("PIN");
            groupName = extras.getString("studentGroup");
            Log.w("Amir", PIN);
            Log.w("Amir", groupName);
        }


        Query questionCountRef = databaseReference.child("Tests").child(PIN);
        questionCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                QuizStatistic_list title = dataSnapshot.getValue(QuizStatistic_list.class);
                String quizTitle_text = title.getQuizTitle();
                quizTitle.setText(quizTitle_text + " ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        populateListView();



    }

    private void populateListView() {

        final ListView questionsList = (ListView) findViewById(R.id.listView_quizStatistic);
        DatabaseReference myQuery = databaseReference.child("Tests").child(PIN).child("Questions");
        final FirebaseListAdapter<QuizStatistic_list> adapter = new FirebaseListAdapter<QuizStatistic_list>(this, QuizStatistic_list.class, R.layout.list_quiz_statistic, myQuery) {
            @Override
            protected void populateView(View v, QuizStatistic_list model, int position) {

                TextView quizQuestion, questionNumber, ansA, ansB, ansC, ansD;

                quizQuestion = (TextView) v.findViewById(R.id.textView_quizQuestion);
                questionNumber = (TextView) v.findViewById(R.id.textView_questionNumber);
                ansA = (TextView) v.findViewById(R.id.textView_ansA);
                ansB = (TextView) v.findViewById(R.id.textView_ansB);
                ansC = (TextView) v.findViewById(R.id.textView_ansC);
                ansD = (TextView) v.findViewById(R.id.textView_ansD);

                quizQuestion.setText(model.getQuestion());
                questionNumber.setText(model.getQuestionNumber());
                ansA.setText("A:  " + model.getAnswerA());
                ansB.setText("B:  " + model.getAnswerB());
                ansC.setText("C:  " + model.getAnswerC());
                ansD.setText("D:  " + model.getAnswerD());




               String correctAnswer = model.getCorrectAnswer();

                if (Objects.equals(correctAnswer, "A")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    //textView_answerA.setPadding(20,0,0,0);
                }
                if (Objects.equals(correctAnswer, "B")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    //textView_answerA.setPadding(20,0,0,0);
                }
                if (Objects.equals(correctAnswer, "C")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    //textView_answerA.setPadding(20,0,0,0);
                }
                if (Objects.equals(correctAnswer, "D")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    //textView_answerA.setPadding(20,0,0,0);
                }

                counter++;
            }
        };
        questionsList.setAdapter(adapter);

    }
}
