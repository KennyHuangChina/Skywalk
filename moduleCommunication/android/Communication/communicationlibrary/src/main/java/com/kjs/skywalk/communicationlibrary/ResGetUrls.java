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

        mString += "  Small picture: " + GetSmallPicture() + "\n";
        mString += "  Middle picture: " + GetMiddlePicture() + "\n";
        mString += "  Large picture: " + GetLargePicture() + "\n";

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
        if (null == mUrlSmall || mUrlSmall.isEmpty()) {
            return null;
        }
        return CUtilities.PicFullUrl(mUrlSmall);
    }

    @Override
    public String GetMiddlePicture() {
        if (null == mUrlMiddle || mUrlMiddle.isEmpty()) {
            return null;
        }
        return CUtilities.PicFullUrl(mUrlMiddle);
    }

    @Override
    public String GetLargePicture() {
        if (null == mUrlLarge || mUrlLarge.isEmpty()) {
            return null;
        }
        return CUtilities.PicFullUrl(mUrlLarge);
    }
}
