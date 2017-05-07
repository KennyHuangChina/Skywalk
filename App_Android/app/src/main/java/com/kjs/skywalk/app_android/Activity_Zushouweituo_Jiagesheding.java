package com.kjs.skywalk.app_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Activity_Zushouweituo_Jiagesheding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo__jiagesheding);
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title:
            {
                finish();
            }
            break;
            case R.id.tv_prev:
            {
                finish();
            }
            break;
            case R.id.tv_next:
            {
                startActivity(new Intent(this, Activity_Zushouweituo_Xuanzedaili.class));
            }
            break;
        }
    }
}
