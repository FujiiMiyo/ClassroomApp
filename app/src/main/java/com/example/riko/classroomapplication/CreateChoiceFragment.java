package com.example.riko.classroomapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private String subjectID;
    private String assignname;
    private String subjectname;
    private FirebaseDatabase database;
    private DatabaseReference table_assign;
    private Integer tmp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_assign, container, false);
        initInstance();
        clickButton();
        //inputQuestionAns();

        if (getArguments() != null) {
            subjectID = getArguments().getString("subjectID");
            assignname = getArguments().getString("assignname");
            subjectname = getArguments().getString("subjectname");
            //Toast.makeText(getContext(), Username, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Bundle == null", Toast.LENGTH_SHORT).show();
        }

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

        //---------- Button -------------//
        btnAdd = view.findViewById(R.id.btnAdd);
        btnReset = view.findViewById(R.id.btnReset);
        btnSubmit = view.findViewById(R.id.btnSubmit);
    }


    //-------------------------------- Add button ------------------------------------//
    private void addQuestionAns() {
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
                } else {
                    Choice choice = new Choice(txtNo.getText().toString(), edittextQuest.getText().toString(),
                            edittextA.getText().toString(), edittextB.getText().toString(), edittextC.getText().toString(), edittextD.getText().toString(),
                            "choice", "A");
                    //Add Question
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Assign assign = new Assign();
                        assign = postSnapshot.getValue(Assign.class);

                        if (assign.getAssignname().equals(assignname)){
                            //Log.e( "Key",postSnapshot.getKey());
                            //Log.e( "Name",assign.getAssignname());
                            table_assign.child(postSnapshot.getKey()).child("Question").child(txtNo.getText().toString()).setValue(choice);
                        }
                    }
                    Toast.makeText(getActivity(), "Add a question", Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /*private void numberQuestion() {
        int tmp = Integer.valueOf(txtNo.getText().toString());
        txtNo.setText( String.valueOf( tmp+1) );
    }*/
    //-------------------------------------------------------------------------------//


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
                } else {
                    Choice choice = new Choice(txtNo.getText().toString(), edittextQuest.getText().toString(),
                            edittextA.getText().toString(), edittextB.getText().toString(), edittextC.getText().toString(), edittextD.getText().toString(),
                            "choice", "A");
                    //Add Question
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Assign assign = new Assign();
                        assign = postSnapshot.getValue(Assign.class);

                        if (assign.getAssignname().equals(assignname)){
                            //Log.e( "Key",postSnapshot.getKey());
                            //Log.e( "Name",assign.getAssignname());
                            table_assign.child(postSnapshot.getKey()).child("Question").child(txtNo.getText().toString()).setValue(choice);
                        }
                    }

                    Toast.makeText(getActivity(), "Add a question", Toast.LENGTH_SHORT).show();
                    showAddItemDialog();

                    /*FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    CreateSpaceFragment fragment = new CreateSpaceFragment();
                    transaction.replace(R.id.fragment_container_exam, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();*/
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
    //---------------------------------------------------------------------------------//





    private void clickButton() {
        btnAdd.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            addQuestionAns();
            //Toast.makeText(getActivity(), "Add a question", Toast.LENGTH_SHORT);
            //numberQuestion();
        } else if (v == btnReset) {
            Toast.makeText(getActivity(), "Reset this question", Toast.LENGTH_SHORT);
            edittextQuest.getText().clear();
            edittextA.getText().clear();
            edittextB.getText().clear();
            edittextC.getText().clear();
            edittextD.getText().clear();
        } else if (v == btnSubmit) {
            Toast.makeText(getActivity(), "Submit assignment", Toast.LENGTH_SHORT);
            submitQuestionAns();
        }
    }

}
