package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by kenny on 2017/2/22.
 */
class ResGetPropertyList extends ResBase implements IApiResults.IResultList {

    private PropertyList mPropList = null;

    ResGetPropertyList(JSONObject obj) {
//        super(obj);
        mPropList = new PropertyList();
        parse(obj);
    }

    protected int parse(JSONObject obj) {
        int nRes = super.parse(obj);
        if (0 != nRes) {
            return nRes;
        }

        // parse property list
        nRes = mPropList.parseList(obj);
        if (0 != nRes) {
            return nRes;
        }

        return 0;
    }

    @Override
    public String DebugString() {
        super.DebugString();
        if (null != mPropList) {
            mString += mPropList.DebugList();
        }
        return mString;
    }

    @Override
    public int GetTotalNumber() {
        if (null != mPropList) {
            return mPropList.GetTotalNumber();
        }
        return 0;
    }

    @Override
    public int GetFetchedNumber() {
        if (null != mPropList) {
            return mPropList.GetFetchedNumber();
        }
        return 0;
    }

    @Override
    public ArrayList<Object> GetList() {
        if (null != mPropList) {
            return mPropList.GetList();
        }
        return null;
    }

    class PropertyList extends ResList {

        PropertyList() {
            mForceGetList = false;
        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("Properties");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    ProperryInfo newProp = new ProperryInfo(array.getJSONObject(n));
                    if (null == newProp) {
                        return -2;
                    }
                    mList.add(newProp);
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
            return ((ProperryInfo)item).ListItemInfo2String();
        }

        class ProperryInfo implements IApiResults.IPropertyInfo, InternalDefines.IListItemInfoInner {
            private int       mId         = 0;
            private String    mName       = "";
            private String    mAddress    = "";
            private String    mDesciption = "";

            ProperryInfo(JSONObject obj) {
                try {
                    mId = obj.getInt("Id");
                    mName = obj.getString("PropName");
                    mAddress = obj.getString("PropAddress");
                    mDesciption = obj.getString("PropDesc");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int GetId() { return mId; }
            @Override
            public String GetName() { return mName; }
            @Override
            public String GetAddress() { return mAddress; }
            @Override
            public String GetDesc() { return mDesciption; }

            @Override
            public String ListItemInfo2String() {
                return " id: " + mId + ", name: " + mName + ", addr: " + mAddress + ", desc: " + mDesciption;
            }
        }
    }
}