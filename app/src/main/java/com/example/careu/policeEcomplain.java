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
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
    String uploadUrl = "http://10.0.2.2/careu-php/evidence.php";
    List<Bitmap> bitmaps = new ArrayList<>();
    //Bitmap bitmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_ecomplain);

        districtSpinner = findViewById(R.id.disSpinner);
        policeSpinner = findViewById(R.id.policeSpinner);
        categorySpinner = findViewById(R.id.cateSpinner);
        note = findViewById(R.id.note);
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setType("image/*");
        startActivityForResult(intent,1);                   //select image from gallery

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode== RESULT_OK && data != null){

//            Uri path = data.getData();
//            try {
//                bitmaps = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            ClipData clipData = data.getClipData(); //this will  be null if choose 1 image
            if(clipData != null){
                for(int i=0;i<clipData.getItemCount();i++){
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    try {
                        InputStream is = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Uri imageUri = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bitmaps.add(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String imageToString(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbytes,Base64.DEFAULT);
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params = new HashMap();
                params.put("userName",username);
                params.put("time",time);
                params.put("date",date);
                params.put("category",category);

                for(int i=0;i<bitmaps.size();i++){
                    params.put("image",imageToString(bitmaps.get(i)));
                    params.put("name",String.valueOf(i));
                }


                return params;
            }
        };

        MySingleton.getInstance(policeEcomplain.this).addToRequestQue(stringRequest);

        BackgroundWorkerRequest backgroundWorkerRequest = new BackgroundWorkerRequest(this);
        backgroundWorkerRequest.execute(type,username,date,time,district,policeStation,category,description);
    }


}