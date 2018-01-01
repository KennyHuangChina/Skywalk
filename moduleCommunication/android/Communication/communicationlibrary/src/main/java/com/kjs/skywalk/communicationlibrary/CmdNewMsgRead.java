package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/16.
 */

class CmdNewMsgRead extends CommunicationBase {

    CmdNewMsgRead(Context context, int msgId) {
        super(context, CommunicationInterface.CmdID.CMD_READ_NEW_MSG);
        mMethodType = "PUT";
        mArgs = new Args(msgId);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/sysmsg/%d/read", ((Args)mArgs).getMsgId());
        return mCommandURL;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsReadMessage {
        private int mMsgId = 0;

        Args(int msg) {
            mMsgId = msg;
        }

        @Override
        public int getMsgId() {
            return mMsgId;
        }

        @Override
        public boolean checkArgs() {
            if (mMsgId <= 0) {
                Log.e(TAG, "msg:" + mMsgId);
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }

            if (mMsgId !=((Args)arg2).mMsgId) {
                return false;
            }
            return true;
        }
    }
}
