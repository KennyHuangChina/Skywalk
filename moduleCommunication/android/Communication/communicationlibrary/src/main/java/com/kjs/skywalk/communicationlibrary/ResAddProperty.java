package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/3/2.
 */

class ResAddProperty extends ResBase implements IApiResults.IAddRes {
    private int mPropId = 0;

    ResAddProperty(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();
        mString += "  new property id: " + mPropId + "\n";
        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        try {
            mPropId = obj.getInt("Id");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;   // super.parseResult(obj);
    }

    @Override
    public int GetId() {
        return mPropId;
    }
}
