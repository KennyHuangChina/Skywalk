package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/24.
 */

class CommandTest extends CommunicationBase {

    private String mUserName = "";
    private String mPassword = "";
    private String mRadom = "";
    private String mType = "1";

    CommandTest(Context context) {
        super(context);
        TAG = "CommandTest";
        Log.i(TAG, "Constructor");
        mMethodType = "POST";
    }
    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true;
    }

    @Override
    public HashMap<String, String> doCreateResultMap(JSONObject jObject) {
        return null;
    }

    @Override
    public int doBeforeConnect(HttpConnector http) {
        http.setReadCookie(false);
        http.setWriteCookie(true);
        return 0;
    }

    @Override
    public int doOperation(HashMap<String, String> map, CommunicationInterface.CICommandListener commandListener, CommunicationInterface.CIProgressListener progressListener) {
        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/admin/test";

        generateRequestData();

        super.doOperation(map, commandListener, progressListener);

        Log.i(TAG, "doOperation ... out");

        return CommunicationError.CE_ERROR_NO_ERROR;
    }

    private void generateRequestData() {

    }

}
