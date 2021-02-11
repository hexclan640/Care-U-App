package com.example.careu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class feedback extends AppCompatActivity {
    EditText _feedback;
    String feedbackMassage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        _feedback = findViewById(R.id.feedback);
         feedbackMassage=_feedback.getText().toString();
        Toast.makeText(feedback.this,feedbackMassage , Toast.LENGTH_SHORT).show();
        final Intent l = new Intent(this, requestList.class);
        if (feedbackMassage.isEmpty()){
            AlertDialog.Builder alert = new AlertDialog.Builder(feedback.this);
            alert.setTitle("Feedback");
            alert.setMessage("Cannot Enter the Empty FeedBack Field");
            alert.setPositiveButton("Request-List", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(l);
                }
            });
            alert.setCancelable(true);
        }
    }

    public void sendfeedback(View view) {
        Toast.makeText(feedback.this,feedbackMassage , Toast.LENGTH_SHORT).show();
    }
}
