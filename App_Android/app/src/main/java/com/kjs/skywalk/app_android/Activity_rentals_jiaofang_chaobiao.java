package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

/**
 * Created by admin on 2017/3/7.
 */

public class Activity_rentals_jiaofang_chaobiao extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentals_progress_jiaofang_chaobiao);

//        Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.input_dialog_number_one);
//        dialog.show();
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
