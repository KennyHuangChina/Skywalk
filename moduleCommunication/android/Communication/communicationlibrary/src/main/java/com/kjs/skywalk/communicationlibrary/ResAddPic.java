package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/10/21.
 */

class ResAddPic extends ResAddResource implements IApiResults.IAddPic {
    private String mPicChecksum = null;

    ResAddPic(int nErrCode, JSONObject jObject) {
        super(nErrCode); //, jObject);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();
        mString += "  checksum: " + GetPicChecksum() + "\n";
        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        int nRes = super.parseResult(obj);
        if (0 != nRes) {
            return nRes;
        }

        // picture checksum
        try {
            mPicChecksum = obj.getString("Checksum");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return nRes;
    }

    @Override
    public String GetPicChecksum() {
        return mPicChecksum;
    }
}
