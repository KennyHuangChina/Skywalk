package com.kjs.skywalk.communicationlibrary;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/1.
 */

class CmdGetHouseInfo extends CommunicationBase {
    private int     mHouseId;           // house id
    private boolean mGetPrivateInfo;    // if need to get the private info of house

    CmdGetHouseInfo(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO);
        mMethodType = "GET";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/" + mHouseId;
        if (mGetPrivateInfo) {
            mCommandURL += "?p=1";
        }
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX) ||
                !map.containsKey(CommunicationParameterKey.CPK_PRIVATE_INFO)) {
            return false;
        }

        String hid = map.get(CommunicationParameterKey.CPK_INDEX);
        if (null == hid || hid.isEmpty()) {
            return false;
        }
        try {
            mHouseId = Integer.parseInt(hid);
            mGetPrivateInfo = Boolean.parseBoolean(map.get(CommunicationParameterKey.CPK_PRIVATE_INFO));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseInfo result = new ResGetHouseInfo(nErrCode, jObject);
        return result;
    }
}
