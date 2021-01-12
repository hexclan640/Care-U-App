package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class request1 extends AppCompatActivity {

    TextView dateTime,district,policeStation,noOfPatients,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_1);

        dateTime = findViewById(R.id.dateTime);
        district = findViewById(R.id.district);
        policeStation = findViewById(R.id.policeStation);
        noOfPatients = findViewById(R.id.patients);
        description = findViewById(R.id.description);
        Intent intent =getIntent();
        String s = intent.getStringExtra("date");
        dateTime.setText(s);
    }
    public void feedbk(View view) {
        Intent i= new Intent(this,feedback.class);
        startActivity(i);
    }

}
