package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/5/14.
 */

class ResGetNewMsgCount extends ResBase implements IApiResults.INewMsgCount {
    private int mNewMsgCount = 0;

    ResGetNewMsgCount(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();

        mString += "  new event: " + mNewMsgCount + "\n";
        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        try {
            mNewMsgCount = obj.getInt("NewMsgCnt");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public int GetNewMsgCount() {
        return mNewMsgCount;
    }
}
