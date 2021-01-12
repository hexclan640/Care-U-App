package com.example.careu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class requestList extends AppCompatActivity {

    String requesturl = "http://10.0.2.2/careu-php/1990AmbulanceRequests.php?userName=";
    ListView requestView;

    private static String date [];
    private static String time[];
    private static String noP[];
    private static  String des[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        sessionManagement sessionManagement = new sessionManagement(this);
        String userName = sessionManagement.getSession();
        requesturl = "http://10.0.2.2/careu-php/1990AmbulanceRequests.php?userName="+userName;
        requestView = findViewById(R.id.requestListview);

        requestView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent rq = new Intent(getApplicationContext(),request1.class);
                rq.putExtra("date",date[i]);
                startActivity(rq);
                Toast.makeText(requestList.this, date[i], Toast.LENGTH_SHORT).show();
            }
        });

        fetch_data_into_array(requestView);
    }

    private void fetch_data_into_array(View view) {

        class dbManager extends AsyncTask<String,Void,String>{

            @Override
            protected void onPostExecute(String data) {
                try{
                    JSONArray jsonArray = new JSONArray(data);
                    JSONObject jsonObject = null;

                    date = new String[jsonArray.length()];
                    time = new String[jsonArray.length()];
                    noP = new String[jsonArray.length()];
                    des = new String[jsonArray.length()];

                    for(int i=0; i< jsonArray.length();i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        date[i] = jsonObject.getString("date");
                        time[i] = jsonObject.getString("time");
                        noP[i] = jsonObject.getString("numberOfPatients");
                        des[i] = jsonObject.getString("description");
                    }

                    MyAdapter myAdapter = new MyAdapter(getApplicationContext(),date,time,noP,des);
                    requestView.setAdapter(myAdapter);

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
        obj.execute(requesturl);
    }

    class MyAdapter extends ArrayAdapter<String>{

        Context context;
        String date[];
        String time[];
        String noP[];
        String des[];


         MyAdapter(Context c, String date[], String time[], String noP[], String des[]) {
            super(c,R.layout.request_row,R.id.tv1,date);

            context = c;
            this.date = date;
            this.time = time;
            this.noP = noP;
            this.des = des;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.request_row,parent,false);

            TextView tv1=row.findViewById(R.id.tv1);
            TextView tv2=row.findViewById(R.id.tv2);
            TextView tv3=row.findViewById(R.id.tv3);
            TextView tv4=row.findViewById(R.id.tv4);

            tv1.setText(date[position]);
            tv2.setText(time[position]);
            tv3.setText(noP[position]);
            tv4.setText(des[position]);
            return row;
        }
    }


}