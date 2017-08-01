package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/8/1.
 */

class CmdGetUserHouseWatchList extends CommunicationBase {
    private int mBeginPosi  = 0;
    private int mFetchCount = 0;

    CmdGetUserHouseWatchList(Context context, int bgn, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_GET_USER_HOUSE_WATCH_LIST);
        mBeginPosi  = bgn;
        mFetchCount = cnt;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/housewatch/user";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("bgn=" + mBeginPosi);
        mRequestData += "&";
        mRequestData += ("cnt=" + mFetchCount);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
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
        ResGetHouseList result = new ResGetHouseList(nErrCode, jObject);
        return result;
    }
}
