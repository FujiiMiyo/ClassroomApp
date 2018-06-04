package com.example.riko.classroomapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Answer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentAssignWriteFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView txtQuest;
    private TextView txtNo;
    private EditText edittextA;
    private FirebaseDatabase database;
    private DatabaseReference table_ans;
    private DatabaseReference table_quest;
    private ImageButton btnReset;
    private Button btnSubmit;
    private String numberQuestion;
    private String question;
    private String Username;
    private String assignname;
    private String subjectID;

    private String answerWrite;

    private String name;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_assign_write_stu, container, false);

        if (getArguments() != null) {

            numberQuestion = getArguments().getString("numberQuestion");
            question = getArguments().getString("question");
            Username = getArguments().getString("Username");
            name = getArguments().getString("name");
            assignname = getArguments().getString("assignname");
            subjectID = getArguments().getString("subjectID");
            answerWrite = getArguments().getString("answerWrite");
            //Toast.makeText(getContext(), Username, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Bundle == null", Toast.LENGTH_SHORT).show();
        }

        initInstance();
        clickButton();

        return view;
    }

    private void initInstance() {


        //----------- Firebase ---------------//
        database = FirebaseDatabase.getInstance();
        table_ans = database.getReference("Student_answer");
        table_quest = database.getReference("Assign");

        //----------- Question -----------//
        txtQuest = view.findViewById(R.id.txtQuest);
        txtNo = view.findViewById(R.id.txtNo);
        edittextA = view.findViewById(R.id.edittextA);
        /*radioA = view.findViewById(R.id.radioA);
        radioB = view.findViewById(R.id.radioA);
        radioC = view.findViewById(R.id.radioA);
        radioD = view.findViewById(R.id.radioA);*/

        //---------- Button -------------//
        btnReset = view.findViewById(R.id.btnReset);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        //----------- Set Up -----------//
        txtQuest.setText(question);
        txtNo.setText(numberQuestion);
    }

    private void submitChoiceAns(){

        Query searchQuery = table_ans.orderByChild("subjectID").equalTo(subjectID);
        //Log.e("tag",searchQuery.toString());
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.e("data", dataSnapshot.toString());
                String checkDataStuAnswer = "False";
                String dataKey = "";
                String checkAnswer = "False";
                //Setup Data Answer
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Log.e("data", postSnapshot.toString());
                    Answer answer = new Answer();
                    answer = postSnapshot.getValue(Answer.class);

                    //Log.e("Answer_assignname",answer.getAssignname());
                    //Log.e("Answer_Username",answer.getUsername());

                    if (answer.getAssignname().equals(assignname) && answer.getUsername().equals(Username)) {
                        //Check data in Student_answer
                        checkDataStuAnswer = "True";
                        //getKey for Setup
                        dataKey = postSnapshot.getKey();
                    }
                }
                //Check Answer
                Log.e("Answer",edittextA.getText().toString());
                if (edittextA.getText().toString().equals(answerWrite)){
                    checkAnswer = "True";
                }
                //Add Answer
                if (checkDataStuAnswer.equals("True")){
                    //Set Valuse No_Question
                    table_ans.child(dataKey).child("No_question").child(numberQuestion).child("answer").setValue(edittextA.getText().toString());
                    table_ans.child(dataKey).child("No_question").child(numberQuestion).child("check").setValue(checkAnswer);
                }else {
                    //create answer Member
                    Answer answer = new Answer(Username,assignname,subjectID,0, name);
                    String newKey = table_ans.push().getKey();
                    table_ans.child(newKey).setValue(answer);
                    //Set Valuse No_Question
                    table_ans.child(newKey).child("No_question").child(numberQuestion).child("answer").setValue(edittextA.getText().toString());
                    table_ans.child(dataKey).child("No_question").child(numberQuestion).child("check").setValue(checkAnswer);
                    //Log.e("Testkey",newKey);
                }
                CheckScore();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void CheckScore(){
        Log.e("CheckScore","Runing");
        Query searchQuery = table_ans.orderByChild("subjectID").equalTo(subjectID);
        searchQuery.addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Answer answer = new Answer();
                answer = dataSnapshot.getValue(Answer.class);
                int countAnswer = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.child("No_question").getChildren()) {
                    //Log.e("Snapshot",postSnapshot.child("check").getValue().toString());
                    if (postSnapshot.child("check").getValue().toString().equals("True")){
                        countAnswer++;
                    }
                }
                table_ans.child(dataSnapshot.getKey()).child("score").setValue(countAnswer);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void clickButton() {
        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == btnReset) {
            Toast.makeText(getActivity(), "Reset this question", Toast.LENGTH_SHORT);
            edittextA.getText().clear();
        } else if (v == btnSubmit) {
            Toast.makeText(getActivity(), "Submit assignment", Toast.LENGTH_SHORT);
            submitChoiceAns();
        }
    }

}
