package com.example.riko.classroomapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Member;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signin1Activity extends AppCompatActivity {

    private EditText editextUsername;
    private EditText editextPassword;
    Button buttonSignin;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin1);

        backToolbar();

        editextUsername = findViewById(R.id.editextUsername);
        editextPassword = findViewById(R.id.editextPassword);
        buttonSignin = findViewById(R.id.buttonSignin);


        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_member = database.getReference("Member");

        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(Signin1Activity.this);
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
                            Toast.makeText(Signin1Activity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                        } else if (editextPassword.getText().toString().isEmpty()) {
                            progressDialog.dismiss();
                            Toast.makeText(Signin1Activity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                        } else {
                            //Check if user not exist in database
                            if (dataSnapshot.child(editextUsername.getText().toString()).exists()) {
                                //Get user information
                                progressDialog.dismiss();
                                Member member = dataSnapshot.child(editextUsername.getText().toString()).getValue(Member.class);
                                if (member.getPassword().equals(editextPassword.getText().toString())) {
                                    //Toast.makeText(Signin1Activity.this, "Sign In successfully! ", Toast.LENGTH_SHORT).show();

                                    //TODO:
                                    if (member.getStatus().equals("Teacher")) {
                                        Intent signIn = new Intent(Signin1Activity.this, TeacherActivity.class);
                                        signIn.putExtra("Username", member.getUsername());
                                        signIn.putExtra("Status", member.getStatus());
                                        signIn.putExtra("Name", member.getName());
                                        signIn.putExtra("Password", member.getPassword());
                                        startActivity(signIn);
                                        Toast.makeText(Signin1Activity.this, "Sign In successfully! Welcome " + editextUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent signIn = new Intent(Signin1Activity.this, StudentActivity.class);
                                        signIn.putExtra("Username", member.getUsername());
                                        signIn.putExtra("Status", member.getStatus());
                                        signIn.putExtra("Name", member.getName());
                                        signIn.putExtra("Password", member.getPassword());
                                        startActivity(signIn);
                                        Toast.makeText(Signin1Activity.this, "Sign In successfully! Welcome " + editextUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(Signin1Activity.this, "Wrong username or password!!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Signin1Activity.this, "There isn't this username!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
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
