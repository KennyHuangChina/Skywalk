package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

//import CommunicationBase;

/**
 * Created by kenny on 2017/3/1.
 */

class CmdLoginBySms extends CommunicationBase {
    private String  mLoginName      = "";   // login name. should be phone number
    private String  mSMS            = "";   // sms code
    private int     mType           = 1;    // client type. 0 - web; 1 - APP
    private String  mPicSessionId   = "";   // picture session id. ref to captcha GUID of API sec_pic
    private String  mPicResult      = "";   // picture result

    CmdLoginBySms(Context context, String strAPI) {
        super(context, strAPI);
        TAG = "CmdLoginBySms";
//        Log.i(TAG, "Constructor");
        mMethodType = "POST";

        // generate a random
        mRadom = generateRandom();
    }

    @Override
    public int doOperation(HashMap<String, String> map,
                           CommunicationInterface.CICommandListener commandListener,
                           CommunicationInterface.CIProgressListener progressListener) {
        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/admin/loginsms";
//        mCommandURL += "/";

        generateRequestData();

        super.doOperation(map, commandListener, progressListener);

        Log.i(TAG, "doOperation ... out");

        return CommunicationError.CE_ERROR_NO_ERROR;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
//        return super.checkParameter(map);
        if(!map.containsKey(CommunicationParameterKey.CPK_USER_NAME) ||
                !map.containsKey(CommunicationParameterKey.CPK_SMS_CODE)) {
            return false;
        }

        mLoginName = map.get(CommunicationParameterKey.CPK_USER_NAME);
        if (null == mLoginName || mLoginName.isEmpty()) {
            return false;
        }

        mSMS = map.get(CommunicationParameterKey.CPK_SMS_CODE);
        if (null == mSMS || mSMS.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        // Store current login session to local
        try {
            SKSessionStore sessStore = SKSessionStore.getInstance(mContext);
            if (null == sessStore) {
                return null;
            }
            // if login not success, the Sid field will be empty, which means the Sid stored before
            //    will be cleaned, just like what API Logout did
            String loginSession = "";
            if (null != jObject) {
                loginSession = jObject.getString("Sid");
            }
            sessStore.save(loginSession);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        ResLogin result = new ResLogin(nErrCode, jObject);
        return result;
    }

    private void generateRequestData() {
        mRequestData = ("ver=" +  mVersion);
        mRequestData += "&";
        mRequestData += ("ln=" +  mLoginName);
        mRequestData += "&";
        mRequestData += ("sms=" +  mSMS);
        mRequestData += "&";
//        mRequestData += ("rd=" + mRadom);
//        mRequestData += "&";
        mRequestData += ("typ=" + mType);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
