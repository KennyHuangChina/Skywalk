package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/16.
 */

class CmdNewMsgRead extends CommunicationBase {
    private int mMsgId = 0;

    CmdNewMsgRead(Context context, int msgId) {
        super(context, CommunicationInterface.CmdID.CMD_READ_NEW_MSG);
        mMethodType = "PUT";
        mMsgId = msgId;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/sysmsg/"+ mMsgId + "/read";
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mMsgId <= 0) {
            Log.e(TAG, "msg:" + mMsgId);
            return false;
        }
        return true;
    }
}
