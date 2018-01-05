package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/13.
 */

class CmdAddFacility extends CommunicationBase {

    CmdAddFacility(Context context, int type, String name, String pic) {
        super(context, CommunicationInterface.CmdID.CMD_ADD_FACILITY);
        mMethodType = "POST";
        mArgs = new ApiArgsFacility(type, name, pic);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/facility";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("name=" + ((ApiArgsFacility)mArgs).getName());
        mRequestData += "&";
        mRequestData += ("type=" + ((ApiArgsFacility)mArgs).getType());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResAddResource result = new ResAddResource(nErrCode, jObject);
        return result;
    }
}
