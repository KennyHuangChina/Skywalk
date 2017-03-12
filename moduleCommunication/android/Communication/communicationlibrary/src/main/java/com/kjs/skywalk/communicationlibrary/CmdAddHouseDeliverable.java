package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdAddHouseDeliverable extends CommunicationBase {
    private int     mHouseId        = 0;
    private int     mDeliverable    = 0;    // deliverable id
    private int     mDelivQty       = 0;    // deliverable quantity
    private String  mDesc           = "";   // deliverable description

    CmdAddHouseDeliverable(Context context, String strAPI) {
        super(context, strAPI);
        TAG = "CmdAddHouseDeliverable";
        mMethodType = "POST";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/housedeliv/" + mHouseId;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("did=" + mDeliverable);
        mRequestData += "&";
        mRequestData += ("qty=" + mDelivQty);
        mRequestData += "&";
        mRequestData += ("desc=" + mDesc);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX) ||
                !map.containsKey(CommunicationParameterKey.CPK_DELIVERABLE_ID) ||
                !map.containsKey(CommunicationParameterKey.CPK_QTY) ||
                !map.containsKey(CommunicationParameterKey.CPK_DESC) ) {
            return false;
        }

        try {
            mHouseId = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
            if (mHouseId <= 0) {
                Log.e(TAG, "mHouseId:" + mHouseId);
                return false;
            }
            mDeliverable = Integer.parseInt(map.get(CommunicationParameterKey.CPK_DELIVERABLE_ID));
            if (mDeliverable <= 0) {
                Log.e(TAG, "mDeliverable:" + mDeliverable);
                return false;
            }
            mDelivQty = Integer.parseInt(map.get(CommunicationParameterKey.CPK_QTY));
            if (mDelivQty < 0) {
                Log.e(TAG, "mDelivQty:" + mDelivQty);
                return false;
            }
            mDesc = map.get(CommunicationParameterKey.CPK_DESC);
            if (mDesc.length() == 0) {
                Log.e(TAG, "mDesc:" + mDesc);
                return false;
            }
            mDesc = String2Base64(mDesc);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResAddResource res = new ResAddResource(nErrCode, jObject);
        return res;
    }
}
