package com.kjs.skywalk.communicationlibrary;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdGetFacilityTypeList extends CommunicationBase {

    CmdGetFacilityTypeList(Context context, String strAPI) {
        super(context, strAPI);
        TAG = "CmdGetFacilityTypeList";
        mMethodType = "GET";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/facitypelst";
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true;    // super.checkParameter(map);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResIdNameList res = new ResIdNameList(nErrCode, jObject);
        return res;
    }
}
