package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/11.
 */

class CmdSetHouseCoverImg extends CommunicationBase {
    private int mHouse      = 0;
    private int mCoverImg   = 0;

    CmdSetHouseCoverImg(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_GET_SET_HOUSE_COVER_IMAGE);
        mMethodType = "PUT";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX) ||
                !map.containsKey(CommunicationParameterKey.CPK_HOUSE_COVER_IMG) ) {
            return false;
        }

        try {
            mHouse = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
            mCoverImg = Integer.parseInt(map.get(CommunicationParameterKey.CPK_HOUSE_COVER_IMG));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/covimg/" + mHouse;
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("img=" + mCoverImg);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
