package com.example.riko.classroomapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Member;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddExamsActivity extends AppCompatActivity implements View.OnClickListener {

    //<------------------------------------------------>
    final String TAG = "TTwTT";
    //-- Toolbar --***//
    private Toolbar toolbar;
    //<------------------------------------------------>

    private boolean doubleBackToExitPressedOnce;
    private String assignname;
    private String subjectID;
    private Button btnChoice;
    private Button btnWrite;
    private ImageButton btnAdd;
    private String subjectname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exams);

        initInstance();
        backToolbar();
        clickButton();

        if (savedInstanceState == null) {
            //Toast.makeText(this, "TeacherActivity", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
                    new CreateSpaceFragment()).commit();
        }
    }

    private void initInstance() {
        //-- Toolbar --***//
        toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        assignname = intent.getStringExtra("assignname");
        subjectID = intent.getStringExtra("subjectID");
        subjectname = intent.getStringExtra("subjectname");
        toolbar.setTitle(assignname);
        btnChoice = findViewById(R.id.btnChoice);
        btnWrite = findViewById(R.id.btnWrite);
        btnAdd = findViewById(R.id.btnAdd);

        btnChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initChoice();
            }
        });
    }

    private void initChoice() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_assign = database.getReference().child("Assign");
        table_assign.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Member member = dataSnapshot.child(textUsername.getText().toString()).getValue(Member.class);
                //Send data to courseFragment
                Bundle choiceFragment = new Bundle();
                choiceFragment.putString("subjectID", subjectID);
                choiceFragment.putString("assignname", assignname);
                choiceFragment.putString("subjectname", subjectname);
                CreateChoiceFragment myObj = new CreateChoiceFragment();
                myObj.setArguments(choiceFragment);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
                        myObj).commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initWrite() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_assign = database.getReference().child("Assign");
        table_assign.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Member member = dataSnapshot.child(textUsername.getText().toString()).getValue(Member.class);
                //Send data to courseFragment
                Bundle writeFragment = new Bundle();
                writeFragment.putString("subjectID", subjectID);
                writeFragment.putString("assignname", assignname);
                writeFragment.putString("subjectname", subjectname);
                CreateWriteFragment myObj = new CreateWriteFragment();
                myObj.setArguments(writeFragment);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
                        myObj).commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void clickButton() {
        btnChoice.setOnClickListener(this);
        btnWrite.setOnClickListener(this);
    }

    private void backToolbar() {
        //toolbar.setTitle(getString(R.string.assignment));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //------------------------------- Back Press --------------------------------------//
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(1);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    public void onClick(View v) {
        if (v == btnChoice){
            /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
                    new CreateChoiceFragment()).commit();*/
            initChoice();
        } else  if (v == btnWrite){
            /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
                    new CreateWriteFragment()).commit();*/
            initWrite();
        }
    }



}
