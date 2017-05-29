package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/21.
 */

class CmdModifyHouseEvent extends CommunicationBase {
    private int     mEventId    = 0;
    private String  mEventDesc  = "";

    CmdModifyHouseEvent(Context context, int eventId) {
        super(context, CommunicationInterface.CmdID.CMD_MODIFY_HOUSE_EVENT);
        mMethodType = "PUT";
        mEventId = eventId;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/event/" + mEventId;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("desc=" + mEventDesc);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mEventId <= 0) {
            Log.d(TAG, "mEventId: " + mEventId);
            return false;
        }

        if (!map.containsKey(CommunicationParameterKey.CPK_DESC)) {
            return false;
        }

        mEventDesc = map.get(CommunicationParameterKey.CPK_DESC);
        if (mEventDesc.isEmpty()) {
            Log.d(TAG, "event description not set");
            return false;
        }
        mEventDesc = String2Base64(mEventDesc);
        Log.d(TAG, "mEventDesc: " + mEventDesc);

        return true;
    }
}
