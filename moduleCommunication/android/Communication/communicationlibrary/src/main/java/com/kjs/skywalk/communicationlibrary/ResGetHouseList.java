package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/3/2.
 */

class ResGetHouseList extends ResBase implements IApiResults.IResultList {

    private HouseDigestList mHouseDigestList = null;

    ResGetHouseList(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mHouseDigestList = new HouseDigestList();
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();
        if (null != mHouseDigestList) {
            mString += mHouseDigestList.DebugList();
        }
        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        // parse house list
        return mHouseDigestList.parseList(obj);
    }

    @Override
    public int GetTotalNumber() {
        if (null != mHouseDigestList) {
            return mHouseDigestList.GetTotalNumber();
        }
        return 0;
    }

    @Override
    public int GetFetchedNumber() {
        if (null != mHouseDigestList) {
            return mHouseDigestList.GetFetchedNumber();
        }
        return 0;
    }

    @Override
    public ArrayList<Object> GetList() {
        if (null != mHouseDigestList) {
            return mHouseDigestList.GetList();
        }
        return null;
    }

    class HouseDigestList extends ResList {
//        HouseDigestList() {
//        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("HouseDigests");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    HouseDigestInfo newItem = new HouseDigestInfo(array.getJSONObject(n));
                   if (null == newItem) {
                        return -2;
                    }
                    mList.add(newItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return -3;
            }

            return 0;
        }
    }
}
