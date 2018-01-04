package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/2.
 */

class CmdAddProperty extends CommunicationBase {

    CmdAddProperty(Context context, String name, String addr, String desc) {
        super(context, CommunicationInterface.CmdID.CMD_ADD_PROPERTY);
        mMethodType = "POST";
        mArgs = new ApiArgPropertyInfo(name, addr, desc);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/property/new";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("prop=" + ((ApiArgPropertyInfo)mArgs).getName());
        mRequestData += "&";
        mRequestData += ("addr=" + ((ApiArgPropertyInfo)mArgs).getAddress());
        mRequestData += "&";
        mRequestData += ("desc=" + ((ApiArgPropertyInfo)mArgs).getDesc());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResAddResource result = new ResAddResource(nErrCode, jObject);
        return result;
    }
}

