package com.example.careu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class policeRequestList extends AppCompatActivity {

    String requesturl = "http://10.0.2.2/careu-php/119PoliceRequest.php?userName=";
    ListView requestView;

    private static String date [];
    private static String time[];
    private static String category[];
    private static  String des[];
    private static String policeStation[];
    private  static String requestId[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_request_list);

        sessionManagement sessionManagement = new sessionManagement(this);
        String userName = sessionManagement.getSession();

        requesturl = "http://10.0.2.2/careu-php/119PoliceRequest.php?userName="+userName;
        requestView = findViewById(R.id.requestListview);

        requestView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent rq = new Intent(getApplicationContext(),request2.class);
                rq.putExtra("date",date[i]);
                rq.putExtra("time",time[i]);
                rq.putExtra("category",category[i]);
                rq.putExtra("policeStation",policeStation[i]);
                rq.putExtra("description",des[i]);
                rq.putExtra("requestId",requestId[i]);
                startActivity(rq);
//                Toast.makeText(requestList.this, date[i], Toast.LENGTH_SHORT).show();
            }
        });
        fetch_data_into_array(requestView);
    }

    private void fetch_data_into_array(View view) {
        class dbManager extends AsyncTask<String,Void,String> {

            @Override
            protected void onPostExecute(String data) {
                try{
                    JSONArray jsonArray = new JSONArray(data);
                    JSONObject jsonObject = null;

                    date = new String[jsonArray.length()];
                    time = new String[jsonArray.length()];
                    category = new String[jsonArray.length()];
                    policeStation = new String[jsonArray.length()];
                    des = new String[jsonArray.length()];
                    requestId = new String[jsonArray.length()];

                    for(int i=0; i< jsonArray.length();i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        date[i] = jsonObject.getString("date");
                        time[i] = jsonObject.getString("time");
                        category[i] = jsonObject.getString("category");
                        policeStation[i] = jsonObject.getString("policeStation");
                        des[i] = jsonObject.getString("description");
                        requestId[i]=jsonObject.getString("requestId");
                    }

                    MyAdapter myAdapter = new MyAdapter(getApplicationContext(),date,time,category,policeStation,des,requestId);
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

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String date[];
        String time[];
        String category[];
        String policeStation[];
        String des[];
        String requestId[];

        MyAdapter(Context c, String date[], String time[], String category[],String policeStation[], String des[],String requestId[]) {
            super(c,R.layout.request_row,R.id.tv1,date);

            context = c;
            this.date = date;
            this.time = time;
            this.category = category;
            this.policeStation = policeStation;
            this.des = des;
            this.requestId =requestId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.police_request_row,parent,false);

            TextView tv0=row.findViewById(R.id.tv0);
            TextView tv1=row.findViewById(R.id.tv1);
            TextView tv2=row.findViewById(R.id.tv2);
            TextView tv3=row.findViewById(R.id.tv3);
            TextView tv4=row.findViewById(R.id.tv4);

            tv0.setText("000000"+"-"+requestId[position]);
            tv1.setText(date[position]);
            tv2.setText(time[position]);
            tv3.setText(category[position]);
            tv4.setText(des[position]);
            return row;
        }
    }
}