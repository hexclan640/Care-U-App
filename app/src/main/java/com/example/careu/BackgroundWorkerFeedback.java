package com.example.careu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Struct;

public class BackgroundWorkerFeedback extends AsyncTask<String,Void,String> {

    AlertDialog alertDialog;
    Context context;

    BackgroundWorkerFeedback(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params){
        int type = Integer.parseInt(params[0]);
        String username = params[1];
        String feedbackMassage = params[2];
        String time = params[3];
        String requestID= params[4];
        String rate= params[5];
        int rId= Integer.parseInt(requestID);

        String profileUrl = "http://10.0.2.2/careu-php/feedback.php?userName="+username+"&type="+type+"&requestID="+rId;
        try {
            URL url = new URL(profileUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String request =URLEncoder.encode("userName", "UTF-8")+"="+URLEncoder.encode(username, "UTF-8")+
                    "&"+URLEncoder.encode("feedbackMassage", "UTF-8")+"="+URLEncoder.encode(feedbackMassage, "UTF-8")+
                    "&"+URLEncoder.encode("time", "UTF-8")+"="+URLEncoder.encode(time, "UTF-8")+
                    "&"+URLEncoder.encode("rate", "UTF-8")+"="+URLEncoder.encode(rate, "UTF-8");
            bufferedWriter.write(request);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog = new AlertDialog.Builder(context).create();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s.equals("Successfully saved the feedback")){
            alertDialog.setMessage(s);
            alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent toHome = new Intent(context,homePageDuplicate.class);
                    context.startActivity(toHome);
                }
            });
            alertDialog.show();
        }else {
            alertDialog.setMessage(s);
            alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent toHome = new Intent(context,homePageDuplicate.class);
                    context.startActivity(toHome);
                }
            });
            alertDialog.show();
        }

//        if(s.equals("hello")) {
//            alertDialog.setMessage(s);
//            alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    Intent toHome = new Intent(context,homePageDuplicate.class);
//                    context.startActivity(toHome);
//                }
//            });
//            alertDialog.show();
//            //Toast.makeText(myprofile.class, "userfound", Toast.LENGTH_SHORT).show();
//        }else if(s.equals("can not find the user")){
//            alertDialog.setMessage(s);
//            alertDialog.show();
//        }else if(s.equals("user found")){
//            alertDialog.setMessage(s);
//            alertDialog.show();
//        }

    }
}

//if (s.equals("Success fully Updated the feedback"))