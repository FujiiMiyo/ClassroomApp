package com.example.riko.classroomapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private DatabaseReference table_quest;
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
    private long totalQuestion;
    private long countQuestion;


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
            totalQuestion = getArguments().getLong("totalQuestion");
            countQuestion = getArguments().getLong("countQuestion");

            //Toast.makeText(getContext(), Username, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Bundle == null", Toast.LENGTH_SHORT).show();
        }

        initInstance();
        initFirebase();
        clickButton();
        onClickRadioButton();
        return view;
    }

    private void initInstance() {

        //----------- Firebase ---------------//
        database = FirebaseDatabase.getInstance();
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

    private void initFirebase() {

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
            //submitAns();
        }
    }
}
