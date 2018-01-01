package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/8/1.
 */

class CmdGetUserHouseWatchList extends CommunicationBase {

    CmdGetUserHouseWatchList(Context context, int bgn, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_GET_USER_HOUSE_WATCH_LIST);
        mArgs = new ApiArgFetchList(bgn, cnt);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/housewatch/user";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("bgn=" + ((ApiArgFetchList)mArgs).getBeginPosi());
        mRequestData += "&";
        mRequestData += ("cnt=" + ((ApiArgFetchList)mArgs).getFetchCnt());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseList result = new ResGetHouseList(nErrCode, jObject);
        return result;
    }
}
