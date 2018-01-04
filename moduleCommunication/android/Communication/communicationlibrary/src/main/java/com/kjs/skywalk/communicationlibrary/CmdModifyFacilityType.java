package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/1.
 */

class CmdModifyFacilityType extends CommunicationBase {

    CmdModifyFacilityType(Context context, int type, String name) {
        super(context, CommunicationInterface.CmdID.CMD_EDIT_FACILITY_TYPE);
        mMethodType = "PUT";
        mArgs = new Args(type, name);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/facility/type/" + ((Args)mArgs).getId();
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("name=" + ((Args)mArgs).getName());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsEditFacilityType {
        private String mName = null;

        Args(int type, String name) {
            super(type);
        }

        @Override
        public boolean checkArgs() {
            if (null == mName || mName.isEmpty()) {
                Log.e(TAG, "[checkArgs] mName:" + mName);
                return false;
            }
            mName = String2Base64(mName);
            return super.checkArgs();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (!mName.equals(((Args)arg2).mName)) {
                return false;
            }
            return true;
        }

        @Override
        public String getName() {
            return mName;
        }
    }
}
