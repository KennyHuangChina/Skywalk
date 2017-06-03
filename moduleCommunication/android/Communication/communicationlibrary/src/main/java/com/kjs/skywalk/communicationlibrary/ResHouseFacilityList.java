package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Created by kenny on 2017/3/17.
 */

class ResHouseFacilityList extends ResBase implements IApiResults.IResultList {
    private FacilityList mList = null;

    ResHouseFacilityList(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mList = new FacilityList();
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();
        if (null != mList) {
            mString += mList.DebugList();
        }
        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        // parse facility list
        if (null != mList) {
            return mList.parseList(obj);
        }
        return super.parseResult(obj);
    }

    @Override
    public int GetTotalNumber() {
        if (null != mList) {
            return mList.GetTotalNumber();
        }
        return 0;
    }

    @Override
    public int GetFetchedNumber() {
        if (null != mList) {
            return mList.GetFetchedNumber();
        }
        return 0;
    }

    @Override
    public ArrayList<Object> GetList() {
        if (null != mList) {
            return mList.GetList();
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    class FacilityList extends ResList {

        FacilityList() {
            mForceGetList = true;   // list without properties: total & fetched
        }
        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("Facilities");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    FacilityInfo newItem = new FacilityInfo(array.getJSONObject(n));
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

        /////////////////////////////////////////////////////////////////////////////////////////
        //
        class FacilityInfo implements IApiResults.IHouseFacilityInfo, InternalDefines.IListItemInfoInner {
            private int     mId     = 0;
            private String  mName   = "";
            private String  mType   = "";
            private String  mIcon   = "";
            private int     mQty    = 0;
            private String  mDesc   = "";

            FacilityInfo(JSONObject obj) {
                try {
                    mId     = obj.getInt("Id");
                    mName   = obj.getString("Name");
                    mType   = obj.getString("Type");
                    mIcon   = obj.getString("Icon");
                    mQty    = obj.getInt("Qty");
                    mDesc   = obj.getString("Desc");
                    if (!mIcon.isEmpty()) {
                        mIcon = PicFullUrl(mIcon);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int GetId() {
                return mId;
            }

            @Override
            public String GetName() {
                return mName;
            }

            @Override
            public String GetType() {
                return mType;
            }

            @Override
            public String GetIcon() {
                return mIcon;
            }

            @Override
            public int GetQty() {
                return mQty;
            }

            @Override
            public String GetDesc() {
                return mDesc;
            }

            @Override
            public String ListItemInfo2String() {
                return " id: " + GetId() + ", name: " + GetName() + ", mType: " + GetType()
                        + ", qty: " + GetQty() + ", desc: " + GetDesc() + ", icon: " + GetIcon();
            }
        }
    }
}
