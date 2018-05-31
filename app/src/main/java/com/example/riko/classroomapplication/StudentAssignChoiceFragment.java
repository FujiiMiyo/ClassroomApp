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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Answer;
import com.example.riko.classroomapplication.Model.Assign;
import com.example.riko.classroomapplication.Model.Choice;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentAssignChoiceFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private View view;
    private TextView txtQuest;
    private TextView txtNo;
    private RadioGroup radioGroupChoice;
    private RadioButton radioA;
    private RadioButton radioB;
    private RadioButton radioC;
    private RadioButton radioD;
    private FirebaseDatabase database;
    private DatabaseReference table_ans;
    private DatabaseReference table_quest;
    private String assignname;
    private String subjectID;
    private String Username;
    private String sel = " ";
    private ImageButton btnReset;
    private Button btnSubmit;
    private String numberQuestion;
    private String question;
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String choiceD;
    private String answerChoice;
    private String keyAssign;
    private long totalQuestion;
    private long countQuestion;
    private String name;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_assign_stu, container, false);

        if (getArguments() != null) {
            numberQuestion = getArguments().getString("numberQuestion");
            question = getArguments().getString("question");
            choiceA = getArguments().getString("choiceA");
            choiceB = getArguments().getString("choiceB");
            choiceC = getArguments().getString("choiceC");
            choiceD = getArguments().getString("choiceD");
            answerChoice = getArguments().getString("answer");
            Username = getArguments().getString("Username");
            name = getArguments().getString("name");
            assignname = getArguments().getString("assignname");
            subjectID = getArguments().getString("subjectID");
            keyAssign = getArguments().getString("keyAssign");
            totalQuestion = getArguments().getLong("totalQuestion");
            countQuestion = getArguments().getLong("countQuestion");
            //Log.e("In_subjectID",subjectID);
            //Toast.makeText(getContext(), Username, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Bundle == null", Toast.LENGTH_SHORT).show();
        }

        initInstance();
        clickButton();
        onClickRadioButton();
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
        radioGroupChoice = view.findViewById(R.id.radioGroupChoice);
        radioA = view.findViewById(R.id.radioA);
        radioB = view.findViewById(R.id.radioB);
        radioC = view.findViewById(R.id.radioC);
        radioD = view.findViewById(R.id.radioD);

        //---------- Button -------------//
        btnReset = view.findViewById(R.id.btnReset);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        //----------- Set Up -----------//
        txtQuest.setText(question);
        txtNo.setText(String.valueOf(countQuestion));
        radioA.setText(choiceA);
        radioB.setText(choiceB);
        radioC.setText(choiceC);
        radioD.setText(choiceD);
    }


    private void submitChoiceAns(){

        Query searchQuery = table_ans.orderByChild("subjectID").equalTo(subjectID);
        //Log.e("tag",searchQuery.toString());
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("data", dataSnapshot.toString());
                int checkDataStuAnswer = 0;
                long finalScore = 0;
                String dataKey = "fail";
                ///MARK radiochoicegri

                //Setup Data Answer
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Log.e("data", postSnapshot.toString());
                    Answer answer = new Answer();
                    answer = postSnapshot.getValue(Answer.class);

                    if (answer.getAssignname().equals(assignname) && answer.getUsername().equals(Username)) {
                        //Check data in Student_answer
                        checkDataStuAnswer++;
                        //getKey for Setup
                        dataKey = postSnapshot.getKey();
                        //First Question or Not
                        if(countQuestion == 0){
                            answer.setScore(0);
                        }
                        //Add Score
                        if (sel.equals(answerChoice)){
                            finalScore = answer.getScore()+1;
                        }
                    }
                }
                //Add Answer
                if (checkDataStuAnswer > 0){
                    table_ans.child(dataKey).child("No_question").child(String.valueOf(countQuestion)).child("answer").setValue(sel);
                    table_ans.child(dataKey).child("score").setValue(finalScore);
                }else {
                    //create answer Member
                    Answer answer = new Answer(Username,assignname,subjectID,0, name);
                    String newKey = table_ans.push().getKey();
                    table_ans.child(newKey).setValue(answer);
                    table_ans.child(newKey).child("No_question").child(String.valueOf(countQuestion)).child("answer").setValue(sel);
                    //Log.e("Testkey",newKey);
                }
                //End Add Data

                //Next Question
                Log.e("keyAssign",keyAssign);
                Query assignQuery = table_quest;
                assignQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Log.e("assign",dataSnapshot.toString());
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            Log.e("assign",postSnapshot.child(keyAssign).child("Quest").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Log.e("TestRun","Running");
    }



    //------------- Radio Button: Choice -----------------//
    private void onClickRadioButton() {
        radioGroupChoice.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radioA:
                sel = "A";
                break;
            case R.id.radioB:
                sel = "B";
                break;
            case R.id.radioC:
                sel = "C";
                break;
            case R.id.radioD:
                sel = "D";
                break;
        }
    }
    //--------------------------------------------------//


    private void clickButton() {
        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == btnReset) {
            Toast.makeText(getActivity(), "Reset this question", Toast.LENGTH_SHORT);
            radioGroupChoice.clearCheck();
        } else if (v == btnSubmit) {
            Toast.makeText(getActivity(), "Submit assignment", Toast.LENGTH_SHORT);
            submitChoiceAns();
        }
    }
}
