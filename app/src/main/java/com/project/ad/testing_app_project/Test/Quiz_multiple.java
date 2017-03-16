package com.project.ad.testing_app_project.Test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ad.testing_app_project.R;
import com.project.ad.testing_app_project.Tab_profile.tab1.QuizStatistic_list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Quiz_multiple extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    TextView tv_numberOfQuestions;
    TextView tv_question;
    TextView tv_quizTitle;
    String teacherID;
    String myAnswer, correctAnswer;
    String studentGroup, studentName;
    String quizTitle;
    Boolean checkAnswerClick = false;
    //counter for questions
    Integer numOfQuestions = 0;
    Integer numOfCorrAns = 0;
    String questionNumber;
    Integer counter = 1;
    String PIN;

    Button btn_nextQuestion;
    Character[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    ArrayList<String> myAnswersArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_multiple);

        tv_quizTitle = (TextView) findViewById(R.id.textView_quizTitle);
        tv_numberOfQuestions = (TextView) findViewById(R.id.textView_numberOfQuestions);
        tv_question = (TextView) findViewById(R.id.textView_question);
        btn_nextQuestion = (Button) findViewById(R.id.button_nextQuestions);

        //getting PIN number from "Starting" class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            PIN = extras.getString("PIN");
        }

        //getting quiz information
        DatabaseReference quizInfoRef = databaseReference.child("Tests").child(PIN);
        quizInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Quiz_multiple_adapter quiz_question = dataSnapshot.getValue(Quiz_multiple_adapter.class);
                tv_quizTitle.setText(quiz_question.getQuizTitle() + " ");
                quizTitle = quiz_question.getQuizTitle();
                teacherID = quiz_question.getTeacherID();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //counting how many questions we have
        DatabaseReference countQRef = databaseReference.child("Tests").child(PIN).child("Questions");
        countQRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("I have ", dataSnapshot.getChildrenCount() + " questions");
                numOfQuestions = (int) dataSnapshot.getChildrenCount();
                tv_numberOfQuestions.setText("1 / " + numOfQuestions + "");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        getUserInfo();


        btn_nextQuestion.setOnClickListener(this);
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
                populateListView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void populateListView() {

        final ListView questionsList = (ListView) findViewById(R.id.listView_answers);
        final DatabaseReference myQuery = databaseReference.child("Tests").child(PIN).child("Questions").child("Question " + counter);
        final ArrayList<String> arrayQuestion = new ArrayList<String>();
        final ArrayAdapter arrayAdapter = new ArrayAdapter(Quiz_multiple.this, R.layout.list_answers, R.id.textView_answer, arrayQuestion);

        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Quiz_multiple_adapter adapter = dataSnapshot.getValue(Quiz_multiple_adapter.class);
                tv_numberOfQuestions.setText(counter + " / " + numOfQuestions);
                correctAnswer = adapter.getCorrectAnswer();
                questionNumber = adapter.getQuestionNumber();
                tv_question.setText(adapter.getQuestion());

                for (int i = 0; i < dataSnapshot.getChildrenCount() - 3; i++) {
                    Log.e("i have ", dataSnapshot.getChildrenCount() - 3 + " answers");
                    arrayQuestion.add(adapter.getAnswer(alphabet[i]));
                    Log.e("Answers", adapter.getAnswer(alphabet[i]) + "");
                    questionsList.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        questionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = alphabet[i].toString();
                checkAnswerClick = true;
                myAnswer = alphabet[i].toString();
                view.setSelected(true);
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == btn_nextQuestion && counter < numOfQuestions) {

            if (checkAnswerClick) {
                //adding answer to user profile to
                myAnswersArray.add(myAnswer);

                if (myAnswer.equals(correctAnswer)) {
                    numOfCorrAns++;
                }
                checkAnswerClick = false;
                counter++;

                populateListView();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Quiz_multiple.this);
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

            //FirebaseDatabase.getInstance().getReference().child("userInformation").child(user.getUid()).child("myPassedQuizes").child(PIN).child("Question " + counter).child("myAnswer").setValue(myAnswer);
            myAnswersArray.add(myAnswer);
            Log.e("myArray", Arrays.toString(myAnswersArray.toArray()));
            Log.e("myArray", myAnswersArray.size() + "");
            Log.e("myArray", myAnswersArray.get(0) + "");

            for (int i = 0; i < myAnswersArray.size(); i++) {
                FirebaseDatabase.getInstance().getReference().child("userInformation").child(user.getUid()).child("myPassedQuizes").child(PIN).child("Question " + (i + 1)).child("myAnswer").setValue(myAnswersArray.get(i));
                Log.e("myAnswer" , myAnswersArray.get(i));
            }
            //adding answer to teacher profile for graph data
            addAnswersArray();

            AlertDialog.Builder builder = new AlertDialog.Builder(Quiz_multiple.this);
            builder.setTitle("You have finished quiz")
                    .setMessage("Submit your answers to see your mark!")
                    .setCancelable(false)
                    .setNegativeButton("Submit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent(getApplicationContext(), Result.class);
                                    intent.putExtra("num_correctAnswers", numOfCorrAns.toString());
                                    Log.w("My answers number is", numOfCorrAns.toString());

                                    Quiz_question quiz_question = new Quiz_question();
                                    quiz_question.setUserResult(numOfCorrAns.toString());
                                    quiz_question.setQuizTitle(quizTitle);
                                    quiz_question.setQuizID(PIN);

                                    //adding student passed PIN quiz to teachers profile
                                    DatabaseReference refToTeacherQuizID = databaseReference.child("userInformation").child(teacherID).child("teacherQuizes");
                                    DatabaseReference quizIDRef = refToTeacherQuizID.child(PIN);
                                    Map<String, Object> quizIDUpdates = new HashMap<String, Object>();
                                    quizIDUpdates.put("quizID", PIN);
                                    quizIDUpdates.put("quizTitle", quizTitle);
                                    quizIDRef.updateChildren(quizIDUpdates);

                                    //adding student result and student information to teachers profile
                                    DatabaseReference refToTeacher = databaseReference.child("userInformation").child(teacherID).child("teacherQuizes").child(PIN).child("groups").child(studentGroup).child("userNames");
                                    DatabaseReference teacherRef = refToTeacher.child(studentName);
                                    Map<String, Object> teacherUpdates = new HashMap<String, Object>();
                                    teacherUpdates.put("name", studentName);
                                    teacherUpdates.put("group", studentGroup);
                                    teacherUpdates.put("userResult", numOfCorrAns.toString());
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
                                    hopperUpdates.put("userResult", numOfCorrAns.toString());
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

    private void addAnswersArray() {

        for (int i = 0; i < myAnswersArray.size(); i++) {

            final Integer index = i;

            final DatabaseReference questionStatRef = databaseReference.child("userInformation").child(teacherID).child("teacherQuizes").child(PIN).child("groups").child(studentGroup).child("userAnswers").child("Question " + (i + 1));
            questionStatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Quiz_multiple_adapter adapter = dataSnapshot.getValue(Quiz_multiple_adapter.class);

                    if (!dataSnapshot.child("myAnswer").exists()) {
                        questionStatRef.child("myAnswer").setValue(myAnswersArray.get(index));
                    } else {
                        String questionAnswersArray = adapter.getMyAnswer();
                        questionStatRef.child("myAnswer").setValue(questionAnswersArray + myAnswersArray.get(index));
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}
