package com.example.careu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class registrationPage extends AppCompatActivity {

    EditText _fname,_lname,_email,_pnum,_username,_pwd,_nic,_address,_pwd1,_r1,_r1_num,_r2,_r2_num,_r3,_r3_num;

    AwesomeValidation awesomeValidation;
    Button selectPics1,selectPics2,upload1,upload2,btnReg;
    Bitmap bitmap1,bitmap2;
    Spinner _gender;
    DatePicker _birthDay;

    int imgStatus1 = 0;
    int imgStatus2 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);


        _fname = findViewById(R.id.fname);
        _lname = findViewById(R.id.lname);
        _email = findViewById(R.id.email);
        _pnum = findViewById(R.id.pnum);
        _username = findViewById(R.id.usrname);
         _pwd = findViewById(R.id.pwd);
         _pwd1 = findViewById(R.id.pwd1);
        _nic = findViewById(R.id.nic);
        _address = findViewById(R.id.address);






        selectPics1 = findViewById(R.id.btnSelectPic1);
        selectPics2 = findViewById(R.id.btnSelectPic2);
        btnReg = findViewById(R.id.btnReg);




        selectPics1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(registrationPage.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(registrationPage.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setType("image/*");
                startActivityForResult(intent,1);
                 imgStatus1 = 1;
            }
        });

        selectPics2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(registrationPage.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(registrationPage.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setType("image/*");
                startActivityForResult(intent,2);
                imgStatus2 = 2;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode== RESULT_OK && data != null){
            Uri path = data.getData();
            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode==2 && resultCode== RESULT_OK && data != null){
            Uri path = data.getData();
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbytes,Base64.DEFAULT);
    }

    public void register(View view) throws ExecutionException, InterruptedException {


        String uploadUrl = "http://10.0.2.2/careu-php/uploadID.php";
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        String s;
        String fname = _fname.getText().toString();
        awesomeValidation.addValidation(this,R.id.fname, RegexTemplate.NOT_EMPTY,R.string.Invalid_First_name);
        if (fname.matches(".*\\d.*")){
            _fname.setError("Cannot be contrains numbers in the First name");
        }

        String lname = _lname.getText().toString();
      awesomeValidation.addValidation(this,R.id.lname, RegexTemplate.NOT_EMPTY,R.string.Invalid_Last_name);
        if (lname.matches(".*\\d.*")){
            _lname.setError("Cannot be contrains numbers in the First name");
        }


        String email = _email.getText().toString();
       awesomeValidation.addValidation(this,R.id.email, Patterns.EMAIL_ADDRESS,R.string.Invalid_email);

        String phone = _pnum.getText().toString();

       awesomeValidation.addValidation(this,R.id.pnum,"[0]{1}[7]{1}[1||2||5||6||7||8]{1}[0-9]{7}$",R.string.invalid_number1);

        final String username = _username.getText().toString();
       awesomeValidation.addValidation(this,R.id.usrname, RegexTemplate.NOT_EMPTY,R.string.Invalid_user_name);

        String pwd = _pwd.getText().toString();
        awesomeValidation.addValidation(this,R.id.pwd,".{8,}",R.string.invalid_password);

        String pwd1 = _pwd1.getText().toString();
        awesomeValidation.addValidation(this,R.id.pwd1,pwd,R.string.notMatchingPassword);
        String NIC = _nic.getText().toString();

        if (NIC.length()==10){
            awesomeValidation.addValidation(this,R.id.nic,"[0-9]{9}[V|v]{1}$",R.string.Invalid_NIC);

        }else if (NIC.length()==12) {
//            Toast.makeText(this, "length is"+NIC.length(), Toast.LENGTH_LONG).show();
            awesomeValidation.addValidation(this, R.id.nic, "[0-9]{12}$", R.string.Invalid_NIC);
        }else if (NIC.isEmpty()){
//            Toast.makeText(this, "length is", Toast.LENGTH_LONG).show();
            _nic.setError("Please Enter Your NIC number");
//            awesomeValidation.addValidation(this,R.id.nic,RegexTemplate.NOT_EMPTY,R.string.Invalid_NIC);
        }else {
            awesomeValidation.addValidation(this,R.id.nic,"",R.string.Invalid_NIC);

        }
        String gender=null;
        if (!NIC.isEmpty()){
            gender = getGender(NIC);
        }



        String address = _address.getText().toString();
        awesomeValidation.addValidation(this,R.id.address, RegexTemplate.NOT_EMPTY,R.string.Invalid_address);


        int day = 0;
        int month= 0;
        int year= 0;
        int bday_array[]=new int[2];
        if (!NIC.isEmpty()){
            String nic1=null;
            int yearnumber,daynumber;
           if (NIC.length()==10){
               nic1=NIC.substring(0,2);
               yearnumber=Integer.parseInt(nic1);
               yearnumber = 1900 + yearnumber;
               year=yearnumber;
               nic1 = NIC.substring(2,5);
               daynumber=Integer.parseInt(nic1);
               bday_array=findBirthday(year,daynumber);
               month = bday_array[0];
               day = bday_array[1];
           }else if (NIC.length()==12){
               nic1=NIC.substring(0,4);
               yearnumber=Integer.parseInt(nic1);
               year=yearnumber;
               nic1 = NIC.substring(4,7);
               daynumber=Integer.parseInt(nic1);
               bday_array=findBirthday(year, daynumber);
               month = bday_array[0];
               day = bday_array[1];
           }
        }


        String dateOfBirth = year + "/" + month + "/"+day  ;






        if (awesomeValidation.validate() && imgStatus1==1 && imgStatus2==2){

            String type = "register";



            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
//            String state =backgroundWorker.execute(type,fname,lname,email,phone,username,pwd,NIC,address,gender,dateOfBirth,r1,r1_num,r2,r2_num,r3,r3_num).get();
            String state =backgroundWorker.execute(type,fname,lname,email,phone,username,pwd,NIC,address,gender,dateOfBirth).get();
            Toast.makeText(this, state, Toast.LENGTH_LONG).show();

            if (state.equals("Registration successful")){
                final Intent k = new Intent(this, loginPage.class);
                final Intent l = new Intent(this, MainActivity.class);
                //this.startActivity(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(registrationPage.this);
                builder.setMessage("Admins will aprove you");
                builder.setPositiveButton("LogIn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(k);
                    }
                });
                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(l);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }

        }else if(imgStatus1==0 || imgStatus2==0){
         Toast toast = Toast.makeText(this, Html.fromHtml("<font color='#FFFFFF' >" + "Please select Your ID Photos" + "</font>"), Toast.LENGTH_LONG);
         View viewt = toast.getView();
         viewt.setPadding(20,20,20,20);
         viewt.setBackgroundResource(android.R.color.holo_red_dark);
         toast.show();
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                   // Toast.makeText(registrationPage.this,Response,Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })


        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params = new HashMap<>();
                params.put("userName",username);
                params.put("name",_nic.getText().toString().trim()+"_1");

                
                params.put("image",imageToString(bitmap1));

                return params;
            }
        };
        MySingleton.getInstance(registrationPage.this).addToRequestQue(stringRequest);

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, uploadUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                   // Toast.makeText(registrationPage.this,Response,Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params = new HashMap<>();
                params.put("userName",username);
                params.put("name",_nic.getText().toString().trim()+"_2");
                params.put("image",imageToString(bitmap2));

                return params;
            }
        };
        MySingleton.getInstance(registrationPage.this).addToRequestQue(stringRequest2);
    }

    private int[] findBirthday(int year, int nic) {
            int bd_array[] = new int[2];
            int m , d;
            boolean leap = false;
            if (nic>=500){
                nic= nic-500;
            }

                if (year % 4 == 0) {
                    if (year % 100 == 0) {
                        if (year % 400 == 0)
                            leap = true;
                        else
                            leap = false;
                    }
                    else
                        leap = true;
                }

                else{
                    leap = false;
                }



                if (leap){

                    if (nic<=1 && nic<=31){
                        bd_array[0] = 1;
                        bd_array[1] = nic;
                    }
                    if (32<=nic && nic <= 60){
                        bd_array[0] = 2;
                        bd_array[1] = nic-31;
                    }
                    if (61<=nic && nic <= 91){
                        bd_array[0] = 3;
                        bd_array[1] = nic-60;
                    }
                    if (92<=nic && nic <= 121){
                        bd_array[0] = 4;
                        bd_array[1] = nic-91;
                    }
                    if (122<=nic && nic <= 152){
                        bd_array[0] = 5;
                        bd_array[1] = nic-121;
                    }
                    if (153<=nic && nic <=182 ){
                        bd_array[0] = 6;
                        bd_array[1] = nic-152;
                    }
                    if (183<=nic && nic <= 213){
                        bd_array[0] = 7;
                        bd_array[1] = nic-182;
                    }
                    if (214<=nic && nic <=244){
                        bd_array[0] = 8;
                        bd_array[1] = nic-213;
                    }
                    if (245<=nic && nic <= 274){
                        bd_array[0] = 9;
                        bd_array[1] = nic-244;
                    }
                    if (275<=nic && nic <= 305){
                        bd_array[0] = 10;
                        bd_array[1] = nic-274;
                    }
                    if (306<=nic && nic <=335){
                        bd_array[0] = 11;
                        bd_array[1] = nic-305;
                    }if (336<=nic && nic <=366){
                        bd_array[0] = 12;
                        bd_array[1] = nic-335;
                    }


                } else{
                    if (nic<=1 && nic<=31){
                        bd_array[0] = 1;
                        bd_array[1] = nic;
                    }
                    if (32<=nic && nic <= 59){
                        bd_array[0] = 2;
                        bd_array[1] = nic-31;
                    }
                    if (61<=nic && nic <= 91){
                        bd_array[0] = 3;
                        bd_array[1] = nic-60;
                    }
                    if (92<=nic && nic <= 121){
                        bd_array[0] = 4;
                        bd_array[1] = nic-91;
                    }
                    if (122<=nic && nic <= 152){
                        bd_array[0] = 5;
                        bd_array[1] = nic-121;
                    }
                    if (153<=nic && nic <=182 ){
                        bd_array[0] = 6;
                        bd_array[1] = nic-152;
                    }
                    if (183<=nic && nic <= 213){
                        bd_array[0] = 7;
                        bd_array[1] = nic-182;
                    }
                    if (214<=nic && nic <=244){
                        bd_array[0] = 8;
                        bd_array[1] = nic-213;
                    }
                    if (245<=nic && nic <= 274){
                        bd_array[0] = 9;
                        bd_array[1] = nic-244;
                    }
                    if (275<=nic && nic <= 305){
                        bd_array[0] = 10;
                        bd_array[1] = nic-274;
                    }
                    if (306<=nic && nic <=335){
                        bd_array[0] = 11;
                        bd_array[1] = nic-305;
                    }if (336<=nic && nic <=366){
                        bd_array[0] = 12;
                        bd_array[1] = nic-335;
                    }

                }

        return bd_array;

    }

    private String getGender(String nic) {
        String nic1 = null;
        int number = 0;
        String gender1=null;
        if(nic.length() == 10){
                nic1=nic.substring(2,5);
                number=Integer.parseInt(nic1);

                if (number<500){
                    gender1 = "Male";

                }else{
                    gender1 = "Female";

                }

        }else if (nic.length()==12){
                nic1=nic.substring(4,7);
                number=Integer.parseInt(nic1);

                if (number<500){
                    gender1 = "Male";

                }else{
                    gender1 = "Female";

                }
        }
        return gender1;
    }


    public void number (String s,int k) {
        if (s.length()==10){
            awesomeValidation.addValidation(this,k,"[0]{1}[7]{1}[0||1||2||5||6||7||8]{1}[0-9]{7}$",R.string.invalid_number1);
        }else if (s.length()==9){
            if (s.startsWith("0")){
                awesomeValidation.addValidation(this,k,"[0]{1}[7]{1}[0||1||2||5||6||7||8]{1}[0-9]{7}$",R.string.invalid_number);
            }else {
                awesomeValidation.addValidation(this, k, "[7]{1}[0||1||2||5||6||7||8]{1}[0-9]{7}$", R.string.invalid_number2);
            }
        }else{
            awesomeValidation.addValidation(this,k,"[0]{1}[7]{1}[0||1||2||5||6||7||8]{1}[0-9]{7}$",R.string.invalid_number);
        }
   }
}
