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

    CmdGetHouseDeliverables(Context context) {
        super(context, "GetHouseDeliverables");
        TAG = "CmdGetHouseDeliverables";
        mMethodType = "GET";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/hdl/" + mHouseId;
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX)) {
            return false;
        }

        try {
            mHouseId = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
            if (mHouseId <= 0) {
                Log.e(TAG, "mHouseId:" + mHouseId);
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
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
