package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/8/6.
 */

class ResResetPass extends ResBase implements IApiResults.IResetPassword {
    private String mUser = null;

    ResResetPass(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();

        mString += "  user: " + GetUser() + "\n";

        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        try {
            mUser = obj.getString("User");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public String GetUser() {
        return mUser;
    }
}
