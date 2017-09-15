package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/5/20.
 */

class ResGetSysMsgList extends ResBase implements IApiResults.IResultList {
    private SysMsgList mSysMsgList = null;

    ResGetSysMsgList(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mSysMsgList = new SysMsgList();
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();
        if (null != mSysMsgList) {
            mString += mSysMsgList.DebugList();
        }
        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        // parse system message list
        return mSysMsgList.parseList(obj);
    }

    @Override
    public int GetTotalNumber() {
        if (null != mSysMsgList) {
            return mSysMsgList.GetTotalNumber();
        }
        return 0;
    }

    @Override
    public int GetFetchedNumber() {
        if (null != mSysMsgList) {
            return mSysMsgList.GetFetchedNumber();
        }
        return 0;
    }

    @Override
    public ArrayList<Object> GetList() {
        if (null != mSysMsgList) {
            return mSysMsgList.GetList();
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //
    class SysMsgList extends ResList {
//        SysMsgList() {
//        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("Msgs");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    SysMsgInfo newItem = new SysMsgInfo(array.getJSONObject(n));
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
