package com.kjs.skywalk.app_android;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.sax.RootElement;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.kjs.skywalk.app_android.Apartment.Activity_ApartmentDetail;
import com.kjs.skywalk.app_android.Homepage.fragmentHomePage;
import com.kjs.skywalk.app_android.ClassDefine.IntentExtraKeyValue;
import com.kjs.skywalk.communicationlibrary.CmdExecRes;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiArgs;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.text.SimpleDateFormat;
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

    private RelativeLayout mContainer = null;
    private fragmentFangYuanGuanLiInfo1 mFragInfo1 = null;
    private fragmentFangYuanGuanLiInfo2 mFragInfo2 = null;
    private fragmentFangYuanGuanLiInfo3 mFragInfo3 = null;
    int mTestCount = 0;
//    int mHouseId = 2;
    private String mInfo2 = "";
    IApiResults.IHouseCertDigestInfo mCertDigestInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangyuan_guanli);

        mContainer = (RelativeLayout)findViewById(R.id.container);

        mFragInfo1 = new fragmentFangYuanGuanLiInfo1();
        mFragInfo2 = new fragmentFangYuanGuanLiInfo2();
        mFragInfo3 = new fragmentFangYuanGuanLiInfo3();
    }

    @Override
    public void onResume() {
        super.onResume();

        getHouseInfo();
    }

    private void getHouseInfo() {
        CmdExecRes res = CommandManager.getCmdMgrInstance(this).GetHouseInfo(mHouseId, true);
        if (res.mError != CommunicationError.CE_ERROR_NO_ERROR) {
            kjsLogUtil.e(String.format("Fail to GetHouseInfo, err:0x%x", res.mError));
        } else {
            StoreCommand(res);
        }
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
    public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
        if (null == iResult) {
            kjsLogUtil.w("result is null");
            return;
        }
        // Filter all other commands
        CmdExecRes cmd = RetrieveCommand(cmdSeq);
        if (null == cmd) {  // result is not we wanted
            return;
        }
        kjsLogUtil.i(String.format("[command: %d(%s)] --- %s", command, CommunicationInterface.CmdID.GetCmdDesc(command), iResult.DebugString()));

        if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
            commonFun.showToast_info(getApplicationContext(), mContainer, "获取房屋信息失败:" + iResult.GetErrDesc());
            super.onCommandFinished(command, cmdSeq, iResult);
            return;
        }

        if (command == CMD_GET_HOUSE_INFO) {
            IApiArgs.IArgsGetHouseInfo args = (IApiArgs.IArgsGetHouseInfo)iResult.GetArgs();
            if (args.getId() == mHouseId) {
                updateHouseInfo((IApiResults.IGetHouseInfo) iResult);
            }
        }
    }

    private void updateHouseInfo(final IApiResults.IGetHouseInfo houseInfo) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mInfo2 = commonFun.getHouseTypeString(houseInfo.Bedrooms(), houseInfo.Livingrooms(), houseInfo.Bathrooms());
                mInfo2 += " | ";
                mInfo2 += String.format("%d m²", houseInfo.Acreage() / 100);
                mInfo2 += " | ";
                mInfo2 += String.format("%d/%d F", houseInfo.Floorthis(), houseInfo.FloorTotal());

                IApiResults.IHouseCertDigestInfo certInfo = (IApiResults.IHouseCertDigestInfo)houseInfo;
                updateFragment(certInfo);
            }
        });
    }

    private void updateFragment(IApiResults.IHouseCertDigestInfo info) {
        kjsLogUtil.i(info.CertDesc());
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        switch (info.CertStat()) {
            case HOUSE_CERT_STAT_WAIT:
                fragTransaction.replace(R.id.info_container, mFragInfo1);mFragInfo1.setHouseLocation(mHouseLocation);
                mFragInfo1.setHouseInfo(mInfo2);
                mFragInfo1.setHouseLocation(mHouseLocation);
                break;
            case HOUSE_CERT_STAT_PASSED:
                mFragInfo3.setHouseLocation(mHouseLocation);
                mFragInfo3.setHouseInfo(mInfo2);
                Date date = info.CertTime();
                //SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                String t = format.format(date);
                mFragInfo3.setPassTime(t);
                fragTransaction.replace(R.id.info_container, mFragInfo3);
                break;
            case HOUSE_CERT_STAT_FAILED:
                mFragInfo2.setHouseInfo(mInfo2);
                mFragInfo2.setHouseLocation(mHouseLocation);
                mFragInfo2.setReason(info.CertDesc());
                fragTransaction.replace(R.id.info_container, mFragInfo2);
                break;
        }

        fragTransaction.commit();
    }

}
