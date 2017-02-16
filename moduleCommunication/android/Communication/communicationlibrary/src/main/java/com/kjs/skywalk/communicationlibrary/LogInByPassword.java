package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by admin on 2017/1/23.
 */

class LogInByPassword extends CommunicationBase {

    private String mUserName = "";
    private String mPassword = "";
    private String mRadom = "";
    private String mType = "1";

    LogInByPassword(Context context) {
        super(context);
        TAG = "LogInByPassword";
        Log.i(TAG, "Constructor");
        mMethodType = "POST";
        mSessionID = "xxxxx";
    }
    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if(!map.containsKey(CommunicationParameterKey.CPK_USER_NAME) ||
                !map.containsKey(CommunicationParameterKey.CPK_PASSWORD) ||
                !map.containsKey(CommunicationParameterKey.CPK_RANDOM)) {
            return false;
        }

        mUserName = map.get(CommunicationParameterKey.CPK_USER_NAME);
        if(mUserName == null || mUserName.isEmpty()) {
            return false;
        }
        mPassword = map.get(CommunicationParameterKey.CPK_PASSWORD);
        if(mPassword == null || mPassword.isEmpty()) {
            return false;
        }
        mRadom = map.get(CommunicationParameterKey.CPK_RANDOM);
        if(mRadom == null || mRadom.isEmpty()) {
            return false;
        }

        NativeCall pNC = NativeCall.GetNativeCaller();
        byte[] pass = pNC.EncryptPassword(mPassword, "123456", mRadom, 1);
        Log.w(TAG, "pass:" + pass);

        return true;
    }

    private void generateRequestData() {
        mRequestData = ("ln=" +  mUserName);
        mRequestData += "&";
        mRequestData += ("pw=" + mPassword);
        mRequestData += "&";
        mRequestData += ("rd=" + mRadom);
        mRequestData += "&";
        mRequestData += ("typ=" + mType);
    }

    @Override
    public HashMap<String, String> doCreateResultMap(JSONObject jObject) {
        return null;
    }

    @Override
    public int doOperation(HashMap<String, String> map, CommunicationInterface.CICommandListener commandListener, CommunicationInterface.CIProgressListener progressListener) {
        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/admin/loginpass";
        mCommandURL += "/";

        generateRequestData();

        super.doOperation(map, commandListener, progressListener);

        Log.i(TAG, "doOperation ... out");

        return CommunicationError.CE_ERROR_NO_ERROR;
    }
}
