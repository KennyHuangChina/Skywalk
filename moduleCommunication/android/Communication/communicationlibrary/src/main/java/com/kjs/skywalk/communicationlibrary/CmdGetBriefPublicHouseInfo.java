package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/20.
 */

class CmdGetBriefPublicHouseInfo extends CommunicationBase {

    CmdGetBriefPublicHouseInfo(Context context, int house_id){
        super(context, CommunicationInterface.CmdID.CMD_GET_BRIEF_PUBLIC_HOUSE_INFO);
        mNeedLogin  = false;
        mArgs = new ApiArgHouseId(house_id);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/digest/", ((ApiArgHouseId)mArgs).getHouseId());
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResHousePublicBriefInfo result = new ResHousePublicBriefInfo(nErrCode, jObject);
        return result;
    }
}
