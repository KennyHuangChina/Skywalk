package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import org.json.JSONObject;
import java.util.HashMap;
//import CommunicationBase;

/**
 * Created by kenny on 2017/3/2.
 */

class CmdGetPropertyInfo extends CommunicationBase {

    private int mPropertyId = 0;    // property id

    CmdGetPropertyInfo(Context context) {
        super(context);
        TAG = "CmdGetPropertyInfo";
//        Log.i(TAG, "Constructor");
        mAPI = CommunicationCommand.CC_GET_GET_PROPERTY_INFO;
        mMethodType = "GET";
    }

    @Override
    public int doOperation(HashMap<String, String> map,
                           CommunicationInterface.CICommandListener commandListener,
                           CommunicationInterface.CIProgressListener progressListener) {
//        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/house/property/" + mPropertyId;
        super.doOperation(map, commandListener, progressListener);

//        Log.i(TAG, "doOperation ... out");
        return CommunicationError.CE_ERROR_NO_ERROR;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX)) {
            return false;
        }

        String pid = map.get(CommunicationParameterKey.CPK_INDEX);
        if (null == pid || pid.isEmpty()) {
            return false;
        }
        try {
            mPropertyId = Integer.parseInt(pid);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetPropertyInfo result = new ResGetPropertyInfo(nErrCode, jObject);
        return result;
    }
}
