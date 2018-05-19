package com.example.riko.classroomapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Subject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherMenuExamsActivity extends AppCompatActivity implements View.OnClickListener {


    //<------------------------------------------------>
    final String TAG = "TTwTT";
    //-- Toolbar --***//
    private Toolbar toolbar;
    //<------------------------------------------------>

    private boolean doubleBackToExitPressedOnce;
    private FloatingActionButton fab;

    private ImageButton searchBtn;
    private EditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_menu_exams);
        initInstance();
        backToolbar();
        initFirebase();
        // displayDrawerLayout();
    }



    private void initInstance() {
        // TODO;
        //-- Toolbar --***//
        toolbar = findViewById(R.id.toolbar);

        //---------- Fad Button -------------//
        fab = findViewById(R.id.fabPlus);
        fab.setOnClickListener(this);

        //-------------------- Search --------------------------//
        searchField = findViewById(R.id.search_field);
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);
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


    //--------------------- Firebase Intent -----------------------//
    private void initFirebase() {
        //Init Firebase SignIn
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_subject = database.getReference("Subject");
        table_subject.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent intent = getIntent();
                String subjectID = intent.getStringExtra("subjectID");
                String subjectname = intent.getStringExtra("subjectname");
                toolbar.setTitle(subjectname);
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

    private void showAddItemDialog(final Context c) {
        final Dialog addAssignDialog = new Dialog(c);
        addAssignDialog.setContentView(R.layout.dialog_add_assign_name);
        final EditText editextSubjectID = addAssignDialog.findViewById(R.id.editextAssignname);
        ImageButton btnAddAssign = addAssignDialog.findViewById(R.id.btnAddAssign);
        ImageButton btnCancel = addAssignDialog.findViewById(R.id.btnCancel);
        //SAVE
        btnAddAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, "Assignment already is added", Toast.LENGTH_SHORT).show();
                addAssignDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAssignDialog.cancel();
            }
        });
        addAssignDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == searchBtn) {
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            //String searchText = searchField.getText().toString().toUpperCase();
            //GetSearchFirebase(searchText);
        } else if (v == fab) {
            Toast.makeText(this, "Add a new assignment", Toast.LENGTH_SHORT).show();
            showAddItemDialog(this);
        }
    }
}
