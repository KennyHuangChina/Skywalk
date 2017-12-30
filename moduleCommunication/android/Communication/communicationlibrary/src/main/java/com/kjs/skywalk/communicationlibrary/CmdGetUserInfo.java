package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/2/27.
 */

class CmdGetUserInfo extends CommunicationBase {

    CmdGetUserInfo(Context context, int user) {
        super(context, CommunicationInterface.CmdID.CMD_GET_USER_INFO);
        mArgs = new Args(user);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/user/" + ((Args)mArgs).getUsrId();
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetUserInfo result = new ResGetUserInfo(nErrCode, jObject);
        return result;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsGetUserInfo {
        private int mUserId = 0;

        Args(int uid) {
            mUserId = uid;
        }

        @Override
        public boolean checkArgs() {
            String Fn = "[checkArgs] ";
            if (mUserId <= 0) {
                Log.e(TAG, Fn + "Invalid user id:" + mUserId);
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (mUserId != ((Args)arg2).getUsrId()) {
                return false;
            }
            return true;
        }

        @Override
        public int getUsrId() {
            return mUserId;
        }
    }
}
