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
                    Integer item = array.getInt(n);
//                    HouseInfo newItem = new HouseInfo(item);
//                    if (null == newItem) {
//                        return -2;
//                    }
                    mList.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return -3;
            }

            return 0;
        }

        protected String getListItem2String(Object item) {
            if (null == item) {
                return "";
            }
            return " id: " + (Integer)item;
//            return ((HouseInfo)item).ListItemInfo2String();
        }
    }
}
