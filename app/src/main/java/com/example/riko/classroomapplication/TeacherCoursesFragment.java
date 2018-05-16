package com.example.riko.classroomapplication;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Subject;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;


public class TeacherCoursesFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getName();
    //test comment
    //Test MyCommit

    private ImageButton searchBtn;
    private TextView textSubjectID, textSubjectname;
    private RecyclerView recyclerViewSubject;
    private FloatingActionButton fab;
    private EditText searchField;
    private BottomSheetDialog bottomSheetMenu;
    private View view;
    private LinearLayout exam, vdo, files, delete;
    private FirebaseDatabase database;
    private DatabaseReference table_subject;
    private Query list_subject;
    private SubjectAdapter subjectAdapter;
    private List<Subject> listSubjectID;
    private List<Subject> listSubjectName;
    private FirebaseRecyclerAdapter recyclerAdapter;
    private Dialog addSubjectDialog;


    public TeacherCoursesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_courses1, container, false);
        initInstance();
        return view;
    }


    private void initInstance() {
        //--------------------- Firebase ----------------------------//
        database = FirebaseDatabase.getInstance();
        table_subject = database.getReference("Subject");
        //list_subject = database.getReference("Subject").orderByChild("subjectID");
        //-------------------- Search --------------------------//
        textSubjectID = view.findViewById(R.id.textSubjectId);
        textSubjectname = view.findViewById(R.id.textSubject);
        searchField = view.findViewById(R.id.search_field);
        searchBtn = view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);

        //---------- Fad Button -------------//
        fab = view.findViewById(R.id.fabPlus);
        fab.setOnClickListener(this);

        //--------------- RecyclerView --------------------//
        recyclerViewSubject = view.findViewById(R.id.recyclerViewSubject);
        recyclerViewSubject.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(view.getContext());
        recyclerViewSubject.setLayoutManager(LM);
        recyclerViewSubject.setItemAnimator(new DefaultItemAnimator());
        //recyclerViewSubject.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerViewSubject.setAdapter(recyclerAdapter);


        //----------------- Subject list -------------------------------//
        listSubjectID = new ArrayList<>();
        listSubjectName = new ArrayList<>();
        subjectAdapter = new SubjectAdapter(listSubjectID, listSubjectName);
        GetSubjectFirebase();

        /*
        Query query =  FirebaseDatabase.getInstance().getReference().child("Subject");
        FirebaseRecyclerOptions<Subject> options = new FirebaseRecyclerOptions.Builder<Subject>()
                .setQuery(query, Subject.class)
                .build();
        recyclerAdapter = new FirebaseRecyclerAdapter<Subject, SubjectViewHolder>(options) {

            @NonNull
            @Override
            public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_subject_names, parent, false);
                return new SubjectViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull SubjectViewHolder holder, int position, @NonNull Subject model) {
                Subject subject = model;
                holder.setSubject(subject.getSubjectID(), subject.getSubjectname());
            }
        };*/

    }
    /*
    public static class SubjectViewHolder extends RecyclerView.ViewHolder{
        TextView textSubjectId;
        TextView textSubject;

        public SubjectViewHolder(View itemView) {
            super(itemView);
            textSubjectId = itemView.findViewById(R.id.textSubjectId);
            textSubject = itemView.findViewById(R.id.textSubject);
        }

        public void setSubject(String subjectID, String subjectname){
            textSubjectId.setText(subjectID);
            textSubject.setText(subjectname);
        }
    }*/


    //---------------- Subject List -------------------------------------------------//
    void GetSubjectFirebase() {
        //Query searchQuery = table_subject.orderByChild("subjectname").startAt(searchText).endAt(searchText + "\uf8ff");
        table_subject.orderByChild("subjectname").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Subject subject = new Subject();
                subject = dataSnapshot.getValue(Subject.class);
                //Add to ArrayList
                listSubjectID.add(subject);
                listSubjectName.add(subject);
                //Add List into Adapter/RecyclerView
                recyclerViewSubject.setAdapter(subjectAdapter);
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

    void GetSearchFirebase(String searchText) {

        //Clear ListSubject
        listSubjectID.clear();
        listSubjectName.clear();

        Query searchQuery = table_subject.orderByChild("subjectname").startAt(searchText).endAt(searchText + "\uf8ff");
        searchQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Subject subject = new Subject();
                subject = dataSnapshot.getValue(Subject.class);


                //Add to ArrayList
                listSubjectID.add(subject);
                listSubjectName.add(subject);
                //Add List into Adapter/RecyclerView
                recyclerViewSubject.setAdapter(subjectAdapter);
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
    public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

        List<Subject> listArrayID;
        List<Subject> listArrayName;

        public SubjectAdapter(List<Subject> ListID, List<Subject> ListName) {
            this.listArrayID = ListID;
            this.listArrayName = ListName;
        }

        @NonNull
        @Override
        public SubjectAdapter.SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_subject_names, parent, false);
            return new SubjectViewHolder(v);
        }


        @Override
        public void onBindViewHolder(@NonNull SubjectAdapter.SubjectViewHolder holder, int position) {
            Subject subjectID = listArrayID.get(position);
            Subject subjectname = listArrayName.get(position);
            holder.textSubjectId.setText(subjectID.getSubjectID());
            holder.textSubject.setText(subjectname.getSubjectname());

        }

        public class SubjectViewHolder extends RecyclerView.ViewHolder {
            TextView textSubjectId;
            TextView textSubject;

            public SubjectViewHolder(View itemView) {
                super(itemView);
                textSubjectId = itemView.findViewById(R.id.textSubjectId);
                textSubject = itemView.findViewById(R.id.textSubject);
            }
        }

        @Override
        public int getItemCount() {
            return listArrayID.size();
        }
    }

     /*
    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }*/


    //-------------------- Dialog Add Subject -----------------------------------------//
    private void showAddItemDialog(Context c) {
        addSubjectDialog = new Dialog(c);
        addSubjectDialog.setContentView(R.layout.dialog_add_subject);
        EditText editextSubjectID = addSubjectDialog.findViewById(R.id.editextSubjectID);
        EditText editextSubjectName = addSubjectDialog.findViewById(R.id.editextSubjectName);
        ImageButton btnAddSubject = addSubjectDialog.findViewById(R.id.btnAddSubject);
        ImageButton btnCancel = addSubjectDialog.findViewById(R.id.btnCancel);
        addSubjectDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == searchBtn) {
            Toast.makeText(getActivity(), "Search", Toast.LENGTH_SHORT).show();
            String searchText = searchField.getText().toString().toUpperCase();
            GetSearchFirebase(searchText);
        } else if (v == fab) {
            Toast.makeText(getContext(), "Add your subject", Toast.LENGTH_SHORT).show();
            showAddItemDialog(getContext());
        }
    }


}
