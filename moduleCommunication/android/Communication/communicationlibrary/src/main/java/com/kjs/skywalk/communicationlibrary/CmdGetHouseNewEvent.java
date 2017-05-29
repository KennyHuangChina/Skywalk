package com.kjs.skywalk.communicationlibrary;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/15.
 */

class CmdGetHouseNewEvent extends CommunicationBase {

    CmdGetHouseNewEvent(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_NEW_EVENTS);
        mMethodType = "GET";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/event/houses";
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true;    // super.checkParameter(map);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseNewEvent result = new ResGetHouseNewEvent(nErrCode, jObject);
        return result;
    }
}
