package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/4.
 */

class CmdAmendHouse extends CommunicationBase {
    private CommunicationInterface.HouseInfo mHouseInfo = null;

    CmdAmendHouse(Context context, CommunicationInterface.HouseInfo houseInfo) {
        super(context, CommunicationInterface.CmdID.CMD_AMEND_HOUSE);
        mHouseInfo = houseInfo;
        mMethodType = "PUT";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (null == mHouseInfo) {
            return false;
        }
        return mHouseInfo.CheckHoseInfo(TAG, false);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/" + mHouseInfo.mHouseId;
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
        mRequestData += ("buy_date=" + mHouseInfo.mBuyDate);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
