package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CIProgressListener;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CICommandListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Jackie on 2017/1/20.
 */

class GetBriefPublicHouseInfo extends CommunicationBase {

    private String mParamID = "";

    GetBriefPublicHouseInfo(Context context){
        super(context);
        TAG = "GetBriefPublicHouseInfo";
        Log.i(TAG, "Constructor");
        mMethodType = "GET";
        mAPI = CommunicationCommand.CC_GET_BRIEF_PUBLIC_HOUSE_INFO;
    }

    private void generateRequestData() {
        mRequestData = "";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if(!map.containsKey(CommunicationParameterKey.CPK_INDEX)) {
             return false;
        }

        mParamID = map.get(CommunicationParameterKey.CPK_INDEX);
        if(mParamID == null || mParamID.isEmpty()) {
            return false;
        }

       return true;
    }

    @Override
    public int doOperation(HashMap<String, String> map, CICommandListener commandListener, CIProgressListener progressListener) {
        Log.i(TAG, "doOperation");

        mCommandURL = "/v1/house/digest";
        mCommandURL += "/" + mParamID;

        generateRequestData();

        super.doOperation(map, commandListener, progressListener);

        Log.i(TAG, "doOperation ... out");

        return CommunicationError.CE_ERROR_NO_ERROR;
    }

    @Override
    public HashMap<String, String> doCreateResultMap(JSONObject jObject) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            JSONObject obj = jObject.getJSONObject("HouseDigest");
            if(obj == null) {
                return null;
            }

            map.put("Id", Integer.toString(obj.getInt("Id")));
            map.put("Property", obj.getString("Property"));
            map.put("PropertyAddr", obj.getString("PropertyAddr"));
            map.put("Bedrooms", Integer.toString(obj.getInt("Bedrooms")));
            map.put("Livingrooms", Integer.toString(obj.getInt("Livingrooms")));
            map.put("Bathrooms", Integer.toString(obj.getInt("Bathrooms")));
            map.put("Acreage", Integer.toString(obj.getInt("Acreage")));
            map.put("Rental", Integer.toString(obj.getInt("Rental")));
            map.put("Pricing", Integer.toString(obj.getInt("Pricing")));
            map.put("CoverImg", Integer.toString(obj.getInt("CoverImg")));

            JSONArray array = obj.getJSONArray("Tags");
            map.put("TagCount", Integer.toString(array.length()));
            for(int i = 0; i < array.length(); i ++) {
                String s = array.getString(i);
                JSONObject tmpObj = new JSONObject(s);
                if(tmpObj != null) {
                    int tagId = tmpObj.getInt("TagId");
                    String tagDesc = tmpObj.getString("TagDesc");
                    String key = "TagID_" + i;
                    String value = "" + tagId;
                    map.put(key, value);
                    key = "TagDesc_" + i;
                    value = tagDesc;
                    map.put(key, value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return map;
    }
}
