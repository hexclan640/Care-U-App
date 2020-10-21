package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnlogin,btnreg;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences=getSharedPreferences("logIn", MODE_PRIVATE);
        if(sharedPreferences.contains("logInStatus"))
        {
            Intent intent=new Intent(getApplicationContext(),homePageDuplicate.class);
            startActivity(intent);
        }

        btnlogin = findViewById(R.id.btnLog);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sp.edit().putBoolean("logged",true).apply();
                Intent i = new Intent(getApplicationContext(),loginPage.class);
                startActivity(i);
            }
        });
        btnreg = findViewById(R.id.btnReg);
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toreg = new Intent(getApplicationContext(),registrationPage.class);
                startActivity(toreg);
            }
        });
    }
}
