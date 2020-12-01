package com.example.careu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class loginPage extends AppCompatActivity {

    Button btnlogin;
    TextView forgetpw;
    EditText txtuserName,txtpassword;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);



        forgetpw = findViewById(R.id.txtfp);
        forgetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), forgetPassword.class);
                startActivity(intent);
            }
        });
    }



    public void homePage(View view) throws ExecutionException, InterruptedException {
        //sp.edit().putBoolean("logged",true).apply();
        txtuserName = findViewById(R.id.txtuserName);
        txtpassword = findViewById(R.id.txtpw);
        //sp.edit().putBoolean("logged",true).apply();
        String username = txtuserName.getText().toString();
        String password = txtpassword.getText().toString();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        String reg = backgroundWorker.execute(type,username,password).get();


        if (reg.equals("login success")){

            User user = new User(username);
            sessionManagement sessionManagement = new sessionManagement(loginPage.this);
            sessionManagement.saveSession(user);

            Intent ne = new Intent(this,homePageDuplicate.class);
            ne.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(ne);

        }else {
            Toast.makeText(this,reg, Toast.LENGTH_LONG).show();
        }


    }


}
