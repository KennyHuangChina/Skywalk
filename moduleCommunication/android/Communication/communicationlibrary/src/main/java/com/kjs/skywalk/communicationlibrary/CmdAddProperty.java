package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/2.
 */

class CmdAddProperty extends CommunicationBase {
    private String mPropName = "";
    private String mPropAddr = "";
    private String mPropDesc = "";

    CmdAddProperty(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_ADD_PROPERTY);
        mMethodType = "POST";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/property/new";
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

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_PROPERTY_NAME)) {
            return false;
        }

        mPropName = map.get(CommunicationParameterKey.CPK_PROPERTY_NAME);
        Log.d(TAG, "mPropName: " + mPropName);
//        mPropName = getUTF8XMLString(mPropName);
//        Log.d(TAG, "mPropName: " + mPropName);
        mPropName = String2Base64(mPropName);
        Log.d(TAG, "mPropName: " + mPropName);

        if (map.containsKey(CommunicationParameterKey.CPK_PROPERTY_ADDR)) {
            mPropAddr = map.get(CommunicationParameterKey.CPK_PROPERTY_ADDR);
        }
        mPropAddr = String2Base64(mPropAddr);

        if (map.containsKey(CommunicationParameterKey.CPK_PROPERTY_DESC)) {
            mPropDesc = map.get(CommunicationParameterKey.CPK_PROPERTY_DESC);
        }
        mPropDesc = String2Base64(mPropDesc);

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResAddResource result = new ResAddResource(nErrCode, jObject);
        return result;
    }
}

