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
        mArgs = new ApiArgsObjId(picId);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/pic/" + ((ApiArgsObjId)mArgs).getId();
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
