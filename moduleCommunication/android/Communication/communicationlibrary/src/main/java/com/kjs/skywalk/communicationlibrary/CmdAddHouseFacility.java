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
    private int mHouse = 0;
//    private int mNumb = 0;
    private ArrayList<CommunicationInterface.FacilityItem> mList = null;

    CmdAddHouseFacility(Context context, int house, ArrayList<CommunicationInterface.FacilityItem> list) {
        super(context, CommunicationInterface.CmdID.CMD_ADD_HOUSE_FACILITY);
        TAG = "CmdAddHouseFacility";
        mMethodType = "POST";
        mHouse = house;
        mList = list;   // new ArrayList<CommunicationInterface.FacilityItem>();
    }

    @Override
    public String getRequestURL() {
//        mCommandURL = "/v1/house/housefacilities/" + mHouse;
        mCommandURL = "/v1/accessory/house/" + mHouse + "/facilities";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("numb=" + mList.size());
        for (int n = 0; n < mList.size(); n++) {
            mRequestData += "&";
            CommunicationInterface.FacilityItem item = mList.get(n);
            mRequestData += ("fid_" + n + "=" + item.mFId);
            mRequestData += "&";
            mRequestData += ("fqty_" + n + "=" + item.mQty);
            mRequestData += "&";
            mRequestData += ("fdesc_" + n + "=" + String2Base64(item.mDesc));
        }
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mHouse <= 0) {
            Log.e(TAG, "mHouse: " + mHouse);
            return false;
        }
        if (null == mList || 0 == mList.size()) {
            Log.e(TAG, "No facilities assigned for house");
            return false;
        }
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResBase result = new ResBase(nErrCode, jObject);
        return result;
    }
}
