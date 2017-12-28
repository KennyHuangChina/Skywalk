package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdGetBehalfHouses extends CommunicationBase {

    CmdGetBehalfHouses(Context context, int type, int bgn, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_GET_BEHALF_HOUSE_LIST);
        mArgs = new ApiArgsGetBehalfHouseList(type, bgn, cnt);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/behalf";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        ApiArgsGetBehalfHouseList args = (ApiArgsGetBehalfHouseList)mArgs;
        mRequestData = ("type=" + args.getType());
        mRequestData += "&";
        mRequestData += ("bgn=" + args.getBeginPosi());
        mRequestData += "&";
        mRequestData += ("cnt=" + args.getFetchCnt());
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseList result = new ResGetHouseList(nErrCode, jObject);
        return result;
    }
}
