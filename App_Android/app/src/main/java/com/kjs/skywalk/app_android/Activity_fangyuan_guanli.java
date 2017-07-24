package com.kjs.skywalk.app_android;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kjs.skywalk.app_android.Apartment.Activity_ApartmentDetail;
import com.kjs.skywalk.app_android.Homepage.fragmentHomePage;
import com.kjs.skywalk.app_android.ClassDefine.IntentExtraKeyValue;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_BRIEF_PUBLIC_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;

public class Activity_fangyuan_guanli extends SKBaseActivity {

    private fragmentFangYuanGuanLiInfo1 mFragInfo1 = null;
    private fragmentFangYuanGuanLiInfo2 mFragInfo2 = null;
    private fragmentFangYuanGuanLiInfo3 mFragInfo3 = null;
    int mTestCount = 0;
//    int mHouseId = 2;
    private CommandManager mCmdMgr = null;
    private String mInfo2 = ""; // "2房2厅1卫 | 105㎡ | 10/11F"

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fangyuan_guanli);

            mCmdMgr = new CommandManager(this, this, this);
        mCmdMgr.GetHouseInfo(mHouseId, false);
        kjsLogUtil.i("GetHouseInfo: " + mHouseId);

        mCmdMgr.GetBriefPublicHouseInfo(mHouseId);

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
                commonFun.startActivityWithHouseId(this, Activity_fangyuan_zhaopian.class, mHouseId);
            }
            break;
            case R.id.ll_kanfangshijian:
            {
                Intent intent = new Intent(this, Activity_Zushouweituo_Kanfangshijian.class);
                intent.putExtra("house_id", mHouseId);
                intent.putExtra("house_location", mHouseLocation);
                // 最佳看房时间
                startActivity(intent);
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
                commonFun.startActivityWithHouseId(this, Activity_HouseholdDeliverables.class, mHouseId);
            }
            break;
            case R.id.ll_fangwusheshi:
            {
                // 房屋设施
                commonFun.startActivityWithHouseId(this, Activity_HouseholdAppliances.class, mHouseId);
            }
            break;
            case R.id.ll_shegnhuozhanghu:
            {
                // 生活账户
                startActivity(new Intent(this, Activity_Shenghuozhanghu.class));
            }
            break;
            case R.id.ll_fuwu:
            {
                // 服务及收费标准
            }
            break;

        }
    }

    @Override
    public void onCommandFinished(int command, IApiResults.ICommon iResult) {
        kjsLogUtil.i("Activity_ApartmentDetail::onCommandFinished");
        if (null == iResult) {
            kjsLogUtil.w("result is null");
            return;
        }
        kjsLogUtil.i(String.format("[command: %d] --- %s", command, iResult.DebugString()));
        if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
            kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
            return;
        }

        if (command == CMD_GET_HOUSE_INFO) {
            updateHouseInfo((IApiResults.IGetHouseInfo) iResult);
        }

    }

    private void updateHouseInfo(final IApiResults.IGetHouseInfo IHouseInfo) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mInfo2 = commonFun.getHouseTypeString(IHouseInfo.Bedrooms(), IHouseInfo.Livingrooms(), IHouseInfo.Bathrooms());
                mInfo2 += " | ";
                mInfo2 += String.format("%d m²", IHouseInfo.Acreage() / 100);
                mInfo2 += " | ";
                mInfo2 += String.format("%d/%d F", IHouseInfo.Floorthis(), IHouseInfo.FloorTotal());

                updateFragmentInfo();

//                // 1: wait for rent, 2: rented, 3: Due, open for ordering
//                IHouseInfo.RentStat();
//                public interface IHouseCertInfo {
//                    int     CertStat();     // certification stat. 1: wait for certification; 2: Certitication passed; 3: Certification failed
//                    Date CertTime();
//                    String  CertDesc();
//                }


            }
        });
    }

    private void updateFragmentInfo() {
        if (mFragInfo1 != null)
            mFragInfo1.updateInfo(mHouseLocation, mInfo2);
    }

}
