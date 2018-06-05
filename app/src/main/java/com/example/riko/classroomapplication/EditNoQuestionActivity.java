package com.example.riko.classroomapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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

import com.example.riko.classroomapplication.Model.Assign;
import com.example.riko.classroomapplication.Model.Choice;
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

public class EditNoQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    //<------------------------------------------------>
    final String TAG = "TTwTT";
    //-- Toolbar --***//
    private Toolbar toolbar;
    //<------------------------------------------------>

    private EditText searchField;
    private ImageButton searchBtn;

    private boolean doubleBackToExitPressedOnce;
    private String assignname;
    private String subjectID;
    private String Username;

    private FirebaseDatabase database;
    private DatabaseReference table_quest;
    private RecyclerView recyclerViewNoQuestion;
    private FirebaseRecyclerAdapter recyclerAdapter;
    private List<Choice> listNoQuestion;
    private List<Choice> listQuestion;
    private NoQuestionAdapter noQuestionAdapter;

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

        //------------- Search No. -------------------//
        searchField = findViewById(R.id.search_field);
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);

        //--------------- RecyclerView --------------------//
        recyclerViewNoQuestion = findViewById(R.id.recyclerViewNoQuestion);
        recyclerViewNoQuestion.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(this);
        recyclerViewNoQuestion.setLayoutManager(LM);
        recyclerViewNoQuestion.setItemAnimator(new DefaultItemAnimator());
        //recyclerViewNoQuestion.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //recyclerViewNoQuestion.setAdapter(noQuestionAdapter);

        //----------------- Question list -------------------------------//
        listNoQuestion = new ArrayList<>();
        listQuestion = new ArrayList<>();
        noQuestionAdapter = new NoQuestionAdapter(EditNoQuestionActivity.this, listNoQuestion, listQuestion, new NoQuestionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Choice choice) {
                Intent iassign = new Intent(EditNoQuestionActivity.this, EditExamsActivity.class);
                iassign.putExtra("assignname", assignname);
                iassign.putExtra("subjectID", subjectID);
                Log.e("Send NumberQuestion",choice.getNumberQuestion());
                iassign.putExtra("numberQuestion", choice.getNumberQuestion());
                startActivity(iassign);
            }
        });
        GetNoQuestionFirebase();
        //Log.e("Final",noQuestionAdapter.listNoQuestion.get(0).getQuestion());

    }

    //***************************************************** No. Question lists ********************************************************************************//
    private void GetNoQuestionFirebase() {
        //Clear ListSubject
        listNoQuestion.clear();
        listQuestion.clear();
        Log.e("QuestionList_Clear",listNoQuestion.toString());
        Query searchQuery = table_quest.orderByChild("subjectID").equalTo(subjectID);
        searchQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Log.e("CheckQuest",dataSnapshot.toString());
                Assign assign = new Assign();
                assign = dataSnapshot.getValue(Assign.class);
                if (assign.getAssignname().equals(assignname)){
                    if (dataSnapshot.hasChild("Quest")) {
                        for (DataSnapshot postSnapshot : dataSnapshot.child("Quest").getChildren()){
                            Choice choice = new Choice();
                            choice = postSnapshot.getValue(Choice.class);
                            //Log.e("Quest",postSnapshot.toString());
                            listNoQuestion.add(choice);
                            listQuestion.add(choice);
                        }

                        Log.e("QuestionList_1",listNoQuestion.toString());

                    }
                }
                else{
                    //TODO NoQuest;
                }
                recyclerViewNoQuestion.setAdapter(noQuestionAdapter);


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

    //<------------------------ Firebase search field and display list ------------------------------------>//
    private void GetSearchNoQuestionFirebase(final String searchText) {
        //Clear ListSubject
        listNoQuestion.clear();
        listQuestion.clear();
        Log.e("QuestionList_Clear",listNoQuestion.toString());
        Query searchQuery = table_quest.orderByChild("subjectID").equalTo(subjectID);
        searchQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Log.e("CheckQuest",dataSnapshot.toString());
                Assign assign = new Assign();
                assign = dataSnapshot.getValue(Assign.class);
                if (assign.getAssignname().equals(assignname)){
                    if (dataSnapshot.hasChild("Quest")) {
                        for (DataSnapshot postSnapshot : dataSnapshot.child("Quest").getChildren()){
                            Choice choice = new Choice();
                            choice = postSnapshot.getValue(Choice.class);
                            if (choice.getNumberQuestion().contains(searchText)) {
                                //Log.e("Quest",postSnapshot.toString());
                                listNoQuestion.add(choice);
                                listQuestion.add(choice);
                            } else if (choice.getQuestion().contains(searchText)) {
                                //Log.e("Quest",postSnapshot.toString());
                                listNoQuestion.add(choice);
                                listQuestion.add(choice);
                            }
                        }

                        Log.e("QuestionList_1",listNoQuestion.toString());

                    }
                }
                else{
                    //TODO NoQuest;
                }
                recyclerViewNoQuestion.setAdapter(noQuestionAdapter);


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
    //**************************************************************************************************//

    //---------------- No. question List -------------------------------------------------//
    public static class NoQuestionAdapter extends RecyclerView.Adapter<NoQuestionAdapter.NoQuestionViewHolder> {

        List<Choice> listAssignNoQuestion;
        List<Choice> listAssignQuestion;
        final OnItemClickListener listener;
        private Context context;
        private String TAG = "TT3TT";

        public NoQuestionAdapter(Context context, List<Choice> listAssignNoQuestion, List<Choice> listAssignQuestion, OnItemClickListener listener) {
            this.context = context;
            this.listAssignNoQuestion = listAssignNoQuestion;
            this.listAssignQuestion = listAssignQuestion;
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
        public void onBindViewHolder(@NonNull NoQuestionAdapter.NoQuestionViewHolder holder, final int position) {
            final Choice no_quest = listAssignQuestion.get(position);
            final Choice question = listAssignQuestion.get(position);
            holder.bind(no_quest, question, listener);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "This subject already deleted!", Toast.LENGTH_SHORT).show();
                    //deleteSubject(subjectID.getSubjectID(), subjectname.getSubjectname(), date.getTime(), position);
                    final Dialog deleteDialog = new Dialog(context);
                    deleteDialog.setContentView(R.layout.dialog_delete_no_quest);
                    ImageButton btnConfirm = deleteDialog.findViewById(R.id.btnConfirm);
                    ImageButton btnCancel = deleteDialog.findViewById(R.id.btnCancel);
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSubject(no_quest.getNumberQuestion(), question.getQuestion(), position);
                            deleteDialog.dismiss();
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteDialog.cancel();
                        }
                    });
                    deleteDialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return listAssignQuestion.size();
        }

        public class NoQuestionViewHolder extends RecyclerView.ViewHolder {

            private final TextView txtNo;
            private final TextView txtQuest;
            private final ImageButton btnDelete;
            RelativeLayout list_item_no_quest;

            public NoQuestionViewHolder(View itemView) {
                super(itemView);
                list_item_no_quest = itemView.findViewById(R.id.list_item_no_quest);
                txtNo = itemView.findViewById(R.id.txtNo);
                txtQuest = itemView.findViewById(R.id.txtQuest);
                btnDelete = itemView.findViewById(R.id.btnDelete);
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

        //--------------------- Delete subject button ------------------------------//
        private void deleteSubject(final String no_quest, String question, final int position) {
            //TODO;
            /*DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Assign");
            Query subjectQuery = ref.child("Quest").orderByChild("subjectID").equalTo(subjectID);
            subjectQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot subjectSnapshot : dataSnapshot.getChildren()) {
                        subjectSnapshot.getRef().removeValue();
                        listAssignNoQuestion.remove(position);
                        listAssignQuestion.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, listAssignNoQuestion.size());
                        Log.d("Delete subject", "Subject has been deleted");
                        Toast.makeText(context, "Subject has been deleted.", Toast.LENGTH_SHORT).show();
                        //Log.e("CheckQuest",dataSnapshot.toString());
                    }
                    //Log.e("CheckQuest",dataSnapshot.toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });*/
        }
    }

    //--------------------- Back press Toolbar -----------------------//
    private void backToolbar() {
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


    //------------ Onclick --------------//
    @Override
    public void onClick(View v) {
        if (v == searchBtn){
            if (searchField.getText().toString().isEmpty()) {
                //Toast.makeText(getActivity(), "Please enter no. question", Toast.LENGTH_SHORT).show();
                GetNoQuestionFirebase();
            } else {
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                String searchText = searchField.getText().toString();
                GetSearchNoQuestionFirebase(searchText);
            }
        }
    }
}
