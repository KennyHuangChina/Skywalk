package com.kjs.skywalk.communicationlibrary;

import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/22.
 */

class ResBase implements IApiResults.ICommon {
    int  mErrCode = -1; // Unknown
    String mErrDesc = "";
    String mString = "";

    @Override
    public String DebugString() {
        mString += ("mErrCode: " + mErrCode + "\n");
        mString += ("mErrDesc: " + mErrDesc + "\n");
        return mString;
    }

    protected int parse(JSONObject obj) {
        return 0;
    }
}
