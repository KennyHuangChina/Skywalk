package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/31.
 */

class CmdGetAgencyList extends CommunicationBase {
    private int mBegin = 0;
    private int mCnt = 0;

    CmdGetAgencyList(Context context, int begin, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_GET_AGENCY_LIST);
        mMethodType = "GET";
        mBegin = begin;
        mCnt = cnt;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/AgencyList";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData += ("bgn=" + mBegin);
        mRequestData += "&";
        mRequestData += ("cnt=" + mCnt);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mBegin < 0) {
            Log.e(TAG, "mBegin:" + mBegin);
            return false;
        }
        if (mCnt < 0) {
            Log.e(TAG, "mCnt:" + mCnt);
            return false;
        }
        return true;    // return super.checkParameter(map);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetAgencyList res = new ResGetAgencyList(nErrCode, jObject);
        return res;
    }
}
