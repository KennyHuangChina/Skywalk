package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/31.
 */

class CmdGetAgencyList extends CommunicationBase {

    CmdGetAgencyList(Context context, int begin, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_GET_AGENCY_LIST);
//        mNeedLogin  = false;
        mArgs = new ApiArgFetchList(begin, cnt);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/AgencyList";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData += ("bgn=" + ((ApiArgFetchList)mArgs).getBeginPosi());
        mRequestData += "&";
        mRequestData += ("cnt=" + ((ApiArgFetchList)mArgs).getFetchCnt());

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetAgencyList res = new ResGetAgencyList(nErrCode, jObject);
        return res;
    }
}
