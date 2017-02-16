package com.project.ad.testing_app_project.Tab_profile.tab2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.project.ad.testing_app_project.Starting;
import com.project.ad.testing_app_project.Test.Quiz_question;

import java.util.Objects;

public class Check_quiz extends AppCompatActivity implements View.OnClickListener {

    //Firebase values
    public FirebaseAuth firebaseAuth;
    FirebaseUser user;
    //question Text in activity
    TextView question;
    //numver of questions
    Integer numOfQuestions;
    //PIN number entered in Starting.class
    String PIN;
    //Question number counter
    Integer counter = 1;
    //counter of correct answers
    Integer num_correctAnswers = 0;
    //Button to the next question and submit button
    Button next_question, previous_question;
    //TextView for showing numbers of total questions and number of current question (ex. 3/5)
    TextView QuestionProgressNumber;
    //Value of chosen answer
    String myAnswer;
    //Value of correct answer from database
    String correct_answer;
    //Quiz Title String
    String quizTitle;
    //Textview For showing answer text
    TextView textView_answerA;
    TextView textView_answerB;
    TextView textView_answerC;
    TextView textView_answerD;

    ProgressDialog dialog;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_check_quiz);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        question = (TextView) findViewById(R.id.textView_question);
        QuestionProgressNumber = (TextView) findViewById(R.id.textView_ProgressNumber_result);

        next_question = (Button) findViewById(R.id.btn_next);
        previous_question = (Button) findViewById(R.id.btn_back);

        textView_answerA = (TextView) findViewById(R.id.tv_answerA);
        textView_answerB = (TextView) findViewById(R.id.tv_answerB);
        textView_answerC = (TextView) findViewById(R.id.tv_answerC);
        textView_answerD = (TextView) findViewById(R.id.tv_answerD);

        //getting PIN number from "Starting" class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            PIN = extras.getString("PIN_result");
            num_correctAnswers = Integer.parseInt(extras.getString("userResult"));
            Log.w("Selecter PIN", PIN);
            Log.w("User result", num_correctAnswers.toString());
        }


        //counting how many questions we have
        DatabaseReference questionCountRef = databaseReference.child("Tests").child(PIN);

        questionCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numOfQuestions = (int) dataSnapshot.getChildrenCount() - 2;
                Log.w("I have ", numOfQuestions.toString() + " questions");
                Quiz_question quiz_question = dataSnapshot.getValue(Quiz_question.class);
                quizTitle = quiz_question.getQuizTitle();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        dialog = ProgressDialog.show(Check_quiz.this, "", "Loading...", true);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //showing first question on activity
                showQuestion();
                dialog.dismiss();
            }
        }, 2000);

        //add onclick to class
        next_question.setOnClickListener(this);
        previous_question.setOnClickListener(this);

    }

    public void showQuestion() {

        textView_answerA.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
        textView_answerB.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
        textView_answerC.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
        textView_answerD.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));

        if (counter == 1) {
            previous_question.setVisibility(View.GONE);
        } else {
            previous_question.setVisibility(View.VISIBLE);
        }

        DatabaseReference ref = databaseReference.child("Tests").child(PIN).child("Question " + counter);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                Quiz_question quiz_question = snapshot.getValue(Quiz_question.class);
                Log.w("Correct answer", quiz_question.getCorrectAnswer());
                String current_Question = quiz_question.getQuestion();
                String current_answerA = quiz_question.getAnswerA();
                String current_answerB = quiz_question.getAnswerB();
                String current_answerC = quiz_question.getAnswerC();
                String current_answerD = quiz_question.getAnswerD();
                correct_answer = quiz_question.getCorrectAnswer();
                QuestionProgressNumber.setText(counter.toString() + "/" + numOfQuestions);
                question.setText(current_Question);
                textView_answerA.setText(current_answerA);
                textView_answerB.setText(current_answerB);
                textView_answerC.setText(current_answerC);
                textView_answerD.setText(current_answerD);

                switch (correct_answer) {
                    case "A":
                        textView_answerA.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape_green));
                        break;
                    case "B":
                        textView_answerB.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape_green));
                        break;
                    case "C":
                        textView_answerC.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape_green));
                        break;
                    case "D":
                        textView_answerD.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape_green));
                        break;
                }

                myAnswer();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


    }

    private void myAnswer() {
        DatabaseReference refUserResult = databaseReference.child("userInformation").child(user.getUid()).child("myPassedQuizes").child(PIN).child("Question " + counter);
        refUserResult.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Quiz_question quiz_question = dataSnapshot.getValue(Quiz_question.class);
                String myAnswer = quiz_question.getMyAnswer();
                Log.w("My answer", myAnswer);
                switch (myAnswer) {
                    case "A":
                        if (Objects.equals(correct_answer, myAnswer)) {
                            textView_answerA.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_correctanswer));
                        } else {
                            textView_answerA.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape_red));
                        }
                        break;
                    case "B":
                        if (Objects.equals(correct_answer, myAnswer)) {
                            textView_answerB.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_correctanswer));
                        } else {
                            textView_answerB.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape_red));
                        }
                        break;
                    case "C":
                        if (Objects.equals(correct_answer, myAnswer)) {
                            textView_answerC.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_correctanswer));
                        } else {
                            textView_answerC.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape_red));
                        }
                        break;
                    case "D":
                        if (Objects.equals(correct_answer, myAnswer)) {
                            textView_answerD.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_correctanswer));
                        } else {
                            textView_answerD.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape_red));
                        }
                        break;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == next_question && counter < numOfQuestions) {
            counter++;
            showQuestion();
        } else if (view == previous_question) {
            counter--;
            showQuestion();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(Check_quiz.this);
            builder.setTitle("You have finished quiz")
                    .setMessage("Your result: " + num_correctAnswers + " "+ (numOfQuestions-num_correctAnswers) + " mistakes")
                    .setCancelable(false)
                    .setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(getApplicationContext(), Starting.class));

                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }


    }
}
