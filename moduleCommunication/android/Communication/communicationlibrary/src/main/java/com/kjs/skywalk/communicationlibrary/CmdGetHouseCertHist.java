package com.kjs.skywalk.communicationlibrary;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/9/18.
 */

class CmdGetHouseCertHist extends CommunicationBase {

    CmdGetHouseCertHist(Context context, int house_id) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_CERTIFY_HIST);
        mArgs = new ApiArgHouseId(house_id);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/certhist", ((ApiArgHouseId)mArgs).getHouseId());
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseCertHist res = new ResGetHouseCertHist(nErrCode, jObject);
        return res;
    }
}
