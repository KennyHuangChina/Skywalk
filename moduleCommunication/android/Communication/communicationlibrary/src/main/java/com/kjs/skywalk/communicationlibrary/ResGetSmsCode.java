package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/27.
 */

class ResGetSmsCode extends ResBase implements IApiResults.IGetSmsCode {
    private String mSmsCode = "";

    ResGetSmsCode(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();
        mString += "  SMS code: " + mSmsCode + "\n";
        return mString;
    }

    protected int parseResult(JSONObject obj) {
        try {
            mSmsCode = obj.getString("SmsCode");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public String GetSmsCode() {
        return mSmsCode;
    }
}
