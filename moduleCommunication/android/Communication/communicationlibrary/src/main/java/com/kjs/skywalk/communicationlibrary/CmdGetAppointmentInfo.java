package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/9/24.
 */

class CmdGetAppointmentInfo extends CommunicationBase {

    CmdGetAppointmentInfo(Context context, int apid) {
        super(context, CommunicationInterface.CmdID.CMD_GET_APPOINTMENT_INFO);
        mArgs = new ApiArgsObjId(apid);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/appointment/%d", ((ApiArgsObjId)mArgs).getId());
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return new ResGetAppointmentInfo(nErrCode, jObject);
    }
}
