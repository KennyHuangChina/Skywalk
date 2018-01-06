package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/21.
 */

class CmdModifyHouseEvent extends CommunicationBase {
//    private int     mEventId    = 0;
//    private String  mEventDesc  = "";

    CmdModifyHouseEvent(Context context, int eventId, String desc) {
        super(context, CommunicationInterface.CmdID.CMD_MODIFY_HOUSE_EVENT);
        mMethodType = "PUT";
        mArgs = new Args(eventId, desc);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/event/" + ((Args)mArgs).getId();
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("desc=" + String2Base64(((Args)mArgs).getEventDesc()));
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsModifyHouseEvent {
        private String  mEventDesc  = "";

        Args(int eid, String desc) {
            super(eid);
            mEventDesc = desc;
        }

        @Override
        public boolean checkArgs() {
            if (!super.checkArgs()) {
                return false;
            }
            if (null == mEventDesc || mEventDesc.isEmpty()){
                Log.e(TAG, "[checkArgs] mEventDesc:" + mEventDesc);
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            return super.isEqual(arg2) && mEventDesc.equals(((Args)arg2).mEventDesc);
        }

        @Override
        public String getEventDesc() {
            return mEventDesc;
        }
    }
}
