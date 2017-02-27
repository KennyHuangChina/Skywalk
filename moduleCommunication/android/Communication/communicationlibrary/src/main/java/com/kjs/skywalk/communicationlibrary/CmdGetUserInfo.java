package com.kjs.skywalk.communicationlibrary;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/2/27.
 */

class CmdGetUserInfo extends CommunicationBase {
    private int mUserId = 0;

    CmdGetUserInfo(Context context) {
        super(context);
        TAG = "CmdGetUserInfo";
//        Log.i(TAG, "Constructor");
        mAPI = CommunicationCommand.CC_GET_USER_INFO;
        mMethodType = "GET";
    }

    @Override
    public int doOperation(HashMap<String, String> map,
                           CommunicationInterface.CICommandListener commandListener,
                           CommunicationInterface.CIProgressListener progressListener) {
//        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/admin/user/" + mUserId;

        super.doOperation(map, commandListener, progressListener);

//        Log.i(TAG, "doOperation ... out");
        return CommunicationError.CE_ERROR_NO_ERROR;
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
