package com.kjs.skywalk.app_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Activity_Zushouweituo_shenhe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo_shenhe);
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
            {
                finish();
            }
            break;
        }
    }
}
