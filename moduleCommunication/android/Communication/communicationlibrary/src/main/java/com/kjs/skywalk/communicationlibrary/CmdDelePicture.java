package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/12.
 */

class CmdDelePicture extends CommunicationBase {

    CmdDelePicture(Context context, int picId) {
        super(context, CommunicationInterface.CmdID.CMD_DEL_PICTURE);
        mMethodType = "DELETE";
        mArgs = new Args(picId);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/pic/" + ((Args)mArgs).getPicId();
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsDelePic {
        private int mPicId = 0;

        Args(int pic) {
            mPicId = pic;
        }

        @Override
        public boolean checkArgs() {
            return true;    // super.checkArgs();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (mPicId != ((Args)arg2).mPicId) {
                return false;
            }
            return true;
        }

        @Override
        public int getPicId() {
            return mPicId;
        }
    }
}
