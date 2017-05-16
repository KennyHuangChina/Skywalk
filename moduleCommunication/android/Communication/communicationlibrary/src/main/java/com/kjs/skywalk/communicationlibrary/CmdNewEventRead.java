package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/16.
 */

class CmdNewEventRead extends CommunicationBase {
    private int mEventId = 0;

    CmdNewEventRead(Context context, int eventId) {
        super(context, CommunicationInterface.CmdID.CMD_READ_NEW_EVENT);
        TAG = "CmdNewEventRead";
        mMethodType = "PUT";
        mEventId = eventId;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/event/"+ mEventId + "/read";
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
}
