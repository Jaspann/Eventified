package com.example.eventified;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addUrls();

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toolbar.setTitle("Home");

        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.bringToFront();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            //navigationView.setCheckedItem(R.id.nav_home);
            toolbar.setTitle("Home");
        }
    }

    @Override
    public void onBackPressed()
    {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            moveTaskToBack(true);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                toolbar.setTitle("Home");
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
                toolbar.setTitle("Search");
                break;
            case R.id.nav_eboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BoardClubListFragment()).commit();
                toolbar.setTitle("Your Clubs");
                break;
            case R.id.nav_member:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ClubListFragment()).commit();
                toolbar.setTitle("Clubs");
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addUrls(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("LoginLambda", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/loginlambda?");
        editor.putString("SignUpLambda", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/signuplambda?");
        editor.putString("getClubSearch", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/getclubsearch?");
        editor.putString("getClubInfo", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/getclubinfo?");
        editor.putString("putClubEventRegister", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/putclubeventregister?");
        editor.putString("putStudentClub", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/putstudentclub?");
        editor.putString("memberClubList", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/memberclublist?");
        editor.putString("getClubOwner", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/getclubowner?");
        editor.putString("getHomeEvents", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/gethomeevents?");
        editor.putString("getEventRegistered", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/geteventregistered?");
        editor.putString("putAddClubEvent", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/putaddclubevent?");
        editor.putString("putAddUserEvent", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/putadduserevent?");
        editor.putString("putDeleteClubEvent", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/putdeleteclubevent?");
        editor.putString("putDeleteUserEvent", "https://9dzzv631oi.execute-api.us-east-1.amazonaws.com/test/putdeleteuserevent?");

        editor.apply();
    }
}