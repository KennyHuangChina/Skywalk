package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kjs.skywalk.communicationlibrary.CmdExecRes;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_CERTIFY_HOUSE;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Zushouweituo_Kanfangshijian extends SKBaseActivity {

    private RelativeLayout mContainer = null;
    private int mHouseID = 5;
    private String mHouseLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo_kanfangshijian);

        TextView titleText = (TextView) findViewById(R.id.textViewActivityTitle);
        if (mHouseLocation == null || mHouseLocation.isEmpty()) {
            titleText.setText("未知房源");
        } else {
            titleText.setText(mHouseLocation);
        }
        findViewById(R.id.imageViewActivityClose).setVisibility(View.INVISIBLE);

        findViewById(R.id.textViewWorkingDayEvening).setSelected(true);
        findViewById(R.id.textViewWorkingDayMorning).setSelected(true);
        findViewById(R.id.textViewWorkingDayNoon).setSelected(true);
        findViewById(R.id.textViewHolidayEvening).setSelected(true);
        findViewById(R.id.textViewHolidayMorning).setSelected(true);
        findViewById(R.id.textViewHolidayNoon).setSelected(true);

        mContainer = (RelativeLayout)findViewById(R.id.container);

    }

    private boolean mWorkingDayMorming = true;
    private boolean mWorkingDayNoon = true;
    private boolean mWorkingDayEvening = true;
    private boolean mHolidayMorning = true;
    private boolean mHolidayNoon = true;
    private boolean mHolidayEvening = true;
    private String mTime = "";
    private int mTimeWorkingDay = 0;
    private int mTimeHoliday = 0;

    private boolean collectData(){
        mWorkingDayEvening = findViewById(R.id.textViewWorkingDayEvening).isSelected();
        mWorkingDayMorming = findViewById(R.id.textViewWorkingDayMorning).isSelected();
        mWorkingDayNoon = findViewById(R.id.textViewWorkingDayNoon).isSelected();
        mHolidayEvening = findViewById(R.id.textViewHolidayEvening).isSelected();
        mHolidayMorning = findViewById(R.id.textViewHolidayMorning).isSelected();
        mHolidayNoon = findViewById(R.id.textViewHolidayNoon).isSelected();
        EditText text = (EditText)findViewById(R.id.editText);
        mTime = text.getText().toString();

        if(!mWorkingDayEvening && !mWorkingDayMorming && !mWorkingDayNoon &&
                !mHolidayEvening && !mHolidayMorning && !mHolidayNoon && mTime.isEmpty()) {
            commonFun.showToast_info(this, text, "请至少选择一个看房时间或输入自定义看房时间");
            return false;
        }

        mTimeWorkingDay = 0;
        mTimeHoliday = 0;

        if(mWorkingDayMorming) {
            mTimeWorkingDay += 1;
        }
        if(mWorkingDayNoon) {
            mTimeWorkingDay += 2;
        }
        if(mWorkingDayEvening) {
            mTimeWorkingDay += 4;
        }
        if(mHolidayMorning) {
            mTimeHoliday += 1;
        }
        if(mHolidayNoon) {
            mTimeHoliday += 2;
        }
        if(mHolidayEvening) {
            mTimeHoliday += 4;
        }
        return true;
    }

    @Override
    public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
        if (null == iResult) {
            kjsLogUtil.w("result is null");
            return;
        }
        CmdExecRes cmd = RetrieveCommand(cmdSeq);
        if (null == cmd) {  // result is not we wanted
            return;
        }
        kjsLogUtil.i(String.format("[command: %d(%s)] --- %s", command, CommunicationInterface.CmdID.GetCmdDesc(command), iResult.DebugString()));

        int errCode = iResult.GetErrCode();
        if (CommunicationError.CE_ERROR_NO_ERROR != errCode) {
            kjsLogUtil.e("Command:" + command + " finished with error: " + errCode);
            super.onCommandFinished(command, cmdSeq, iResult);
            if (command == CommunicationInterface.CmdID.CMD_SET_HOUSE_SHOWTIME) {
                commonFun.showToast_info(Activity_Zushouweituo_Kanfangshijian.this, mContainer, "保存失败: " + iResult.GetErrDesc());
                hideWaiting();
            }
            return;
        }

        if (command == CommunicationInterface.CmdID.CMD_SET_HOUSE_SHOWTIME) {
            commonFun.showToast_info(Activity_Zushouweituo_Kanfangshijian.this, mContainer, "保存成功");
            hideWaiting();
        }
    }

    private void doSave() {
        if(!collectData()) {
            return;
        }

        if(mHouseID < 0) {
            return;
        }

        CmdExecRes res = CommandManager.getCmdMgrInstance(this).SetHouseShowtime(mHouseID, mTimeWorkingDay, mTimeHoliday, mTime);
        if (res.mError == CE_ERROR_NO_ERROR) {
            StoreCommand(res);
            showWaiting(mContainer);
        } else {
            kjsLogUtil.e("Fail to send command SetHouseShowtime, err:" + res.mError);
            commonFun.showToast_info(this, mContainer, "接口调用失败");
        }
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title: {
                finish();
            }
            break;
            case R.id.textViewWorkingDayMorning:
            case R.id.textViewWorkingDayNoon:
            case R.id.textViewWorkingDayEvening:
            case R.id.textViewHolidayMorning:
            case R.id.textViewHolidayNoon:
            case R.id.textViewHolidayEvening:
                v.setSelected(!v.isSelected());
            break;
            case R.id.imageViewActivityBack:
                finish();
            break;
            case R.id.textViewSave:
                doSave();
                break;
        }
    }
}
