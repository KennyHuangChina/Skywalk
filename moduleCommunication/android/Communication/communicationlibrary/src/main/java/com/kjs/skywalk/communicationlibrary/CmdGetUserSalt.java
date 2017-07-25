package com.kjs.skywalk.communicationlibrary;

//import CommunicationBase;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/18.
 */

class CmdGetUserSalt extends CommunicationBase {

    private String mUserName = "";

    CmdGetUserSalt(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_GET_USER_SALT);
        mMethodType = "GET";
        mNeedLogin = false;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map)
    {
        if (!map.containsKey(CommunicationParameterKey.CPK_USER_NAME)) {
            return false;
        }

        mUserName = map.get(CommunicationParameterKey.CPK_USER_NAME);
        if (null == mUserName || mUserName.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/salt";
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("un=" + mUserName);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetUserSalt result = new ResGetUserSalt(nErrCode, jObject);
        return result;
    }
}
