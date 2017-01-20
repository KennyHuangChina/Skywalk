package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/20.
 */

class CommunicationBase {
    protected Context mContext = null;
    protected String mMethodType = "";
    protected String mServerURL = "";
    protected String mCommandURL = "";

    protected MyUtils mUtils = null;

    CommunicationBase(Context context) {
        Log.i(InternalDefines.TAG_COMMUNICATION_BASE, "Constructor");
        mContext = context;

        mUtils = new MyUtils(context);
    }

    public interface CheckParameter {
        boolean checkParameter(HashMap<String, String> map);
    }
}
