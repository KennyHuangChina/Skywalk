package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by kenny on 2017/5/8.
 */

class CmdAddPicture extends CommunicationBase {
    private int mHouse = 0;
    private int mType = -1;
    private String mDesc = "";

    CmdAddPicture(Context context) {
        super(context, CommunicationInterface.CmdID.CMD_ADD_PICTURE);
        TAG = "CmdAddPicture";
        mMethodType = "POST";
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/pic/newpic";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("house=" + mHouse);
        mRequestData += "&";
        mRequestData += ("type=" + mType);
        mRequestData += "&";
        mRequestData += ("desc=" + mDesc);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_INDEX) ||
                !map.containsKey(CommunicationParameterKey.CPK_TYPE) ||
                !map.containsKey(CommunicationParameterKey.CPK_DESC) ||
                !map.containsKey(CommunicationParameterKey.CPK_IMG_FILE)) {
            return false;
        }

        try {
            mHouse = Integer.parseInt(map.get(CommunicationParameterKey.CPK_INDEX));
            if (mHouse < 0) {
                Log.e(TAG, "mHouse: " + mHouse);
                return false;
            }
            mType = Integer.parseInt(map.get(CommunicationParameterKey.CPK_TYPE));
            if (mType < 0) {
                Log.e(TAG, "mType: " + mType);
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        mDesc = map.get(CommunicationParameterKey.CPK_DESC);
        String picFile = map.get(CommunicationParameterKey.CPK_IMG_FILE);
        if (picFile.length() == 0) {
            Log.e(TAG, "picture file not assigned");
            return false;
        }
//        String picFile1 = ScaledownPicture(picFile);
//        if (picFile1.isEmpty()) {
//            Log.e(TAG, "fail to get picture name after scaling down");
//            return false;
//        }

        mFile = picFile; //picFile1;
//        if (0 != picFile.compareToIgnoreCase(picFile1)) {
//            mDelFileExit = true;
//        }
        Log.i(TAG, "house:" + mHouse + ", type:" + mType + ", desc:" + mDesc + ", file:" + mFile);

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResAddResource result = new ResAddResource(nErrCode, jObject);
        return result;
    }
}
