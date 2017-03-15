package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/24.
 */

class CmdLogout extends CommunicationBase {
    private String mType = "1";

    CmdLogout(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_LOG_OUT);
        TAG = "Logout";
//        Log.i(TAG, "Constructor");
        mMethodType = "POST";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/logout";
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        // Clear the current login session
       SKSessionStore sessStore = SKSessionStore.getInstance(mContext);
        if (null == sessStore) {
            return null;
        }
        sessStore.save("");

        return null;
    }
}
