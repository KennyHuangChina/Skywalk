package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

/**
 * Created by admin on 2017/3/16.
 */

public class Activity_Weituoqueren extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tijiaoweituo);
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title:
            {
                finish();
            }
            break;
        }
    }
}
