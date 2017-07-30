package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_APPOINT_NUMB;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_APPOINT_NUMB_DESC;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_PUBLISH_TIME;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_PUBLISH_TIME_DESC;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_RENTAL;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_RENTAL_DESC;

/**
 * Created by kenny on 2017/7/30.
 */

class CmdGetHouseList_AppointSee extends CommunicationBase {
    private int mBeginPosi  = 0;
    private int mFetchCount = 0;

    CmdGetHouseList_AppointSee(Context context, int bgn, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_HOUSE_LST_APPOINT_SEE);
        mNeedLogin  = false;
        mBeginPosi  = bgn;
        mFetchCount = cnt;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mBeginPosi < 0) {
            Log.e(TAG, "mBeginPosi: " + mBeginPosi);
            return false;
        }
        if (mFetchCount < 0) {
            Log.e(TAG, "mFetchCount: " + mFetchCount);
            return false;
        }
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseList result = new ResGetHouseList(nErrCode, jObject);
        return result;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/appointment/seeHouselist";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("bgn=" + mBeginPosi);
        mRequestData += "&";
        mRequestData += ("cnt=" + mFetchCount);
       Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
