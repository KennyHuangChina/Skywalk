package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/6/22.
 */

class CmdGetAppointmentListHouseSee extends CommunicationBase {

    private int     mHouseId        = 0;
    private int     mBeginPosi      = 0;
    private int     mFetchCount     = 0;

    CmdGetAppointmentListHouseSee(Context context, int house_id, int begin, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_APPOINT_HOUSE_SEE_LST);
        mHouseId    = house_id;
        mBeginPosi  = begin;
        mFetchCount = cnt;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/appointment/house/%d/seelist", mHouseId);
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData += ("bgn=" + mBeginPosi);
        mRequestData += "&";
        mRequestData += ("fCnt=" + mFetchCount);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mHouseId <= 0) {
            Log.e(TAG, "mHouseId: " + mHouseId);
            return false;
        }

        if (mBeginPosi < 0) {
            Log.e(TAG, "mBeginPosi: " + mBeginPosi);
            return false;
        }

        if (mFetchCount < 0) {
            Log.e(TAG, "mFetchCount: " + mFetchCount);
            return false;
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return new ResGetAppointmentList(nErrCode, jObject);
    }
}
