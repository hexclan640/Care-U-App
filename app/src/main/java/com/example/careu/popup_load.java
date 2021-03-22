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

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.VerticalStepView;
import com.baoyachi.stepview.bean.StepBean;

import java.util.ArrayList;
import java.util.List;

public class popup_load extends AppCompatActivity {
    Dialog dialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_load);

        TextView t = findViewById(R.id.statuspop);
        t.setText("warnning");


        sessionManagement sessionManagement = new sessionManagement(this);
        String username = sessionManagement.getSession();

        Intent intent =getIntent();
        String s = intent.getStringExtra("status");
        int status = Integer.valueOf(s);

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
        list0.add("Sending the request to the ambulance");
        list0.add("Processing");
        list0.add("Accepted the request from operator");

//        int s = check_status(username);

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
        Button btn = findViewById(R.id.loadbtn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent l = new Intent(popup_load.this, homePageDuplicate.class);
                startActivity(l);
                finish();
            }
        });

//        if (status==1){
//
//        }

    }

    private int check_status(String username) {
        String type="check-status";
        BackgroundWorkerRequest backgroundWorkerRequest = new BackgroundWorkerRequest(this);
        backgroundWorkerRequest.execute(type, username);


        return 0;
    }
}