package com.example.riko.classroomapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Choice;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class EditNoQuestionActivity extends AppCompatActivity {

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
    private RecyclerView recyclerViewNoQuestion;
    private List<Choice> listNoQuestion;
    private NoQuestionAdapter noQuestionAdapter;
    private List<Choice> listQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_no_question);

        initInstances();
        backToolbar();
    }

    private void initInstances() {
        //-- Toolbar --***//
        toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        assignname = intent.getStringExtra("assignname");
        subjectID = intent.getStringExtra("subjectID");
        toolbar.setTitle(assignname);

        //----- Firebase ------//
        database = FirebaseDatabase.getInstance();
        table_quest = database.getReference("Assign");

        //--------------- RecyclerView --------------------//
        recyclerViewNoQuestion = findViewById(R.id.recyclerViewNoQuestion);

        //----------------- Question list -------------------------------//
        listNoQuestion = new ArrayList<>();
        listQuestion = new ArrayList<>();
        noQuestionAdapter = new NoQuestionAdapter(EditNoQuestionActivity.this, listNoQuestion, listQuestion, new NoQuestionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Choice choice) {
                Intent iassign = new Intent(EditNoQuestionActivity.this, EditExamsActivity.class);
                iassign.putExtra("assignname", assignname);
                iassign.putExtra("subjectID", subjectID);
                //iassign.putExtra("Username", Username);
                startActivity(iassign);
            }
        });
        GetNoQuestionFirebase();

    }

    private void GetNoQuestionFirebase() {
        //Clear ListSubject
        listNoQuestion.clear();
        listQuestion.clear();

        Query searchQuery = table_quest.orderByChild("subjectID").equalTo(subjectID);
        searchQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                /*for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Assign assign = new Assign();
                    assign = postSnapshot.getValue(Assign.class);
                    if (assign.getAssignname().equals(assignname)){
                        if (postSnapshot.hasChild("Quest")) {
                           //TODO;
                        }
                    }
                }*/

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

    //---------------- No. question List -------------------------------------------------//
    public static class NoQuestionAdapter extends RecyclerView.Adapter<NoQuestionAdapter.NoQuestionViewHolder>{

        List<Choice> listNoQuestion;
        List<Choice> listQuestion;
        final OnItemClickListener listener;
        private Context context;

        public NoQuestionAdapter(Context context, List<Choice> listNoQuestion, List<Choice> listQuestion, OnItemClickListener listener) {
            this.context = context;
            this.listNoQuestion = listNoQuestion;
            this.listQuestion = listQuestion;
            this.listener = listener;
        }

        public interface OnItemClickListener {
            void onItemClick(Choice choice);
        }

        @NonNull
        @Override
        public NoQuestionAdapter.NoQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_assign_no_quest, parent, false);
            return new NoQuestionViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull NoQuestionAdapter.NoQuestionViewHolder holder, int position) {
            Choice no_quest = listNoQuestion.get(position);
            Choice question = listQuestion.get(position);
            holder.bind(no_quest, question, listener);
        }

        @Override
        public int getItemCount() {
            return listNoQuestion.size();
        }

        public class NoQuestionViewHolder extends RecyclerView.ViewHolder {

            private final TextView txtNo;
            private final TextView txtQuest;
            RelativeLayout list_item_no_quest;

            public NoQuestionViewHolder(View itemView) {
                super(itemView);
                list_item_no_quest = itemView.findViewById(R.id.list_item_no_quest);
                txtNo = itemView.findViewById(R.id.txtNo);
                txtQuest = itemView.findViewById(R.id.txtQuest);
            }

            public void bind(final Choice no_quest, Choice question, final OnItemClickListener listener) {
                txtNo.setText(no_quest.getNumberQuestion());
                txtQuest.setText(question.getQuestion());
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(no_quest);
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
}