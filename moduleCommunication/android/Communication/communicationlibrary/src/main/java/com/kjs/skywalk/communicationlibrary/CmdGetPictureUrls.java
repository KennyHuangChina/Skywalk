package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/13.
 */

class CmdGetPictureUrls extends CommunicationBase {
    private int mPicId = 0;
    private int mPicSize = 0;

    CmdGetPictureUrls(Context context, int picId, int picSize) {
        super(context, CommunicationInterface.CmdID.CMD_GET_PIC_URL);
        TAG = "CmdGetPictureUrls";
        mMethodType = "GET";
        mPicId = picId;
        mPicSize = picSize;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/pic/" + mPicId;
        if (mPicSize > 0) {
            mCommandURL += "?size=" + mPicSize;
        }
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        super.generateRequestData();
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mPicId <= 0) {
            Log.e(TAG, "picture:" + mPicId);
            return false;
        }
        if (mPicSize < 0 || mPicSize > 4) {
            Log.e(TAG, "mPicSize:" + mPicSize);
            return false;
        }
        return true; // super.checkParameter(map);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetUrls result = new ResGetUrls(nErrCode, jObject);
        return result;
    }
}
