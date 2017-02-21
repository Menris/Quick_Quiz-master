package com.project.ad.testing_app_project.Test;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ad.testing_app_project.BeaconConnect;
import com.project.ad.testing_app_project.R;
import com.project.ad.testing_app_project.Starting;
import com.project.ad.testing_app_project.Tab_profile.tab2.Check_quiz;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Quiz extends AppCompatActivity implements View.OnClickListener {


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
    Button next_question;
    //TextView for showing numbers of total questions and number of current question (ex. 3/5)
    TextView QuestionProgressNumber;
    //Value of chosen answer
    String myAnswer;
    //Value of correct answer from database
    String correct_answer;
    //Quiz Title String
    String quizTitle;
    //teacher ID
    String teacherID;
    //student group
    String studentGroup;
    //student name
    String studentName;
    //checking value for radiobuttons
    Boolean check_Radio = false;
    ProgressDialog dialog;

    RadioButton ansA, ansB, ansC, ansD;
    RadioGroup radioGroupAB;
    RadioGroup radioGroupCD;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_quiz);

        dialog = ProgressDialog.show(Quiz.this, "", "Loading...", true);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //showing first question on activity
                showQuestion();
                dialog.dismiss();
            }
        }, 2000);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        question = (TextView) findViewById(R.id.textView_question);
        QuestionProgressNumber = (TextView) findViewById(R.id.textView_ProgressNumber);
        radioGroupAB = (RadioGroup) findViewById(R.id.radioGroupAB);
        radioGroupCD = (RadioGroup) findViewById(R.id.radioGroupCD);

        ansA = (RadioButton) findViewById(R.id.radio_questionA);
        ansB = (RadioButton) findViewById(R.id.radio_questionB);
        ansC = (RadioButton) findViewById(R.id.radio_questionC);
        ansD = (RadioButton) findViewById(R.id.radio_questionD);

        next_question = (Button) findViewById(R.id.btn_next);

        //getting PIN number from "Starting" class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            PIN = extras.getString("PIN");
        }

        //counting how many questions we have
        final DatabaseReference questionCountRef = databaseReference.child("Tests").child(PIN);
        questionCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("I have ", dataSnapshot.getChildrenCount() - 2 + " questions");
                numOfQuestions = (int) dataSnapshot.getChildrenCount() - 2;
                Quiz_question quiz_question = dataSnapshot.getValue(Quiz_question.class);
                quizTitle = quiz_question.getQuizTitle();
                teacherID = quiz_question.getTeacherID();
                getUserInfo();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //add onclick to class
        next_question.setOnClickListener(this);
    }

    public void getUserInfo() {
        DatabaseReference studentInfoRef = databaseReference.child("userInformation").child(user.getUid());
        studentInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Quiz_question userInfo = dataSnapshot.getValue(Quiz_question.class);
                studentGroup = userInfo.getGroup();
                studentName = userInfo.getName();
                //showing first question on activity
                showQuestion();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void showQuestion() {

        //show question
        ansA.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
        ansB.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
        ansC.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
        ansD.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));

        DatabaseReference ref = databaseReference.child("Tests").child(PIN).child("Question " + counter);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                Quiz_question quiz_question = snapshot.getValue(Quiz_question.class);

                String current_Question = quiz_question.getQuestion();
                String current_answerA = quiz_question.getAnswerA();
                String current_answerB = quiz_question.getAnswerB();
                String current_answerC = quiz_question.getAnswerC();
                String current_answerD = quiz_question.getAnswerD();
                correct_answer = quiz_question.getCorrectAnswer();
                QuestionProgressNumber.setText(counter.toString() + "/" + numOfQuestions);
                Log.e("I have ", numOfQuestions.toString());
                question.setText(current_Question);
                ansA.setText(current_answerA);
                ansB.setText(current_answerB);
                ansC.setText(current_answerC);
                ansD.setText(current_answerD);
                radioGroupAB.clearCheck();
                radioGroupCD.clearCheck();

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Quiz.this, Starting.class);
        startActivity(intent);
        finish();
    }



    @Override
    public void onClick(View view) {
        Quiz_question quiz_question_myAnswer = new Quiz_question();
        quiz_question_myAnswer.setMyAnswer(myAnswer);
        //press on button NEXT question and checking the number of question
        if (view == next_question && counter < numOfQuestions) {

            FirebaseDatabase.getInstance().getReference().child("userInformation").child(user.getUid()).child("myPassedQuizes").child(PIN).child("Question " + counter).setValue(quiz_question_myAnswer);
            if (check_Radio) {
                counter++;
                if (myAnswer.equals(correct_answer)) {
                    num_correctAnswers++;
                }
                showQuestion();
                check_Radio = false;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Quiz.this);
                builder.setTitle("Alert!")
                        .setMessage("Please, answer the question!")
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        } else {
            FirebaseDatabase.getInstance().getReference().child("userInformation").child(user.getUid()).child("myPassedQuizes").child(PIN).child("Question " + counter).setValue(quiz_question_myAnswer);
            AlertDialog.Builder builder = new AlertDialog.Builder(Quiz.this);
            builder.setTitle("You have finished quiz")
                    .setMessage("Submit your answers to see your mark!")
                    .setCancelable(false)
                    .setNegativeButton("Submit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent(getApplicationContext(), Result.class);
                                    intent.putExtra("num_correctAnswers", num_correctAnswers.toString());
                                    Log.w("My answers number is", num_correctAnswers.toString());

                                    Quiz_question quiz_question = new Quiz_question();
                                    quiz_question.setUserResult(num_correctAnswers.toString());
                                    quiz_question.setQuizTitle(quizTitle);
                                    quiz_question.setQuizID(PIN);

                                    //adding student passed PIN quiz to teachers profile
                                    DatabaseReference refToTeacherQuizID = databaseReference.child("userInformation").child(teacherID).child("teacherQuizes");
                                    DatabaseReference quizIDRef = refToTeacherQuizID.child(PIN);
                                    Map<String, Object> quizIDUpdates = new HashMap<String, Object>();
                                    quizIDUpdates.put("quizID", PIN);
                                    quizIDRef.updateChildren(quizIDUpdates);

                                    //adding student result and student information to teachers profile
                                    DatabaseReference refToTeacher = databaseReference.child("userInformation").child(teacherID).child("teacherQuizes").child(PIN).child("groups").child(studentGroup).child("userNames");
                                    DatabaseReference teacherRef = refToTeacher.child(studentName);
                                    Map<String, Object> teacherUpdates = new HashMap<String, Object>();
                                    teacherUpdates.put("name", studentName);
                                    teacherUpdates.put("group", studentGroup);
                                    teacherUpdates.put("userResult", num_correctAnswers.toString());
                                    teacherRef.updateChildren(teacherUpdates);

                                    //adding student group to teachers profile
                                    DatabaseReference refToGroup = databaseReference.child("userInformation").child(teacherID).child("teacherQuizes").child(PIN).child("groups");
                                    DatabaseReference groupRef = refToGroup.child(studentGroup);
                                    Map<String, Object> groupUpdates = new HashMap<String, Object>();
                                    groupUpdates.put("group", studentGroup);
                                    groupRef.updateChildren(groupUpdates);


                                    //adding your quiz results to YOUR profile
                                    DatabaseReference refQuiz = databaseReference.child("userInformation").child(user.getUid()).child("myPassedQuizes");
                                    DatabaseReference hopperRef = refQuiz.child(PIN);
                                    Map<String, Object> hopperUpdates = new HashMap<String, Object>();
                                    hopperUpdates.put("userResult", num_correctAnswers.toString());
                                    hopperUpdates.put("quizTitle", quizTitle);
                                    hopperUpdates.put("quizID", PIN);
                                    hopperRef.updateChildren(hopperUpdates);

                                    startActivity(intent);

                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

        }


    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();


        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_questionA:
                if (checked) {
                    myAnswer = "A";
                    check_Radio = true;
                    radioGroupCD.clearCheck();
                    ansA.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape_green));
                    ansB.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
                    ansC.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
                    ansD.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
                    break;
                }
            case R.id.radio_questionB:
                if (checked) {
                    myAnswer = "B";
                    check_Radio = true;
                    radioGroupCD.clearCheck();
                    ansA.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
                    ansB.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape_green));
                    ansC.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
                    ansD.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
                    break;
                }
            case R.id.radio_questionC:
                if (checked) {
                    myAnswer = "C";
                    radioGroupAB.clearCheck();
                    check_Radio = true;
                    ansA.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
                    ansB.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
                    ansC.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape_green));
                    ansD.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
                    break;
                }
            case R.id.radio_questionD:
                if (checked) {
                    myAnswer = "D";
                    check_Radio = true;
                    radioGroupAB.clearCheck();
                    ansA.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
                    ansB.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
                    ansC.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape));
                    ansD.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonshape_green));
                    break;
                }
            default:
                break;
        }
    }
}
