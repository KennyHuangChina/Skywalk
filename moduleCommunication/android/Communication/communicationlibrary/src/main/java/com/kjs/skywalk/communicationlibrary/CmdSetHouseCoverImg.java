package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/11.
 */

class CmdSetHouseCoverImg extends CommunicationBase {
    private int mHouse      = 0;
    private int mCoverImg   = 0;

    CmdSetHouseCoverImg(Context context, String strAPI) {
        super(context, strAPI);
        TAG = "CmdSetHouseCoverImg";
//        Log.i(TAG, "Constructor");
        mMethodType = "PUT";
    }

    @Override
    public int doOperation(HashMap<String, String> map,
                           CommunicationInterface.CICommandListener commandListener,
                           CommunicationInterface.CIProgressListener progressListener) {
        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/house/covimg/" + mHouse;
//        mCommandURL += "/";
        generateRequestData();
        super.doOperation(map, commandListener, progressListener);

        Log.i(TAG, "doOperation ... out");
        return CommunicationError.CE_ERROR_NO_ERROR;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX) ||
                !map.containsKey(CommunicationParameterKey.CPK_HOUSE_COVER_IMG) ) {
            return false;
        }

        try {
            mHouse = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
            mCoverImg = Integer.parseInt(map.get(CommunicationParameterKey.CPK_HOUSE_COVER_IMG));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void generateRequestData() {
        mRequestData = ("img=" + mCoverImg);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
