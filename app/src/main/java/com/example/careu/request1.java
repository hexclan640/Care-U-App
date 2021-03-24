package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class request1 extends AppCompatActivity {

    TextView dateTime,district,policeStation,noOfPatients,description,requestId,status,reply;
    String rId;
    private static String message[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_1);

        dateTime = findViewById(R.id.dateTime);
        requestId = findViewById(R.id.requestId);
        policeStation = findViewById(R.id.policeStation);
        noOfPatients = findViewById(R.id.patients);
        description = findViewById(R.id.description);
        status = findViewById(R.id.requeststatus);
        reply = findViewById(R.id.reply);
        Intent intent =getIntent();
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String des = intent.getStringExtra("description");
        String poliStation = intent.getStringExtra("policeStation");
        String nop =intent.getStringExtra("numberOfPatients");
        String ID =  intent.getStringExtra("requestId");
        String reqstatus= intent.getStringExtra("status");
        rId= ID;
        dateTime.setText(date+" - " +time );
        requestId.setText("0000"+"-"+ID);
        description.setText(des);
        noOfPatients.setText(nop);
        policeStation.setText(poliStation);

        if (reqstatus.equals("0")){
            status.setText("pending");
        }else if(reqstatus.equals("1")){
            status.setText("Accepted by operator");
        }else if(reqstatus.equals("2")){
            status.setText("Rejected by operator");
        }else{
            status.setText("Timed out After 30s");
        }

        String requesturl = "http://10.0.2.2/careu-php/getReply.php?requestId="+ID;
        request1.dbManager obj = new request1.dbManager();
        obj.execute(requesturl);


    }
    public void feedbk(View view) {
        Intent i = new Intent(getApplicationContext(),feedback.class);
        i.putExtra("id",rId);
//        Intent i= new Intent(this,feedback.class);
        startActivity(i);
    }

    class dbManager extends AsyncTask<String,Void,String> {

        @Override
        protected void onPostExecute(String data) {
            try{
                JSONArray ja = new JSONArray(data);
                JSONObject jo = null;

                message = new String[ja.length()];

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    message[i] = jo.getString("message");
                }
                reply.setText(message[0]);

            }catch (Exception ex){
//                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                reply.setText("--No Reply--");
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

}
