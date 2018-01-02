package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdGetHouseDeliverables extends CommunicationBase {

    CmdGetHouseDeliverables(Context context, int house) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_DELIVERABLES);
        mArgs = new ApiArgHouseId(house);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/accessory/house/%d/deliverables", ((ApiArgHouseId)mArgs).getHouseId());
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResHouseDeliverables res = new ResHouseDeliverables(nErrCode, jObject);
        return res;
    }
}
