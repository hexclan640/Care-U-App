package com.example.careu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AsyncNotedAppOp;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Layout;
import android.view.LayoutInflater;

import androidx.loader.content.AsyncTaskLoader;

public class LoadingDialog {

    Activity activity;
    AlertDialog alertDialog;
//    Context activity;

    LoadingDialog(Activity myActivity){
        activity = myActivity;
    }

    void startLoading(){

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            //LayoutInflater inflater = LayoutInflater.from(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.custom_dialog,null));
            builder.setCancelable(true);

            alertDialog = builder.create();
            alertDialog.show();

    }

    void dismissDialog(){
        alertDialog.dismiss();
    }

}
