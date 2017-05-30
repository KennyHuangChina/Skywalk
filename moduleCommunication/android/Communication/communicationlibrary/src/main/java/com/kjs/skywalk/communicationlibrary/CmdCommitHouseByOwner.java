package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/3.
 */

class CmdCommitHouseByOwner extends CommunicationBase {
    private CommunicationInterface.HouseInfo mHouseInfo = null;
    private int mAgent = 0;    // agent id. 0 means no agent assigned

    CmdCommitHouseByOwner(Context context, CommunicationInterface.HouseInfo houseInfo, int agent) {
        super(context, CommunicationInterface.CmdID.CMD_COMMIT_HOUSE_BY_OWNER);
        mHouseInfo = houseInfo;
        mAgent = agent;
//        Log.i(TAG, "Constructor");
        mMethodType = "POST";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (null == mHouseInfo) {
            return false;
        }
        return mHouseInfo.CheckHoseInfo(TAG, true);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResAddResource result = new ResAddResource(nErrCode, jObject);
        return result;
    }


    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/commit";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("prop=" + mHouseInfo.mPropId);
        mRequestData += "&";
        mRequestData += ("build=" + mHouseInfo.mBuilding);
        mRequestData += "&";
        mRequestData += ("house=" + mHouseInfo.mHouseNo);
        mRequestData += "&";
        mRequestData += ("floor_total=" + mHouseInfo.mFloorTotal);
        mRequestData += "&";
        mRequestData += ("floor_this=" + mHouseInfo.mFloorThis);
        mRequestData += "&";
        mRequestData += ("Bedrooms=" + mHouseInfo.mBedrooms);
        mRequestData += "&";
        mRequestData += ("LivingRooms=" + mHouseInfo.mLivingrooms);
        mRequestData += "&";
        mRequestData += ("Bathrooms=" + mHouseInfo.mBathrooms);
        mRequestData += "&";
        mRequestData += ("Acreage=" + mHouseInfo.mAcreage);
        mRequestData += "&";
        mRequestData += ("4sale=" + mHouseInfo.mForSale);
        mRequestData += "&";
        mRequestData += ("4rent=" + mHouseInfo.mForRent);
        mRequestData += "&";
        mRequestData += ("decor=" + mHouseInfo.mDecorate);
        mRequestData += "&";
        mRequestData += ("agen=" + mAgent);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
