package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/3/12.
 */

class ResHouseDeliverables extends ResBase implements IApiResults.IResultList {
    private DeliverableList mList = null;

    ResHouseDeliverables(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mList = new DeliverableList();
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
        // parse deliverable list
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

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    class DeliverableList extends ResList {

        DeliverableList() {
            mForceGetList = true;   // list without properties: total & fetched
        }
        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("Deliverables");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    DeliverableInfo newItem = new DeliverableInfo(array.getJSONObject(n));
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
        class DeliverableInfo implements IApiResults.IDeliverableInfo,
                                            InternalDefines.IListItemInfoInner {
            private int     mId     = 0;
            private String  mName   = "";
            private int     mQty    = 0;
            private String  mDesc   = "";

            DeliverableInfo(JSONObject obj) {
                try {
                    mId     = obj.getInt("Id");
                    mName   = obj.getString("Name");
                    mQty    = obj.getInt("Qty");
                    mDesc   = obj.getString("Desc");
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
            public int GetQty() {
                return mQty;
            }

            @Override
            public String GetDesc() {
                return mDesc;
            }

            @Override
            public String ListItemInfo2String() {
                return " id: " + mId + ", name: " + mName + ", qty: " + mQty + ", desc: " + mDesc;
            }
        }
    }
}
