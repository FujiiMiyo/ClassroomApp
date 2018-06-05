package com.example.riko.classroomapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Answer;
import com.example.riko.classroomapplication.Model.Member;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CheckScoresActivity extends AppCompatActivity {

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

    private ImageButton btnLeft;
    private ImageButton btnRight;
    private EditText edittextNo;
    private ImageButton searchBtn;

    private String subjectID;
    private String subjectname;
    private String assignname;
    private RecyclerView recyclerViewScore;
    //private FirebaseRecyclerAdapter recyclerAdapter;
    private List<Answer> listStuUserName;
    private List<Answer> listName;
    private List<Answer> listScore;
    private ScoreAdapter scoreAdapter;
    private FirebaseDatabase database;
    private DatabaseReference table_answer;
    private DatabaseReference table_member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_scores);

        initInstance();
        GetScoreFirebase();
        backToolbar();
    }

    private void initInstance() {

        //--------------------- Firebase ----------------------------//
        database = FirebaseDatabase.getInstance();
        table_answer = database.getReference("Student_answer");
        table_member = database.getReference("Member");



        //-- Toolbar --***//
        toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        assignname = intent.getStringExtra("assignname");
        subjectID = intent.getStringExtra("subjectID");
        toolbar.setTitle(assignname);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        //-----------------------------------------------//

        //------- Search No. ------------//
        /*btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);*/
        edittextNo = findViewById(R.id.edittextNo);
        searchBtn = findViewById(R.id.searchBtn);


        //--------------- RecyclerView --------------------//
        recyclerViewScore = findViewById(R.id.recyclerViewSubject);
        recyclerViewScore.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(this);
        recyclerViewScore.setLayoutManager(LM);
        recyclerViewScore.setItemAnimator(new DefaultItemAnimator());
        //recyclerViewSubject.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        //recyclerViewScore.setAdapter(recyclerAdapter);


        //----------------- Score list -------------------------------//
        listStuUserName = new ArrayList<>();
        listName = new ArrayList<>();
        listScore = new ArrayList<>();
        scoreAdapter = new ScoreAdapter(this, listStuUserName,listName ,listScore ,new ScoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Answer answer) {
                Toast.makeText(CheckScoresActivity.this, "Check score", Toast.LENGTH_SHORT).show();
                /*Intent chkscore = new Intent(CheckScoresActivity.this, TeacherMenuExamsActivity.class);
                chkscore.putExtra("Username", answer.getUsername());
                //chkscore.putExtra("Name", answer.getName());
                chkscore.putExtra("score", answer.getScore());
                startActivity(chkscore);*/
            }
        });


    }

    //TODO;
    void GetScoreFirebase() {

        //Clear ListSubject
        listStuUserName.clear();
        listName.clear();
        listScore.clear();
        Log.e("subjectID",subjectID.toString());
        //Display Score
        Query searchQuery = table_answer.orderByChild("subjectID").equalTo(subjectID);
        searchQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Log.e("Data",dataSnapshot.toString());
                //Key Function to Add Score List
                Answer answer = new Answer();
                answer = dataSnapshot.getValue(Answer.class);

                if (answer.getAssignname().equals(assignname)) {
                    //Add to ArrayList
                    listStuUserName.add(answer);
                    //Add Score
                    listScore.add(answer);
                    //Add name
                    listName.add(answer);


                }
                //Log.e("DataList",listStuUserName);

                //Log.e("Tag",listStuUserName.toString());
                //Add List into Adapter/RecyclerView
                recyclerViewScore.setAdapter(scoreAdapter);
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

    //---------------- Score List -------------------------------------------------//
    public static class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>{

        List<Answer> listStuUserName;
        List<Answer> listName;
        List<Answer> listScore;
        final OnItemClickListener listener;
        private Context context;

        public ScoreAdapter(Context context, List<Answer> listStuUserName,List<Answer> listName, List<Answer> listScore, OnItemClickListener listener) {
            this.listStuUserName = listStuUserName;
            this.listScore = listScore;
            this.listName = listName;
            this.context = context;
            this.listener = listener;
        }

        public interface OnItemClickListener {
            void onItemClick(Answer answer);
        }

        @NonNull
        @Override
        public ScoreAdapter.ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_score_stu_username, parent, false);
            return new ScoreViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ScoreAdapter.ScoreViewHolder holder, int position) {
            final Answer userName = listStuUserName.get(position);
            final Answer name = listName.get(position);
            final Answer score = listScore.get(position);
            holder.bind(userName, name ,score ,listener);
        }

        @Override
        public int getItemCount() {
            return listStuUserName.size();
        }

        public class ScoreViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout list_item_score_stu;
            TextView textUserName;
            TextView textName;
            TextView textScore;

            public ScoreViewHolder(View itemView) {
                super(itemView);
                textUserName = itemView.findViewById(R.id.textUsername);
                textName = itemView.findViewById(R.id.textName);
                textScore = itemView.findViewById(R.id.textScore);
            }

            public void bind(final Answer userName, Answer name, Answer score, final OnItemClickListener listener) {
                textUserName.setText(userName.getUsername());
                textName.setText(name.getName());
                textScore.setText(String.valueOf(score.getScore()));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(userName);
                    }
                });
            }
        }
    }
    //-------------------------------------------------------------------------//



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


}
