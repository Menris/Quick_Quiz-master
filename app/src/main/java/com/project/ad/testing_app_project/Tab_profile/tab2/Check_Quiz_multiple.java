package com.project.ad.testing_app_project.Tab_profile.tab2;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.project.ad.testing_app_project.Tab_profile.tab1.QuizStatistic_list;
import com.project.ad.testing_app_project.Test.Quiz_multiple_adapter;
import com.project.ad.testing_app_project.Test.Quiz_question;

import java.util.Objects;

public class Check_Quiz_multiple extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    //public ListView questionsList;
    public TextView quizTitle;

    public String PIN, groupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check__quiz_multiple);


        quizTitle = (TextView) findViewById(R.id.textView_quizTitle);

        //getting PIN number from "Starting" class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            PIN = extras.getString("PIN_result");
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
        final ProgressDialog dialog;
        dialog = ProgressDialog.show(Check_Quiz_multiple.this, "", "Loading...", true);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //showing first question on activity
                populateListView();
                dialog.dismiss();
            }
        }, 2000);


    }

    private void populateListView() {

        final Integer counter = 1;
        final ListView questionsList = (ListView) findViewById(R.id.listView_checkQuiz);
        final Character[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        DatabaseReference myQuery = databaseReference.child("Tests").child(PIN).child("Questions");

        final FirebaseListAdapter<QuizStatistic_list> adapter = new FirebaseListAdapter<QuizStatistic_list>(this, QuizStatistic_list.class, R.layout.list_quiz_statistic, myQuery) {
            @Override
            protected void populateView(View v, final QuizStatistic_list model, final int position) {

                final TextView quizQuestion, questionNumber, ansA, ansB, ansC, ansD, ansE, ansF, ansG, ansH, ansI, ansJ;
                final TextView statA, statB, statC, statD, statE, statF, statG, statH, statI, statJ;
                final LinearLayout layoutE, layoutF, layoutG, layoutH, layoutI, layoutJ;

                quizQuestion = (TextView) v.findViewById(R.id.textView_quizQuestion);
                questionNumber = (TextView) v.findViewById(R.id.textView_questionNumber);

                ansA = (TextView) v.findViewById(R.id.textView_ansA);
                ansB = (TextView) v.findViewById(R.id.textView_ansB);
                ansC = (TextView) v.findViewById(R.id.textView_ansC);
                ansD = (TextView) v.findViewById(R.id.textView_ansD);
                ansE = (TextView) v.findViewById(R.id.textView_ansE);
                ansF = (TextView) v.findViewById(R.id.textView_ansF);
                ansG = (TextView) v.findViewById(R.id.textView_ansG);
                ansH = (TextView) v.findViewById(R.id.textView_ansH);
                ansI = (TextView) v.findViewById(R.id.textView_ansI);
                ansJ = (TextView) v.findViewById(R.id.textView_ansJ);


                statA = (TextView) v.findViewById(R.id.statA);
                statB = (TextView) v.findViewById(R.id.statB);
                statC = (TextView) v.findViewById(R.id.statC);
                statD = (TextView) v.findViewById(R.id.statD);
                statE = (TextView) v.findViewById(R.id.statE);
                statF = (TextView) v.findViewById(R.id.statF);
                statG = (TextView) v.findViewById(R.id.statG);
                statH = (TextView) v.findViewById(R.id.statH);
                statI = (TextView) v.findViewById(R.id.statI);
                statJ = (TextView) v.findViewById(R.id.statJ);

                layoutE = (LinearLayout) v.findViewById(R.id.layoutE);
                layoutF = (LinearLayout) v.findViewById(R.id.layoutF);
                layoutG = (LinearLayout) v.findViewById(R.id.layoutG);
                layoutH = (LinearLayout) v.findViewById(R.id.layoutH);
                layoutI = (LinearLayout) v.findViewById(R.id.layoutI);
                layoutJ = (LinearLayout) v.findViewById(R.id.layoutJ);

                layoutE.setVisibility(View.VISIBLE);
                layoutF.setVisibility(View.VISIBLE);
                layoutG.setVisibility(View.VISIBLE);
                layoutH.setVisibility(View.VISIBLE);
                layoutI.setVisibility(View.VISIBLE);
                layoutJ.setVisibility(View.VISIBLE);

                quizQuestion.setText(model.getQuestion());
                questionNumber.setText(model.getQuestionNumber());

                DatabaseReference refAnswersNumber = databaseReference.child("Tests").child(PIN).child("Questions").child("Question " + model.getQuestionNumber());

                refAnswersNumber.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Quiz_multiple_adapter adapter = dataSnapshot.getValue(Quiz_multiple_adapter.class);

                        ansA.setText("A: " + adapter.getAnswer('A'));
                        ansB.setText("B: " + adapter.getAnswer('B'));
                        ansC.setText("C: " + adapter.getAnswer('C'));
                        ansD.setText("D: " + adapter.getAnswer('D'));
                        ansE.setText("E: " + adapter.getAnswer('E'));
                        ansF.setText("F: " + adapter.getAnswer('F'));
                        ansG.setText("G: " + adapter.getAnswer('G'));
                        ansH.setText("H: " + adapter.getAnswer('H'));
                        ansI.setText("I: " + adapter.getAnswer('I'));
                        ansJ.setText("J: " + adapter.getAnswer('J'));

                        if (adapter.getAnswer('E') == null) {
                            layoutE.setVisibility(View.GONE);
                            layoutF.setVisibility(View.GONE);
                            layoutG.setVisibility(View.GONE);
                            layoutH.setVisibility(View.GONE);
                            layoutI.setVisibility(View.GONE);
                            layoutJ.setVisibility(View.GONE);
                        } else if (adapter.getAnswer('F') == null) {
                            layoutF.setVisibility(View.GONE);
                            layoutG.setVisibility(View.GONE);
                            layoutH.setVisibility(View.GONE);
                            layoutI.setVisibility(View.GONE);
                            layoutJ.setVisibility(View.GONE);
                        } else if (adapter.getAnswer('G') == null) {
                            layoutG.setVisibility(View.GONE);
                            layoutH.setVisibility(View.GONE);
                            layoutI.setVisibility(View.GONE);
                            layoutJ.setVisibility(View.GONE);
                        } else if (adapter.getAnswer('H') == null) {
                            layoutH.setVisibility(View.GONE);
                            layoutI.setVisibility(View.GONE);
                            layoutJ.setVisibility(View.GONE);
                        } else if (adapter.getAnswer('I') == null) {
                            layoutI.setVisibility(View.GONE);
                            layoutJ.setVisibility(View.GONE);
                        } else if (adapter.getAnswer('J') == null) {
                            layoutJ.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


                final String correctAnswer = model.getCorrectAnswer();

                if (Objects.equals(correctAnswer, "A")) {
                    statA.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    statB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "B")) {
                    statA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    statC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "C")) {
                    statA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    statD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "D")) {
                    statA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statD.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    statE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "E")) {
                    statA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statE.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    statF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "F")) {
                    statA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statF.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    statG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "G")) {
                    statA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statG.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    statH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "H")) {
                    statA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statH.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    statI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "I")) {
                    statA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statI.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    statJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "J")) {
                    statA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    statJ.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                }


                DatabaseReference refUserResult = databaseReference.child("userInformation").child(user.getUid()).child("myPassedQuizes").child(PIN).child("Question " + model.getQuestionNumber());
                refUserResult.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Quiz_question quiz_question = dataSnapshot.getValue(Quiz_question.class);
                        String myAnswer = quiz_question.getMyAnswer();
                        Log.w("My answer", myAnswer);
                        switch (myAnswer) {
                            case "A":
                                if (!Objects.equals(correctAnswer, myAnswer)) {
                                    statA.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mistake, 0, 0, 0);
                                }
                                break;
                            case "B":
                                if (!Objects.equals(correctAnswer, myAnswer)) {
                                    statB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mistake, 0, 0, 0);
                                }
                                break;
                            case "C":
                                if (!Objects.equals(correctAnswer, myAnswer)) {
                                    statC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mistake, 0, 0, 0);
                                }
                                break;
                            case "D":
                                if (!Objects.equals(correctAnswer, myAnswer)) {
                                    statD.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mistake, 0, 0, 0);
                                }
                                break;
                            case "E":
                                if (!Objects.equals(correctAnswer, myAnswer)) {
                                    statE.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mistake, 0, 0, 0);
                                }
                                break;
                            case "F":
                                if (!Objects.equals(correctAnswer, myAnswer)) {
                                    statF.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mistake, 0, 0, 0);
                                }
                                break;
                            case "G":
                                if (!Objects.equals(correctAnswer, myAnswer)) {
                                    statG.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mistake, 0, 0, 0);
                                }
                                break;
                            case "H":
                                if (!Objects.equals(correctAnswer, myAnswer)) {
                                    statH.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mistake, 0, 0, 0);
                                }
                                break;
                            case "I":
                                if (!Objects.equals(correctAnswer, myAnswer)) {
                                    statI.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mistake, 0, 0, 0);
                                }
                                break;
                            case "J":
                                if (!Objects.equals(correctAnswer, myAnswer)) {
                                    statJ.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mistake, 0, 0, 0);
                                }
                                break;
                            default:
                                break;
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getMessage());
                    }
                });


            }
        };
        questionsList.setAdapter(adapter);
    }
}
