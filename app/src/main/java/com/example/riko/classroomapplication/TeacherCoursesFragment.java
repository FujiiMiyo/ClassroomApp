package com.example.riko.classroomapplication;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private List<Subject> listDate;
    private FirebaseRecyclerAdapter recyclerAdapter;
    private Dialog addSubjectDialog;
    private SubjectAdapter.OnItemClickListener listener;
    private String Username;

    public TeacherCoursesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_courses1, container, false);

        if (getArguments() != null) {
            Username = getArguments().getString("Username");
            //Toast.makeText(getContext(), Username, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Bundle == null", Toast.LENGTH_SHORT).show();
        }

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
        listDate = new ArrayList<>();
        //subjectAdapter = new SubjectAdapter(listSubjectID, listSubjectName, listener);
        subjectAdapter = new SubjectAdapter(getContext(), listSubjectID, listSubjectName, listDate, new SubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Subject subject) {
                //selectSubject();
                Toast.makeText(getContext(), "Subject is clicked", Toast.LENGTH_SHORT).show();
                Intent isubject = new Intent(getActivity(), TeacherMenuExamsActivity.class);
                isubject.putExtra("subjectID", subject.getSubjectID());
                isubject.putExtra("subjectname", subject.getSubjectname());
                startActivity(isubject);
                //Toast.makeText(getContext(), "Assignment", Toast.LENGTH_SHORT).show();
            }
        });
        GetSubjectFirebase();

    }

    //***************************************************** Subject lists ********************************************************************************//
    //<------------------------ Firebase search field and display list ------------------------------------>//
    void GetSubjectFirebase() {
        listSubjectID.clear();
        listSubjectName.clear();
        listDate.clear();
        Query searchQuery = table_subject.orderByChild("username").equalTo(Username);
        searchQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Subject subject = new Subject();
                Subject subject = dataSnapshot.getValue(Subject.class);

                //Add to ArrayList
                listSubjectID.add(subject);
                listSubjectName.add(subject);
                listDate.add(subject);
                //Add List into Adapter/RecyclerView
                recyclerViewSubject.setAdapter(subjectAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //Add List into Adapter/RecyclerView
                //recyclerViewSubject.setAdapter(subjectAdapter);
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
        listSubjectID.clear();
        listSubjectName.clear();
        listDate.clear();
        Query searchQuery = table_subject.orderByChild("username").equalTo(Username);
        searchQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Subject subject = new Subject();
                subject = dataSnapshot.getValue(Subject.class);

                if (subject.getSubjectname().contains(searchText)){
                    //Add to ArrayList
                    listSubjectID.add(subject);
                    listSubjectName.add(subject);
                    listDate.add(subject);
                }
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
    //------------------------------------------------------------------------------------------//

    //---------------- Subject List -------------------------------------------------//
    public static class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

        List<Subject> listArrayID;
        List<Subject> listArrayName;
        List<Subject> listDate;
        final OnItemClickListener listener;
        private Context context;

        public interface OnItemClickListener {
            void onItemClick(Subject subject);
        }

        public SubjectAdapter(Context context, List<Subject> ListID, List<Subject> ListName, List<Subject> ListDate,OnItemClickListener listener) {
            this.context = context;
            this.listArrayID = ListID;
            this.listArrayName = ListName;
            this.listDate = ListDate;
            this.listener = listener;
        }

        @NonNull
        @Override
        public SubjectAdapter.SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_subject_names, parent, false);
            return new SubjectViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull SubjectAdapter.SubjectViewHolder holder, final int position) {
            final Subject subjectID = listArrayID.get(position);
            final Subject subjectname = listArrayName.get(position);
            final Subject date = listDate.get(position);
            holder.bind(subjectID, subjectname, date, listener);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "This subject already deleted!", Toast.LENGTH_SHORT).show();
                    //deleteSubject(subjectID.getSubjectID(), subjectname.getSubjectname(), date.getTime(), position);
                    final Dialog deleteDialog = new Dialog(context);
                    deleteDialog.setContentView(R.layout.dialog_delete_subject);
                    ImageButton btnConfirm = deleteDialog.findViewById(R.id.btnConfirm);
                    ImageButton btnCancel = deleteDialog.findViewById(R.id.btnCancel);
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSubject(subjectID.getSubjectID(), subjectname.getSubjectname(), date.getTime(), position);
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

        public class SubjectViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout list_item_subject_id;
            TextView textSubjectId;
            TextView textSubject;
            TextView textDate;
            ImageButton btnDelete;

            public SubjectViewHolder(View itemView) {
                super(itemView);
                list_item_subject_id = itemView.findViewById(R.id.list_item_subject_id);
                textSubjectId = itemView.findViewById(R.id.textSubjectId);
                textSubject = itemView.findViewById(R.id.textSubject);
                textDate = itemView.findViewById(R.id.textDate);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            }

            public void bind(final Subject subjectID, Subject subjectname, Subject date, final OnItemClickListener listener) {
                textSubjectId.setText(subjectID.getSubjectID());
                textSubject.setText(subjectname.getSubjectname());
                textDate.setText(date.getTime());
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

        //--------------------- Delete subject button ------------------------------//
        private void deleteSubject(final String subjectID, String subjectname, String date, final int position) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query subjectQuery = ref.child("Subject").orderByChild("subjectID").equalTo(subjectID);
            subjectQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot subjectSnapshot : dataSnapshot.getChildren()) {
                        subjectSnapshot.getRef().removeValue();
                        listArrayID.remove(position);
                        listArrayName.remove(position);
                        listDate.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, listArrayID.size());
                        Log.d("Delete subject", "Subject has been deleted");
                        Toast.makeText(context, "Subject has been deleted.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });
        }
    }
    //**************************************************************************************************************************************************//


    //*************************************************************************************************************************************************//
    //-------------------- Dialog Add Subject -----------------------------------------//
    private void showAddItemDialog(final Context c) {
        final Dialog addSubjectDialog = new Dialog(c);
        addSubjectDialog.setContentView(R.layout.dialog_add_subject);
        final EditText editextSubjectID = addSubjectDialog.findViewById(R.id.editextSubjectID);
        final EditText editextSubjectName = addSubjectDialog.findViewById(R.id.editextSubjectName);
        ImageButton btnAddSubject = addSubjectDialog.findViewById(R.id.btnAddSubject);
        ImageButton btnCancel = addSubjectDialog.findViewById(R.id.btnCancel);
        //SAVE
        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //table_subject.addValueEventListener(new ValueEventListener() {
                //addListenerForSingleValueEvent reads data just 1 times only
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDataFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                final String currentDate = simpleDataFormat.format(calendar.getTime());

                table_subject.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if editText is empty
                        if (editextSubjectID.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Please enter subject id", Toast.LENGTH_SHORT).show();
                        } else if (editextSubjectName.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Please enter subject name", Toast.LENGTH_SHORT).show();
                        } /*else if (dataSnapshot.child(editextSubjectID.getText().toString()).exists()) {
                            Toast.makeText(c, "Subject id has existed", Toast.LENGTH_SHORT).show();
                        } else if (dataSnapshot.child(editextSubjectName.getText().toString()).exists()) {
                            Toast.makeText(c, "Subject name has existed", Toast.LENGTH_SHORT).show();
                        }*/ else {
                            Subject subject = new Subject(editextSubjectID.getText().toString().toUpperCase(), editextSubjectName.getText().toString().toUpperCase(), Username, currentDate);
                            table_subject.push().setValue(subject);
                            Toast.makeText(c, "Subject already is added", Toast.LENGTH_SHORT).show();
                            /*Intent signUp = new Intent(Signup1Activity.this, MainActivity.class);
                            startActivity(signUp);*/
                            addSubjectDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubjectDialog.cancel();
            }
        });
        addSubjectDialog.show();
    }

    //*************************************************************************************************************************************************//

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
            if (searchField.getText().toString().isEmpty()) {
                //Toast.makeText(getActivity(), "Please enter subjectname", Toast.LENGTH_SHORT).show();
                GetSubjectFirebase();
            } else {
                Toast.makeText(getActivity(), "Search", Toast.LENGTH_SHORT).show();
                String searchText = searchField.getText().toString().toUpperCase();
                GetSearchFirebase(searchText);
            }
        } else if (v == fab) {
            Toast.makeText(getContext(), "Add a new subject", Toast.LENGTH_SHORT).show();
            showAddItemDialog(getContext());
        }
    }


}