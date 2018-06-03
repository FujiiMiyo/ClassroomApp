package com.example.riko.classroomapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Assign;
import com.example.riko.classroomapplication.Model.Choice;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditChoiceFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private View view;
    private ImageButton btnAdd;
    private ImageButton btnReset;
    private Button btnSubmit;
    private RadioGroup radioGroupChoice;
    private EditText edittextQuest;
    private EditText edittextA;
    private EditText edittextB;
    private EditText edittextC;
    private EditText edittextD;
    private FirebaseDatabase database;
    private DatabaseReference table_assign;
    private TextView txtNo;
    private String sel = " ";

    private String numberQuestion;
    private String question;
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String choiceD;
    private String answerChoice;

    private String subjectID;
    private String assignname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_assign, container, false);

        if (getArguments() != null) {
            numberQuestion = getArguments().getString("numberQuestion");
            question = getArguments().getString("question");
            choiceA = getArguments().getString("choiceA");
            choiceB = getArguments().getString("choiceB");
            choiceC = getArguments().getString("choiceC");
            choiceD = getArguments().getString("choiceD");
            subjectID = getArguments().getString("subjectID");
            assignname = getArguments().getString("assignname");
            answerChoice = getArguments().getString("answerChoice");

            //Toast.makeText(getContext(), Username, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Bundle == null", Toast.LENGTH_SHORT).show();
        }
        initInstance();
        clickButton();


        return view;
    }
    private void initInstance() {
        //Init Firebase
        database = FirebaseDatabase.getInstance();
        table_assign = database.getReference("Assign");

        //----------- Text View -----------//
        txtNo = view.findViewById(R.id.txtNo);

        //---------- Edit Text -----------//
        edittextQuest = view.findViewById(R.id.edittextQuest);
        edittextA = view.findViewById(R.id.edittextA);
        edittextB = view.findViewById(R.id.edittextB);
        edittextC = view.findViewById(R.id.edittextC);
        edittextD = view.findViewById(R.id.edittextD);

        //---------- Radio Button ----------//
        radioGroupChoice = view.findViewById(R.id.radioGroupChoice);
        /*radioA = view.findViewById(R.id.radioA);
        radioB = view.findViewById(R.id.radioA);
        radioC = view.findViewById(R.id.radioA);
        radioD = view.findViewById(R.id.radioA);*/

        //---------- Button -------------//
        btnReset = view.findViewById(R.id.btnReset);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        //----------- Set Up -----------//
        edittextQuest.setText(question);
        txtNo.setText(numberQuestion);
        edittextA.setText(choiceA);
        edittextB.setText(choiceB);
        edittextC.setText(choiceC);
        edittextD.setText(choiceD);

        //---------- Choice -------------//
        sel = answerChoice;
        switch (answerChoice){
            case "A":
                radioGroupChoice.check(R.id.radioA);
                break;
            case "B":
                radioGroupChoice.check(R.id.radioB);
                break;
            case "C":
                radioGroupChoice.check(R.id.radioC);
                break;
            case "D":
                radioGroupChoice.check(R.id.radioD);
                break;
        }
        
    }

    //--------------------------- Submit button -------------------------------------//
    private void submitQuestionAns() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please waiting . . .");
        progressDialog.show();

        Query searchQuery = table_assign.orderByChild("subjectID").equalTo(subjectID);
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Check if editText is empty
                if (edittextQuest.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Please enter question", Toast.LENGTH_SHORT).show();
                } else if (edittextA.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Please enter answer A", Toast.LENGTH_SHORT).show();
                } else if (edittextB.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Please enter answer B", Toast.LENGTH_SHORT).show();
                } else if (edittextC.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Please enter answer C", Toast.LENGTH_SHORT).show();
                } else if (edittextD.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Please enter answer D", Toast.LENGTH_SHORT).show();
                } else if (radioGroupChoice.getCheckedRadioButtonId() == -1) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Please select an answer", Toast.LENGTH_SHORT).show();
                } else {
                    //Add Question
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Assign assign = new Assign();
                        assign = postSnapshot.getValue(Assign.class);
                        //Log.e( "Check",assignname +" "+assign.getAssignname());
                        if (assign.getAssignname().equals(assignname)){
                            /*
                            //Count Question
                            String totalQuest;
                            if (postSnapshot.child("Quest").hasChild("totalQuest")){
                                totalQuest = postSnapshot.child("Quest").child("totalQuest").getValue().toString();
                                Log.e( "QuestCount",totalQuest);
                                //NoChange Total Question
                                totalQuest = String.valueOf(1 + Integer.parseInt(totalQuest));
                                table_assign.child(postSnapshot.getKey()).child("Quest").child("totalQuest").setValue(totalQuest);
                            }else {
                                totalQuest = "1";
                                table_assign.child(postSnapshot.getKey()).child("Quest").child("totalQuest").setValue(totalQuest);
                            }*/
                            //Log.e( "Key",postSnapshot.getKey());
                            //Log.e( "Name",assign.getAssignname());
                            //Log.e("Count",numberQuestion.toString());
                            Choice choice = new Choice(numberQuestion, edittextQuest.getText().toString(),
                                    edittextA.getText().toString(), edittextB.getText().toString(), edittextC.getText().toString(), edittextD.getText().toString(),
                                    "choice", sel);
                            table_assign.child(postSnapshot.getKey()).child("Quest").child(numberQuestion).setValue(choice);
                        }
                    }
                    Toast.makeText(getActivity(), "Add question", Toast.LENGTH_SHORT).show();
                    showAddItemDialog();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showAddItemDialog() {
        Dialog addSubjectDialog = new Dialog(getContext());
        addSubjectDialog.setContentView(R.layout.dialog_createassign__success);
        addSubjectDialog.show();
    }

    private void clickButton() {
        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == btnReset) {
            Toast.makeText(getActivity(), "Reset this question", Toast.LENGTH_SHORT);
            edittextQuest.getText().clear();
            edittextA.getText().clear();
            edittextB.getText().clear();
            edittextC.getText().clear();
            edittextD.getText().clear();
            radioGroupChoice.clearCheck();
        } else if (v == btnSubmit) {
            submitQuestionAns();
            Toast.makeText(getActivity(), "Submit assignment", Toast.LENGTH_SHORT);
        }
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
}
