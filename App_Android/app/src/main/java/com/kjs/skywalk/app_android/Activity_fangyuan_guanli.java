package com.kjs.skywalk.app_android;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kjs.skywalk.app_android.Homepage.fragmentHomePage;

public class Activity_fangyuan_guanli extends AppCompatActivity {

    private fragmentFangYuanGuanLiInfo1 mFragInfo1 = null;
    private fragmentFangYuanGuanLiInfo2 mFragInfo2 = null;
    private fragmentFangYuanGuanLiInfo3 mFragInfo3 = null;
    int mTestCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangyuan_guanli);

        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        mFragInfo1 = new fragmentFangYuanGuanLiInfo1();
        fragTransaction.replace(R.id.info_container, mFragInfo1);
        fragTransaction.commit();

        // for test
        findViewById(R.id.info_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();

                mTestCount++;
                if(mTestCount % 3 == 0) {
                    if(mFragInfo1 == null) {
                        mFragInfo1 = new fragmentFangYuanGuanLiInfo1();
                    }
                    fragTransaction.replace(R.id.info_container, mFragInfo1);
                } else if(mTestCount % 3 == 1) {
                    if(mFragInfo2 == null) {
                        mFragInfo2 = new fragmentFangYuanGuanLiInfo2();
                    }
                    fragTransaction.replace(R.id.info_container, mFragInfo2);
                } else if(mTestCount % 3 == 2) {
                    if(mFragInfo3 == null) {
                        mFragInfo3 = new fragmentFangYuanGuanLiInfo3();
                    }
                    fragTransaction.replace(R.id.info_container, mFragInfo3);
                }

                fragTransaction.commit();
            }
        });
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
