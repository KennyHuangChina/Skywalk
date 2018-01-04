package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/11.
 */

class CmdModifyPropertyInfo extends CommunicationBase {

    CmdModifyPropertyInfo(Context context, int pid, String name, String addr, String desc) {
        super(context, CommunicationInterface.CmdID.CMD_MODIFY_PROPERTY);
        mMethodType = "PUT";
        mArgs = new Args(pid, name, addr, desc);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/property/" + ((Args)mArgs).getPropertyId() + "/info";
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("prop=" + ((Args)mArgs).getName());
        mRequestData += "&";
        mRequestData += ("addr=" + ((Args)mArgs).getAddress());
        mRequestData += "&";
        mRequestData += ("desc=" + ((Args)mArgs).getDesc());

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgPropertyInfo implements IApiArgs.IArgsModifyProperty {
        private int mPropId = 0;

        Args(int pid, String name, String addr, String desc) {
            super(name, addr, desc);
        }

        @Override
        public boolean checkArgs() {
            if (mPropId <= 0) {
                Log.e(TAG, "[checkArgs] mPropId:" + mPropId);
                return false;
            }
            return super.checkArgs();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (mPropId != ((Args)arg2).mPropId) {
                return false;
            }
            return true;
        }

        @Override
        public int getPropertyId() {
            return mPropId;
        }
    }
}
