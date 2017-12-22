package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/9/26.
 */

class CmdAssignAppointmentReceptionist extends CommunicationBase {
    private int mAppointment    = 0;
    private int mReceptionist   = 0;

    CmdAssignAppointmentReceptionist(Context context, int apid, int rectptionist) {
        super(context, CommunicationInterface.CmdID.CMD_ASSIGN_APPOINTMENT_RECEPTIONIST);
        mAppointment = apid;
        mReceptionist = rectptionist;
        mMethodType = "PUT";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/appointment/%d/recpt", mAppointment);
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("r=" + mReceptionist);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mAppointment <= 0) {
            Log.e(TAG, String.format("Invalid appointment id:%d", mAppointment));
            return false;
        }
        if (mReceptionist <= 0) {
            Log.e(TAG, String.format("Invalid receptionist:%d", mReceptionist));
            return false;
        }
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return new ResBase(nErrCode, jObject);
    }
}
