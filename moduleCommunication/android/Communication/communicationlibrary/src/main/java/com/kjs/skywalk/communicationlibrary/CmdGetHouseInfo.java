package com.kjs.skywalk.communicationlibrary;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/1.
 */

class CmdGetHouseInfo extends CommunicationBase {
    private int mHouseId;      // house id

    CmdGetHouseInfo(Context context, String strAPI) {
        super(context, strAPI);
        TAG = "CmdGetHouseInfo";
//        Log.i(TAG, "Constructor");
        mMethodType = "GET";
    }

    @Override
    public int doOperation(HashMap<String, String> map,
                           CommunicationInterface.CICommandListener commandListener,
                           CommunicationInterface.CIProgressListener progressListener) {
//        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/house/" + mHouseId;
        super.doOperation(map, commandListener, progressListener);

//        Log.i(TAG, "doOperation ... out");
        return CommunicationError.CE_ERROR_NO_ERROR;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX)) {
            return false;
        }

        String hid = map.get(CommunicationParameterKey.CPK_INDEX);
        if (null == hid || hid.isEmpty()) {
            return false;
        }
        try {
            mHouseId = Integer.parseInt(hid);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseInfo result = new ResGetHouseInfo(nErrCode, jObject);
        return result;
    }
}
