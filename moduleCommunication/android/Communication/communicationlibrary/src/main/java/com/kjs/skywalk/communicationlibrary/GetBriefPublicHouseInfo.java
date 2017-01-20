package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/20.
 */

class GetBriefPublicHouseInfo extends CommunicationBase implements CommunicationBase.CheckParameter{
    private final String TAG = "GetBriefPublicHouseInfo";

    GetBriefPublicHouseInfo(Context context){
        super(context);
        Log.i(TAG, "Constructor");
        mMethodType = "GET";
        mCommandURL = "/v1/house/digest";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return false;
    }
}
