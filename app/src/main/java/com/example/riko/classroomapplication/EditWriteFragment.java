package com.example.riko.classroomapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
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

import com.example.riko.classroomapplication.Model.Assign;
import com.example.riko.classroomapplication.Model.Write;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditWriteFragment extends Fragment implements View.OnClickListener {

    private View view;
    private String subjectID;
    private String assignname;
    private String subjectname;
    private FirebaseDatabase database;
    private DatabaseReference table_assign;
    private TextView txtNo;
    private EditText edittextQuest;
    private EditText edittextA;

    private ImageButton btnReset;
    private Button btnSubmit;

    private String numberQusetion;
    private String question;
    private String answerWrite;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_assign_write, container, false);
        if (getArguments() != null) {
            numberQusetion = getArguments().getString("numberQusetion");
            question = getArguments().getString("question");
            subjectID = getArguments().getString("subjectID");
            assignname = getArguments().getString("assignname");
            answerWrite = getArguments().getString("answerWrite");
            //Log.e("numberQusetion",numberQusetion);
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

        //---------- Button -------------//

        btnReset = view.findViewById(R.id.btnReset);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        //----------- Set Up -----------//
        edittextQuest.setText(question);
        txtNo.setText(numberQusetion);
        edittextA.setText(answerWrite);

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
                    Toast.makeText(getActivity(), "Please enter answer", Toast.LENGTH_SHORT).show();
                } else {
                    //Add Question
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Assign assign = new Assign();
                        assign = postSnapshot.getValue(Assign.class);
                        Log.e( "Check",assignname +" "+assign.getAssignname());
                        if (assign.getAssignname().equals(assignname)){
                            /*
                            //Count Question
                            String totalQuest;
                            if (postSnapshot.child("Quest").hasChild("totalQuest")){
                                totalQuest = postSnapshot.child("Quest").child("totalQuest").getValue().toString();
                                //Log.e( "QuestCount",totalQuest);
                                totalQuest = String.valueOf(1 + Integer.parseInt(totalQuest));
                                table_assign.child(postSnapshot.getKey()).child("Quest").child("totalQuest").setValue(totalQuest);
                            }else {
                                totalQuest = "1";
                                table_assign.child(postSnapshot.getKey()).child("Quest").child("totalQuest").setValue(totalQuest);
                            }*/
                            //Log.e( "Key",postSnapshot.getKey());
                            //Log.e( "Name",assign.getAssignname());
                            // Log.e("Count",String.valueOf(count));
                            Write write = new Write(numberQusetion, edittextQuest.getText().toString(), "write", edittextA.getText().toString());
                            table_assign.child(postSnapshot.getKey()).child("Quest").child(numberQusetion).setValue(write);
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
    //---------------------------------------------------------------------------------//
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
        } else if (v == btnSubmit) {
            Toast.makeText(getActivity(), "Submit assignment", Toast.LENGTH_SHORT);
            submitQuestionAns();
        }
    }
}
