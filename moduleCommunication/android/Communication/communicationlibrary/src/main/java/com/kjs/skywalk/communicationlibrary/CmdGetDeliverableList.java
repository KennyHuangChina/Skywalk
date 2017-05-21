package com.kjs.skywalk.communicationlibrary;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdGetDeliverableList extends CommunicationBase {

    CmdGetDeliverableList(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_GET_DELIVERABLE_LIST);
        mMethodType = "GET";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/deliverables";
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResIdNameList res = new ResIdNameList(nErrCode, jObject);
        return res;
    }
}
