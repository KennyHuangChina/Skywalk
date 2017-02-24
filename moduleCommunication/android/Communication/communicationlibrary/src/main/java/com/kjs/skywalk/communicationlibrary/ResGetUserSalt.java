package com.kjs.skywalk.communicationlibrary;

//import ResBase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/23.
 */

public class ResGetUserSalt extends ResBase {
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

    public String GetSalt() { return mSalt; }
    public String GetRandom() { return mRandom; }
}
