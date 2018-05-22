package com.example.riko.classroomapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentChangepwFragment extends Fragment implements View.OnClickListener {

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
        view = inflater.inflate(R.layout.fragment_changepw_stu, container, false);
        initInstance();
        return view;
    }

    private void initInstance() {

        editextPassword = view.findViewById(R.id.editextPassword);
        editextNewPassword = view.findViewById(R.id.editextNewPassword);
        editextConfirmPassword = view.findViewById(R.id.editextConfirmPassword);
        buttonChangepw = view.findViewById(R.id.buttonChangepw);
        buttonChangepw.setOnClickListener(this);

        Bundle changepw = this.getArguments();
        if (changepw != null) {
            mParam1 = changepw.getString("Username");
            mParam2 = changepw.getString("Password");
            Toast.makeText(getContext(), "Bundle != null", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "Bundle == null", Toast.LENGTH_SHORT).show();
        }
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
                    progressDialog.dismiss();
                    pass = editextPassword.getText().toString();




                    /*
                    Query searchQuery = table_member.child("password").equalTo(pass);
                    if (searchQuery != null) {
                        Toast.makeText(getContext(), "getdata" + pass, Toast.LENGTH_SHORT).show();
                    }*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}