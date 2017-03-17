package com.project.ad.testing_app_project.Tab_profile.tab1;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.project.ad.testing_app_project.Tab_profile.My_Profile;
import com.project.ad.testing_app_project.Tab_profile.tab2.Check_Quiz_multiple;
import com.project.ad.testing_app_project.Test.Quiz_multiple_adapter;

import java.text.DecimalFormat;
import java.util.Objects;

public class QuizStatistic extends AppCompatActivity {


    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    //public ListView questionsList;
    public TextView quizTitle;

    public String PIN, groupName, teacherID;

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
                Quiz_multiple_adapter title = dataSnapshot.getValue(Quiz_multiple_adapter.class);
                String quizTitle_text = title.getQuizTitle();
                quizTitle.setText(quizTitle_text + " ");
                teacherID = title.getTeacherID();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


                populateListView();





    }

    private void populateListView() {

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(QuizStatistic.this, "", "Loading...", true);

        final Integer counter = 1;
        final ListView questionsList = (ListView) findViewById(R.id.listView_quizStatistic);
        final Character[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        DatabaseReference myQuery = databaseReference.child("Tests").child(PIN).child("Questions");

        FirebaseListAdapter<QuizStatistic_list> adapter = new FirebaseListAdapter<QuizStatistic_list>(this, QuizStatistic_list.class, R.layout.list_quiz_statistic, myQuery) {
            @Override
            protected void populateView(View v, final QuizStatistic_list model, final int position) {

                final TextView quizQuestion, questionNumber, ansA, ansB, ansC, ansD, ansE, ansF, ansG, ansH, ansI, ansJ;
                final TextView statA, statB, statC, statD, statE, statF, statG, statH, statI, statJ;
                final LinearLayout layoutE, layoutF, layoutG, layoutH, layoutI, layoutJ;

                quizQuestion = (TextView) v.findViewById(R.id.textView_quizQuestion);
                questionNumber = (TextView) v.findViewById(R.id.textView_questionNumber);

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

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //showing first question on activity
                                DatabaseReference questionStatRef  = databaseReference.child("userInformation").child(teacherID).child("teacherQuizes").child(PIN).child("groups").child(groupName).child("userAnswers").child("Question " + model.getQuestionNumber());
                                questionStatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Quiz_multiple_adapter adapter = dataSnapshot.getValue(Quiz_multiple_adapter.class);

                                        Integer intA = 0;
                                        Integer intB = 0;
                                        Integer intC = 0;
                                        Integer intD = 0;
                                        Integer intE = 0;
                                        Integer intF = 0;
                                        Integer intG = 0;
                                        Integer intH = 0;
                                        Integer intI = 0;
                                        Integer intJ = 0;

                                        String questionAnswersArray = adapter.getMyAnswer();

                                        String[] array = questionAnswersArray.split("");

                                        for (int i = 0; i < array.length; i++) {
                                            Log.e("arrayLength", array.length + "");
                                            if (Objects.equals(array[i], "A")) {
                                                intA++;
                                            }
                                            if (Objects.equals(array[i], "B")) {
                                                intB++;
                                            }
                                            if (Objects.equals(array[i], "C")) {
                                                intC++;
                                            }
                                            if (Objects.equals(array[i], "D")) {
                                                intD++;
                                            }
                                            if (Objects.equals(array[i], "E")) {
                                                intE++;
                                            }
                                            if (Objects.equals(array[i], "F")) {
                                                intF++;
                                            }
                                            if (Objects.equals(array[i], "G")) {
                                                intG++;
                                            }
                                            if (Objects.equals(array[i], "H")) {
                                                intH++;
                                            }
                                            if (Objects.equals(array[i], "I")) {
                                                intI++;
                                            }
                                            if (Objects.equals(array[i], "J")) {
                                                intJ++;
                                            }
                                        }

                                        double resultA = (double)intA / (array.length - 1) * 100;
                                        double resultB = (double)intB / (array.length - 1) * 100;
                                        double resultC = (double)intC / (array.length - 1) * 100;
                                        double resultD = (double)intD / (array.length - 1) * 100;
                                        double resultE = (double)intE / (array.length - 1) * 100;
                                        double resultF = (double)intF / (array.length - 1) * 100;
                                        double resultG = (double)intG / (array.length - 1) * 100;
                                        double resultH = (double)intH / (array.length - 1) * 100;
                                        double resultI = (double)intI / (array.length - 1) * 100;
                                        double resultJ = (double)intJ / (array.length - 1) * 100;



                                        statA.setText(new DecimalFormat("##.##").format(resultA) + "%");
                                        statB.setText(new DecimalFormat("##.##").format(resultB) + "%");
                                        statC.setText(new DecimalFormat("##.##").format(resultC) + "%");
                                        statD.setText(new DecimalFormat("##.##").format(resultD) + "%");
                                        statE.setText(new DecimalFormat("##.##").format(resultE) + "%");
                                        statF.setText(new DecimalFormat("##.##").format(resultF) + "%");
                                        statG.setText(new DecimalFormat("##.##").format(resultG) + "%");
                                        statH.setText(new DecimalFormat("##.##").format(resultH) + "%");
                                        statI.setText(new DecimalFormat("##.##").format(resultI) + "%");
                                        statJ.setText(new DecimalFormat("##.##").format(resultJ) + "%");

                                        Log.e("myAnswersArray", questionAnswersArray);
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                                dialog.dismiss();
                            }
                        }, 1000);

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

                String correctAnswer = model.getCorrectAnswer();

                if (Objects.equals(correctAnswer, "A")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "B")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "C")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "D")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    ansE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "E")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansE.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    ansF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "F")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansF.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    ansG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "G")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansG.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    ansH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "H")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansH.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    ansI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "I")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansI.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                    ansJ.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (Objects.equals(correctAnswer, "J")) {
                    ansA.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansD.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansE.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansF.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansG.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansH.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ansJ.setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct_answer_small, 0, 0, 0);
                }


               // addPieChart();
            }
        };
        questionsList.setAdapter(adapter);



    }



    /*private void addPieChart() {

        chart = (PieChart) findViewById(R.id.myBarChart);

        final List<PieEntry> pieEntry = new ArrayList<>();
        pieEntry.add(new PieEntry(18.5f, "Green"));
        pieEntry.add(new PieEntry(26.7f, "Yellow"));
        pieEntry.add(new PieEntry(24.0f, "Red"));
        pieEntry.add(new PieEntry(30.8f, "Blue"));

        final PieDataSet dataset = new PieDataSet(pieEntry, "# of Calls");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PieData data = new PieData(dataset);
                //showing first question on activity
                chart.setData(data);
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                chart.animateY(500);
                chart.invalidate();
            }
        }, 100);

    }*/
}
