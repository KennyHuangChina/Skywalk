package com.kjs.skywalk.app_android;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Activity_shenhe_zhengjian extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__shenhe_zhengjian);
    }



    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title: {
                finish();
            }
        }
    }

}
