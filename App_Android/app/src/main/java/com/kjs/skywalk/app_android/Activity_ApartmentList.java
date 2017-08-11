package com.kjs.skywalk.app_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Activity_ApartmentList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__apartment_list);
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_title: {
                finish();
            }
            break;
        }
    }
}
