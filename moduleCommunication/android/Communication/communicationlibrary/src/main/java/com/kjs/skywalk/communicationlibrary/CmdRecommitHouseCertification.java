package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/10/1.
 */

class CmdRecommitHouseCertification extends CommunicationBase {
    private int     mHouse      = -1;
    private String  mComments   = null;

    CmdRecommitHouseCertification(Context context, int house, String comments) {
        super(context, CommunicationInterface.CmdID.CMD_RECOMMIT_HOUSE_CERTIFICATON);
        mMethodType = "POST";
        mHouse      = house;
        mComments   = comments;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/recert", mHouse);
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("cc=" + String2Base64(mComments));
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mHouse <= 0) {
            Log.e(TAG, "mHouse: " + mHouse);
            return false;
        }
        if (null == mComments || mComments.isEmpty()) {
            Log.e(TAG, "mComments not set");
            return false;
        }
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return new ResAddResource(nErrCode, jObject);
    }
}
