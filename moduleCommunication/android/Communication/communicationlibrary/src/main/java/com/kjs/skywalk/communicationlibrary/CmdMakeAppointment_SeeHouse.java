package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import com.kjs.skywalk.communicationlibrary.CommunicationBase;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by kenny on 2017/6/24.
 */

class CmdMakeAppointment_SeeHouse extends CommunicationBase {
    private int     mHouseId    = 0;
    private String  mPhone      = null;
    private String  mTimeBgn    = null;
    private String  mTimeEnd    = null;
    private String  mDesc       = null;

    CmdMakeAppointment_SeeHouse(Context context, int house_id, String phone,
                                String time_bgn, String time_end, String desc) {
        super(context, CommunicationInterface.CmdID.CMD_APPOINT_SEE_HOUSE);
        mMethodType = "POST";
        mHouseId    = house_id;
        mPhone      = phone;
        mTimeBgn    = time_bgn;
        mTimeEnd    = time_end;
        mDesc       = desc;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/appointment/see/house/" + mHouseId;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("phone=" + mPhone);
        mRequestData += "&";
        mRequestData += ("pb=" + mTimeBgn);
        mRequestData += "&";
        mRequestData += ("pe=" + mTimeEnd);
        mRequestData += "&";
        mRequestData += ("desc=" + mDesc);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mHouseId <= 0) {
            Log.e(TAG, "mHouseId: " + mHouseId);
            return false;
        }
        if (mTimeBgn.isEmpty()) {
            Log.e(TAG, "mTimeBgn not set" );
            return false;
        }
//        Date timeBegin = Date.valueOf(mTimeBgn);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date timeBegin;
        try {
            timeBegin = format.parse(mTimeBgn);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Invalid mTimeBgn: " + mTimeBgn);
            return false;
        }

        if (mTimeEnd.isEmpty()) {
            Log.e(TAG, "mTimeEnd not set" );
            return false;
        }
        Date timeEnd;
        try {
            timeBegin = format.parse(mTimeEnd);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Invalid mTimeEnd: " + mTimeEnd);
            return false;
        }

        if (mDesc.isEmpty()) {
            Log.e(TAG, "mDesc not set" );
            return false;
        }
        mDesc = String2Base64(mDesc);

        return true; // super.checkParameter(map);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return new ResAddResource(nErrCode, jObject);
    }
}
