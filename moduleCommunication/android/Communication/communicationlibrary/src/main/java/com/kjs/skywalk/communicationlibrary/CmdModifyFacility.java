package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/1.
 */

class CmdModifyFacility extends CommunicationBase {
    private int     mFacilityId = -1;
    private int     mTypeId     = -1;
    private String  mName       = "";

    CmdModifyFacility(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_EDIT_FACILITY);
        mMethodType = "PUT";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/facility/" + mFacilityId;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("name=" + mName);
        mRequestData += ("&type=" + mTypeId);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_NAME) ||
                !map.containsKey(CommunicationParameterKey.CPK_TYPE) ||
                !map.containsKey(CommunicationParameterKey.CPK_INDEX)) {
            return false;
        }

        // Facility id & type
        try {
            mFacilityId = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
            mTypeId = Integer.parseInt(map.get(CommunicationParameterKey.CPK_TYPE));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        // Facility name
        mName = map.get(CommunicationParameterKey.CPK_NAME);
        if (mName.length() == 0) {
            Log.e(TAG, "mName: " + mName);
            return false;
        }
        mName = String2Base64(mName);
        Log.d(TAG, "mName: " + mName);

        // Facility ICON
        if (map.containsKey(CommunicationParameterKey.CPK_IMG_FILE)) {
            String picFile = map.get(CommunicationParameterKey.CPK_IMG_FILE);
            if (picFile.length() == 0) {
                Log.e(TAG, "picture file not assigned");
                return false;
            }
            if (!CUtilities.isPicture(picFile)) {
                Log.e(TAG, "picture file not exist");
                return false;
            }
            mFile = picFile;
        }

        return true;
    }
}
