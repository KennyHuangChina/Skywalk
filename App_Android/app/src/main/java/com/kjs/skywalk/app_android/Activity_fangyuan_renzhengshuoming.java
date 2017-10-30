package com.kjs.skywalk.app_android;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Activity_fangyuan_renzhengshuoming extends SKBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzhengshuoming);

    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title:
            {
                finish();
            }
            break;
            case R.id.textViewCommit:
            {
                finish();
//                Intent i = new Intent(this, Activity_fangyuan_renzheng_customer.class);
                Intent i = new Intent(this, Activity_shenhe_zhengjian.class);
                i.putExtra(ClassDefine.IntentExtraKeyValue.KEY_HOUSE_ID, mHouseId);
                i.putExtra(ClassDefine.IntentExtraKeyValue.KEY_USER_ID, mUserId);
                startActivity(i);
            }
            break;
        }
    }
}
