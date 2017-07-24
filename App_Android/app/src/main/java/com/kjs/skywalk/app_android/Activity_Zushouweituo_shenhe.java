package com.kjs.skywalk.app_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Activity_Zushouweituo_shenhe extends SKBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo_shenhe);
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
            {
                finish();
            }
            break;
        }
    }
}
