package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/2.
 */

class CmdGetHouseList extends CommunicationBase {

    private int mType = 0;  // 0: all house; 1: recommend; 2: deducted; 3: new
    private int mBeginPosi = 0;
    private int mFetchCount = 0;

    CmdGetHouseList(Context context, String strAPI) {
        super(context, strAPI);
        TAG = "CmdGetHouseList";
//        Log.i(TAG, "Constructor");
        mMethodType = "GET";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_HOUSE_TYPE)) {
            return false;
        }

        try {
            mType = Integer.parseInt(map.get(CommunicationParameterKey.CPK_HOUSE_TYPE));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        try {
            mBeginPosi = Integer.parseInt(map.get(CommunicationParameterKey.CPK_LIST_BEGIN));
            mFetchCount = Integer.parseInt(map.get(CommunicationParameterKey.CPK_LIST_CNT));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseList result = new ResGetHouseList(nErrCode, jObject);
        return result;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/list";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("type=" + mType);
        mRequestData += "&";
        mRequestData += ("bgn=" + mBeginPosi);
        mRequestData += "&";
        mRequestData += ("cnt=" + mFetchCount);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
