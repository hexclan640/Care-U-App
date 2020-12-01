package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class addRelations extends AppCompatActivity {
    EditText _r1,_r1_num,_r2,_r2_num,_r3,_r3_num;
    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_relations);
        _r1 = findViewById(R.id.r1);
        _r1_num = findViewById(R.id.r1_num);


    }




    public void addRelation(View view) throws ExecutionException, InterruptedException {


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        String r1 = _r1.getText().toString();

        String r1_num = _r1_num.getText().toString();
        if (!r1.isEmpty()){
//           number(_r1_num.getText().toString(),R.id.r1_num);
            awesomeValidation.addValidation(this,R.id.r1_num,"[0]{1}[7]{1}[1||2||5||6||7||8]{1}[0-9]{7}$",R.string.invalid_number1);
        }



        if (awesomeValidation.validate()) {
           // Toast.makeText(this,"Hellp" ,Toast.LENGTH_LONG).show();
            sessionManagement sessionManagement1 = new sessionManagement(addRelations.this);
            String userName = sessionManagement1.getSession();
            String type = "AddRelation";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            String state =backgroundWorker.execute(type,userName,r1,r1_num).get();
           // Toast.makeText(this, state, Toast.LENGTH_LONG).show();
            if (state.equals("Sucessfully add the Relatives")){

                final Intent k = new Intent(this, homePageDuplicate.class);

                AlertDialog.Builder builder = new AlertDialog.Builder(addRelations.this);
                builder.setMessage("Sucessfully add the Relatives");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(k);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();



            }else if (state.equals("Allready You filled up all the relations")){

                final Intent k = new Intent(this, homePageDuplicate.class);

                AlertDialog.Builder builder = new AlertDialog.Builder(addRelations.this);
                builder.setMessage("Allready You filled up all the relations");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(k);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }else{

                final Intent k = new Intent(this, addRelations.class);
                final Intent l = new Intent(this, MainActivity.class);
                //this.startActivity(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(addRelations.this);
                builder.setMessage(state);
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(k);
                    }
                });builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(l);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }

       }


    }
}