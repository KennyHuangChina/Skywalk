package com.kjs.skywalk.communicationlibrary;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/5.
 */

class CmdCertificateHouse extends CommunicationBase {

    CmdCertificateHouse(Context context, int house, boolean pass, String comment) {
        super(context, CommunicationInterface.CmdID.CMD_CERTIFY_HOUSE);
        mMethodType = "POST";
        mArgs = new Args(house, pass, comment);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/cert/" + ((Args)mArgs).getId();
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("cc=" + ((Args)mArgs).getComments());
        mRequestData += "&";
        mRequestData += ("ps=" + ((Args)mArgs).passed());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsCertifyHouse {
        private boolean mPass           = false;
        private String  mCertComment    = "";

        Args(int house, boolean pass, String comment) {
            super(house);
            mPass           = pass;
            mCertComment    = comment;

            if (null != mCertComment && !mCertComment.isEmpty()) {
                mCertComment = String2Base64(mCertComment);
            }
        }

        @Override
        public boolean checkArgs() {
            String Fn = "[checkArgs] ";
            if (!super.checkArgs()) {
                return false;
            }
            if (null == mCertComment || mCertComment.isEmpty()) {
                Log.e(TAG, Fn + "Comment not set ");
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
            if (mPass != ac.mPass || !mCertComment.equals(ac.mCertComment)) {
                return false;
            }
            return true;
        }

        @Override
        public boolean passed() {
            return mPass;
        }

        @Override
        public String getComments() {
            return mCertComment;
        }
    }
}
