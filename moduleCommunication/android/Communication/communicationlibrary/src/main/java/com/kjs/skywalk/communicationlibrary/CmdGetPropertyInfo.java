package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import org.json.JSONObject;
import java.util.HashMap;
//import CommunicationBase;

/**
 * Created by kenny on 2017/3/2.
 */

class CmdGetPropertyInfo extends CommunicationBase {

    CmdGetPropertyInfo(Context context, int pid) {
        super(context, CommunicationInterface.CmdID.CMD_GET_PROPERTY_INFO);
        mNeedLogin = false;
        mArgs = new ApiArgsObjId(pid);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/property/" + ((ApiArgsObjId)mArgs).getId();
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetPropertyInfo result = new ResGetPropertyInfo(nErrCode, jObject);
        return result;
    }
}
