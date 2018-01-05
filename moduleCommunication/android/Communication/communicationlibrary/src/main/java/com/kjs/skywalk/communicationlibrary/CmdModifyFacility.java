package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/1.
 */

class CmdModifyFacility extends CommunicationBase {

    CmdModifyFacility(Context context, int fid, int type, String name, String pic) {
        super(context, CommunicationInterface.CmdID.CMD_EDIT_FACILITY);
        mMethodType = "PUT";
        mArgs = new Args(fid, type, name, pic);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/facility/" + ((Args)mArgs).getFacilityId();
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("name=" + ((Args)mArgs).getName());
        mRequestData += ("&type=" + ((Args)mArgs).getType());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsFacility implements IApiArgs.IArgsEditFacility {
        private int mFacilityId = -1;

        Args(int fid, int type, String name, String pic) {
            super(type, name, pic);
            mFacilityId = fid;
        }

        @Override
        public boolean checkArgs() {
            if (mFacilityId <= 0) {
                Log.e(TAG, "[checkArgs] mFacilityId:" + mFacilityId);
                return false;
            }
            return super.checkArgs();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (mFacilityId != ((Args)arg2).mFacilityId) {
                return false;
            }
            return true;
        }

        @Override
        public int getFacilityId() {
            return mFacilityId;
        }
    }
}
