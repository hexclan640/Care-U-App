package com.example.careu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.PhotoLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class policeEcomplain extends AppCompatActivity {

    Spinner districtSpinner,policeSpinner,categorySpinner;
    EditText note;
    TextView txtAddress;
    LinearLayout linearMain;
    GalleryPhoto galleryPhoto;
    double latitude;
    double longitude;
    String strLat;
    String strLong;
    String district;
    final int GALLERY_REQUEST = 1200;
    final String TAG = this.getClass().getSimpleName();
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    String uploadUrl = "http://10.0.2.2/careu-php/evidence.php";
    ArrayList<String> imageList = new ArrayList<>();
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_ecomplain);
        linearMain =(LinearLayout) findViewById(R.id.linearMain);

        districtSpinner = findViewById(R.id.disSpinner);
        policeSpinner = findViewById(R.id.policeSpinner);
        categorySpinner = findViewById(R.id.cateSpinner);
        note = findViewById(R.id.note);
        txtAddress = findViewById(R.id.txtAddress);

        galleryPhoto = new GalleryPhoto(getApplicationContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(policeEcomplain.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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
        LocationServices.getFusedLocationProviderClient(policeEcomplain.this).requestLocationUpdates(locationRequest, new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(policeEcomplain.this).removeLocationUpdates(this);
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
                    Geocoder geocoder = new Geocoder(policeEcomplain.this, Locale.getDefault());
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


    public void cancecl_req(View view) {
        Intent i = new Intent(this,homePageDuplicate.class);
        startActivity(i);
    }

    public void uploadEvidence(View view) {
        if (ActivityCompat.checkSelfPermission(policeEcomplain.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(policeEcomplain.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            return;
        }

        Intent in = galleryPhoto.openGalleryIntent();
        startActivityForResult(in,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST && data!= null) {
                //assert data != null;
                galleryPhoto.setPhotoUri(data.getData());
                String photoPath = galleryPhoto.getPath();
                imageList.add(photoPath);
                Log.d(TAG, photoPath);
                try {
                    Bitmap bitmap = PhotoLoader.init().from(photoPath).requestSize(150, 150).getBitmap();

                    ImageView imageView = new ImageView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(0, 0, 10, 10);
                    imageView.setAdjustViewBounds(true);
                    imageView.setImageBitmap(bitmap);

                    linearMain.addView(imageView);

                } catch (FileNotFoundException e) {
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error while loading the image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void requestPolice(View view) {
        String type = "police";

        Calendar cc = Calendar.getInstance();
        int year = cc.get(Calendar.YEAR);
        int month = cc.get(Calendar.MONTH) + 1;
        int mDay = cc.get(Calendar.DAY_OF_MONTH);
        String sYear = Integer.toString(year);
        String sMonth = Integer.toString(month);
        String sDate = Integer.toString(mDay);
        final String date = sYear + "/" + sMonth + "/" + sDate;

        int mHour = cc.get(Calendar.HOUR_OF_DAY);
        int mMinute = cc.get(Calendar.MINUTE);
        int mSecond = cc.get(Calendar.SECOND);
        String sHour = Integer.toString(mHour);
        String sMinute = Integer.toString(mMinute);
        String sSecond = Integer.toString(mSecond);
        final String time = sHour + ":" + sMinute + ":" + sSecond;

        String district = districtSpinner.getSelectedItem().toString();
        String policeStation = policeSpinner.getSelectedItem().toString();
        final String category = categorySpinner.getSelectedItem().toString();
        String description = note.getText().toString();

        sessionManagement sessionManagement = new sessionManagement(this);
        final String username = sessionManagement.getSession();

        final MyCommand myCommand = new MyCommand(getApplicationContext());
        for(String imagePath : imageList){
            try {
                Bitmap bitmap = PhotoLoader.init().from(imagePath).requestSize(200, 200).getBitmap();
                final String encodedString = ImageBase64.encode(bitmap);

                String url ="http://10.0.2.2/careu-php/evidence.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error while uploading the image", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("image",encodedString);
                        params.put("userName",username);
                        params.put("time",time);
                        params.put("date",date);
                        params.put("category",category);
                        return params;
                    }
                };

                myCommand.add(stringRequest);
                //MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Error while loading the image", Toast.LENGTH_SHORT).show();
            }
        }

        myCommand.execute();
        BackgroundWorkerRequest backgroundWorkerRequest = new BackgroundWorkerRequest(this);
        backgroundWorkerRequest.execute(type,username,date,time,district,policeStation,category,description);
    }


}