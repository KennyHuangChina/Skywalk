package com.kjs.skywalk.app_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Activity_PasswordReset extends AppCompatActivity {
    private TextView mTvActv_telephone_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__password_reset);

        mTvActv_telephone_num = (TextView) findViewById(R.id.actv_telephone_num);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boolean isLogin = bundle.getBoolean("islogin");
            String userName = bundle.getString("user_name");
            String userTele = bundle.getString("user_telephone_num");

            if (isLogin) {
                mTvActv_telephone_num.setText(userTele);
                mTvActv_telephone_num.setEnabled(false);
            }
        }
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title: {
                finish();
                break;
            }
        }
    }
}
