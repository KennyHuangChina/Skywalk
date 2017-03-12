package com.kjs.skywalk.communicationlibrary;

//import ResBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/2/23.
 */

class ResList implements InternalDefines.IListInner {
    private int mTotal = 0;
    private int mFetched = 0;
    protected ArrayList<Object> mList = null;
    protected boolean mForceGetList = false;    // true: list without properties: total & fetched; false - list with properties: total & fetched

    ResList() {
        mList = new ArrayList();
    }

    protected String DebugList() {
        String strList = "";
        if (!mForceGetList) {
            strList += ("Total: " + mTotal + "\n");
            strList += ("Fetched: " + mFetched + "\n");
        }

        for (int n = 0; n < mList.size(); n++) {
            strList += ("  <" + n + "> " + getListItem2String(mList.get(n)) + "\n");
        }

        return strList;
    }

    protected int parseList(JSONObject obj) {
        if (null == obj) {
            return -1;
        }
        if (!mForceGetList) {
            try {
                mTotal = obj.getInt("Total");
                mFetched = obj.getInt("Count");
            } catch (JSONException e) {
                e.printStackTrace();
                return -2;
            }
        }

        if (mForceGetList || mFetched > 0) {
            return parseListItems(obj);
        }

        return 0;
    }

    protected int GetTotalNumber() {
        return (mTotal > 0) ? mTotal : (mForceGetList ? mList.size() : 0);
    }
    protected int GetFetchedNumber() {
        return (mFetched > 0) ? mFetched : (mForceGetList ? mList.size() : 0);
    }
    protected ArrayList<Object> GetList() {
        return mList;
    }

    @Override
    public int parseListItems(JSONObject obj) {
        return 0;
    }

    protected String getListItem2String(Object item) {
        if (null == item) {
            return "";
        }
        return ((InternalDefines.IListItemInfoInner)item).ListItemInfo2String();
    }
}
