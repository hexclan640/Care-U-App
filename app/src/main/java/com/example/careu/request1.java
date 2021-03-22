package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class request1 extends AppCompatActivity {

    TextView dateTime,district,policeStation,noOfPatients,description,requestId;
    String rId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_1);

        dateTime = findViewById(R.id.dateTime);
        requestId = findViewById(R.id.requestId);
        policeStation = findViewById(R.id.policeStation);
        noOfPatients = findViewById(R.id.patients);
        description = findViewById(R.id.description);
        Intent intent =getIntent();
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String des = intent.getStringExtra("description");
        String poliStation = intent.getStringExtra("policeStation");
        String nop =intent.getStringExtra("numberOfPatients");
        String ID =  intent.getStringExtra("requestId");
        rId= ID;
        dateTime.setText(date+" - " +time );
        requestId.setText("0000"+"-"+ID);
        description.setText(des);
        noOfPatients.setText(nop);
        policeStation.setText(poliStation);

    }
    public void feedbk(View view) {
        Intent i = new Intent(getApplicationContext(),feedback.class);
        i.putExtra("id",rId);
//        Intent i= new Intent(this,feedback.class);
        startActivity(i);
    }

}
