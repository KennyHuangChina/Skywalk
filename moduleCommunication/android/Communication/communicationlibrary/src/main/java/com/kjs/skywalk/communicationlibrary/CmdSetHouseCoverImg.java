package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/11.
 */

class CmdSetHouseCoverImg extends CommunicationBase {

    CmdSetHouseCoverImg(Context context, int house, int image) {
        super(context, CommunicationInterface.CmdID.CMD_GET_SET_HOUSE_COVER_IMAGE);
        mMethodType = "PUT";
        mArgs = new Args(house, image);
    }


    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/covimg/" + ((Args)mArgs).getId();
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("img=" + ((Args)mArgs).getImageId());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsSetHouseCoverImage {
        private int mCoverImg = 0;

        Args(int house, int image) {
            super(house);
            mCoverImg = image;
        }

        @Override
        public boolean checkArgs() {
            if (!super.checkArgs()) {
                return false;
            }
            if (mCoverImg <= 0) {
                Log.e(TAG, "[checkArgs] mCoverImg:" + mCoverImg);
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (mCoverImg != ((Args)arg2).mCoverImg) {
                return false;
            }
            return true;
        }

        @Override
        public int getImageId() {
            return mCoverImg;
        }
    }
}
