package com.example.riko.classroomapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CreateChoiceFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ImageButton btnAdd;
    private ImageButton btnReset;
    private Button btnSubmit;
    private EditText edittextQuest;
    private EditText edittextA;
    private EditText edittextB;
    private EditText edittextC;
    private EditText edittextD;
    private TextView txtNo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_assign, container, false);
        initInstance();
        clickButton();
        return view;
    }

    private void initInstance() {
        //----------- Text View -----------//
        txtNo = view.findViewById(R.id.txtNo);

        //---------- Edit Text -----------//
        edittextQuest = view.findViewById(R.id.edittextQuest);
        edittextA = view.findViewById(R.id.edittextA);
        edittextB = view.findViewById(R.id.edittextB);
        edittextC = view.findViewById(R.id.edittextC);
        edittextD = view.findViewById(R.id.edittextD);

        //---------- Button -------------//
        btnAdd = view.findViewById(R.id.btnAdd);
        btnReset = view.findViewById(R.id.btnReset);
        btnSubmit = view.findViewById(R.id.btnSubmit);
    }


    private void numberQuestion() {
        int tmp = Integer.valueOf( txtNo.getText().toString());
        txtNo.setText( String.valueOf( tmp+1) );
    }

    private void clickButton() {
        btnAdd.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnAdd){
            Toast.makeText(getActivity(), "Add a question", Toast.LENGTH_SHORT);
            numberQuestion();
        } else if (v == btnReset) {
            Toast.makeText(getActivity(), "Reset this question", Toast.LENGTH_SHORT);
            edittextQuest.getText().clear();
            edittextA.getText().clear();
            edittextB.getText().clear();
            edittextC.getText().clear();
            edittextD.getText().clear();
        } else if (v == btnSubmit) {
            Toast.makeText(getActivity(), "Submit assignment", Toast.LENGTH_SHORT);
            Intent submit = new Intent(getActivity(), TeacherMenuExamsActivity.class);
            startActivity(submit);
        }
    }

}
