package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import com.kjs.skywalk.communicationlibrary.CommunicationBase.CheckParameter;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CIProgressListener;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CICommandListener;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Jackie on 2017/1/20.
 */

class GetBriefPublicHouseInfo extends CommunicationBase
        implements CheckParameter {

    GetBriefPublicHouseInfo(Context context){
        super(context);
        Log.i(TAG, "Constructor");
        mMethodType = "GET";
        mCommandURL = "/v1/house/digest";

        TAG = "GetBriefPublicHouseInfo";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if(!map.containsKey(CommunicationParameterKey.CPK_SESSION_ID)) {
            return false;
        }
        return false;
    }

    @Override
    public int doOperation(HashMap<String, String> map, CICommandListener commandListener, CIProgressListener progressListener) {
        Log.i(TAG, "doOperation");
        super.doOperation(map, commandListener, progressListener);

        return 0;
    }
}
