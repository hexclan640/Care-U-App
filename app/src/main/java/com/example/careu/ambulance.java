package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class ambulance extends AppCompatActivity {

    Spinner districtSpinner,policeSpinner,patientSpinner;
    EditText note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);

        note = findViewById(R.id.note);
        districtSpinner=findViewById(R.id.disSpinner);
        policeSpinner=findViewById(R.id.policeSpinner);
        patientSpinner=findViewById(R.id.patientSpinner);
    }

    public void cancecl_req(View view) {
        Intent i = new Intent(this,homePageDuplicate.class);
        startActivity(i);
    }

    public void requestAmbulance(View view) {

        String type = "ambulance";
        Calendar cc = Calendar.getInstance();
        int year = cc.get(Calendar.YEAR);
        int month = cc.get(Calendar.MONTH) + 1;
        int mDay = cc.get(Calendar.DAY_OF_MONTH);
        String sYear = Integer.toString(year);
        String sMonth = Integer.toString(month);
        String sDate = Integer.toString(mDay);
        String date = sYear+"/"+sMonth+"/"+sDate;

        int mHour = cc.get(Calendar.HOUR_OF_DAY);
        int mMinute = cc.get(Calendar.MINUTE);
        int mSecond = cc.get(Calendar.SECOND);
        String sHour = Integer.toString(mHour);
        String sMinute = Integer.toString(mMinute);
        String sSecond = Integer.toString(mSecond);
        String time = sHour+":"+sMinute+":"+sSecond;

        String district = districtSpinner.getSelectedItem().toString();
        String policeStation = policeSpinner.getSelectedItem().toString();
        String noOfPatients = patientSpinner.getSelectedItem().toString();
        String description = note.getText().toString();

        sessionManagement sessionManagement = new sessionManagement(this);
        String username = sessionManagement.getSession();

        BackgroundWorkerRequest backgroundWorkerRequest = new BackgroundWorkerRequest(this);
        backgroundWorkerRequest.execute(type,username,date,time,district,policeStation,noOfPatients,description);
    }
}