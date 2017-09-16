package com.kjs.skywalk.communicationlibrary;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/9/6.
 */

class CmdGetNewMsgCount extends CommunicationBase {

    CmdGetNewMsgCount(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_GET_NEW_MSG_CNT);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/sysmsg/newmsg";
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true;    // super.checkParameter(map);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetNewMsgCount result = new ResGetNewMsgCount(nErrCode, jObject);
        return result;
    }
}
