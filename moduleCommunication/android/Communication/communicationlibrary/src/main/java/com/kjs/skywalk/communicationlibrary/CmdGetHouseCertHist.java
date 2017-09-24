package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/9/18.
 */

class CmdGetHouseCertHist extends CommunicationBase {
    private int mHouseId = 0;

    CmdGetHouseCertHist(Context context, int house_id) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_CERTIFY_HIST);
        mHouseId = house_id;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/certhist", mHouseId);
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mHouseId <= 0) {
            Log.e(TAG, String.format("mHouseId:%d", mHouseId));
            return false;
        }
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseCertHist res = new ResGetHouseCertHist(nErrCode, jObject);
        return res;
    }
}
