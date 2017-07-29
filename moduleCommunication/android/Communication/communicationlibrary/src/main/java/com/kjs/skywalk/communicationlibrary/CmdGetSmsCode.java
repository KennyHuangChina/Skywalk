package com.kjs.skywalk.communicationlibrary;

//import CommunicationBase;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/2/27.
 */

class CmdGetSmsCode extends CommunicationBase {
    private String mSmsCode = "";
    private String mUserName = "";

    CmdGetSmsCode(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_GET_SMS_CODE);
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
        mCommandURL = "/v1/admin/fetchsms";
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("ln=" + mUserName);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetSmsCode result = new ResGetSmsCode(nErrCode, jObject);
        return result;
    }
}
