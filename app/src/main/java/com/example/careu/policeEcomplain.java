package com.example.careu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class policeEcomplain extends AppCompatActivity {

    Spinner districtSpinner,policeSpinner,categorySpinner;
    EditText note;
    LinearLayout linearMain;
    GalleryPhoto galleryPhoto;
    final int GALLERY_REQUEST = 1200;
    final String TAG = this.getClass().getSimpleName();
    String uploadUrl = "http://10.0.2.2/careu-php/evidence.php";
    ArrayList<String> imageList = new ArrayList<>();
    //List<Bitmap> bitmaps = new ArrayList<>();
    //Bitmap bitmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_ecomplain);
        linearMain =(LinearLayout) findViewById(R.id.linearMain);

        districtSpinner = findViewById(R.id.disSpinner);
        policeSpinner = findViewById(R.id.policeSpinner);
        categorySpinner = findViewById(R.id.cateSpinner);
        note = findViewById(R.id.note);

        galleryPhoto = new GalleryPhoto(getApplicationContext());

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