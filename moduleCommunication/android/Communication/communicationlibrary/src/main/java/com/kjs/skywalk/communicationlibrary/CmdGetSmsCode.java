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

    CmdGetSmsCode(Context context, String user) {
        super(context, CommunicationInterface.CmdID.CMD_GET_SMS_CODE);
        mNeedLogin = false;
        mArgs = new ApiArgsGetSmsCode(user);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/fetchsms";
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("ln=" + ((ApiArgsGetSmsCode)mArgs).getUserName());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetSmsCode result = new ResGetSmsCode(nErrCode, jObject);
        return result;
    }
}
