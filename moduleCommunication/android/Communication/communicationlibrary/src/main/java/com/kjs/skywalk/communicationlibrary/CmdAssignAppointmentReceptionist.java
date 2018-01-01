package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/9/26.
 */

class CmdAssignAppointmentReceptionist extends CommunicationBase {

    CmdAssignAppointmentReceptionist(Context context, int apid, int rectptionist) {
        super(context, CommunicationInterface.CmdID.CMD_ASSIGN_APPOINTMENT_RECEPTIONIST);
        mMethodType = "PUT";
        mArgs = new Args(apid, rectptionist);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/appointment/%d/recpt", ((Args)mArgs).getAppointment());
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("r=" + ((Args)mArgs).getReceptionist());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return new ResBase(nErrCode, jObject);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsAssignAppointmentReceptionist {
        private int mAppointment    = 0;
        private int mReceptionist   = 0;

        Args(int apid, int receptionist) {
            mAppointment = apid;
            mReceptionist = receptionist;
        }

        @Override
        public boolean checkArgs() {
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
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }

            Args ac = (Args)arg2;
            if (mAppointment != ac.mAppointment || mReceptionist != ac.mReceptionist) {
                return false;
            }
            return true;
        }

        @Override
        public int getAppointment() {
            return mAppointment;
        }

        @Override
        public int getReceptionist() {
            return mReceptionist;
        }
    }
}
