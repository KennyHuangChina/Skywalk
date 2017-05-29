package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/5/13.
 */

class ResGetUrls extends ResBase implements IApiResults.IPicUrls {
    private String mUrlSmall = "";
    private String mUrlMiddle = "";
    private String mUrlLarge = "";

    ResGetUrls(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();

        mString += "  Small picture: " + mUrlSmall + "\n";
        mString += "  Middle picture: " + mUrlMiddle + "\n";
        mString += "  Large picture: " + mUrlLarge + "\n";

        return mString;
    }

    protected int parseResult(JSONObject obj) {
        try {
            mUrlSmall = obj.getString("Url_s");
            mUrlMiddle  = obj.getString("Url_m");
            mUrlLarge = obj.getString("Url_l");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public String GetSmallPicture() {
        return mUrlSmall;
    }

    @Override
    public String GetMiddlePicture() {
        return mUrlMiddle;
    }

    @Override
    public String GetLargePicture() {
        return mUrlLarge;
    }


}
