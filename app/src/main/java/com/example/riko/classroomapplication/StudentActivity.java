package com.example.riko.classroomapplication;

//-- Toolbar & DrawerLayout --//
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
//-----------------------------//



public class StudentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //-- DrawerLayout --//
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //-- Toolbar & DrawerLayout --//
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout_stu);
        NavigationView navigationView = findViewById(R.id.nav_view_stu);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ChangepwFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_changepw);
        }

        //---------------------------------------------------------//


    }

    //-- Toolbar & DrawerLayout --//
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_coures:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TeacherCoursesFragment()).commit();
                break;
            case R.id.nav_changepw:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChangepwFragment()).commit();
                break;
            case R.id.nav_logout:
                signOut();
                Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        Intent intent = new Intent(StudentActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //---------------------------------------------------------//



}
