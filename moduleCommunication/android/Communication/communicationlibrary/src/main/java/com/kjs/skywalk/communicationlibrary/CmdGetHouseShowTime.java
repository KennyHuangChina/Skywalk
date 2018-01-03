package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/6/21.
 */

class CmdGetHouseShowTime extends CommunicationBase {

    CmdGetHouseShowTime(Context context, int house_id) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_SHOWTIME);
        mArgs = new ApiArgHouseId(house_id);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/showtime", ((ApiArgHouseId)mArgs).getHouseId());
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseShowtime res = new ResGetHouseShowtime(nErrCode, jObject);
        return res;
    }
}
