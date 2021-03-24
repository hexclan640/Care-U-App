package com.example.careu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class homePageDuplicate extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sharedPreferences1,sharedPreferences2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_duplicate);


        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.app_bar);

        sessionManagement sessionManagement1 = new sessionManagement(homePageDuplicate.this);
        String username = sessionManagement1.getSession();
       // Toast.makeText(this, username+" is logged in", Toast.LENGTH_SHORT).show();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.myProfile:
                        Intent myp = new Intent(getApplicationContext(),myprofile.class);
                        startActivity(myp);
                        return true;

                    case R.id.myRequests:
                        Intent myr = new Intent(getApplicationContext(),myrequests.class);
                        startActivity(myr);
                        return true;

                    case R.id.editProfile:
                        Intent editp = new Intent(getApplicationContext(),edit_prof.class);
                        startActivity(editp);
                        return true;

                    case R.id.addRelations:
                        Intent relations = new Intent(getApplicationContext(),addRelations.class);
                        startActivity(relations);
                        return true;

                    case R.id.logOut:

                        sessionManagement sessionManagement = new sessionManagement(homePageDuplicate.this);
                        sessionManagement.removeSession();
                        Intent logout=new Intent(getApplicationContext(),MainActivity.class);
                        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logout);
                        return true;
                }
                return true;
            }
        });

        startBackGroundProcessButtonClick();
    }

    @SuppressLint("ShortAlarm")
    public void startBackGroundProcessButtonClick(){

        Intent intent = new Intent(this, MyBackground.class);
        intent.setAction("BackgroundProcess");

        //Set the repeated Task
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 60000, pendingIntent);

//        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Police_Emergency_Service(View view) {
        Intent i = new Intent(this,policeEcomplain.class);
        startActivity(i);
    }

    public void Suwasariya_Ambulance_Srevice(View view) {
        Intent i = new Intent(this,ambulance.class);
        i.putExtra("retry","0");
        startActivity(i);
    }

    public void profile(View view) {
        Intent i = new Intent(this, myprofile.class);
        startActivity(i);
    }

    public void emgency_Numbers(View view) {
        Intent i = new Intent(this, emergencyNumbersCate.class);
        startActivity(i);
    }

    public void first_Aid_Instructions(View view) {
        Intent i = new Intent(this,firstAidInstruction.class);
        startActivity(i);
    }

}