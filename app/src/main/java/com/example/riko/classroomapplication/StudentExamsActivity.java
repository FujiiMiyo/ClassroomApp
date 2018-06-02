package com.example.riko.classroomapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Assign;
import com.example.riko.classroomapplication.Model.Subject;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class StudentExamsActivity extends AppCompatActivity implements View.OnClickListener {

    //<------------------------------------------------>
    final String TAG = "TTwTT";
    //-- Toolbar --***//
    private Toolbar toolbar;
    //<------------------------------------------------>

    private boolean doubleBackToExitPressedOnce;
    private FloatingActionButton fab;

    private ImageButton searchBtn;
    private EditText searchField;
    private RecyclerView recyclerViewAssign;
    private FirebaseDatabase database;
    private DatabaseReference table_assign;
    private List<Subject> listSubjectID;
    private List<Assign> listAssignName;
    private AssignAdapter assignAdapter;
    private String subjectID;
    private String subjectname;
    private String Username;
    private BottomSheetDialog bottomSheetMenu;
    private View sheetView;
    private LinearLayout createAssign;
    private LinearLayout editAssign;
    private LinearLayout checkScore;
    private LinearLayout delete;
    private Intent iassign;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_exams);
        initInstance();
        //bottomSheetSelectMenu();
        backToolbar();
    }

    private void initInstance() {

        //-- Toolbar --***//
        toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        subjectID = intent.getStringExtra("subjectID");
        subjectname = intent.getStringExtra("subjectname");
        Username = intent.getStringExtra("Username");
        name = intent.getStringExtra("name");
        toolbar.setTitle(subjectname);

        //----- Firebase ------//
        database = FirebaseDatabase.getInstance();
        table_assign = database.getReference().child("Assign");

        //-------------------- Search --------------------------//
        searchField = findViewById(R.id.search_field);
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);

        //--------------- RecyclerView --------------------//
        recyclerViewAssign = findViewById(R.id.recyclerViewAssign);
        recyclerViewAssign.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(this);
        recyclerViewAssign.setLayoutManager(LM);
        recyclerViewAssign.setItemAnimator(new DefaultItemAnimator());
        //recyclerViewAssign.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        //recyclerViewAssign.setAdapter(recyclerAdapter);

        //----------------- Assignment list -------------------------------//
        listSubjectID = new ArrayList<>();
        listAssignName = new ArrayList<>();
        assignAdapter = new AssignAdapter(listAssignName, new AssignAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Assign assign) {
                //Intent iassign = new Intent(StudentExamsActivity.this, StudentAssignmentActivity.class);
                Intent iassign = new Intent(StudentExamsActivity.this, StudentAssignNoQuestionActivity.class);
                iassign.putExtra("assignname", assign.getAssignname());
                iassign.putExtra("subjectID", subjectID);
                iassign.putExtra("Username", Username);
                iassign.putExtra("name", name);
                startActivity(iassign);
                //displaySelectMenu();
            }
        });
        GetAssignFirebase();

    }

    //***************************************************** Assignment lists ********************************************************************************//
    //<------------------------ Firebase search field and display list ------------------------------------>//
    private void GetAssignFirebase() {

        //Clear ListSubject
        listAssignName.clear();

        Query searchQuery = table_assign.orderByChild("subjectID").equalTo(subjectID);
        searchQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Assign assign = new Assign();
                assign = dataSnapshot.getValue(Assign.class);
                //Add to ArrayList
                listAssignName.add(assign);
                //Add List into Adapter/RecyclerView
                recyclerViewAssign.setAdapter(assignAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void GetSearchFirebase(final String searchText) {

        //Clear ListSubject
        listAssignName.clear();

        Query searchQuery = table_assign.orderByChild("subjectID").equalTo(subjectID);
        searchQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Assign assign = new Assign();
                assign = dataSnapshot.getValue(Assign.class);
                if (assign.getAssignname().contains(searchText) ){
                    //Add to ArrayList
                    listAssignName.add(assign);
                }
                //Add List into Adapter/RecyclerView
                recyclerViewAssign.setAdapter(assignAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    //---------------- Subject List -------------------------------------------------//
    public static class AssignAdapter extends RecyclerView.Adapter<StudentExamsActivity.AssignAdapter.AssignHolder> {

        List<Assign> listAssignName;
        final OnItemClickListener listener;
        private Context context;

        public interface OnItemClickListener {
            void onItemClick(Assign assign);
        }

        public AssignAdapter(List<Assign> listAssignName, OnItemClickListener listener) {
            this.listAssignName = listAssignName;
            this.listener = listener;
        }

        @NonNull
        @Override
        public AssignAdapter.AssignHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_assign_names, parent, false);
            return new StudentExamsActivity.AssignAdapter.AssignHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AssignAdapter.AssignHolder holder, int position) {
            Assign assignname = listAssignName.get(position);
            holder.bind(assignname, listener);
        }

        @Override
        public int getItemCount() {
            return listAssignName.size();
        }

        public class AssignHolder extends RecyclerView.ViewHolder {
            TextView textAssignName;
            RelativeLayout list_assign_names;

            public AssignHolder(View itemView) {
                super(itemView);
                list_assign_names = itemView.findViewById(R.id.list_item_assign_name);
                textAssignName = itemView.findViewById(R.id.textAssignName);
            }

            public void bind(final Assign assignname, final OnItemClickListener listener) {
                textAssignName.setText(assignname.getAssignname());
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(assignname);
                    }
                });
            }
        }
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
        if (v == searchBtn) {
            if (searchField.getText().toString().isEmpty()) {
                //Toast.makeText(getActivity(), "Please enter subjectname", Toast.LENGTH_SHORT).show();
                GetAssignFirebase();
            } else {
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                String searchText = searchField.getText().toString().toUpperCase();
                GetSearchFirebase(searchText);
            }
        }
    }
}
