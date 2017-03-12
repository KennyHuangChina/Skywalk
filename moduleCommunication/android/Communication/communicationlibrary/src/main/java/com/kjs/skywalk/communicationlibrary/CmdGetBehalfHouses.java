package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdGetBehalfHouses extends CmdGetHouseList {
//    protected int mType = 0;  // 0: all; 1: to rent; 2: rented; 3: to sale

    CmdGetBehalfHouses(Context context, String strAPI) {
        super(context, strAPI);
        TAG = "CmdGetBehalfHouses";
        mMethodType = "GET";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/behalf";
        return mCommandURL;
    }
}
