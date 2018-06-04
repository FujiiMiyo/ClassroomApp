package com.example.riko.classroomapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Answer;
import com.example.riko.classroomapplication.Model.Assign;
import com.example.riko.classroomapplication.Model.Choice;
import com.example.riko.classroomapplication.Model.Write;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentAssignmentActivity extends AppCompatActivity implements View.OnClickListener {

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
    private DatabaseReference table_ans;
    private Button btnChoice;
    private Button btnWrite;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assignment);
        initInstance();
        backToolbar();
        //onClickButtonType();

        if (savedInstanceState == null) {
            //Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
            //selectFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_exam,
                    new CreateSpaceFragment()).commit(); //*** Fragment: select Type ***//
        }
    }

    private void initInstance() {

        //----------- Firebase ---------------//
        database = FirebaseDatabase.getInstance();
        table_quest = database.getReference("Assign");
        table_ans = database.getReference("Student_answer");

        //-- Toolbar --***//
        toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        assignname = intent.getStringExtra("assignname");
        subjectID = intent.getStringExtra("subjectID");
        Username = intent.getStringExtra("Username");
        name = intent.getStringExtra("name");
        toolbar.setTitle(assignname);
        btnChoice = findViewById(R.id.btnChoice);
        btnWrite = findViewById(R.id.btnWrite);



    }

    private void onClickButtonType() {
        btnChoice.setOnClickListener(this);
        btnWrite.setOnClickListener(this);
    }


    private void selectChoiceFragment() {
        Query searchQuery = table_quest.orderByChild("subjectID").equalTo(subjectID);
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            //TODO;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Assign assign = new Assign();
                    assign = postSnapshot.getValue(Assign.class);
                    //check assign
                    if (assign.getAssignname().equals(assignname)) {
                        if (postSnapshot.hasChild("Quest")) {
                            //choice type
                            if (postSnapshot.child("Quest").child("1").child("type").getValue().toString().equals("choice")) {

                                Choice choice = new Choice();
                                choice = postSnapshot.child("Quest").child("1").getValue(Choice.class);
                                //Count Question
                                String totalQuest = postSnapshot.child("Quest").child("totalQuest").getValue().toString();
                                long totalQuestion = Long.parseLong(totalQuest);

                                Bundle bundleChoice = new Bundle();
                                bundleChoice.putString("numberQuestion", choice.getNumberQuestion());
                                bundleChoice.putString("question", choice.getQuestion());
                                bundleChoice.putString("choiceA", choice.getChoiceA());
                                bundleChoice.putString("choiceB", choice.getChoiceB());
                                bundleChoice.putString("choiceC", choice.getChoiceC());
                                bundleChoice.putString("choiceD", choice.getChoiceD());
                                bundleChoice.putString("answer", choice.getAnswer());
                                bundleChoice.putString("Username", Username);
                                bundleChoice.putString("name", name);
                                bundleChoice.putString("subjectID", subjectID);
                                bundleChoice.putString("assignname", assignname);
                                bundleChoice.putString("keyAssign", postSnapshot.getKey());
                                bundleChoice.putLong("totalQuestion", totalQuestion);
                                bundleChoice.putLong("countQuestion", 1);

                                //create answer Member
                                //Answer answer = new Answer(Username,assignname,subjectID,0);
                                //table_ans.push().setValue(answer);

                                StudentAssignChoiceFragment myObj = new StudentAssignChoiceFragment();
                                myObj.setArguments(bundleChoice);

                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
                                        myObj).commit();
                            }
                        } else {
                            Log.e("Tag", "No Question in this Assignment");
                            Bundle bundleSpace = new Bundle();
                            StudentAssignSpaceFragment myObj = new StudentAssignSpaceFragment();
                            myObj.setArguments(bundleSpace);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
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

    private void selectWriteFragment() {
        Query searchQuery = table_quest.orderByChild("subjectID").equalTo(subjectID);
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            //TODO;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Assign assign = new Assign();
                    assign = postSnapshot.getValue(Assign.class);
                    //check assign
                    if (assign.getAssignname().equals(assignname)) {
                        if (postSnapshot.hasChild("Quest")) {
                            //choice type
                            if (postSnapshot.child("Quest").child("1").child("type").getValue().toString().equals("write")) {

                                // write type
                                Write write = new Write();
                                write = postSnapshot.child("Quest").child("1").getValue(Write.class);
                                //Count Question
                                long totalQuestion = postSnapshot.child("Quest").getChildrenCount();

                                Bundle bundleWrite = new Bundle();
                                bundleWrite.putString("numberQuestion", write.getNumberQuestion());
                                bundleWrite.putString("question", write.getQuestion());
                                bundleWrite.putString("answer", write.getAnswer());
                                bundleWrite.putString("Username", Username);
                                bundleWrite.putString("name", name);
                                bundleWrite.putString("subjectID", subjectID);
                                bundleWrite.putString("assignname", assignname);
                                bundleWrite.putLong("totalQuestion", totalQuestion);
                                bundleWrite.putLong("countQuestion", 1);

                                //create answer Member
                                //Answer answer = new Answer(Username,assignname,subjectID,0);
                                //table_ans.push().setValue(answer);

                                StudentAssignWriteFragment myObj = new StudentAssignWriteFragment();
                                myObj.setArguments(bundleWrite);

                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
                                        myObj).commit();
                            }

                        } else {
                            Log.e("Tag", "No Question in this Assignment");
                            Bundle bundleSpace = new Bundle();
                            StudentAssignSpaceFragment myObj = new StudentAssignSpaceFragment();
                            myObj.setArguments(bundleSpace);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
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

    /*private void selectFragment(){
        Query searchQuery = table_quest.orderByChild("subjectID").equalTo(subjectID);
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener(){

            //TODO;
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
                                String totalQuest = postSnapshot.child("Quest").child("totalQuest").getValue().toString();
                                long totalQuestion = Long.parseLong(totalQuest);

                                Bundle bundleChoice = new Bundle();
                                bundleChoice.putString("numberQuestion", choice.getNumberQuestion());
                                bundleChoice.putString("question", choice.getQuestion());
                                bundleChoice.putString("choiceA",choice.getChoiceA());
                                bundleChoice.putString("choiceB",choice.getChoiceB());
                                bundleChoice.putString("choiceC",choice.getChoiceC());
                                bundleChoice.putString("choiceD",choice.getChoiceD());
                                bundleChoice.putString("answer",choice.getAnswer());
                                bundleChoice.putString("Username",Username);
                                bundleChoice.putString("subjectID",subjectID);
                                bundleChoice.putString("assignname",assignname);
                                bundleChoice.putString("keyAssign",postSnapshot.getKey());
                                bundleChoice.putLong("totalQuestion",totalQuestion);
                                bundleChoice.putLong("countQuestion",1);

                                //create answer Member
                                //Answer answer = new Answer(Username,assignname,subjectID,0);
                                //table_ans.push().setValue(answer);

                                StudentAssignChoiceFragment myObj = new StudentAssignChoiceFragment();
                                myObj.setArguments(bundleChoice);

                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
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
                                bundleWrite.putString("answer",write.getAnswer());
                                bundleWrite.putString("Username",Username);
                                bundleWrite.putString("subjectID",subjectID);
                                bundleWrite.putString("assignname",assignname);
                                bundleWrite.putLong("totalQuestion",totalQuestion);
                                bundleWrite.putLong("countQuestion",1);

                                //create answer Member
                                //Answer answer = new Answer(Username,assignname,subjectID,0);
                                //table_ans.push().setValue(answer);

                                StudentAssignWriteFragment myObj = new StudentAssignWriteFragment();
                                myObj.setArguments(bundleWrite);

                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
                                        myObj).commit();
                            }

                        }
                        else {
                            Log.e( "Tag","No Question in this Assignment");
                            Bundle bundleSpace = new Bundle();
                            StudentAssignSpaceFragment myObj = new StudentAssignSpaceFragment();
                            myObj.setArguments(bundleSpace);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
                                    myObj).commit();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

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
        if (v == btnChoice) {
            selectChoiceFragment();
        } else if (v == btnWrite) {
            selectWriteFragment();
        }
    }
}
