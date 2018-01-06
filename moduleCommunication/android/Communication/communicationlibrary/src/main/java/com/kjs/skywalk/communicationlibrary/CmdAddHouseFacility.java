package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kenny on 2017/3/16.
 */
class CmdAddHouseFacility extends CommunicationBase {
//    private int mHouse = 0;
////    private int mNumb = 0;
//    private ArrayList<CommunicationInterface.FacilityItem> mList = null;

    CmdAddHouseFacility(Context context, int house, ArrayList<IApiArgs.IFacilityItem> list) {
        super(context, CommunicationInterface.CmdID.CMD_ADD_HOUSE_FACILITY);
        mMethodType = "POST";
        mArgs = new Args(house, list);
    }

    @Override
    public String getRequestURL() {
//        mCommandURL = "/v1/house/housefacilities/" + mHouse;
        mCommandURL = "/v1/accessory/house/" + ((Args)mArgs).getId() + "/facilities";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("numb=" + ((Args)mArgs).getList().size());
        for (int n = 0; n < ((Args)mArgs).getList().size(); n++) {
            mRequestData += "&";
            FacilityItem item = (FacilityItem)((Args)mArgs).getList().get(n);
            mRequestData += ("fid_" + n + "=" + item.mFId);
            mRequestData += "&";
            mRequestData += ("fqty_" + n + "=" + item.mQty);
            mRequestData += "&";
            mRequestData += ("fdesc_" + n + "=" + String2Base64(item.mDesc));
        }
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResBase result = new ResBase(nErrCode, jObject);
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsAddHouseFacility {
        private ArrayList<IApiArgs.IFacilityItem> mList = null;

        Args(int house, ArrayList<IApiArgs.IFacilityItem> list) {
            super(house);
            mList = list;
        }

        @Override
        public boolean checkArgs() {
            if (null == mList || 0 == mList.size()) {
                Log.e(TAG, "[checkArgs] mList:" + mList);
                return false;
            }
//            for (FacilityItem fi : mList) {
            for (int n = 0; n < mList.size(); n++) {
                FacilityItem fi = (FacilityItem) mList.get(n);
                if (null == fi || !fi.checkFacilityItem()) {
                    return false;
                }
            }
            return super.checkArgs();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (mList.size() != ((Args)arg2).mList.size()) {
                return false;
            }
            for (int n = 0; n < mList.size(); n++) {
                FacilityItem fiThis = (FacilityItem)mList.get(n);
                FacilityItem fcThat = (FacilityItem)((Args)arg2).mList.get(n);
                if (!fiThis.equal(fcThat)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public ArrayList<IApiArgs.IFacilityItem> getList() {
            return mList;
        }
    }
}
