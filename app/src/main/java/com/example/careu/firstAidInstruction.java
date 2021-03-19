package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class firstAidInstruction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_instruction);
    }

    public void btn1(View view) {
        Intent i= new Intent(this, Pop1.class);
        startActivity(i);
    }

    public void btn2(View view) {
        Intent i= new Intent(this, Pop2.class);
        startActivity(i);
    }

    public void btn3(View view) {
        Intent i= new Intent(this, Pop3.class);
        startActivity(i);
    }

    public void btn4(View view) {
        Intent i= new Intent(this, Pop4.class);
        startActivity(i);

    }

    public void btn5(View view) {
        Intent i= new Intent(this, Pop5.class);
        startActivity(i);
    }


    public void btn6(View view) {
        Intent i= new Intent(this, Pop6.class);
        startActivity(i);
    }
}