package com.example.careu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.VerticalStepView;
import com.baoyachi.stepview.bean.StepBean;

import java.util.ArrayList;
import java.util.List;

public class popup_load extends AppCompatActivity {
    Dialog dialog2;
    TextView t ;
    Button btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_load);

         t = findViewById(R.id.statuspop);
         btn1 = findViewById(R.id.loadbtn1);
         btn2 = findViewById(R.id.loadbtn2);



        sessionManagement sessionManagement = new sessionManagement(this);
        String username = sessionManagement.getSession();

        Intent intent =getIntent();
        String s = intent.getStringExtra("status");
        int status = Integer.valueOf(s);
        String a = intent.getStringExtra("after30sec");
        int after30sec = Integer.valueOf(a);
        String m = intent.getStringExtra("massage");
        int massage = Integer.valueOf(m);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width =dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.75));
        getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        getWindow().setLayout((int)(width*.92),ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations= R.style.animation;

//        getWindow().set

//        HorizontalStepView setpview5 = (HorizontalStepView)findViewById(R.id.step_view5);
//
//        List<StepBean> stepsBeanList = new ArrayList<>();
//        StepBean stepBean0 = new StepBean("Test-1",1);
//        StepBean stepBean1 = new StepBean("Test-2",1);
//        StepBean stepBean2 = new StepBean("Test-3",1);
//        StepBean stepBean3 = new StepBean("Test-4",0);
//        StepBean stepBean4 = new StepBean("Test-5",-1);
//        stepsBeanList.add(stepBean0);
//        stepsBeanList.add(stepBean1);
//        stepsBeanList.add(stepBean2);
//        stepsBeanList.add(stepBean3);
//        stepsBeanList.add(stepBean4);
//
//        setpview5
//                .setStepViewTexts(stepsBeanList)//总步骤
//                .setTextSize(12)//set textSize
//                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(popup_load.this, android.R.color.white))//设置StepsViewIndicator完成线的颜色
//                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(popup_load.this, R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
//                .setStepViewComplectedTextColor(ContextCompat.getColor(popup_load.this, android.R.color.white))//设置StepsView text完成线的颜色
//                .setStepViewUnComplectedTextColor(ContextCompat.getColor(popup_load.this, R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
//                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(popup_load.this, R.drawable.complted))//设置StepsViewIndicator CompleteIcon
//                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(popup_load.this, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
//                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(popup_load.this, R.drawable.attention));//设置StepsViewIndicator AttentionIcon
//
//
//
//

//        dialog2 =new Dialog(popup_load.this);
//        dialog2.setContentView(R.layout.activity_popup_load);
//        dialog2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
//        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog2.getWindow().getAttributes().windowAnimations= R.style.animation;
//        dialog2.show();
        VerticalStepView mSetpview0 = (VerticalStepView) findViewById(R.id.step_view0);
        List<String> list0 = new ArrayList<>();

        if (massage==1){
            list0.add("Sending the request to the ambulance");
            list0.add("Send the massage to relations");
            list0.add("Processing");
            list0.add("Accepted by operator");

        }else{
            list0.add("Sending the request to the ambulance");
            list0.add("Processing");
            list0.add("Accepted by operator");
        }




//        int s = check_status(username);
        if (status==4 && massage==1){
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            t.setText("Sending request");
        }
        else if (status==3 && massage==1){
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            t.setText("Sending Massage");


        }
        else if (status==3 && massage!=1){
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            t.setText("Sending Massage");
        }else if (status==2) {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            t.setText("Processing");
        }else  if (status==1 && after30sec==0){
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            t.setText("Wait Until Operator Response");
        }else if (status==1 && after30sec==1) {
            final String description = intent.getStringExtra("description");
            final String noOfPatent = intent.getStringExtra("noOfPatient");
            Toast.makeText(popup_load.this, noOfPatent, Toast.LENGTH_SHORT).show();
            t.setText("Time out the Request");
            btn1.setText("Home");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(popup_load.this,homePageDuplicate.class);
                    startActivity(i);
                    finish();
                }
            });

            btn2.setText("Retry the new Request");
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(popup_load.this,ambulance.class);
                    i.putExtra("retry","1");
                    i.putExtra("description",description);
                    i.putExtra("noOfPatients",noOfPatent);
                    startActivity(i);
                    finish();
                }
            });
        } else if(status==0 && after30sec==1){
            t.setText("Accepted the request");
            btn1.setText("Home");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(popup_load.this,homePageDuplicate.class);
                    startActivity(i);
                    finish();
                }
            });

            btn2.setText("RequestList");
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(popup_load.this,myrequests.class);
                    startActivity(i);
                    finish();
                }
            });
        }

        mSetpview0.setStepsViewIndicatorComplectingPosition(list0.size() -status)
                .reverseDraw(false)//default is true
                .setStepViewTexts(list0)
                .setLinePaddingProportion(0.85f)//indicator
                .setTextSize(15)
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(popup_load.this, android.R.color.white))//StepsViewIndicator
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(popup_load.this, R.color.uncompleted_text_color))//StepsViewIndicator
                .setStepViewComplectedTextColor(ContextCompat.getColor(popup_load.this, android.R.color.white))//StepsView text
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(popup_load.this, R.color.uncompleted_text_color))//StepsView text
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(popup_load.this, R.drawable.complted))//StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(popup_load.this, R.drawable.default_icon))//StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(popup_load.this, R.drawable.attention));//StepsViewIndicator AttentionIcon

//        int s = check_status(username);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent l = new Intent(popup_load.this, homePageDuplicate.class);
                startActivity(l);
                finish();
            }
        });



    }

    private int check_status(String username) {
        String type="check-status";
        BackgroundWorkerRequest backgroundWorkerRequest = new BackgroundWorkerRequest(this);
        backgroundWorkerRequest.execute(type, username);


        return 0;
    }
}