package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CIProgressListener;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CICommandListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Jackie on 2017/1/20.
 */

class CmdGetBriefPublicHouseInfo extends CommunicationBase {

    private String mParamID = "";

    CmdGetBriefPublicHouseInfo(Context context, String strAPI){
        super(context, strAPI);
        TAG = "GetBriefPublicHouseInfo";
        Log.i(TAG, "Constructor");
        mMethodType = "GET";
    }

    private void generateRequestData() {
        mRequestData = "";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if(!map.containsKey(CommunicationParameterKey.CPK_INDEX)) {
             return false;
        }

        mParamID = map.get(CommunicationParameterKey.CPK_INDEX);
        if(mParamID == null || mParamID.isEmpty()) {
            return false;
        }

       return true;
    }

    @Override
    public int doOperation(HashMap<String, String> map, CICommandListener commandListener, CIProgressListener progressListener) {
        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/house/digest";
        mCommandURL += "/" + mParamID;

        generateRequestData();

        super.doOperation(map, commandListener, progressListener);

        Log.i(TAG, "doOperation ... out");

        return CommunicationError.CE_ERROR_NO_ERROR;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResHousePublicBriefInfo result = new ResHousePublicBriefInfo(nErrCode, jObject);
        return result;
    }
}
