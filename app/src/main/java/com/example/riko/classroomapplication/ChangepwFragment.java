package com.example.riko.classroomapplication;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Member;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

public class ChangepwFragment extends Fragment implements View.OnClickListener {

    final String TAG = "TTwTT";

    private View view;
    private EditText editextPassword;
    private EditText editextNewPassword;
    private EditText editextConfirmPassword;
    private Button buttonChangepw;
    private String pass;
    private String newpass;
    private String username;
    private String mParam1;
    private String mParam2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_changepw, container, false);
        initInstance();
        return view;

    }

    private void initInstance() {

        editextPassword = view.findViewById(R.id.editextPassword);
        editextNewPassword = view.findViewById(R.id.editextNewPassword);
        editextConfirmPassword = view.findViewById(R.id.editextConfirmPassword);
        buttonChangepw = view.findViewById(R.id.buttonChangepw);
        buttonChangepw.setOnClickListener(this);

        /*mParam1 = getArguments().getString("Username");
        mParam2 = getArguments().getString("Password");*/
    }

    @Override
    public void onClick(View v) {
        if (v == buttonChangepw) {
            initFirebase();
        }
    }

    private void initFirebase() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please waiting . . .");
        progressDialog.show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_member = database.getReference("Member");



        table_member.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (editextPassword.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
                } else if (editextNewPassword.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Please enter your new password", Toast.LENGTH_SHORT).show();
                } else if (editextConfirmPassword.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Please confirm your password", Toast.LENGTH_SHORT).show();
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}
