package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/22.
 */

class ResBase implements IApiResults.ICommon {
    int  mErrCode = -1; // Unknown
    String mErrDesc = "";
    String mString = "";

    ResBase() {
    }

    ResBase(JSONObject jObject) {
        parse(jObject);
    }

    @Override
    public String DebugString() {
        mString += ("mErrCode: " + mErrCode + "\n");
        mString += ("mErrDesc: " + mErrDesc + "\n");
        return mString;
    }

    protected int parse(JSONObject obj) {
        if (null == obj) {
            return -1;
        }
        try {
            mErrCode = obj.getInt("ErrCode");
            mErrDesc = obj.getString("ErrString");
        } catch (JSONException e) {
            e.printStackTrace();
            return -2;
        }

        return 0;
    }
}
