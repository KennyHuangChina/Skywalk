package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by kenny on 2017/9/24.
 */

class CmdMakeAppointmentAction extends CommunicationBase {
    private int     mAppointment = 0;
    private int     mAct         = 0;
    private String  mTimeBegin   = null;
    private String  mTimeEnd     = null;
    private String  mComments    = null;

    CmdMakeAppointmentAction(Context context, int aid, int act, String tb, String te, String actTxt) {
        super(context, CommunicationInterface.CmdID.CMD_MAKE_APPOINTMENT_ACTION);
        mAppointment= aid;
        mAct        = act;
        mTimeBegin  = tb;
        mTimeEnd    = te;
        mComments   = actTxt;
        mMethodType = "POST";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/appointment/%d/act", mAppointment);
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        if (mAct > 0) {
            mRequestData = ("act=" + mAct);
        }
        if (null != mTimeBegin && !mTimeBegin.isEmpty()
                && null != mTimeEnd && !mTimeEnd.isEmpty()) {
            if (!mRequestData.isEmpty()) {
                mRequestData += "&";
            }
            mRequestData += String.format("tb=%s&te=%s", mTimeBegin, mTimeEnd);
        }
        if (null != mComments && !mComments.isEmpty()) {
            if (!mRequestData.isEmpty()) {
                mRequestData += "&";
            }
            mRequestData += ("ac=" + String2Base64(mComments));
        }

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        String Fn = "[checkParameter] ";

        if (mAct < 2 || mAct > 5) {
            Log.e(TAG, Fn + String.format("Invalid action:%d, (2 ~ 5)", mAct));
            return false;
        }

        if (null == mComments || mComments.isEmpty()) {
            Log.e(TAG, Fn + "Action comments not set");
            return false;
        }

        if (3 == mAct /*|| 1 == mAct*/) {
            if (null == mTimeBegin || mTimeBegin.isEmpty()) {
                Log.e(TAG, Fn + "begin time not set");
                return false;
            }
            if (null == mTimeEnd || mTimeEnd.isEmpty()) {
                Log.e(TAG, Fn + "end time not set");
                return false;
            }
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            try {
                Date timeBgn = format.parse(mTimeBegin);
                Date timeEnd = format.parse(mTimeEnd);
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            mTimeBegin  = null;
            mTimeEnd    = null;
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return new ResAddResource(nErrCode, jObject);
    }
}
