package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by admin on 2017/1/23.
 */

class LogInByPassword extends CommunicationBase {

    private String mUserName = "";
    private String mPassword = "";
    private String mRadom = "";
    private String mType = "1";

    LogInByPassword(Context context) {
        super(context);
        TAG = "LogInByPassword";
        Log.i(TAG, "Constructor");
        mMethodType = "POST";
        mSessionID = "xxxxx";
    }
    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if(!map.containsKey(CommunicationParameterKey.CPK_USER_NAME) ||
                !map.containsKey(CommunicationParameterKey.CPK_PASSWORD) ||
                !map.containsKey(CommunicationParameterKey.CPK_RADOM)) {
            return false;
        }

        mUserName = map.get(CommunicationParameterKey.CPK_USER_NAME);
        if(mUserName == null || mUserName.isEmpty()) {
            return false;
        }
        mPassword = map.get(CommunicationParameterKey.CPK_PASSWORD);
        if(mPassword == null || mPassword.isEmpty()) {
            return false;
        }
        mRadom = map.get(CommunicationParameterKey.CPK_RADOM);
        if(mRadom == null || mRadom.isEmpty()) {
            return false;
        }

        return true;
    }

    private void generateRequestData() {
        mRequestData = ("ln=" +  mUserName);
        mRequestData += "&";
        mRequestData += ("pw=" + mPassword);
        mRequestData += "&";
        mRequestData += ("rd=" + mRadom);
        mRequestData += "&";
        mRequestData += ("typ=" + mType);
    }
    private HashMap<String, String> createResultMap(JSONObject jObject) {
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

    @Override
    public int doOperation(HashMap<String, String> map, CommunicationInterface.CICommandListener commandListener, CommunicationInterface.CIProgressListener progressListener) {
        Log.i(TAG, "doOperation");
        super.doOperation(map, commandListener, progressListener);

        mCommandURL = "/v1/admin/loginpass";
        mCommandURL += "/";

        generateRequestData();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String returnCode = "";
                int retValue = InternalDefines.ERROR_CODE_OK;
                HttpConnector http = new HttpConnector(mContext);
                http.setURL(mServerURL, mCommandURL);
                http.setRequestMethod(mMethodType);
                http.setRequestData(mRequestData);
                if((retValue = http.connect()) != InternalDefines.ERROR_CODE_OK) {
                    String strError = InternalDefines.getErrorDescription(retValue);
                    Log.e(TAG, "Can't connect to server. error: " +  strError);
                    returnCode = "" + retValue;
                    mCommandListener.onCommandFinished(CommunicationCommand.CC_GET_BRIEF_PUBLIC_HOUSE_INFO, returnCode, strError, null);
                    return;
                }

                if((retValue = http.sendRequest(mRequestData)) != InternalDefines.ERROR_CODE_OK) {
                    String strError = InternalDefines.getErrorDescription(retValue);
                    Log.e(TAG, "Can't connect to server. error: " +  strError);
                    returnCode = "" + retValue;
                    mCommandListener.onCommandFinished(CommunicationCommand.CC_GET_BRIEF_PUBLIC_HOUSE_INFO, returnCode, strError, null);
                    return;
                }

                http.disconnect();

                JSONObject jObject = http.getJsonObject();
                if(jObject == null) {
                    String strError = InternalDefines.getErrorDescription(InternalDefines.ERROR_CODE_HTTP_REQUEST_FAILED);
                    returnCode = "" + InternalDefines.ERROR_CODE_HTTP_REQUEST_FAILED;
                    mCommandListener.onCommandFinished(CommunicationCommand.CC_LOG_IN_BY_PASSWORD, returnCode, strError, null);

                    return;
                }

                HashMap<String, String> map = createResultMap(jObject);

                String strError = InternalDefines.getErrorDescription(InternalDefines.ERROR_CODE_OK);
                returnCode = "" + InternalDefines.ERROR_CODE_OK;
                mCommandListener.onCommandFinished(CommunicationCommand.CC_LOG_IN_BY_PASSWORD, returnCode, strError, map);
            }
        }).start();

        Log.i(TAG, "doOperation ... out");

        return CommunicationError.CE_ERROR_NO_ERROR;
    }
}
