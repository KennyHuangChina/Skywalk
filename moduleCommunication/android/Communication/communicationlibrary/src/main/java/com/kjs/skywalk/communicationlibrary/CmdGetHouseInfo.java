package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/1.
 */

class CmdGetHouseInfo extends CommunicationBase {
    private int     mHouseId;           // house id
    private boolean mbBackend;          // if fetch real, complete house info for back-end using

    CmdGetHouseInfo(Context context, int house_id, boolean bBackEnd) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO);
        mMethodType = "GET";
        mHouseId    = house_id;
        mbBackend   = bBackEnd;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/" + mHouseId;
        if (mbBackend) {
            mCommandURL += "?be=1";
        }
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mHouseId <= 0) {
            Log.e(TAG, "mHouseId: " + mHouseId);
            return false;
        }
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseInfo result = new ResGetHouseInfo(nErrCode, jObject);
        return result;
    }
}
