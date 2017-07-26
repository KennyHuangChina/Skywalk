package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdGetBehalfHouses extends CommunicationBase {
    protected   int mType       = 0;  // 0: all; 1: to rent; 2: rented; 3: to sale; 4: wait for approve
    private     int mBegin      = 0;
    private     int mFetchCnt   = 0;

    CmdGetBehalfHouses(Context context, int type, int bgn, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_GET_BEHALF_HOUSE_LIST);
        mType       = type;
        mBegin      = bgn;
        mFetchCnt   = cnt;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/behalf";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("type=" + mType);
        mRequestData += "&";
        mRequestData += ("bgn=" + mBegin);
        mRequestData += "&";
        mRequestData += ("cnt=" + mFetchCnt);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mType < 0 || mType > 4) {
            Log.e(TAG, "mType:" + mType);
            return false;
        }

        if (mBegin < 0) {
            Log.e(TAG, "mBegin:" + mBegin);
            return false;
        }

        if (mFetchCnt < 0) {
            Log.e(TAG, "mFetchCnt:" + mFetchCnt);
            return false;
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseList result = new ResGetHouseList(nErrCode, jObject);
        return result;
    }
}
