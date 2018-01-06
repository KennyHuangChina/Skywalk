package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/17.
 */

class CmdGetHouseFacilityList extends CommunicationBase {
//    private int mHouseId = 0;

    CmdGetHouseFacilityList(Context context, int house) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSEFACILITY_LIST);
        mArgs = new ApiArgsObjId(house);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/house/" + ((ApiArgsObjId)mArgs).getId() + "/facilities";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        super.generateRequestData();
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResHouseFacilityList res = new ResHouseFacilityList(nErrCode, jObject);
        return res;
    }
}
