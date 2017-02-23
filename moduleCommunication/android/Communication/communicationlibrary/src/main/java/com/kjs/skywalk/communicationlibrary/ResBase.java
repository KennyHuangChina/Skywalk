package com.kjs.skywalk.communicationlibrary;

/**
 * Created by kenny on 2017/2/22.
 */

public class ResBase {
    int  mErrCode = -1; // Unknown
    String mErrDesc = "";
    String mString = "";

    public String toString() {
        mString += ("mErrCode: " + mErrCode + "\n");
        mString += ("mErrDesc: " + mErrDesc + "\n");
        return mString;
    }
}
