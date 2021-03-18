package com.example.careu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class feedback extends AppCompatActivity {
    EditText _feedback;
    String feedbackMassage,feedbackurl;
    String feedbackId[];
    android.app.AlertDialog alertDialog;
    String user,ID;
    View feedbackView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        sessionManagement sessionManagement1 = new sessionManagement(feedback.this);
         user= sessionManagement1.getSession();
        Intent intent =getIntent();
         ID = intent.getStringExtra("id");
         int rId= Integer.parseInt(ID);
        feedbackurl = "http://10.0.2.2/careu-php/feedback.php?userName="+user+"&type="+0+"&requestID="+rId;
        _feedback = findViewById(R.id.feedback);
//         feedbackMassage=_feedback.getText().toString();
//        Toast.makeText(feedback.this,"The user Id="+ID , Toast.LENGTH_SHORT).show();

         retive_last_feedback();
    }
    private void retive_last_feedback() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, feedbackurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray feedback =  new JSONArray(response);
                    JSONObject feedbackObject = feedback.getJSONObject(0);

                     String feedbackComment= feedbackObject.getString("feedbackComment");
                    _feedback.setText(feedbackComment);




                    //Toast.makeText(myprofile.this, username, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(feedback.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }





    public void sendfeedback(View view) {
        _feedback = findViewById(R.id.feedback);
        feedbackMassage=_feedback.getText().toString();
        alertDialog = new AlertDialog.Builder(feedback.this).create();
        alertDialog.setTitle("Feedback");
        if (feedbackMassage.isEmpty()){
            Toast.makeText(feedback.this,feedbackMassage , Toast.LENGTH_SHORT).show();
//            AlertDialog.Builder alert = new AlertDialog.Builder(feedback.this);
            final Intent k = new Intent(this, requestList.class);
            final Intent l = new Intent(this, feedback.class);
            //this.startActivity(i);
            AlertDialog.Builder builder = new AlertDialog.Builder(feedback.this);
            builder.setMessage("Cannot Empty FeedBack Feild");
            builder.setPositiveButton("MyRequest List", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(k);
                }
            });
            builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(l);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        }else {
            String date =  Calendar.getInstance().getTime().toString();
            BackgroundWorkerFeedback backgroundWorkerFeedback = new BackgroundWorkerFeedback(this);
            backgroundWorkerFeedback.execute("1",user,feedbackMassage,date,ID);



            Toast.makeText(feedback.this,feedbackMassage , Toast.LENGTH_SHORT).show();
            Toast.makeText(feedback.this,date, Toast.LENGTH_SHORT).show();
//            AlertDialog.Builder alert = new AlertDialog.Builder(feedback.this);
            alertDialog.setMessage("Success fully added the FEEDBACK THANK YOU !");
            alertDialog.setButton("Home", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final Intent l = new Intent(feedback.this, homePageDuplicate.class);
                    startActivity(l);
                }
            });
            alertDialog.show();
        }
    }



}


