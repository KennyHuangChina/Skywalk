package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/14.
 */

class CmdGetHousePicList extends CommunicationBase {
    private int mHouse = 0;
    private int mType = -1;

    CmdGetHousePicList(Context context, int house, int type) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_PIC_LIST);
        TAG = "CmdGetHousePicList";
        mMethodType = "GET";
        mHouse = house;
        mType = type;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/pic/house/" + mHouse;
        if (mType > 0) {
            mCommandURL += "?st=" + mType;
        }
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        super.generateRequestData();
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
//        return super.checkParameter(map);
        if (mHouse <= 0) {
            Log.e(TAG, "house:" + mHouse);
            return false;
        }
        if (mType < 0 || mType > 4) {
            Log.e(TAG, "type:" + mType);
            return false;
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResHousePicList result = new ResHousePicList(nErrCode, jObject);
        return result;
    }
}
