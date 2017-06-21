package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/6/21.
 */

class CmdGetHouseShowTime extends CommunicationBase {
    private int mHouseId = 0;

    CmdGetHouseShowTime(Context context, int house_id) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_SHOWTIME);
        mHouseId = house_id;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/showtime", mHouseId);
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
        ResGetHouseShowtime res = new ResGetHouseShowtime(nErrCode, jObject);
        return res;
    }
}
