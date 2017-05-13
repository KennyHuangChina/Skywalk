package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/12.
 */

class CmdDelePicture extends CommunicationBase {
    private int mPicId = 0;

    CmdDelePicture(Context context, int picId) {
        super(context, CommunicationInterface.CmdID.CMD_DEL_PICTURE);
        TAG = "CmdDelePicture";
        mMethodType = "DELETE";
        mPicId = picId;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/pic/" + mPicId;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true;
    }
}
