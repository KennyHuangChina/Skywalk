package com.kjs.skywalk.communicationlibrary;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/14.
 */

class CmdGetNewEventCount extends CommunicationBase {

    CmdGetNewEventCount(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_GET_NEW_EVENT_CNT);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/event/count";
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true;    // super.checkParameter(map);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetNewEventCount result = new ResGetNewEventCount(nErrCode, jObject);
        return result;
    }
}
