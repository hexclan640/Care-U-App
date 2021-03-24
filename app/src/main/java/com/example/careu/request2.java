package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class request2 extends AppCompatActivity {

    TextView dateTime,policeStation,category,description,requestId,reply;
    String rId;
    private static String message[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_2);

        dateTime = findViewById(R.id.dateTime);
        requestId = findViewById(R.id.requestId);
        policeStation = findViewById(R.id.policeStation);
        category = findViewById(R.id.category);
        description = findViewById(R.id.description);
        reply = findViewById(R.id.reply);
        Intent intent =getIntent();
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String des = intent.getStringExtra("description");
        String poliStation = intent.getStringExtra("policeStation");
        String cate =intent.getStringExtra("category");
        String ID =  intent.getStringExtra("requestId");
        rId= ID;
        dateTime.setText(date+" - " +time );
        requestId.setText("0000"+"-"+ID);
        description.setText(des);
        category.setText(cate);
        policeStation.setText(poliStation);

        String requesturl = "http://10.0.2.2/careu-php/getReply.php?requestId="+ID;
        dbManager obj = new dbManager();
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
