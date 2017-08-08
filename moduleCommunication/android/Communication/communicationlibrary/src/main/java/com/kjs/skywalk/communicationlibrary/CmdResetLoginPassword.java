package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/8/6.
 */

class CmdResetLoginPassword extends CommunicationBase {
    private String mUser    = null;
    private String mNewPass = null;
    private String mSalt    = null;
    private String mSms     = null;

    CmdResetLoginPassword(Context context, String user, String newPass, String sms, String salt, String rand) {
        super(context, CommunicationInterface.CmdID.CMD_RESET_LOGIN_PASS);
        mMethodType = "PUT";
        mNeedLogin  = false;
        mUser       = user;
        mNewPass    = newPass;
        mSalt       = salt;
        mSms        = sms;

//        mRadom = rand;
        // generate a random
        mRadom = generateRandom();
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/resetpass";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("v=1.0");
        mRequestData += "&";
        mRequestData += ("u=" + mUser);
        mRequestData += "&";
        mRequestData += ("p=" + mNewPass);
        mRequestData += "&";
        mRequestData += ("r=" + mRadom);
        mRequestData += "&";
        mRequestData += ("s=" + mSms);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (null == mUser || mUser.isEmpty()) {
            Log.e(TAG, "mUser not set");
            return false;
        }
        if (null == mNewPass || mNewPass.isEmpty()) {
            Log.e(TAG, "mNewPass not set");
            return false;
        }
        if (null == mSms || mSms.isEmpty()) {
            Log.e(TAG, "mSms not set");
            return false;
        }

        NativeCall pNC = NativeCall.GetNativeCaller();
        byte[] pass = pNC.EncryptNewPassword(mNewPass, mSalt, mRadom, mVersion);
        if (null == pass) {
            return false;
        }
        Log.w(TAG, "pass:" + pass);
//        map.put(CommunicationParameterKey.CPK_PASSWORD, new String(pass));
//        String strNewPass = new String(pass);
        mNewPass = Base64.encodeToString(pass, Base64.URL_SAFE);
        Log.d(TAG, "mNewPass:" + mNewPass);

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResResetPass res = new ResResetPass(nErrCode, jObject);
        return res;
    }
}
