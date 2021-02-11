package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class feedback extends AppCompatActivity {
    EditText _feedback;
    String feedbackMassage;
    android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

//        _feedback = findViewById(R.id.feedback);
//         feedbackMassage=_feedback.getText().toString();
//        Toast.makeText(feedback.this,feedbackMassage , Toast.LENGTH_SHORT).show();

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
            Toast.makeText(feedback.this,feedbackMassage , Toast.LENGTH_SHORT).show();
//            Toast.makeText(feedback.this,feedbackMassage , Toast.LENGTH_SHORT).show();
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
