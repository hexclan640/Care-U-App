package com.example.careu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.os.Looper;
import android.view.View;
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

public class ambulance extends AppCompatActivity {


    //sessionManagement sessionManagement1 = new sessionManagement(ambulance.this);
    //String user = sessionManagement1.getSession();

     String apiurl="http://10.0.2.2/careu-php/sendAlertSMS.php?userName=";

    Spinner districtSpinner,policeSpinner,patientSpinner;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    EditText note;
    TextView txtAddress;
    CheckBox checkLocation;
    double latitude;
    double longitude;
    String strLat;
    String strLong;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);

        note = findViewById(R.id.note);
        districtSpinner=findViewById(R.id.disSpinner);
        policeSpinner=findViewById(R.id.policeSpinner);
        patientSpinner=findViewById(R.id.patientSpinner);

        sessionManagement sessionManagement1 = new sessionManagement(ambulance.this);
        String user = sessionManagement1.getSession();

        apiurl="http://10.0.2.2/careu-php/sendAlertSMS.php?userName="+user;
        districtSpinner = findViewById(R.id.disSpinner);
        policeSpinner = findViewById(R.id.policeSpinner);
        patientSpinner = findViewById(R.id.patientSpinner);
        txtAddress = findViewById(R.id.txtAddress);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    ambulance.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            getCurrentLocation();
        }

        getAddress();

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

    public void requestAmbulance(View view) {


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

        String district = districtSpinner.getSelectedItem().toString();
        String policeStation = policeSpinner.getSelectedItem().toString();
        String noOfPatients = patientSpinner.getSelectedItem().toString();
        String description = note.getText().toString();
        //Toast.makeText(this, latitude+" "+longitude, Toast.LENGTH_SHORT).show();


//        Toast.makeText(ambulance.this, strLat+" "+strLong, Toast.LENGTH_SHORT).show();
        sessionManagement sessionManagement = new sessionManagement(this);
        String username = sessionManagement.getSession();

        BackgroundWorkerRequest backgroundWorkerRequest = new BackgroundWorkerRequest(this);
        backgroundWorkerRequest.execute(type, username, date, time, district, policeStation, noOfPatients, description, strLat, strLong);
    }

    public void send(View view) {

        fetch_data();
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
                    int success = jsonResult.getInt("success");
                    if (success == 1) {

                        JSONArray cars = jsonResult.getJSONArray("cars");
                        for (int i = 0; i < cars.length(); i++) {
                            JSONObject car = cars.getJSONObject(i);

                            //String name = car.getString("name");
                            int phone = car.getInt("phoneNumber");

                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(String.valueOf(phone), null, "It's urgent..Help me!", null, null);

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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}