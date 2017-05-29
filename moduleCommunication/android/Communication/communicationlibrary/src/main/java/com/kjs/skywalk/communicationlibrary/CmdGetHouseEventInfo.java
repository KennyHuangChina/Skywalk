package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/16.
 */

class CmdGetHouseEventInfo extends CommunicationBase {
    private int mEventId = 0;

    CmdGetHouseEventInfo(Context context, int eventId) {
        super(context, CommunicationInterface.CmdID.CMD_GET_EVENT_INFO);
        mMethodType = "GET";
        mEventId = eventId;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/event/"+ mEventId;
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mEventId <= 0) {
            Log.e(TAG, "event:" + mEventId);
            return false;
        }
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetEventInfo result = new ResGetEventInfo(nErrCode, jObject);
        return result;
    }
}
