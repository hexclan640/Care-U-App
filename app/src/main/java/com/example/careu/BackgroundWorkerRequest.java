package com.example.careu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

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

public class BackgroundWorkerRequest extends AsyncTask<String,Void,String> {

    AlertDialog alertDialog;
    Context context;
    Dialog dialog;

    BackgroundWorkerRequest(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params){
        String type = params[0];

        String profileUrl = "http://10.0.2.2/careu-php/Request.php";
        String checkStatus = "http://10.0.2.2/careu-php/checkStatus.php";
        String requestStatus = "http://10.0.2.2/careu-php/getStatus.php";

        if(type.equals("ambulance")) {

            String username = params[1];
            String date = params[2];
            String time = params[3];
            String district = params[4];
            String policeStation = params[5];
            String noOfPatients = params[6];
            String description = params[7];
            String latitude = params[8];
            String longitude = params[9];

            try {
                URL url = new URL(profileUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String request = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") +
                        "&" + URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") +
                        "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") +
                        "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8") +
                        "&" + URLEncoder.encode("district", "UTF-8") + "=" + URLEncoder.encode(district, "UTF-8") +
                        "&" + URLEncoder.encode("policeStation", "UTF-8") + "=" + URLEncoder.encode(policeStation, "UTF-8") +
                        "&" + URLEncoder.encode("noOfPatients", "UTF-8") + "=" + URLEncoder.encode(noOfPatients, "UTF-8") +
                        "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8") +
                        "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8") +
                        "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8");
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
        }else if(type.equals("police")){

            String username = params[1];
            String date = params[2];
            String time = params[3];
            String district = params[4];
            String policeStation = params[5];
            String category = params[6];
            String description = params[7];

            try {
                URL url = new URL(profileUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String request = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") +
                        "&" + URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") +
                        "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") +
                        "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8") +
                        "&" + URLEncoder.encode("district", "UTF-8") + "=" + URLEncoder.encode(district, "UTF-8") +
                        "&" + URLEncoder.encode("policeStation", "UTF-8") + "=" + URLEncoder.encode(policeStation, "UTF-8") +
                        "&" + URLEncoder.encode("category", "UTF-8") + "=" + URLEncoder.encode(category, "UTF-8") +
                        "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8");
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


        }else if (type.equals("check-status")){
            String username = params[1];
            String flag = params[2];

            try {
                URL url = new URL(checkStatus);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String request = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+
                        "&" + URLEncoder.encode("flag", "UTF-8") + "=" + URLEncoder.encode(flag, "UTF-8");
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
        }else if(type.equals("status")){
            String requestId = params[1];
            //String flag = params[2];

            try {
                URL url = new URL(requestStatus);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String request = URLEncoder.encode("requestId", "UTF-8") + "=" + URLEncoder.encode(requestId, "UTF-8");
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
        if(s.equals("119 Request send")) {
//            alertDialog.setMessage("Request send");
//            alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                   Intent toHome = new Intent(context,homePageDuplicate.class);
//                   context.startActivity(toHome);
//                }
//            });
//            alertDialog.show();
            //Toast.makeText(myprofile.class, "userfound", Toast.LENGTH_SHORT).show();
            final Intent k = new Intent(context, homePageDuplicate.class);
            final Intent l = new Intent(context, policeRequestList.class);

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_popup);
            dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.background));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);
            dialog.getWindow().getAttributes().windowAnimations= R.style.animation;
            ImageView imageView = dialog.findViewById(R.id.imageView2);
            imageView.setImageResource(R.drawable.tick);
            TextView detail = dialog.findViewById(R.id.details);
            detail.setText("Request send");

            Button Home = dialog.findViewById(R.id.button4);
            Button request_List =  dialog.findViewById(R.id.button3);

            Home.setText("Go Home");
            Home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    context.startActivity(k);
                }
            });
            request_List.setText("View Request");
            request_List.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    context.startActivity(l);
                }
            });


            dialog.show();
        }else if(s.equals("can not find the user")){
            alertDialog.setMessage(s);
            alertDialog.show();
        }else if(s.equals("user found")){
            alertDialog.setMessage(s);
            alertDialog.show();
        }

    }
}