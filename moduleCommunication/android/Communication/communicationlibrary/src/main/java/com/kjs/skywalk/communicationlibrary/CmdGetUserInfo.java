package com.kjs.skywalk.communicationlibrary;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/2/27.
 */

class CmdGetUserInfo extends CommunicationBase {
    private int mUserId = 0;

    CmdGetUserInfo(Context context, String strAPI) {
        super(context, strAPI);
        TAG = "CmdGetUserInfo";
//        Log.i(TAG, "Constructor");
        mMethodType = "GET";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/user/" + mUserId;
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
//        return super.checkParameter(map);
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX)) {
            return false;
        }

        try {
            mUserId = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
            if (mUserId <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetUserInfo result = new ResGetUserInfo(nErrCode, jObject);
        return result;
    }
}
