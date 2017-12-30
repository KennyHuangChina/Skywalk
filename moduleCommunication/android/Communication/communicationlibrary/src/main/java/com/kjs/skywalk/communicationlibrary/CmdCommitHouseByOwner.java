package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by kenny on 2017/3/3.
 */

class CmdCommitHouseByOwner extends CommunicationBase {
//    private CommunicationInterface.HouseInfo mHouseInfo = null;
//    private int mAgent = 0;    // agent id. 0 means no agent assigned

    CmdCommitHouseByOwner(Context context, CommunicationInterface.HouseInfo houseInfo, int agent) {
        super(context, CommunicationInterface.CmdID.CMD_COMMIT_HOUSE_BY_OWNER);
//        mHouseInfo = houseInfo;
//        mAgent = agent;
//        Log.i(TAG, "Constructor");
        mMethodType = "POST";
        mArgs = new Args(houseInfo, agent);
    }
//
//    @Override
//    public boolean checkParameter(HashMap<String, String> map) {
//        if (null == mHouseInfo) {
//            return false;
//        }
//        return mHouseInfo.CheckHoseInfo(TAG, true);
//    }

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
        mRequestData += "&";
        mRequestData += ("agen=" + ((Args)mArgs).getAgent());

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsCommitNewHouseByLandlord {
        private CommunicationInterface.HouseInfo mHouseInfo = null;
        private int mAgent = 0;    // agent id. 0 means no agent assigned

        Args(CommunicationInterface.HouseInfo houseInfo, int agent) {
            mHouseInfo = houseInfo;
            mAgent = agent;
        }

        @Override
        public boolean checkArgs() {
            if (null == mHouseInfo) {
                return false;
            }
            return mHouseInfo.CheckHoseInfo(TAG, true);
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }

            Args arg_chk = (Args)arg2;
            if (mAgent != arg_chk.mAgent || null == mHouseInfo || null == arg_chk.mHouseInfo) {
                return false;
            }

            return mHouseInfo.IsHouseInfoEqual(arg_chk.mHouseInfo);
        }

        @Override
        public CommunicationInterface.HouseInfo getHouseInfo() {
            return mHouseInfo;
        }

        @Override
        public int getAgent() {
            return mAgent;
        }
    }
}
