package com.project.ad.testing_app_project.Tab_profile.tab2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.project.ad.testing_app_project.R;

import java.util.ArrayList;

/**
 * Created by menri on 19.01.2017.
 */

public class Tab2_overview extends Fragment {

    public Tab2_overview() {

    }
    String numberOfCorrectAnswers;
    public FirebaseAuth firebaseAuth;
    FirebaseUser user;
    public DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ArrayList<String> quizes = new ArrayList<String>();
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_overview, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {



        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Query ref = databaseReference.child("userInformation").child(user.getUid()).child("myPassedQuizes");
        ListView listView = (ListView) getActivity().findViewById(R.id.results_listView);
        final FirebaseListAdapter<Tab2_UserResult> adapter = new FirebaseListAdapter<Tab2_UserResult>(getActivity(), Tab2_UserResult.class, android.R.layout.simple_list_item_2, ref) {
            @Override
            protected void populateView(View view, Tab2_UserResult person, int position) {
                //Populate the item
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text.setTextColor(Color.WHITE);
                text2.setTextColor(Color.WHITE);

                text.setShadowLayer(
                        1.5f, // radius
                        5.0f, // dx
                        5.0f, // dy
                        Color.parseColor("#000000") // shadow color
                );

                text.setText(person.getQuizTitle());
                text2.setText(person.getQuizID()+ " - You have" +" " + person.getUserResult() + " correct answers");
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tab2_UserResult item = (Tab2_UserResult) adapterView.getItemAtPosition(i);
                String pin = item.getQuizID();
                numberOfCorrectAnswers = item.getUserResult();
                final Intent intent = new Intent(getActivity(),Check_Quiz_multiple.class);
                intent.putExtra("PIN_result", pin );
                intent.putExtra("userResult", numberOfCorrectAnswers);
                startActivity(intent);
            }
        });




    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
