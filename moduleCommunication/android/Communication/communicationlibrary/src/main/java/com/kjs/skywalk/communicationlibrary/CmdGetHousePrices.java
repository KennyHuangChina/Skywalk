package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by kenny on 2017/6/10.
 */

class CmdGetHousePrices extends CommunicationBase {
    private int mHouse = -1;
    private int mBegin = 0;
    private int mCount = 0;

    CmdGetHousePrices(Context context, int house, int begin, int count) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_PRICE);
        mMethodType = "GET";
        mHouse = house;
        mBegin = begin;
        mCount = count;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/price", mHouse);
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("bgn=" + mBegin);
        mRequestData += ("&cnt=" + mCount);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResHousePriceList res = new ResHousePriceList(nErrCode, jObject);
        return res;
    }
}
