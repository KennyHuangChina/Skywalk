package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kenny on 2017/3/16.
 */
class FacilityItem {
    public int      mFId    = 0;    // facility id
    public int      mQty    = 0;    // facility quantity
    public String   mDesc   = "";   // facility description

    FacilityItem(int id, int qty, String desc) {
        mFId = id;
        mQty = qty;
        mDesc = desc;
    }
}

class CmdAddHouseFacility extends CommunicationBase {
    private int mHouse = 0;
    private int mNumb = 0;
    private ArrayList<FacilityItem> mList = null;

    CmdAddHouseFacility(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_ADD_HOUSE_FACILITY);
        TAG = "CmdAddHouseFacility";
        mMethodType = "POST";
        mList = new ArrayList<FacilityItem>();
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/housefacilities/" + mHouse;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("numb=" + mNumb);
        mRequestData += "&";
//        mRequestData += ("type=" + mType);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX) ||
                !map.containsKey(CommunicationParameterKey.CPK_COUNT) ) {
            return false;
        }

        try {
            mHouse = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
            mNumb = Integer.parseInt(map.get(CommunicationParameterKey.CPK_COUNT));
            if (mNumb <= 0) {
                return false;
            }
            for (int i = 0; i < mNumb; i++) {
                String keyId = "fid_" + i;
                String keyQty = "fqty_" + i;
                String keyDesc = "fdesc_" + i;
                if (!map.containsKey(keyId) || !map.containsKey(keyQty) || !map.containsKey(keyDesc)) {
                    return false;
                }
                int fid = Integer.parseInt(map.get(keyId));
                int qty = Integer.parseInt(map.get(keyQty));
                FacilityItem newFacility = new FacilityItem(fid, qty, map.get(keyDesc));
                mList.add(newFacility);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
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
