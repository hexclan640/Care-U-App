package com.example.careu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ambulance extends AppCompatActivity {


    String apiurl="http://10.0.2.2/careu-php/sendAlertSMS.php?userName=";

    Spinner districtSpinner,policeSpinner,patientSpinner;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    EditText note;
    TextView txtAddress;
    CheckBox cb_sendSMS;
    double latitude;
    double longitude;
    String strLat,strLong,district,username,s,policeStation,noOfPatients,description;
    int success=3;
    Intent intent;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);

        intent = getIntent();
        note = findViewById(R.id.note);
        districtSpinner=findViewById(R.id.disSpinner);
        policeSpinner=findViewById(R.id.policeSpinner);
        patientSpinner=findViewById(R.id.patientSpinner);
        cb_sendSMS=findViewById(R.id.cb_send);

        sessionManagement sessionManagement1 = new sessionManagement(ambulance.this);
        String user = sessionManagement1.getSession();

        apiurl="http://10.0.2.2/careu-php/sendAlertSMS.php?userName="+user;
        districtSpinner = findViewById(R.id.disSpinner);
        policeSpinner = findViewById(R.id.policeSpinner);
        patientSpinner = findViewById(R.id.patientSpinner);
        txtAddress = findViewById(R.id.txtAddress);

        String reply= intent.getStringExtra("retry");
        int i = 0;
        if (reply.equals("1")){
            description = intent.getStringExtra("description");
            note.setText(description);
            Toast.makeText(ambulance.this, description, Toast.LENGTH_SHORT).show();
            noOfPatients= intent.getStringExtra("noOfPatients");
            if (noOfPatients.equals("more than 5")){
                 i = 5;
            }else {
                i = Integer.valueOf(noOfPatients)-1;
            }
            patientSpinner.setSelection(i);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        cb_sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_sendSMS.isChecked()){
                    cb_sendSMS.setTextColor(getResources().getColor(R.color.colorAccent));
                }else {
                    cb_sendSMS.setTextColor(getResources().getColor(R.color.colorBlack));
                }
            }
        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ambulance.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            getCurrentLocation();
        }

        getAddress();

    }

    private int getIndexDistrict(Spinner districtSpinner, String district) {
        for(int i=0;i<districtSpinner.getCount();i++){
            if(districtSpinner.getItemAtPosition(i).toString().contains(district)){
                return i;
            }
        }
        return 0;
    }

    private int getIndexPolice(Spinner policeSpinner, String district) {
        for(int i=0;i<policeSpinner.getCount();i++){
            if(policeSpinner.getItemAtPosition(i).toString().contains(district)){
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void cancecl_req(View view) {
        Intent i = new Intent(this, homePageDuplicate.class);
        startActivity(i);
    }

    public void getCurrentLocation() {

        //progressBar.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //Toast.makeText(this, "getlocation", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(ambulance.this).requestLocationUpdates(locationRequest, new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(ambulance.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    strLat = String.valueOf(latitude);
                    strLong = String.valueOf(longitude);
//                    Toast.makeText(ambulance.this, strLat+" "+strLong, Toast.LENGTH_SHORT).show();
                }
            }
        }, Looper.getMainLooper());

    }

    public void requestAmbulance(View view) throws ExecutionException, InterruptedException {

        if (cb_sendSMS.isChecked()){
            fetch_data();
        }


        String type = "ambulance";
        Calendar cc = Calendar.getInstance();
        int year = cc.get(Calendar.YEAR);
        int month = cc.get(Calendar.MONTH) + 1;
        int mDay = cc.get(Calendar.DAY_OF_MONTH);
        String sYear = Integer.toString(year);
        String sMonth = Integer.toString(month);
        String sDate = Integer.toString(mDay);
        String date = sYear + "/" + sMonth + "/" + sDate;

        int mHour = cc.get(Calendar.HOUR_OF_DAY);
        int mMinute = cc.get(Calendar.MINUTE);
        int mSecond = cc.get(Calendar.SECOND);
        String sHour = Integer.toString(mHour);
        String sMinute = Integer.toString(mMinute);
        String sSecond = Integer.toString(mSecond);
        String time = sHour + ":" + sMinute + ":" + sSecond;

        district = districtSpinner.getSelectedItem().toString();
        policeStation = policeSpinner.getSelectedItem().toString();
        noOfPatients = patientSpinner.getSelectedItem().toString();
        description = note.getText().toString();
        //Toast.makeText(this, latitude+" "+longitude, Toast.LENGTH_SHORT).show();


//        Toast.makeText(ambulance.this, strLat+" "+strLong, Toast.LENGTH_SHORT).show();
        sessionManagement sessionManagement = new sessionManagement(this);
        username = sessionManagement.getSession();

        BackgroundWorkerRequest backgroundWorkerRequest = new BackgroundWorkerRequest(this);
        final String status= backgroundWorkerRequest.execute(type, username, date, time, district, policeStation, noOfPatients, description, strLat, strLong).get();


        if (status.equals("Request send")){
//            final Intent l = new Intent(ambulance.this, popup_load.class);
//            l.putExtra("status","3");
//            startActivity(l);


//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    final Intent l = new Intent(ambulance.this, popup_load.class);
//                    l.putExtra("status","3");
//                    startActivity(l);
//                    finish();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            final Intent l = new Intent(ambulance.this, popup_load.class);
//                            l.putExtra("status","2");
//                            startActivity(l);
////                            finish(l);
////                            BackgroundWorkerRequest backgroundWorkerRequest = new BackgroundWorkerRequest(ambulance.this);
////                            backgroundWorkerRequest.execute("check-status", username);
//                            Runnable runnable = new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    synchronized (this){
//                                        try {
//                                            wait(3000);
//                                        } catch (InterruptedException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//                            };
//                            runnable.run();
//                            final Intent ll = new Intent(ambulance.this, popup_load.class);
//                            ll.putExtra("status","1");
//                            startActivity(ll);
//
//
//                        }
//                    },2000);
//
//
//
//                }
//            },3000);
//            finish();

//            final Intent l = new Intent(ambulance.this, popup_load.class);
//            l.putExtra("status","3");
//            startActivity(l);
            int t = 500;
            if (success==1){

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent l = new Intent(ambulance.this, popup_load.class);
                        l.putExtra("status","4");
                        l.putExtra("after30sec","0");
                        l.putExtra("massage",Integer.toString(success));
                        startActivity(l);
                        finish();
                    }

                },100);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent l = new Intent(ambulance.this, popup_load.class);
                        l.putExtra("status","3");
                        l.putExtra("after30sec","0");
                        l.putExtra("massage",Integer.toString(success));
                        startActivity(l);
                        finish();
                    }

                },5000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent l = new Intent(ambulance.this, popup_load.class);
                        l.putExtra("status","2");
                        l.putExtra("after30sec","0");
                        l.putExtra("massage",Integer.toString(success));
                        startActivity(l);
                        finish();
                    }

                },8000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent l = new Intent(ambulance.this, popup_load.class);
                        l.putExtra("status","1");
                        l.putExtra("after30sec","0");
                        l.putExtra("massage",Integer.toString(success));
                        startActivity(l);
                        finish();
                    }

                },12000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BackgroundWorkerRequest backgroundWorkerRequeststatus = new BackgroundWorkerRequest(ambulance.this);
                        try {
                            s = backgroundWorkerRequeststatus.execute("check-status", username).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                },30000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (s.equals("okey")){
//                      setContentView(R.layout.activity_popup_load);

                            final Intent l = new Intent(ambulance.this, popup_load.class);
                            l.putExtra("status","0");
                            l.putExtra("after30sec","1");
                            l.putExtra("massage",Integer.toString(success));
                            startActivity(l);
                            finish();


                        }else {
                            final Intent l = new Intent(ambulance.this, popup_load.class);
                            l.putExtra("status","1");
                            l.putExtra("after30sec","1");
                            l.putExtra("massage",Integer.toString(success));
                            startActivity(l);
                            finish();
                        }
                    }

                },31000);



            }else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent l = new Intent(ambulance.this, popup_load.class);
                        l.putExtra("status","3");
                        l.putExtra("after30sec","0");
                        l.putExtra("massage",Integer.toString(success));
                        startActivity(l);
                        finish();
                    }

                },100);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent l = new Intent(ambulance.this, popup_load.class);
                        l.putExtra("status","2");
                        l.putExtra("after30sec","0");
                        l.putExtra("massage",Integer.toString(success));
                        startActivity(l);
                        finish();
                    }

                },5000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent l = new Intent(ambulance.this, popup_load.class);
                        l.putExtra("status","1");
                        l.putExtra("after30sec","0");
                        l.putExtra("massage",Integer.toString(success));
                        startActivity(l);
                        finish();
                    }

                },8000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BackgroundWorkerRequest backgroundWorkerRequeststatus = new BackgroundWorkerRequest(ambulance.this);
                        try {
                            s = backgroundWorkerRequeststatus.execute("check-status", username,"0").get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                },24000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (s.equals("okey")){
//                      setContentView(R.layout.activity_popup_load);

                            final Intent l = new Intent(ambulance.this, popup_load.class);
                            l.putExtra("status","0");
                            l.putExtra("after30sec","1");
                            l.putExtra("massage",Integer.toString(success));
                            startActivity(l);
                            finish();


                        }else {
                            final Intent l = new Intent(ambulance.this, popup_load.class);
                            l.putExtra("status","1");
                            l.putExtra("after30sec","1");
                            l.putExtra("massage",Integer.toString(success));
                            l.putExtra("description",description);
                            l.putExtra("noOfPatient",noOfPatients);
                            startActivity(l);
                            finish();
                            BackgroundWorkerRequest backgroundWorkerRequeststatus = new BackgroundWorkerRequest(ambulance.this);
                            backgroundWorkerRequeststatus.execute("check-status", username,"3");
                        }
                    }

                },25000);


            }



