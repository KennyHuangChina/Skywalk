package com.kjs.skywalk.communicationlibrary;

//import CommunicationBase;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/27.
 */

/////////////////////////////////////////////////////////////////////////////////////////////
//
//
class CmdGetSmsCode extends CommunicationBase {

    CmdGetSmsCode(Context context, String user) {
        super(context, CommunicationInterface.CmdID.CMD_GET_SMS_CODE);
        mNeedLogin = false;
        mArgs = new ApiArgsGetSmsCode(user);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/fetchsms";
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("ln=" + ((ApiArgsGetSmsCode)mArgs).getUserName());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetSmsCode result = new ResGetSmsCode(nErrCode, jObject);
        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    class ApiArgsGetSmsCode extends ApiArgsBase implements IApiArgs.IArgsGetSmsCode {
        private String mUserName = "";

        public ApiArgsGetSmsCode(String user) {
            mUserName = user;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            ApiArgsGetSmsCode args2chk = (ApiArgsGetSmsCode)arg2;
            if (null != mUserName && null != args2chk.mUserName && mUserName.equals(args2chk.mUserName)) {
                return true;
            }
            return false;
        }

        @Override
        public boolean checkArgs() {
            if (null == mUserName || mUserName.isEmpty()) {
                return false;
            }
            return true;
        }

        @Override
        public String getUserName() {
            return mUserName;
        }
    }
}
