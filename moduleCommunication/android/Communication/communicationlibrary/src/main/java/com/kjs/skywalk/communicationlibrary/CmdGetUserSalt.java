package com.kjs.skywalk.communicationlibrary;

//import CommunicationBase;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/18.
 */

class CmdGetUserSalt extends CommunicationBase {

    CmdGetUserSalt(Context context, String user) {
        super(context, CommunicationInterface.CmdID.CMD_GET_USER_SALT);
        mNeedLogin = false;
        mArgs = new ArgsUserName(user);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/salt";
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("un=" + ((ArgsUserName)mArgs).getUserName());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetUserSalt result = new ResGetUserSalt(nErrCode, jObject);
        return result;
    }
}

////////////////////////////////////////////////////////////////////////////////////////////
//
class ArgsUserName extends ApiArgsBase implements IApiArgs.IArgsUserName {
    private String mUserName = "";  // login user name, should be cell phone number

    ArgsUserName(String name) {
        mUserName = name;
    }

    @Override
    public String getUserName() {
        return mUserName;
    }

    @Override
    public boolean checkArgs() {
        String Fn = "[checkArgs] ";
        if (null == mUserName || mUserName.isEmpty()) {
            Log.e(TAG, Fn + "Invalid user name:" + mUserName);
            return false;
        }
        return true;
    }

    @Override
    public boolean isEqual(IApiArgs.IArgsBase arg2) {
        if (!super.isEqual(arg2)) {
            return false;
        }
        if (!mUserName.equals(((ArgsUserName)arg2).mUserName)) {
            return false;
        }
        return true;
    }
}
