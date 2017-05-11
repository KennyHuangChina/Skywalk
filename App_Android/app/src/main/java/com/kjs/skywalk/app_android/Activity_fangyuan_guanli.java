package com.kjs.skywalk.app_android;

import android.app.FragmentTransaction;
import android.content.Intent;
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
            case R.id.tv_lijirenzheng:
            {
                // 立即认证
                startActivity(new Intent(this, Activity_fangyuan_renzhengshuoming.class));
            }
            break;
            case R.id.ll_fangyuanzhaopian:
            {
                // 房源照片
                startActivity(new Intent(this, Activity_fangyuan_zhaopian.class));
            }
            break;
            case R.id.ll_kanfangshijian:
            {
                // 最佳看房时间
                startActivity(new Intent(this, Activity_Zushouweituo_Kanfangshijian.class));
            }
            break;
            case R.id.ll_wuraoshijian:
            {
                // 勿扰时间
            }
            break;
            case R.id.ll_chuzujiaofu:
            {
                // 出租交付
                startActivity(new Intent(this, Activity_HouseholdDeliverables.class));
            }
            break;
            case R.id.ll_fangwusheshi:
            {
                // 房屋设施
                startActivity(new Intent(this, Activity_HouseholdAppliances.class));
            }
            break;
            case R.id.ll_shegnhuozhanghu:
            {
                // 生活账户
            }
            break;
            case R.id.ll_fuwu:
            {
                // 服务及收费标准
            }
            break;

        }
    }
}
