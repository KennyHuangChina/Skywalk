package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

//import CommunicationBase;

public class CmdGetPropertyList extends CommunicationBase {

    private String mPropertyName = "";

    CmdGetPropertyList(Context context) {
        super(context);
        TAG = "CmdGetPropertyList";
//        Log.i(TAG, "Constructor");
        mAPI = CommunicationCommand.CC_GET_PROPERTY_LIST;
        mMethodType = "GET";
    }

    private void generateRequestData() {
        mRequestData = ("name=" + mPropertyName);
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
        return true;
    }

    @Override
    public ResBase doParseResult(JSONObject jObject) {
        ResGetPropertyList result = new ResGetPropertyList(jObject);
        return result;
    }

    @Override
    public HashMap<String, String> doCreateResultMap(JSONObject jObject) {
//        return super.doCreateResultMap(jObject);
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            map.put("Total", jObject.getString("Total"));
            map.put("Count", jObject.getString("Count"));
            JSONArray list = jObject.getJSONArray("Properties");
            if (null == list) {
                return null;
            }

            for (int n = 0; n < list.length(); n++) {

            }

            map.put("Id", jObject.getString("Id"));
            map.put("PropName", jObject.getString("PropName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return map;
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
