package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/18.
 */

class CmdGetHouseEventProcList extends CommunicationBase {
//    private int mEventId = 0;

    CmdGetHouseEventProcList(Context context, int eventId) {
        super(context, CommunicationInterface.CmdID.CMD_GET_EVENT_PROC_LST);
        mArgs = new ApiArgsObjId(eventId);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/event/" + ((ApiArgsObjId)mArgs).getId() + "/proc";
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetEventProcList result = new ResGetEventProcList(nErrCode, jObject);
        return result;
    }
}
