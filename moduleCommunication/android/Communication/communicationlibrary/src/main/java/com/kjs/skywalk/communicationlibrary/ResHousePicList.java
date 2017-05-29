package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/5/14.
 */

class ResHousePicList extends ResBase implements IApiResults.IResultList {
    private HousePicList mList = null;

    ResHousePicList(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mList = new HousePicList();
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
        // parse picture list
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

    /////////////////////////////////////////////////////////////////////////////////////////////
    //
    class HousePicList extends ResList {
        HousePicList() {
            mForceGetList = true;   // list without properties: total & fetched
        }
        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("Pics");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    HousePicInfo newItem = new HousePicInfo(array.getJSONObject(n));
                    if (null == newItem) {
                        return -2;
                    }
                    mList.add(newItem);
                }
                mTotal = array.length();
            } catch (JSONException e) {
                e.printStackTrace();
                return -3;
            }

            return 0;
        }

        /////////////////////////////////////////////////////////////////////////////////////////
        //
        class HousePicInfo implements IApiResults.IHousePicInfo, InternalDefines.IListItemInfoInner {
            private int     mId         = 0;
            private String  mDesc       = "";
            private int     mType       = -1;
            private String  mChecksum   = "";

            HousePicInfo(JSONObject obj) {
                try {
                    mId         = obj.getInt("Id");
                    mDesc       = obj.getString("Desc");
                    mType       = obj.getInt("SubType");
                    mChecksum   = obj.getString("Checksum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int GetId() {
                return mId;
            }

            @Override
            public String GetDesc() {
                return mDesc;
            }

            @Override
            public int GetType() {
                return mType;
            }

            @Override
            public String GetChecksum() {
                return mChecksum;
            }

            @Override
            public String ListItemInfo2String() {
                return " id: " + mId + ", desc: " + mDesc + ", type: " + mType + ", checksum:" + mChecksum;
            }
        }
    }
}
