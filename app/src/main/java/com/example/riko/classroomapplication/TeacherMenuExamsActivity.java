package com.example.riko.classroomapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riko.classroomapplication.Model.Assign;
import com.example.riko.classroomapplication.Model.Member;
import com.example.riko.classroomapplication.Model.Subject;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeacherMenuExamsActivity extends AppCompatActivity implements View.OnClickListener {


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
    private BottomSheetDialog bottomSheetMenu;
    private View sheetView;
    private LinearLayout createAssign;
    private LinearLayout editAssign;
    private LinearLayout checkScore;
    private LinearLayout delete;
    private Intent iassign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_menu_exams);
        initInstance();
        fabButtomAddSubject();
        //bottomSheetSelectMenu();
        backToolbar();

    }

    private void initInstance() {

        //-- Toolbar --***//
        toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        subjectID = intent.getStringExtra("subjectID");
        subjectname = intent.getStringExtra("subjectname");
        toolbar.setTitle(subjectname);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        //-----------------------------------------------//
        //------------ Receive Intent from SignIn ------------***//
        headerView = navigationView.getHeaderView(0);
        textUsername = headerView.findViewById(R.id.txtUsername);
        textStatus = headerView.findViewById(R.id.txtStatus);
        textName = headerView.findViewById(R.id.txtName);
        //------------------------------------------------------//

        //----- Firebase ------//
        database = FirebaseDatabase.getInstance();
        table_assign = database.getReference().child("Assign");

        //---------- Fad Button -------------//
        fab = findViewById(R.id.fabPlus);
        fab.setOnClickListener(this);

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

        //----------------- Subject list -------------------------------//
        listSubjectID = new ArrayList<>();
        listAssignName = new ArrayList<>();
        assignAdapter = new AssignAdapter(listAssignName, new AssignAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Assign assign) {
                bottomSheetSelectMenu();
                displaySelectMenu();
                createAssign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TeacherMenuExamsActivity. this, "Create assignment", Toast.LENGTH_SHORT).show();
                        Intent iassign = new Intent(TeacherMenuExamsActivity.this, AddExamsActivity.class);
                        iassign.putExtra("assignname", assign.getAssignname());
                        startActivity(iassign);
                        bottomSheetMenu.dismiss();
                    }
                });
                editAssign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TeacherMenuExamsActivity. this, "Edit assignment", Toast.LENGTH_SHORT).show();
                        Intent iassign = new Intent(TeacherMenuExamsActivity.this, EditExamsActivity.class);
                        iassign.putExtra("assignname", assign.getAssignname());
                        startActivity(iassign);
                        bottomSheetMenu.dismiss();
                    }
                });
                checkScore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TeacherMenuExamsActivity. this, "Check scores of students", Toast.LENGTH_SHORT).show();
                        Intent iassign = new Intent(TeacherMenuExamsActivity.this, CheckScoresActivity.class);
                        iassign.putExtra("assignname", assign.getAssignname());
                        startActivity(iassign);
                        bottomSheetMenu.dismiss();
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TeacherMenuExamsActivity. this, "Delete assignment", Toast.LENGTH_SHORT).show();
                        bottomSheetMenu.dismiss();
                    }
                });

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

    void GetSearchFirebase(String searchText) {

        //Clear ListSubject
        listAssignName.clear();

        Query searchQuery = table_assign.orderByChild("assignname").startAt(searchText).endAt(searchText + "\uf8ff");
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

    //---------------- Subject List -------------------------------------------------//
    public static class AssignAdapter extends RecyclerView.Adapter<TeacherMenuExamsActivity.AssignAdapter.AssignHolder> {

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
            return new AssignHolder(v);
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

    //--------------- Bottom sheet dialog: Select menu ---------------------//
    private void bottomSheetSelectMenu() {
        bottomSheetMenu = new BottomSheetDialog(this);
        sheetView = TeacherMenuExamsActivity.this.getLayoutInflater().inflate(R.layout.bottom_sheet_menu, null);
        bottomSheetMenu.setContentView(sheetView);
        initSelectMenu();
    }
    private void initSelectMenu() {
        createAssign = sheetView.findViewById(R.id.menuCreateAssign);
        editAssign = sheetView.findViewById(R.id.menuEditAssign);
        checkScore = sheetView.findViewById(R.id.menuScore);
        delete = sheetView.findViewById(R.id.menuDelete);
    }
    private void displaySelectMenu() {
        //Toast.makeText(TeacherMenuExamsActivity. this, "Assignment is clicked", Toast.LENGTH_SHORT).show();
        bottomSheetMenu.show();
    }


    private void showAddItemDialog(final Context c) {
        final Dialog addAssignDialog = new Dialog(c);
        addAssignDialog.setContentView(R.layout.dialog_add_assign_name);
        final EditText editextAssignName = addAssignDialog.findViewById(R.id.editextAssignname);
        ImageButton btnAddAssign = addAssignDialog.findViewById(R.id.btnAddAssign);
        ImageButton btnCancel = addAssignDialog.findViewById(R.id.btnCancel);
        //SAVE
        btnAddAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(c, "Assignment already is added", Toast.LENGTH_SHORT).show();
                addAssignDialog.dismiss();*/
                table_assign.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if editText is empty
                        if (editextAssignName.getText().toString().isEmpty()) {
                            Toast.makeText(c, "Please enter assignment name", Toast.LENGTH_SHORT).show();
                        }  else if (dataSnapshot.child(editextAssignName.getText().toString()).exists()) {
                            Toast.makeText(c, "Assignment name has existed", Toast.LENGTH_SHORT).show();
                        } else {
                            Assign assign = new Assign(editextAssignName.getText().toString().toUpperCase(), subjectID);
                            table_assign.push().setValue(assign);
                            Toast.makeText(c, "Assignment already is added", Toast.LENGTH_SHORT).show();
                            /*Intent signUp = new Intent(Signup1Activity.this, MainActivity.class);
                            startActivity(signUp);*/
                            addAssignDialog.dismiss();
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
                addAssignDialog.cancel();
            }
        });
        addAssignDialog.show();
    }


    //Flot action button: Add subject
    private void fabButtomAddSubject() {
        // Hide Floating Action Button when scrolling in Recycler View
        recyclerViewAssign.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        } else if (v == fab) {
            Toast.makeText(this, "Add a new assignment", Toast.LENGTH_SHORT).show();
            showAddItemDialog(this);
        }
    }
}
