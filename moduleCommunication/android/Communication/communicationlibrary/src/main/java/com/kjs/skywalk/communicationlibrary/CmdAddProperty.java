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
        super(context);
        TAG = "CmdLoginBySms";
//        Log.i(TAG, "Constructor");
        mAPI = CommunicationCommand.CC_GET_ADD_PROPERTY;
        mMethodType = "POST";
    }

    @Override
    public int doOperation(HashMap<String, String> map,
                           CommunicationInterface.CICommandListener commandListener,
                           CommunicationInterface.CIProgressListener progressListener) {
        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/house/property";
//        mCommandURL += "/";

        generateRequestData();

        super.doOperation(map, commandListener, progressListener);

        Log.i(TAG, "doOperation ... out");

        return CommunicationError.CE_ERROR_NO_ERROR;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_PROPERTY_NAME)) {
            return false;
        }

        mPropName = map.get(CommunicationParameterKey.CPK_PROPERTY_NAME);

        if (map.containsKey(CommunicationParameterKey.CPK_PROPERTY_ADDR)) {
            mPropAddr = map.get(CommunicationParameterKey.CPK_PROPERTY_ADDR);
        }

        if (map.containsKey(CommunicationParameterKey.CPK_PROPERTY_DESC)) {
            mPropDesc = map.get(CommunicationParameterKey.CPK_PROPERTY_DESC);
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResAddProperty result = new ResAddProperty(nErrCode, jObject);
        return result;
    }

    private void generateRequestData() {
        mRequestData = ("prop=" +  mPropName);
        mRequestData += "&";
        mRequestData += ("addr=" +  mPropAddr);
        mRequestData += "&";
        mRequestData += ("desc=" +  mPropDesc);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
