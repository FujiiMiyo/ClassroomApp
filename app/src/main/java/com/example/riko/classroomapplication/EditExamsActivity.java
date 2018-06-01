package com.example.riko.classroomapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Assign;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditExamsActivity extends AppCompatActivity {

    //<------------------------------------------------>
    final String TAG = "TTwTT";
    //-- Toolbar --***//
    private Toolbar toolbar;
    //-- DrawerLayout --***//
    private DrawerLayout drawerLayout;
    private TextView textUsername;
    private TextView textStatus;
    private TextView textName;
    private NavigationView navigationView;
    private View headerView;
    //<------------------------------------------------>

    private String userName;

    private boolean doubleBackToExitPressedOnce;

    private String subjectID;
    private String subjectname;
    private String assignname;
    private FirebaseDatabase database;
    private DatabaseReference table_assign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exams);
        initInstance();
        backToolbar();
        if (savedInstanceState == null) {
            //Toast.makeText(this, "TeacherActivity", Toast.LENGTH_SHORT).show();
            selectQuestion();
            /*getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_exam,
                    new EditWriteFragment()).commit();*/
        }
    }

    private void initInstance() {
        //-- Toolbar --***//
        toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        assignname = intent.getStringExtra("assignname");
        subjectID = intent.getStringExtra("subjectID");
        toolbar.setTitle(assignname);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        //----- Firebase ------//
        database = FirebaseDatabase.getInstance();
        table_assign = database.getReference().child("Assign");
        //-----------------------------------------------//
    }

    //--------------------- Back press Toolbar -----------------------//
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

    private void selectQuestion(){

        Query searchQuery = table_assign.orderByChild("subjectID").equalTo(subjectID);
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Log.e("check",dataSnapshot.toString());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Assign assign = new Assign();
                    assign = postSnapshot.getValue(Assign.class);
                    //numberQusetion
                    String numberQusetion = "1";
                    String questionType = "";
                    if (assign.getAssignname().equals(assignname)){
                        questionType = postSnapshot.child("Quest").child(numberQusetion).child("type").getValue().toString();
                        Log.e("Type",questionType);

                        //Choose Question Type
                        if (questionType.equals("choice")){
                            //Set up Question Data
                            String question = postSnapshot.child("Quest").child(numberQusetion).child("question").getValue().toString();
                            String answer = postSnapshot.child("Quest").child(numberQusetion).child("answer").getValue().toString();
                            String choiceA = postSnapshot.child("Quest").child(numberQusetion).child("choiceA").getValue().toString();
                            String choiceB = postSnapshot.child("Quest").child(numberQusetion).child("choiceB").getValue().toString();
                            String choiceC = postSnapshot.child("Quest").child(numberQusetion).child("choiceC").getValue().toString();
                            String choiceD = postSnapshot.child("Quest").child(numberQusetion).child("choiceD").getValue().toString();
                            //Send to Fragment
                            Bundle questionFragment = new Bundle();
                            questionFragment.putString("question", question);
                            questionFragment.putString("answerChoice", answer);
                            questionFragment.putString("choiceA", choiceA);
                            questionFragment.putString("choiceB", choiceB);
                            questionFragment.putString("choiceC", choiceC);
                            questionFragment.putString("choiceD", choiceD);
                            questionFragment.putString("numberQusetion", numberQusetion);
                            questionFragment.putString("subjectID", subjectID);
                            questionFragment.putString("assignname", assignname);
                            EditChoiceFragment myObj = new EditChoiceFragment();
                            myObj.setArguments(questionFragment);

                            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_exam,
                                    myObj).commit();
                        }else {
                            //Set up Question Data
                            String question = postSnapshot.child("Quest").child(numberQusetion).child("question").getValue().toString();
                            String answer = postSnapshot.child("Quest").child(numberQusetion).child("answer").getValue().toString();

                            //Send to Fragment
                            Bundle questionFragment = new Bundle();
                            questionFragment.putString("question", question);
                            questionFragment.putString("answerWrite", answer);
                            questionFragment.putString("numberQusetion", numberQusetion);
                            questionFragment.putString("subjectID", subjectID);
                            questionFragment.putString("assignname", assignname);
                            EditWriteFragment myObj = new EditWriteFragment();
                            myObj.setArguments(questionFragment);

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
