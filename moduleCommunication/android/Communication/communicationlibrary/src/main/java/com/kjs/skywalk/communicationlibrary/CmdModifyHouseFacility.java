package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/4.
 */

class CmdModifyHouseFacility extends CommunicationBase {
    private int     mId     = 0;    // house facility id
    private int     mFid    = 0;    // facility id
    private int     mQty    = 0;    // facility quantity
    private String  mDesc   = "";   // facility description

    CmdModifyHouseFacility(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_EDIT_HOUSE_FACILITY);
        TAG = "CmdModifyHouseFacility";
        mMethodType = "PUT";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/housefacility/" + mId;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("fid=" + mFid);
        mRequestData += ("&fqty=" + mQty);
        mRequestData += ("&fdesc=" + mDesc);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_DESC) ||
                !map.containsKey(CommunicationParameterKey.CPK_QTY) ||
                !map.containsKey(CommunicationParameterKey.CPK_FACILITY_ID) ||
                !map.containsKey(CommunicationParameterKey.CPK_INDEX)) {
            return false;
        }

        try {
            mId = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
            mFid = Integer.parseInt(map.get(CommunicationParameterKey.CPK_FACILITY_ID));
            mQty = Integer.parseInt(map.get(CommunicationParameterKey.CPK_QTY));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        mDesc = map.get(CommunicationParameterKey.CPK_DESC);
        if (mDesc.length() > 0) {
            mDesc = String2Base64(mDesc);
        }
        Log.d(TAG, "mDesc: " + mDesc);

        return true;
    }
}
