package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/13.
 */

class CmdAddFacility extends CommunicationBase {
    private String  mName = "";
    private int     mType = 0;

    CmdAddFacility(Context context, String strAPI) {
        super(context, strAPI);
        TAG = "CmdAddFacility";
        mMethodType = "POST";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/facility";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("name=" + mName);
        mRequestData += "&";
        mRequestData += ("type=" + mType);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_NAME) ||
                !map.containsKey(CommunicationParameterKey.CPK_TYPE)) {
            return false;
        }

        mName = map.get(CommunicationParameterKey.CPK_NAME);
        if (mName.length() == 0) {
            Log.e(TAG, "mName: " + mName);
            return false;
        }
        mName = String2Base64(mName);
        Log.d(TAG, "mName: " + mName);

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
        ResAddResource result = new ResAddResource(nErrCode, jObject);
        return result;
    }
}
