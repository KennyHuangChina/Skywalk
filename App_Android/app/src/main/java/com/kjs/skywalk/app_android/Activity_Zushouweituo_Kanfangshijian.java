package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Zushouweituo_Kanfangshijian extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo_kanfangshijian);
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
            {
                finish();
            }
            break;
        }
    }
}