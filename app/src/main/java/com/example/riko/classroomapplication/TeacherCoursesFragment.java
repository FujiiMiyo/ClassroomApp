package com.example.riko.classroomapplication;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Subject;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
    private View view;
    private FirebaseDatabase database;
    private DatabaseReference table_subject;
    private Query list_subject;
    private SubjectAdapter subjectAdapter;
    private List<Subject> listSubjectID;
    private List<Subject> listSubjectName;
    private FirebaseRecyclerAdapter recyclerAdapter;
    private Dialog addSubjectDialog;
    private SubjectAdapter.OnItemClickListener listener;


    public TeacherCoursesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_courses1, container, false);
        initInstance();
        fabButtomAddSubject();
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
        //subjectAdapter = new SubjectAdapter(listSubjectID, listSubjectName, listener);
        subjectAdapter = new SubjectAdapter(listSubjectID, listSubjectName, new SubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Subject subject) {
                selectSubject();
            }
        });
        GetSubjectFirebase();

    }
    private void selectSubject() {
        Toast.makeText(getContext(), "Subject Clicked", Toast.LENGTH_SHORT).show();
        Intent exams = new Intent(getActivity(), TeacherMenuExamsActivity.class);
        startActivity(exams);
    }


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
    public static class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

        List<Subject> listArrayID;
        List<Subject> listArrayName;
        final OnItemClickListener listener;

        public interface OnItemClickListener {
            void onItemClick(Subject subject);
        }

        public SubjectAdapter(List<Subject> ListID, List<Subject> ListName, OnItemClickListener listener) {
            this.listArrayID = ListID;
            this.listArrayName = ListName;
            this.listener = listener;
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
            /*holder.textSubjectId.setText(subjectID.getSubjectID());
            holder.textSubject.setText(subjectname.getSubjectname());*/
            holder.bind(subjectID, subjectname, listener);

        }

        public class SubjectViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout list_item_subject_id;
            TextView textSubjectId;
            TextView textSubject;

            public SubjectViewHolder(View itemView) {
                super(itemView);
                list_item_subject_id = itemView.findViewById(R.id.list_item_subject_id);
                textSubjectId = itemView.findViewById(R.id.textSubjectId);
                textSubject = itemView.findViewById(R.id.textSubject);
            }

            public void bind(final Subject subjectID, Subject subjectname, final OnItemClickListener listener) {
                textSubjectId.setText(subjectID.getSubjectID());
                textSubject.setText(subjectname.getSubjectname());
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(subjectID);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return listArrayID.size();
        }
    }


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

    //Flot action button: Add subject
    private void fabButtomAddSubject() {
        // Hide Floating Action Button when scrolling in Recycler View
        recyclerViewSubject.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
        //<-----------------------------------------------------------//>
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