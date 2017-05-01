package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/1.
 */

class CmdModifyFacilityType extends CommunicationBase {
    private int mTypeId = -1;
    private String mName = "";

    CmdModifyFacilityType(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_EDIT_FACILITY_TYPE);
        TAG = "CmdModifyFacilityType";
        mMethodType = "PUT";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/facility/type/" + mTypeId;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("name=" + mName);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_NAME) ||
                !map.containsKey(CommunicationParameterKey.CPK_INDEX)) {
            return false;
        }

        try {
            mTypeId = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        mName = map.get(CommunicationParameterKey.CPK_NAME);
        if (mName.length() == 0) {
            Log.e(TAG, "mName: " + mName);
            return false;
        }
        mName = String2Base64(mName);
        Log.d(TAG, "mName: " + mName);

        return true;
    }
}
