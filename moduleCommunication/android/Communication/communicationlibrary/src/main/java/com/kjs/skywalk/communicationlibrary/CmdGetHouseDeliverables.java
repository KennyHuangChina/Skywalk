package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdGetHouseDeliverables extends CommunicationBase {
    private int mHouseId = 0;

    CmdGetHouseDeliverables(Context context, int house) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_DELIVERABLES);
        mMethodType = "GET";
        mHouseId = house;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/house/" + mHouseId + "/deliverables";
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mHouseId <= 0) {
            Log.e(TAG, "mHouseId:" + mHouseId);
            return false;
        }
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResHouseDeliverables res = new ResHouseDeliverables(nErrCode, jObject);
        return res;
    }
}
