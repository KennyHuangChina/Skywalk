package com.kjs.skywalk.communicationlibrary;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/8/2.
 */

class CmdGetLoginUserInfo extends CommunicationBase {

    CmdGetLoginUserInfo(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_GET_LOGIN_USER_INFO);
        mNeedLogin = false;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/loginuser";
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true; // super.checkParameter(map);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetUserInfo result = new ResGetUserInfo(nErrCode, jObject);
        return result;
    }
}
