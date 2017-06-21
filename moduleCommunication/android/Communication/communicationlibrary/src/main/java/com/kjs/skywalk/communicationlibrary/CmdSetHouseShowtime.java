package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import com.kjs.skywalk.communicationlibrary.CommunicationBase;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/6/21.
 */

class CmdSetHouseShowtime extends CommunicationBase {
    private int     mHouse      = 0;    // house id
    private int     mPeriodW    = -1;   // period for working day
    private int     mPeriodV    = -1;   // period for weekend and vacation
    private String  mPeriodDesc = null; // period description

    CmdSetHouseShowtime(Context context, int house_id, int pw, int pv, String pd) {
        super(context, CommunicationInterface.CmdID.CMD_SET_HOUSE_SHOWTIME);
        mMethodType = "PUT";
        mHouse      = house_id;
        mPeriodW    = pw;
        mPeriodV    = pv;
        mPeriodDesc = pd;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/showtime", mHouse);
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("prdw=" + mPeriodW);
        mRequestData += "&";
        mRequestData += ("prdv=" + mPeriodV);
        mRequestData += "&";
        mRequestData += ("desc=" + mPeriodDesc);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mHouse <= 0) {
            Log.e(TAG, "mHouse: " + mHouse);
            return false;
        }
        if (mPeriodW < 0 || mPeriodW > 7) {
            Log.e(TAG, "mPeriodW: " + mPeriodW);
            return false;
        }
        if (mPeriodV < 0 || mPeriodV > 7) {
            Log.e(TAG, "mPeriodV: " + mPeriodV);
            return false;
        }

        return true;    // super.checkParameter(map);
    }
}
