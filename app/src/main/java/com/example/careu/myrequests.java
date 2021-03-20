package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class myrequests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);
    }

    public void ambulance(View view) {
        Intent i= new Intent(this, requestList.class);
        startActivity(i);
    }

    public void police(View view) {
        Intent i= new Intent(this, policeRequestList.class);
        startActivity(i);
    }


}
