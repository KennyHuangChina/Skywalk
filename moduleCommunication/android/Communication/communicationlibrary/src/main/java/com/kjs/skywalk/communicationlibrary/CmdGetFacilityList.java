package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/14.
 */

class CmdGetFacilityList extends CommunicationBase {
    private int mType = 0;  // 0 means all type

    CmdGetFacilityList(Context context, String strAPI) {
        super(context, strAPI);
        TAG = "CmdGetFacilityList";
        mMethodType = "GET";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/facilitys";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("type=" + mType);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_TYPE)) {
            return false;
        }

        try {
            mType = Integer.parseInt(map.get(CommunicationParameterKey.CPK_TYPE));
            if (mType < 0) {
                Log.e(TAG, "mType: " + mType);
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
        ResFacilityList res = new ResFacilityList(nErrCode, jObject);
        return res;
    }
}
