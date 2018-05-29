package com.example.riko.classroomapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Assign;
import com.example.riko.classroomapplication.Model.Choice;
import com.example.riko.classroomapplication.Model.Write;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentAssignmentActivity extends AppCompatActivity {

    //<------------------------------------------------>
    final String TAG = "TTwTT";
    //-- Toolbar --***//
    private Toolbar toolbar;
    //<------------------------------------------------>

    private boolean doubleBackToExitPressedOnce;
    private String assignname;
    private String subjectID;
    private String Username;
    private FirebaseDatabase database;
    private DatabaseReference table_quest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assignment);
        initInstance();
        backToolbar();

        if (savedInstanceState == null) {
            //Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
            selectFragment();
            /*getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_exam,
                    new StudentAssignWriteFragment()).commit();*/
        }
    }

    private void initInstance() {

        //----------- Firebase ---------------//
        database = FirebaseDatabase.getInstance();
        table_quest = database.getReference("Assign");

        //-- Toolbar --***//
        toolbar = findViewById(R.id.toolbar);
        android.content.Intent intent = getIntent();
        assignname = intent.getStringExtra("assignname");
        subjectID = intent.getStringExtra("subjectID");
        Username = intent.getStringExtra("Username");
        toolbar.setTitle(assignname);
    }

    private void selectFragment(){
        Query searchQuery = table_quest.orderByChild("subjectID").equalTo(subjectID);
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Assign assign = new Assign();
                    assign = postSnapshot.getValue(Assign.class);
                    //check assign
                    if (assign.getAssignname().equals(assignname)){
                        if (postSnapshot.hasChild("Quest")) {
                            //choice type
                            if (postSnapshot.child("Quest").child("1").child("type").getValue().toString().equals("choice")){

                                Choice choice = new Choice();
                                choice = postSnapshot.child("Quest").child("1").getValue(Choice.class);
                                //Count Question
                                long totalQuestion = postSnapshot.child("Quest").getChildrenCount();

                                Bundle bundleChoice = new Bundle();
                                bundleChoice.putString("numberQuestion", choice.getNumberQuestion());
                                bundleChoice.putString("question", choice.getQuestion());
                                bundleChoice.putString("choiceA",choice.getChoiceA());
                                bundleChoice.putString("choiceB",choice.getChoiceB());
                                bundleChoice.putString("choiceC",choice.getChoiceC());
                                bundleChoice.putString("choiceD",choice.getChoiceD());
                                bundleChoice.putLong("totalQuestion",totalQuestion);
                                bundleChoice.putLong("countQuestion",1);

                                StudentAssignChoiceFragment myObj = new StudentAssignChoiceFragment();
                                myObj.setArguments(bundleChoice);

                                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_exam,
                                        myObj).commit();
                            }
                            // write type
                            else{
                                Write write = new Write();
                                write = postSnapshot.child("Quest").child("1").getValue(Write.class);
                                //Count Question
                                long totalQuestion = postSnapshot.child("Quest").getChildrenCount();

                                Bundle bundleWrite = new Bundle();
                                bundleWrite.putString("numberQuestion", write.getNumberQuestion());
                                bundleWrite.putString("question", write.getQuestion());
                                bundleWrite.putLong("totalQuestion",totalQuestion);
                                bundleWrite.putLong("countQuestion",1);

                                StudentAssignWriteFragment myObj = new StudentAssignWriteFragment();
                                myObj.setArguments(bundleWrite);

                                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_exam,
                                        myObj).commit();
                            }

                        }
                        else {
                            Log.e( "Tag","No Question in this Assignment");
                            Bundle bundleSpace = new Bundle();
                            StudentAssignSpaceFragment myObj = new StudentAssignSpaceFragment();
                            myObj.setArguments(bundleSpace);
                            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_exam,
                                    myObj).commit();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
}