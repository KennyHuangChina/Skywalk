package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/16.
 */

class CmdNewMsgRead extends CommunicationBase {

    CmdNewMsgRead(Context context, int msgId) {
        super(context, CommunicationInterface.CmdID.CMD_READ_NEW_MSG);
        mMethodType = "PUT";
        mArgs = new ApiArgsObjId(msgId);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/sysmsg/%d/read", ((ApiArgsObjId)mArgs).getId());
        return mCommandURL;
    }
}
