package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/20.
 */

class CmdGetBriefPublicHouseInfo extends CommunicationBase {
    final protected String  TAG = getClass().getSimpleName();
    private int mHouseId = -1;

    CmdGetBriefPublicHouseInfo(Context context, int house_id){
        super(context, CommunicationInterface.CmdID.CMD_GET_BRIEF_PUBLIC_HOUSE_INFO);
        mMethodType = "GET";
        mHouseId = house_id;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/digest/", mHouseId);
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mHouseId <= 0) {
            Log.e(TAG, "mHouseId: " + mHouseId);
             return false;
        }
       return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResHousePublicBriefInfo result = new ResHousePublicBriefInfo(nErrCode, jObject);
        return result;
    }
}
