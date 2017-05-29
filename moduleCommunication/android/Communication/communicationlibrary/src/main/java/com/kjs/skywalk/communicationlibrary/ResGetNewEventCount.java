package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/5/14.
 */

class ResGetNewEventCount extends ResBase implements IApiResults.INewEventCount {
    private int mNewEventCount = 0;

    ResGetNewEventCount(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();

        mString += "  new event: " + mNewEventCount + "\n";
        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        try {
            mNewEventCount = obj.getInt("NewEvent");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public int GetNewEventCount() {
        return mNewEventCount;
    }
}
