package com.kjs.skywalk.communicationlibrary;

//import ResBase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/23.
 */

class ResGetUserSalt extends ResBase implements IApiResults.IGetUserSalt {
//    private String mUserName = "";
    private String mSalt = "";
    private String mRandom = "";

    ResGetUserSalt(JSONObject jObject) {
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();

        mString += "  Salt: " + mSalt + "\n";
        mString += "  Random: " + mRandom + "\n";

        return mString;
    }

    protected int parse(JSONObject obj) {
        int nRet = super.parse(obj);
        if (0 != nRet) {
            return nRet;
        }

        try {
            mSalt = obj.getString("Salt");
            mRandom = obj.getString("Random");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public String GetSalt() { return mSalt; }

    @Override
    public String GetRandom() { return mRandom; }
}
