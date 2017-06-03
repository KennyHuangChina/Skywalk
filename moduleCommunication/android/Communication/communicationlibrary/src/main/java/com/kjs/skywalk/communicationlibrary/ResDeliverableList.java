package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/6/2.
 */

class ResDeliverableList extends ResBase implements IApiResults.IResultList {

    private DeliverableList mList = new DeliverableList();

    ResDeliverableList(int nErrCode, JSONObject jObject) {
        super(nErrCode);
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

    /////////////////////////////////////////////////////////////////////////////////////////
    //
    class DeliverableList extends ResList {

        DeliverableList() {
            mForceGetList = true;   // list without properties: total & fetched
        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("List");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    DeliverableItem newItem = new DeliverableItem(array.getJSONObject(n));
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

        ////////////////////////////////////////////////////////////////////////////////////////////////
        //
        class DeliverableItem implements IApiResults.IDeliverableItem, InternalDefines.IListItemInfoInner {
            private int mId = -1;
            private String mName = "";
            private int mPic = 0;

            DeliverableItem(JSONObject obj) {
                try {
                    mId = obj.getInt("Id");
                    mName = obj.getString("Name");
                    mPic = obj.getInt("Pic");
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
            public int GetPic() {
                return mPic;
            }

            @Override
            public String ListItemInfo2String() {
                return " id: " + mId + ", name: " + mName + ", pic:" + mPic;
            }
        }
    }
}
