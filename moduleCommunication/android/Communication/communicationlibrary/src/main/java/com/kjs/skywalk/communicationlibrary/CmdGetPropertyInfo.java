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
        super(context, CommunicationInterface.CmdID.CMD_GET_PROPERTY_INFO);
        mMethodType = "GET";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/property/" + mPropertyId;
        return mCommandURL;
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
