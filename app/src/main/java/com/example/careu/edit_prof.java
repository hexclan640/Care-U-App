package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class edit_prof extends AppCompatActivity {


    String apiurl ="http://10.0.2.2/careu-php/myprofile.php?userName=";
    EditText _fName,_lName,_email,_phoneNumber;
    TextView _userName,_nicNumber;
    String username,fname,lname,email,phoneNumber,updateStatus;
    AwesomeValidation awesomeValidation;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
        setContentView(R.layout.activity_edit_prof);
        sessionManagement sessionManagement1 = new sessionManagement(edit_prof.this);
        String user = sessionManagement1.getSession();
        apiurl ="http://10.0.2.2/careu-php/myprofile.php?userName="+user;
        _userName = findViewById(R.id.userName);
        _fName = findViewById(R.id.fName);
        _lName = findViewById(R.id.lName);
        _email =findViewById(R.id.email);
        _phoneNumber = findViewById(R.id.phoneNumber);
        _nicNumber = findViewById(R.id.nicNumber);

        viewprofile();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


    }

    private void viewprofile() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray mypro =  new JSONArray(response);
                    JSONObject myproObject = mypro.getJSONObject(0);
                    username = myproObject.getString("userName");
                    _userName.setText(username);
                    fname = myproObject.getString("firstName");
                    lname = myproObject.getString("lastName");
                    _fName.setText(fname);
                    _lName.setText(lname);
                    email = myproObject.getString("email");
                    _email.setText(email);
                    phoneNumber = myproObject.getString("phoneNumber");
                    _phoneNumber.setText(phoneNumber);
                    String nicNumber= myproObject.getString("nicNumber");
                    _nicNumber.setText(nicNumber);
                    Toast.makeText(edit_prof.this, username, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(edit_prof.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void Cancel(View view) {


        Intent i = new Intent(this,myprofile.class);
        startActivity(i);
    }

    public void updateProfile(View view) throws ExecutionException, InterruptedException {

        String type ="updateProfile";

        awesomeValidation.addValidation(this,R.id.fName, RegexTemplate.NOT_EMPTY,R.string.Invalid_First_name);
        awesomeValidation.addValidation(this,R.id.fName, "^[a-zA-Z]*$",R.string.cannotBeNumbers);

        _fName = findViewById(R.id.fName);
        fname =_fName.getText().toString();


        awesomeValidation.addValidation(this,R.id.lName, RegexTemplate.NOT_EMPTY,R.string.Invalid_Last_name);
        awesomeValidation.addValidation(this,R.id.lName, "^[a-zA-Z]*$",R.string.cannotBeNumbers1);

        _lName = findViewById(R.id.lName);
        lname=_lName.getText().toString();


        awesomeValidation.addValidation(this,R.id.email, Patterns.EMAIL_ADDRESS,R.string.Invalid_email);

        _email =findViewById(R.id.email);
        email =_email.getText().toString();

        awesomeValidation.addValidation(this,R.id.phoneNumber,"[0]{1}[7]{1}[1||2||5||6||7||8]{1}[0-9]{7}$",R.string.invalid_number1);

        _phoneNumber = findViewById(R.id.phoneNumber);
        phoneNumber=_phoneNumber.getText().toString();

        if (awesomeValidation.validate()){
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            updateStatus = backgroundWorker.execute(type,username,fname,lname,email,phoneNumber).get();
        }else {
            Toast.makeText(this, "wrongSuccess", Toast.LENGTH_SHORT).show();
            updateStatus="hello-1";
        }


        if (updateStatus.equals("Record updated")){

            dialog = new Dialog(edit_prof.this);
            dialog.setContentView(R.layout.activity_popup);
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.getWindow().getAttributes().windowAnimations= R.style.animation;
            TextView detail = dialog.findViewById(R.id.details);
            detail.setText("Successfully Updated your Profile !");
            Button Home = dialog.findViewById(R.id.button4);
            Home.setText("Go Back Home");
            Home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    final Intent l = new Intent(edit_prof.this, homePageDuplicate.class);
                    startActivity(l);
                }
            });
            Button request_List =  dialog.findViewById(R.id.button3);
            request_List.setText("My Profile");
            request_List.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    final Intent l = new Intent(edit_prof.this, myprofile.class);
                    startActivity(l);
                }
            });
            dialog.show();

        }else {
            dialog = new Dialog(edit_prof.this);
            dialog.setContentView(R.layout.activity_popup);
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.getWindow().getAttributes().windowAnimations= R.style.animation;
            ImageView imageView= dialog.findViewById(R.id.imageView2);
            imageView.setImageResource(R.drawable.warnning);
            TextView detail = dialog.findViewById(R.id.details);
            detail.setText(updateStatus);
            Button Home = dialog.findViewById(R.id.button4);
            Home.setText("Edit Profile");
            Home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    final Intent l = new Intent(edit_prof.this, edit_prof.class);
                    startActivity(l);
                }
            });
            Button request_List =  dialog.findViewById(R.id.button3);
            request_List.setText("My Profile");
            request_List.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    final Intent l = new Intent(edit_prof.this, myprofile.class);
                    startActivity(l);
                }
            });
            dialog.show();

        }


    }
}
