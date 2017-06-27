package com.kjs.skywalk.communicationlibrary;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/24.
 */

class CommandTest extends CommunicationBase {

    private String mUserName = "";
    private String mPassword = "";
    private String mRadom = "";
    private String mType = "1";

    CommandTest(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_TEST);
        mMethodType = "POST";
    }
    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/test";
        return mCommandURL;
    }
}
