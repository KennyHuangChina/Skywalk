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
        mCommandURL = String.format("/v1/appointment/%d/recpt", ((Args)mArgs).getId());
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
    class Args extends ApiArgsObjId implements IApiArgs.IArgsAssignAppointmentReceptionist {
        private int mReceptionist   = 0;

        Args(int apid, int receptionist) {
            super(apid);
            mReceptionist = receptionist;
        }

        @Override
        public boolean checkArgs() {
            if (!super.checkArgs()) {
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
            return super.isEqual(arg2) && mReceptionist != ((Args)arg2).mReceptionist;
        }

        @Override
        public int getReceptionist() {
            return mReceptionist;
        }
    }
}
