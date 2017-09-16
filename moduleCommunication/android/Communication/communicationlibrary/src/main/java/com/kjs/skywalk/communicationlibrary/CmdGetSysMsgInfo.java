package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/16.
 */

class CmdGetSysMsgInfo extends CommunicationBase {
    private int mMsgId = 0;

    CmdGetSysMsgInfo(Context context, int msgId) {
        super(context, CommunicationInterface.CmdID.CMD_GET_MSG_INFO);
        mMsgId = msgId;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/sysmsg/"+ mMsgId;
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mMsgId <= 0) {
            Log.e(TAG, "message:" + mMsgId);
            return false;
        }
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetSysMsgInfo result = new ResGetSysMsgInfo(nErrCode, jObject);
        return result;
    }
}
