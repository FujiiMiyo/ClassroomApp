package com.example.riko.classroomapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Member;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup1Activity extends AppCompatActivity {

    private EditText editextUsername;
    private EditText editextPassword;
    private EditText editextName;
    private RadioGroup radioGroupStatus;
    private RadioButton radioStatus;
    private Button buttonRegister;

    String sel = " ";
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        backToolbar();

        editextUsername = findViewById(R.id.editextUsername);
        editextPassword = findViewById(R.id.editextPassword);
        editextName = findViewById(R.id.editextName);
        radioGroupStatus = findViewById(R.id.radioGroupStatus);
        buttonRegister = findViewById(R.id.buttonRegister);

        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_member = database.getReference("Member");

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(Signup1Activity.this);
                progressDialog.setMessage("Please waiting . . .");
                progressDialog.show();


                //table_member.addValueEventListener(new ValueEventListener() {
                //addListenerForSingleValueEvent reads data just 1 times only
                table_member.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if editText is empty
                        if (editextUsername.getText().toString().isEmpty()) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup1Activity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                        } else if (editextPassword.getText().toString().isEmpty()) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup1Activity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                        } else if (editextName.getText().toString().isEmpty()) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup1Activity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                        } else if (radioGroupStatus.getCheckedRadioButtonId() == -1) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup1Activity.this, "Please select your status", Toast.LENGTH_SHORT).show();
                        } else if (dataSnapshot.child(editextUsername.getText().toString()).exists()) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup1Activity.this, "Username already register", Toast.LENGTH_SHORT).show();
                        } else {
                            Member member = new Member(editextName.getText().toString(), editextPassword.getText().toString(), sel, editextName.getText().toString());
                            table_member.child(editextUsername.getText().toString()).setValue(member);
                            Toast.makeText(Signup1Activity.this, "Sign up successfully!", Toast.LENGTH_SHORT).show();
                            Intent signUp = new Intent(Signup1Activity.this, MainActivity.class);
                            startActivity(signUp);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioStudent:
                if (checked)
                    sel = "Student";
                break;
            case R.id.radioTeacher:
                if (checked)
                    sel = "Teacher";
                break;

        }
    }

    private void backToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
