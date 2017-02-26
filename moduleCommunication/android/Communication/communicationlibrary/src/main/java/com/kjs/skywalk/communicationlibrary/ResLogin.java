package com.kjs.skywalk.communicationlibrary;

//import ResBase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/24.
 */

public class ResLogin extends ResBase implements IApiResults.ILogin {
    private String mSession = "";

    ResLogin(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    protected int parseResult(JSONObject obj) {
        try {
            mSession = obj.getString("Sid");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    @Override
    public String GetSession() {
        return mSession;
    }

    @Override
    public String DebugString() {
        super.DebugString();

        mString += "  session: " + mSession + "\n";
        return mString;
    }
}
