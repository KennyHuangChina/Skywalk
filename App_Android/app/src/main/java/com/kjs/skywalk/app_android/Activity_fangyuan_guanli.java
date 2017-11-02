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
import static com.kjs.skywalk.communicationlibrary.IApiResults.HOUSE_CERT_STAT_FAILED;
import static com.kjs.skywalk.communicationlibrary.IApiResults.HOUSE_CERT_STAT_PASSED;
import static com.kjs.skywalk.communicationlibrary.IApiResults.HOUSE_CERT_STAT_WAIT;

public class Activity_fangyuan_guanli extends SKBaseActivity {

    private fragmentFangYuanGuanLiInfo1 mFragInfo1 = null;
    private fragmentFangYuanGuanLiInfo2 mFragInfo2 = null;
    private fragmentFangYuanGuanLiInfo3 mFragInfo3 = null;
    int mTestCount = 0;
//    int mHouseId = 2;
    private CommandManager mCmdMgr = null;
    private String mInfo2 = "";
    IApiResults.IHouseCertDigestInfo mCertDigestInfo = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangyuan_guanli);

        mFragInfo1 = new fragmentFangYuanGuanLiInfo1();
        mFragInfo2 = new fragmentFangYuanGuanLiInfo2();
        mFragInfo3 = new fragmentFangYuanGuanLiInfo3();

        mCmdMgr = CommandManager.getCmdMgrInstance(this, this, this);
        mCmdMgr.GetHouseInfo(mHouseId, false);
        kjsLogUtil.i("GetHouseInfo: " + mHouseId);
        mCmdMgr.GetBriefPublicHouseInfo(mHouseId);
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
                Intent i = new Intent(this, Activity_fangyuan_renzhengshuoming.class);
                i.putExtra(ClassDefine.IntentExtraKeyValue.KEY_HOUSE_ID, mHouseId);
                i.putExtra(IntentExtraKeyValue.KEY_USER_ID, mUserId);
                startActivity(i);
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
        if(command == CMD_GET_BRIEF_PUBLIC_HOUSE_INFO) {
            mCertDigestInfo = (IApiResults.IHouseCertDigestInfo)iResult;
            updateFragmentInfo(mCertDigestInfo);
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

    private void updateFragmentInfo(IApiResults.IHouseCertDigestInfo info) {

        switch (info.CertStat()) {
            case HOUSE_CERT_STAT_WAIT:
                break;
            case HOUSE_CERT_STAT_PASSED:
                break;
            case HOUSE_CERT_STAT_FAILED:
                break;
        }
        if (mFragInfo1 != null)
//            mFragInfo1.updateInfo(mHouseLocation, mInfo2);
    }

}
