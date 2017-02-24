package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by admin on 2017/1/23.
 */

class CmdLoginByPassword extends CommunicationBase {

    private int mType = 1; // client type: 0 - web; 1 - APP
    private String mSalt = "";  // user salt get from server
    private String mUserName = "";
    private String mPassword = "";
    private String mRadom = "";

    CmdLoginByPassword(Context context) {
        super(context);
        TAG = "CmdLoginByPassword";
        Log.i(TAG, "Constructor");
        mAPI = CommunicationCommand.CC_LOG_IN_BY_PASSWORD;
        mMethodType = "POST";
        mSessionID = "xxxxx";
        mVersion = 1;
    }
    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if(!map.containsKey(CommunicationParameterKey.CPK_USER_NAME) ||
                !map.containsKey(CommunicationParameterKey.CPK_PASSWORD) ||
                !map.containsKey(CommunicationParameterKey.CPK_RANDOM) ||
                !map.containsKey(CommunicationParameterKey.CPK_USER_SALT)) {
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
        mSalt = map.get(CommunicationParameterKey.CPK_USER_SALT);
        if(mSalt == null || mSalt.isEmpty()) {
            return false;
        }

        NativeCall pNC = NativeCall.GetNativeCaller();
        byte[] pass = pNC.EncryptPassword(mPassword, mSalt, mRadom, mVersion);
        if (null == pass) {
            return false;
        }
        Log.w(TAG, "pass:" + pass);
//        map.put(CommunicationParameterKey.CPK_PASSWORD, new String(pass));
//        String strNewPass = new String(pass);
        mPassword = Base64.encodeToString(pass, Base64.URL_SAFE);
        Log.d(TAG, "mPassword:" + mPassword);

        return true;
    }

    private void generateRequestData() {
        mRequestData = ("ver=" +  mVersion);
        mRequestData += "&";
        mRequestData += ("ln=" +  mUserName);
        mRequestData += "&";
//        mRequestData += ("pw=123456abcdefg")
        mRequestData += ("pw=" + mPassword);
        mRequestData += "&";
        mRequestData += ("rd=" + mRadom);
        mRequestData += "&";
        mRequestData += ("typ=" + mType);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(JSONObject jObject) {
        // Store current login session to local
        try {
            SKSessionStore sessStore = SKSessionStore.getInstance(mContext);
            if (null == sessStore) {
                return null;
            }
            String loginSession = jObject.getString("Sid");
            sessStore.save(loginSession);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        ResLogin result = new ResLogin(jObject);
        return result;
    }

    @Override
    public int doOperation(HashMap<String, String> map, CommunicationInterface.CICommandListener commandListener, CommunicationInterface.CIProgressListener progressListener) {
        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/admin/loginpass";
//        mCommandURL += "/";

        generateRequestData();

        super.doOperation(map, commandListener, progressListener);

        Log.i(TAG, "doOperation ... out");

        return CommunicationError.CE_ERROR_NO_ERROR;
    }
}
