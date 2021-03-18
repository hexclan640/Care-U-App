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

import org.json.JSONArray;
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

         fetch_data_into_array();
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


    private void fetch_data_into_array() {

        class dbManager extends AsyncTask<String,Void,String> {

            @Override
            protected void onPostExecute(String data) {
                try{
                    JSONArray jsonArray = new JSONArray(data);
                    JSONObject jsonObject = null;

                    feedbackId = new String[jsonArray.length()];

                    for(int i=0; i< jsonArray.length();i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        feedbackId[i]=jsonObject.getString("feedbackId");
                    }

                    MyAdapter myAdapter = new MyAdapter(getApplicationContext(),feedbackId);


                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer data = new StringBuffer();
                    String line;

                    while ((line = br.readLine()) != null) {
                        data.append(line + "\n");
                    }
                    br.close();

                    return data.toString();

                } catch (Exception ex) {
                    return ex.getMessage();
                }

            }
        }
        dbManager obj = new dbManager();
        obj.execute(feedbackurl);
    }
    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String requestId[];

        MyAdapter(Context c,String requestId[]) {
            super(c,R.layout.activity_feedback);
            context = c;
            this.requestId =requestId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.activity_feedback,parent,false);

            TextView tv0=row.findViewById(R.id.tv0);


            tv0.setText("000000"+"-"+requestId[position]);

            return row;
        }
    }
}


