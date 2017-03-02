package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/3/2.
 */

class ResGetHouseList extends ResBase implements IApiResults.IResultList {

    private HouseList mHouseList = null;

    ResGetHouseList(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mHouseList = new HouseList();
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();
        if (null != mHouseList) {
            mString += mHouseList.DebugList();
        }
        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        // parse house list
        return mHouseList.parseList(obj);
    }

    @Override
    public int GetTotalNumber() {
        if (null != mHouseList) {
            return mHouseList.GetTotalNumber();
        }
        return 0;
    }

    @Override
    public int GetFetchedNumber() {
        if (null != mHouseList) {
            return mHouseList.GetFetchedNumber();
        }
        return 0;
    }

    @Override
    public ArrayList<Object> GetList() {
        if (null != mHouseList) {
            return mHouseList.GetList();
        }
        return null;
    }

    class HouseList extends ResList {
//        HouseList() {
//        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("IDs");
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

        @Override
        public String ListItem2String(Object item) {
            if (null == item) {
                return "";
            }
            return " id: " + (Integer)item;
//            return ((HouseInfo)item).ListItemInfo2String();
        }
    }
}
