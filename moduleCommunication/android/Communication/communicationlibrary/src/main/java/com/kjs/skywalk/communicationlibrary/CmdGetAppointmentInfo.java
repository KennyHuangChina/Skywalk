package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/9/24.
 */

class CmdGetAppointmentInfo extends CommunicationBase {
    private int mAppointment = 0;

    CmdGetAppointmentInfo(Context context, int apid) {
        super(context, CommunicationInterface.CmdID.CMD_GET_APPOINTMENT_INFO);
        mAppointment = apid;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/appointment/%d", mAppointment);
        return mCommandURL;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mAppointment <= 0) {
            Log.e(TAG, String.format("Invalid appointment: %d", mAppointment));
            return false;
        }
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return super.doParseResult(nErrCode, jObject);
    }
}
