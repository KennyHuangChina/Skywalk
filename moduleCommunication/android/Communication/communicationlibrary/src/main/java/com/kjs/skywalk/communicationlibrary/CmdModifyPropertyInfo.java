package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/11.
 */

class CmdModifyPropertyInfo extends CommunicationBase {
    private int     mPropId     = 0;
    private String  mPropName   = "";
    private String  mPropAddr   = "";
    private String  mPropDesc   = "";

    CmdModifyPropertyInfo(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_MODIFY_PROPERTY);
        TAG = "CmdModifyPropertyInfo";
        mMethodType = "PUT";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX) ||
                !map.containsKey(CommunicationParameterKey.CPK_PROPERTY_NAME) ||
                !map.containsKey(CommunicationParameterKey.CPK_PROPERTY_ADDR) ||
                !map.containsKey(CommunicationParameterKey.CPK_PROPERTY_DESC) ) {
            return false;
        }

        try {
            mPropId = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
            if (mPropId <= 0) {
                Log.e(TAG, "mPropId:" + mPropId);
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        mPropName = map.get(CommunicationParameterKey.CPK_PROPERTY_NAME);
        if (TextUtils.isEmpty(mPropName)) {
            Log.e(TAG, "No name specified");
            return false;
        }
        mPropName = String2Base64(mPropName);

        mPropAddr = map.get(CommunicationParameterKey.CPK_PROPERTY_ADDR);
        if (TextUtils.isEmpty(mPropAddr)) {
            Log.e(TAG, "No address specified");
            return false;
        }
        mPropAddr = String2Base64(mPropAddr);

        mPropDesc = map.get(CommunicationParameterKey.CPK_PROPERTY_DESC);
        if (TextUtils.isEmpty(mPropDesc)) {
            Log.e(TAG, "No description specified");
            return false;
        }
        mPropDesc = String2Base64(mPropDesc);

        return true;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/property/" + mPropId;
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("prop=" + mPropName);
        mRequestData += "&";
        mRequestData += ("addr=" + mPropAddr);
        mRequestData += "&";
        mRequestData += ("desc=" + mPropDesc);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
