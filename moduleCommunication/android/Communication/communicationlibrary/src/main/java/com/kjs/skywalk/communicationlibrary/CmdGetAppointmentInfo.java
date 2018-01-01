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
        mArgs = new Args(apid);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/appointment/%d", ((Args)mArgs).getAppointment());
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return new ResGetAppointmentInfo(nErrCode, jObject);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsGetAppointmentInfo {
        private int mAppointment = 0;

        Args(int aid) {
            mAppointment = aid;
        }

        @Override
        public boolean checkArgs() {
            if (mAppointment <= 0) {
                Log.e(TAG, String.format("Invalid appointment: %d", mAppointment));
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (mAppointment != ((Args)arg2).mAppointment) {
                return false;
            }
            return true;
        }

        @Override
        public int getAppointment() {
            return mAppointment;
        }
    }
}
