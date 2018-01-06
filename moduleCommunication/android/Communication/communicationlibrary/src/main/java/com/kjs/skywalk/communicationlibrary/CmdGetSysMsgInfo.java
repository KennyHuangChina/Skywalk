package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/16.
 */

class CmdGetSysMsgInfo extends CommunicationBase {
//    private int mMsgId = 0;

    CmdGetSysMsgInfo(Context context, int msgId) {
        super(context, CommunicationInterface.CmdID.CMD_GET_MSG_INFO);
        mArgs = new ApiArgsObjId(msgId);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/sysmsg/"+ ((ApiArgsObjId)mArgs).getId();
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetSysMsgInfo result = new ResGetSysMsgInfo(nErrCode, jObject);
        return result;
    }
}
