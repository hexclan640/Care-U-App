package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.AsyncNotedAppOp;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.util.concurrent.ExecutionException;

public class forgetPassword extends AppCompatActivity {
    EditText _username ,  _nicNumber;
    AwesomeValidation awesomeValidation;
    //MyTask myTask = new MyTask();

    LoadingDialog loadingDialog = new LoadingDialog(forgetPassword.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        _username = findViewById(R.id.userName);
//        _nicNumber = findViewById(R.id.nicNumber);
    }

    public void resetPassword(View view) throws ExecutionException, InterruptedException {
//        Toast.makeText(forgetPassword.this, "btn pressed", Toast.LENGTH_LONG).show();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        String userName = _username.getText().toString();
        awesomeValidation.addValidation(this,R.id.userName, RegexTemplate.NOT_EMPTY,R.string.Invalid_user_name);
        loadingDialog.startLoading();
//        String nicNumber = _nicNumber.getText().toString();
//        awesomeValidation.addValidation(this,R.id.nicNumber, RegexTemplate.NOT_EMPTY,R.string.Invalid_NIC);
//        if (nicNumber.length()==10){
//
//            awesomeValidation.addValidation(this,R.id.nicNumber,"[0-9]{9}[V|v]{1}$",R.string.Invalid_NIC);
//        }else if (nicNumber.length()==12) {
////            Toast.makeText(this, "length is"+NIC.length(), Toast.LENGTH_LONG).show();
//            awesomeValidation.addValidation(this, R.id.nicNumber, "[0-9]{12}$", R.string.Invalid_NIC);
//        }else {
////            Toast.makeText(this, "length is"+NIC.length(), Toast.LENGTH_LONG).show();
//            awesomeValidation.addValidation(this,R.id.nicNumber,"",R.string.Invalid_NIC);
//        }

        if (awesomeValidation.validate()) {

            String type = "forgetPassword";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type,userName);
            //loadingDialog.dismissDialog();

        }

    }
}