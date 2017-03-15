package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/5.
 */

class CmdCertificateHouse extends CommunicationBase {
    private int     mHouseId        = 0;
    private boolean mPass           = false;
    private String  mCertComment    = "";

    CmdCertificateHouse(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_CERTIFY_HOUSE);
        TAG = "CmdCertificateHouse";
        mMethodType = "POST";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX) ||
                !map.containsKey(CommunicationParameterKey.CPK_HOUSE_CERT_COMMENT) ||
                !map.containsKey(CommunicationParameterKey.CPK_HOUSE_CERT_PASS) ) {
            return false;
        }

        try {
            mHouseId = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
            mCertComment = String2Base64(map.get(CommunicationParameterKey.CPK_HOUSE_CERT_COMMENT));
            mPass = Boolean.parseBoolean(map.get(CommunicationParameterKey.CPK_HOUSE_CERT_PASS));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/cert/" + mHouseId;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("cc=" + mCertComment);
        mRequestData += "&";
        mRequestData += ("ps=" +  mPass);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
