package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/2.
 */

class CmdGetHouseList extends CommunicationBase {
    protected int mType = 0;  // 0: all house; 1: recommend; 2: deducted; 3: new
    protected int mBeginPosi = 0;
    protected int mFetchCount = 0;

    CmdGetHouseList(Context context, String strAPI) {
        super(context, strAPI);
        TAG = "CmdGetHouseList";
//        Log.i(TAG, "Constructor");
        mMethodType = "GET";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_HOUSE_TYPE) ||
                !map.containsKey(CommunicationParameterKey.CPK_LIST_BEGIN) ||
                !map.containsKey(CommunicationParameterKey.CPK_LIST_CNT) ) {
            return false;
        }

        try {
            mType = Integer.parseInt(map.get(CommunicationParameterKey.CPK_HOUSE_TYPE));
            if (mType < 0 || mType > 3) {
                Log.e(TAG, "mType:" + mType);
                return false;
            }
            mBeginPosi = Integer.parseInt(map.get(CommunicationParameterKey.CPK_LIST_BEGIN));
            if (mBeginPosi < 0) {
                Log.e(TAG, "mBeginPosi:" + mBeginPosi);
                return false;
            }
            mFetchCount = Integer.parseInt(map.get(CommunicationParameterKey.CPK_LIST_CNT));
            if (mFetchCount < 0) {
                Log.e(TAG, "mFetchCount:" + mFetchCount);
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
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
