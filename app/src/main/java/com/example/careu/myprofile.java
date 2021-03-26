package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class myprofile extends AppCompatActivity {
    String username= null;
    String apiurl ="http://10.0.2.2/careu-php/myprofile.php?userName=";
    TextView _userName,_fullName,_email,_phoneNumber,_nicNumber ,_r1,_r1Number,_r2,_r2Number,_r3,_r3Number;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//
        setContentView(R.layout.activity_my_profile);


        sessionManagement sessionManagement1 = new sessionManagement(myprofile.this);
        String user = sessionManagement1.getSession();
        apiurl ="http://10.0.2.2/careu-php/myprofile.php?userName="+user;
        _userName = findViewById(R.id.userName);
        _fullName = findViewById(R.id.fullName);
        _email =findViewById(R.id.email);
        _phoneNumber = findViewById(R.id.phoneNumber);
        _nicNumber = findViewById(R.id.nicNumber);
        _r1 = findViewById(R.id.r1);
        _r1Number =findViewById(R.id.r1_num);
        _r2 = findViewById(R.id.r2);
        _r2Number =findViewById(R.id.r2_num);
        _r3 = findViewById(R.id.r3);
        _r3Number =findViewById(R.id.r3_num);
        viewprofile();


    }

    private void viewprofile() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray mypro =  new JSONArray(response);
                    JSONObject myproObject = mypro.getJSONObject(0);
                    JSONObject myproObject1 = mypro.getJSONObject(1);
                    JSONObject myproObject2 = mypro.getJSONObject(2);
                    username = myproObject.getString("userName");
                    _userName.setText(username);
                    String fname = myproObject.getString("firstName");
                    String lname = myproObject.getString("lastName");
                    String fullName = fname + " " +lname;
                    _fullName.setText(fullName);
                    String email = myproObject.getString("email");
                    _email.setText(email);
                    String phoneNumber = myproObject.getString("phoneNumber");
                    _phoneNumber.setText(phoneNumber);
                    String nicNumber= myproObject.getString("nicNumber");
                    _nicNumber.setText(nicNumber);

                    if (myproObject2.getInt("num")==0){
                        String r1 = myproObject.getString("relative");
                        _r1.setText(r1);
                        String r1Number = myproObject.getString("relativeNumber");
                        _r1Number.setText(r1Number);

                        String r2 = myproObject.getString("relative");
                        _r2.setText(r2);
                        String r2Number = myproObject.getString("relativeNumber");
                        _r2Number.setText(r2Number);

                        String r3 = myproObject.getString("relative");
                        _r3.setText(r3);
                        String r3Number = myproObject.getString("relativeNumber");
                        _r3Number.setText(r3Number);

                    }else if (myproObject2.getInt("num")==1){
                        String r1 = myproObject.getString("relative");
                        _r1.setText(r1);
                        String r1Number = myproObject.getString("relativeNumber");
                        _r1Number.setText(r1Number);
                        _r2.setText("No-relative");
                        _r2Number.setText("No-relative");
                        _r3.setText("No-relative");
                        _r3Number.setText("No-relative");

                    }else if (myproObject2.getInt("num")==2){
                        String r1 = myproObject.getString("relative");
                        _r1.setText(r1);
                        String r1Number = myproObject.getString("relativeNumber");
                        _r1Number.setText(r1Number);
                        String r2 = myproObject1.getString("relative");
                        _r2.setText(r2);
                        String r2Number = myproObject1.getString("relativeNumber");
                        _r2Number.setText(r2Number);

                        _r3.setText("No-relative");
                        _r3Number.setText("No-relative");
                    }else {
                        String r1 = myproObject.getString("relative");
                        _r1.setText(r1);
                        String r1Number = myproObject.getString("relativeNumber");
                        _r1Number.setText(r1Number);

                        String r2 = myproObject1.getString("relative");
                        _r2.setText(r2);
                        String r2Number = myproObject1.getString("relativeNumber");
                        _r2Number.setText(r2Number);

                        String r3 = myproObject2.getString("relative");
                        _r3.setText(r3);
                        String r3Number = myproObject2.getString("relativeNumber");
                        _r3Number.setText(r3Number);
                    }



                    //Toast.makeText(myprofile.this, username, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(myprofile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }



    public void my_requests(View view) {
        Intent i= new Intent(this, myrequests.class);
        startActivity(i);
    }

    public void edit_profile(View view) {
        Intent i= new Intent(this,edit_prof.class);
        startActivity(i);
    }
}
