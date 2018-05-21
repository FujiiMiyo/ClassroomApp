package com.example.riko.classroomapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddExamsActivity extends AppCompatActivity implements View.OnClickListener {

    //<------------------------------------------------>
    final String TAG = "TTwTT";
    //-- Toolbar --***//
    private Toolbar toolbar;
    //<------------------------------------------------>

    private boolean doubleBackToExitPressedOnce;
    private String assignname;
    private Button btnChoice;
    private Button btnWrite;
    private ImageButton btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exams);
        initInstance();
        clickButton();
        backToolbar();
    }

    private void initInstance() {
        //-- Toolbar --***//
        toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        assignname = intent.getStringExtra("assignname");
        toolbar.setTitle(assignname);

        btnChoice = findViewById(R.id.btnChoice);
        btnWrite = findViewById(R.id.btnWrite);
        btnAdd = findViewById(R.id.btnAdd);
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
                    new CreateChoiceFragment()).commit();
        } else  if (v == btnWrite){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_exam,
                    new CreateWriteFragment()).commit();
        }
    }
}
