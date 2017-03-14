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
    private int     mHouseId        = 0;
    private int     mPropId         = 0;    // property id
    private int     mBuilding       = 0;    // building no
    private String  mHouseNo        = "";   // house no
    private int     mFloorTotal     = 0;    // total floors
    private int     mFloorThis      = 0;    // floor of this house
    private int     mLivingrooms    = 0;    // living room count
    private int     mBedrooms       = 0;    // bedroom count
    private int     mBathrooms      = 0;    // bathroom count
    private int     mAcreage        = 0;    // acreage, in square meter. 100 times than actual value
    private boolean mSale           = false;// commit for sale
    private boolean mRent           = false;// commit for rent

    CmdAmendHouse(Context context) {
        super(context, "CmdAmendHouse");
        TAG = "CmdAmendHouse";
//        Log.i(TAG, "Constructor");
        mMethodType = "PUT";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX) ||
                !map.containsKey(CommunicationParameterKey.CPK_PROPERTY_ID) ||
                !map.containsKey(CommunicationParameterKey.CPK_BUILDING_NO) ||
                !map.containsKey(CommunicationParameterKey.CPK_HOUSE_NO) ||
                !map.containsKey(CommunicationParameterKey.CPK_FLOOR_TOTA) ||
                !map.containsKey(CommunicationParameterKey.CPK_FLOOR_THIS) ||
                !map.containsKey(CommunicationParameterKey.CPK_LIVINGROOMS) ||
                !map.containsKey(CommunicationParameterKey.CPK_BEDROOMS) ||
                !map.containsKey(CommunicationParameterKey.CPK_BATHROOMS) ||
                !map.containsKey(CommunicationParameterKey.CPK_ACREAGE) ||
                !map.containsKey(CommunicationParameterKey.CPK_4SALE) ||
                !map.containsKey(CommunicationParameterKey.CPK_4RENT) ) {
            return false;
        }

        try {
            mHouseId = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
            mPropId = Integer.parseInt(map.get(CommunicationParameterKey.CPK_PROPERTY_ID));
            mBuilding = Integer.parseInt(map.get(CommunicationParameterKey.CPK_BUILDING_NO));
            if (mBuilding <= 0) {
                Log.e(TAG, "Invalid building no:" + mBuilding);
                return false;
            }
            mHouseNo = map.get(CommunicationParameterKey.CPK_HOUSE_NO);
            if (TextUtils.isEmpty(mHouseNo)) {
                // Kenny; house no could be empty for townhouse or villa
//                Log.e(TAG, "No house no specified");
//                return false;
            }
            mFloorTotal = Integer.parseInt(map.get(CommunicationParameterKey.CPK_FLOOR_TOTA));
            if (mFloorTotal < 1) {
                Log.e(TAG, "Invalid floor total:" + mFloorTotal);
                return false;
            }
            mFloorThis = Integer.parseInt(map.get(CommunicationParameterKey.CPK_FLOOR_THIS));
            if (mFloorThis < 1 || mFloorThis > mFloorTotal) {
                Log.e(TAG, "Invalid floor this:" + mFloorThis);
                return false;
            }
            mLivingrooms = Integer.parseInt(map.get(CommunicationParameterKey.CPK_LIVINGROOMS));
            mBedrooms = Integer.parseInt(map.get(CommunicationParameterKey.CPK_BEDROOMS));
            mBathrooms = Integer.parseInt(map.get(CommunicationParameterKey.CPK_BATHROOMS));
            if (mLivingrooms <= 0 && mBedrooms <= 0 && mBathrooms <= 0) {
                Log.e(TAG, "Invalid livingroom:" + mLivingrooms + ", bedroom:" + mBedrooms + ", bathroom:" + mBathrooms);
                return false;
            }
            mAcreage = Integer.parseInt(map.get(CommunicationParameterKey.CPK_ACREAGE));
            if (mAcreage < 100){
                Log.e(TAG, "Invalid acreage:" + mAcreage);
                return false;
            }
            mSale = Boolean.parseBoolean(map.get(CommunicationParameterKey.CPK_4SALE));
            mRent = Boolean.parseBoolean(map.get(CommunicationParameterKey.CPK_4RENT));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/" + mHouseId;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("prop=" + mPropId);
        mRequestData += "&";
        mRequestData += ("build=" + mBuilding);
        mRequestData += "&";
        mRequestData += ("house=" + mHouseNo);
        mRequestData += "&";
        mRequestData += ("floor_total=" + mFloorTotal);
        mRequestData += "&";
        mRequestData += ("floor_this=" + mFloorThis);
        mRequestData += "&";
        mRequestData += ("Bedrooms=" + mBedrooms);
        mRequestData += "&";
        mRequestData += ("LivingRooms=" + mLivingrooms);
        mRequestData += "&";
        mRequestData += ("Bathrooms=" + mBathrooms);
        mRequestData += "&";
        mRequestData += ("Acreage=" + mAcreage);
        mRequestData += "&";
        mRequestData += ("4sale=" + mSale);
        mRequestData += "&";
        mRequestData += ("4rent=" + mRent);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
