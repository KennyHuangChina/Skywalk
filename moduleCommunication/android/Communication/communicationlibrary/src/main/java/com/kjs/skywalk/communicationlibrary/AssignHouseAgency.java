package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/12/16.
 */

class AssignHouseAgency extends CommunicationBase {
    protected int mHouse = 0;
    protected int mAgent = 0;

    AssignHouseAgency(Context context, int house, int agent) {
        super(context, CommunicationInterface.CmdID.CMD_ASSIGN_HOUSE_AGENT);
        mMethodType = "PUT";
        mHouse = house;
        mAgent = agent;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/assignagency", mHouse);
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("aid=" + mAgent);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mHouse <= 0) {
            Log.e(TAG, String.format("Invalid house: %d", mHouse));
            return false;
        }
        if (mAgent <= 0) {
            Log.e(TAG, String.format("Invalid agent: %d", mAgent));
            return false;
        }
        return true;
    }
}
