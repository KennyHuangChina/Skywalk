package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

//import CommunicationBase;

class CmdGetPropertyList extends CommunicationBase {

    private String mPropertyName = "";
    private int mBeginPosi = 0;
    private int mFetchCount = 0;

    CmdGetPropertyList(Context context) {
        super(context);
        TAG = "CmdGetPropertyList";
//        Log.i(TAG, "Constructor");
        mAPI = CommunicationCommand.CC_GET_PROPERTY_LIST;
        mMethodType = "GET";
    }

    private void generateRequestData() {
        mRequestData = ("name=" + mPropertyName);
        mRequestData += "&";
        mRequestData += ("bgn=" + mBeginPosi);
        mRequestData += "&";
        mRequestData += ("cnt=" + mFetchCount);
        Log.d(TAG, "mRequestData: " + mPropertyName);
    }

    @Override
    public int doOperation(HashMap<String, String> map,
                           CommunicationInterface.CICommandListener commandListener,
                           CommunicationInterface.CIProgressListener progressListener) {
//        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/house/property/list";

        generateRequestData();
        super.doOperation(map, commandListener, progressListener);

//        Log.i(TAG, "doOperation ... out");
        return CommunicationError.CE_ERROR_NO_ERROR;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
//        return super.checkParameter(map);
        if (!map.containsKey(CommunicationParameterKey.CPK_PROPERTY_NAME)) {
            return false;
        }

        mPropertyName = map.get(CommunicationParameterKey.CPK_PROPERTY_NAME);
        if (null == mPropertyName /*|| mPropertyName.isEmpty()*/) {
            return false;
        }
        String strBegin = map.get(CommunicationParameterKey.CPK_LIST_BEGIN);
        if (null != strBegin) {
            mBeginPosi = Integer.parseInt(strBegin);
            if (mBeginPosi < 0) {
                mBeginPosi = 0;
            }
        }
        String strFetchCnt = map.get(CommunicationParameterKey.CPK_LIST_CNT);
        if (null != strFetchCnt) {
            mFetchCount = Integer.parseInt(strFetchCnt);
            if (mFetchCount < 0) {
                mFetchCount = 0;
            }
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(JSONObject jObject) {
        ResGetPropertyList result = new ResGetPropertyList(jObject);
        return result;
    }

    @Override
    public int doBeforeConnect(HttpConnector http) {
        return super.doBeforeConnect(http);
    }

    @Override
    public int doAfterConnect(HttpConnector http) {
        return super.doAfterConnect(http);
    }

    @Override
    public int doConnectFailed(HttpConnector http) {
        return super.doConnectFailed(http);
    }
}
