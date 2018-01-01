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
        mArgs = new Args(user);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/salt";
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("un=" + ((Args)mArgs).getUserName());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetUserSalt result = new ResGetUserSalt(nErrCode, jObject);
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsGetUserSalt {
        private String mUserName = "";

        Args(String name) {
            mUserName = name;
        }

        @Override
        public String getUserName() {
            return mUserName;
        }

        @Override
        public boolean checkArgs() {
            if (null == mUserName || mUserName.isEmpty()) {
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (!mUserName.equals(((Args)arg2).mUserName)) {
                return false;
            }
            return true;
        }
    }
}