//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                        final Intent l = new Intent(ambulance.this, homePageDuplicate.class);
//                        l.putExtra("status","0");
//                        startActivity(l);
//                        finish();
//                    }
//
//
//            },10000);





//            Toast.makeText(this, "on the after 1 pop", Toast.LENGTH_SHORT).show();


//            final Intent l = new Intent(ambulance.this, popup_load.class);
//            l.putExtra("status","3");
//            startActivity(l);

//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//
//                    synchronized (this){
//                        try {
//                            wait(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            };
//            runnable.run();














        }
    }



    public void fetch_data() {

        class Connections extends AsyncTask<String, String, String> {


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


            @Override
            protected void onPostExecute(String result) {

                try {
                    JSONObject jsonResult = new JSONObject(result);
                    success = jsonResult.getInt("success");
                    if (success == 1) {

                        JSONArray cars = jsonResult.getJSONArray("cars");
                        for (int i = 0; i < cars.length(); i++) {
                            JSONObject car = cars.getJSONObject(i);

                            //String name = car.getString("name");
                            int phone = car.getInt("phoneNumber");

                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(String.valueOf(phone), null, "It's urgent..Need your help!", null, null);

                            Toast.makeText(getApplicationContext(), "Message Sent successfully!", Toast.LENGTH_SHORT).show();


                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error!! please add relatives first. ", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }

        Connections obj=new Connections();
        obj.execute(apiurl);

    }

    private void getAddress() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location!=null){
                    Geocoder geocoder = new Geocoder(ambulance.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        txtAddress.setText(addresses.get(0).getAddressLine(0));
                        district = addresses.get(0).getSubAdminArea();

                        districtSpinner.setSelection(getIndexDistrict(districtSpinner,district));
                        policeSpinner.setSelection(getIndexPolice(policeSpinner,district));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}