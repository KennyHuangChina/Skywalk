package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/24.
 */

class LogOut extends CommunicationBase {
    private String mType = "1";

    LogOut(Context context) {
        super(context);
        TAG = "LogOut";
        Log.i(TAG, "Constructor");
        mMethodType = "POST";
        mSessionID = "";
    }
    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if(!map.containsKey(CommunicationParameterKey.CPK_SESSION_ID)) {
            return false;
        }

        return true;
    }

    private void generateRequestData() {
        mRequestData = ("sid=" +  mSessionID);
    }

    @Override
    public HashMap<String, String> doCreateResultMap(JSONObject jObject) {
        return null;
    }

    @Override
    public int doOperation(HashMap<String, String> map, CommunicationInterface.CICommandListener commandListener, CommunicationInterface.CIProgressListener progressListener) {
        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/admin/logout";
        mCommandURL += "/";

        generateRequestData();

        super.doOperation(map, commandListener, progressListener);

        Log.i(TAG, "doOperation ... out");

        return CommunicationError.CE_ERROR_NO_ERROR;
    }
}
