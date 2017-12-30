package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/4.
 */

class CmdAmendHouse extends CommunicationBase {

    CmdAmendHouse(Context context, CommunicationInterface.HouseInfo houseInfo) {
        super(context, CommunicationInterface.CmdID.CMD_AMEND_HOUSE);
        mMethodType = "PUT";
        mArgs = new Args(houseInfo);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/" + ((Args)mArgs).getHouseInfo().mHouseId;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("prop=" + ((Args)mArgs).getHouseInfo().mPropId);
        mRequestData += "&";
        mRequestData += ("build=" + ((Args)mArgs).getHouseInfo().mBuilding);
        mRequestData += "&";
        mRequestData += ("house=" + ((Args)mArgs).getHouseInfo().mHouseNo);
        mRequestData += "&";
        mRequestData += ("floor_total=" + ((Args)mArgs).getHouseInfo().mFloorTotal);
        mRequestData += "&";
        mRequestData += ("floor_this=" + ((Args)mArgs).getHouseInfo().mFloorThis);
        mRequestData += "&";
        mRequestData += ("Bedrooms=" + ((Args)mArgs).getHouseInfo().mBedrooms);
        mRequestData += "&";
        mRequestData += ("LivingRooms=" + ((Args)mArgs).getHouseInfo().mLivingrooms);
        mRequestData += "&";
        mRequestData += ("Bathrooms=" + ((Args)mArgs).getHouseInfo().mBathrooms);
        mRequestData += "&";
        mRequestData += ("Acreage=" + ((Args)mArgs).getHouseInfo().mAcreage);
        mRequestData += "&";
        mRequestData += ("4sale=" + ((Args)mArgs).getHouseInfo().mForSale);
        mRequestData += "&";
        mRequestData += ("4rent=" + ((Args)mArgs).getHouseInfo().mForRent);
        mRequestData += "&";
        mRequestData += ("decor=" + ((Args)mArgs).getHouseInfo().mDecorate);
        mRequestData += "&";
        mRequestData += ("buy_date=" + ((Args)mArgs).getHouseInfo().mBuyDate);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsAmendHouseInfo {
        private CommunicationInterface.HouseInfo mHouseInfo = null;

        Args(CommunicationInterface.HouseInfo house_info) {
            mHouseInfo = house_info;
        }

        @Override
        public boolean checkArgs() {
            String Fn = "[checkArgs] ";
            if (null == mHouseInfo) {
                Log.e(TAG, Fn + "house info not set");
                return false;
            }
            return mHouseInfo.CheckHoseInfo(TAG, false);
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }

            return mHouseInfo.IsHouseInfoEqual(((Args)arg2).mHouseInfo);
        }

        @Override
        public CommunicationInterface.HouseInfo getHouseInfo() {
            return mHouseInfo;
        }
    }
}
