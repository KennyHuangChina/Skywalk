package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdAddDeliverable extends CommunicationBase {
    private String mName = "";

    CmdAddDeliverable(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_ADD_DELIVERABLE);
        mMethodType = "POST";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/deliverable";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("name=" + mName);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_NAME)) {
            return false;
        }

        mName = map.get(CommunicationParameterKey.CPK_NAME);
        if (mName.length() == 0) {
            Log.e(TAG, "mName: " + mName);
            return false;
        }
        mName = String2Base64(mName);
        Log.d(TAG, "mName: " + mName);

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResAddResource result = new ResAddResource(nErrCode, jObject);
        return result;
    }
}
