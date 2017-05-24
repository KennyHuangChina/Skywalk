package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/3/2.
 */

class ResGetPropertyInfo extends ResBase implements IApiResults.IPropertyInfo {
    private int mPropId = 0;
    private String mPropName = "";
    private String mPropAddress = "";
    private String mPropDesc = "";

    ResGetPropertyInfo(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();

        mString += "  property id: " + mPropId + "\n";
        mString += "  name: " + mPropName + "\n";
        mString += "  address: " + mPropAddress + "\n";
        mString += "  desc: " + mPropDesc + "\n";

        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {

        try {
            JSONObject jProp = obj.getJSONObject("PropInfo");

            mPropId = jProp.getInt("Id");
            mPropName = jProp.getString("PropName");
            mPropAddress = jProp.getString("PropAddress");
            mPropDesc = jProp.getString("PropDesc");

        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public int GetId() {
        return mPropId;
    }

    @Override
    public String GetName() {
        return mPropName;
    }

    @Override
    public String GetAddress() {
        return mPropAddress;
    }

    @Override
    public String GetDesc() {
        return mPropDesc;
    }
}
