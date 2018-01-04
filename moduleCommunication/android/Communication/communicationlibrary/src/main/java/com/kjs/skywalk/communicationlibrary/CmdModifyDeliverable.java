package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/4/26.
 */

class CmdModifyDeliverable extends CommunicationBase {

    CmdModifyDeliverable(Context context, int did, String name) {
        super(context, CommunicationInterface.CmdID.CMD_EDIT_DELIVERABLE);
        mMethodType = "PUT";
        mArgs = new Args(did, name);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/deliverable/" + ((Args)mArgs).getId();
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("name=" + ((Args)mArgs).getName());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsModifyDeliverable {
        private String mName = null;

        Args(int did, String name) {
            super(did);
            mName = name;
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
