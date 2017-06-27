package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/11.
 */

class CmdRecommendHouse extends CommunicationBase {
    private int mAct        = 0;   // 1 - recommend; 2 - unrecommend
    private int mHouseId    = 0;

    CmdRecommendHouse(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_GET_RECOMMEND_HOUSE);
        mMethodType = "PUT";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX) ||
                !map.containsKey(CommunicationParameterKey.CPK_HOUSE_RECOMMENT_ACT) ) {
            return false;
        }

        try {
            mHouseId = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
            if (mHouseId <= 0) {
                Log.e(TAG, "mHouseId:" + mHouseId);
                return false;
            }
            mAct = Integer.parseInt(map.get(CommunicationParameterKey.CPK_HOUSE_RECOMMENT_ACT));
            if (1 != mAct && 2 != mAct) {
                Log.e(TAG, "mAct:" + mAct);
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/recommend/" + mHouseId;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("act=" + mAct);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
