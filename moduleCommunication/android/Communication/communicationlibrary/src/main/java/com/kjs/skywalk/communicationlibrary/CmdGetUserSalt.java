package com.kjs.skywalk.communicationlibrary;

//import CommunicationBase;
import android.content.Context;
import android.util.Log;
import org.json.JSONObject;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/18.
 */

class CmdGetUserSalt extends CommunicationBase {

    private String mUserName = "";

    CmdGetUserSalt(Context context) {
        super(context);
        TAG = "CmdGetUserSalt";
//        Log.i(TAG, "Constructor");
        mAPI = CommunicationCommand.CC_GET_USER_SALT;
        mMethodType = "GET";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map)
    {
        if (!map.containsKey(CommunicationParameterKey.CPK_USER_NAME)) {
            return false;
        }

        mUserName = map.get(CommunicationParameterKey.CPK_USER_NAME);
        if (null == mUserName || mUserName.isEmpty()) {
            return false;
        }
        return true;
    }

    private void generateRequestData() {
        mRequestData = ("un=" + mUserName);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(JSONObject jObject) {
        ResGetUserSalt result = new ResGetUserSalt(jObject);
        return result;
    }

    @Override
    public int doOperation(HashMap<String, String> map,
                           CommunicationInterface.CICommandListener commandListener,
                           CommunicationInterface.CIProgressListener progressListener)
    {
//        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/admin/salt";

        generateRequestData();
        super.doOperation(map, commandListener, progressListener);

//        Log.i(TAG, "doOperation ... out");
        return CommunicationError.CE_ERROR_NO_ERROR;
    }
}
