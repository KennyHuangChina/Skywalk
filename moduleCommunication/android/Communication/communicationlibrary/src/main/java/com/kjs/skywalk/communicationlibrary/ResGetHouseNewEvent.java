package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/5/15.
 */

class ResGetHouseNewEvent extends ResBase implements IApiResults.IResultList {
    private HouseNewEventList mList = null;

    ResGetHouseNewEvent(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mList = new HouseNewEventList();
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
        // parse house new event list
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
    class HouseNewEventList extends ResList {

        HouseNewEventList() {
            mForceGetList = true;   // list without properties: total & fetched
        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("Houses");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    HouseNewEventItem newItem = new HouseNewEventItem(array.getJSONObject(n));
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
        class HouseNewEventItem implements IApiResults.IHouseNewEvent, InternalDefines.IListItemInfoInner {
            private int mHouseId = 0;
            private String mProperty = "";
            private int mBuilding = 0;
            private String mHouseNo = "";
            private int mCoverImg = 0;
            private int mEventCount = 0;
            private String mTime = "";
            private String mDesc = "";

            HouseNewEventItem(JSONObject obj) {
                try {
                    mHouseId = obj.getInt("HouseId");
                    mProperty = obj.getString("Property");
                    mBuilding = obj.getInt("Building");
                    mHouseNo = obj.getString("HouseNo");
                    mCoverImg = obj.getInt("Picture");
                    mEventCount = obj.getInt("EventCnt");
                    mTime = obj.getString("Time");
                    mDesc = obj.getString("Desc");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int GetHouseId() {
                return mHouseId;
            }

            @Override
            public String GetProperty() {
                return mProperty;
            }

            @Override
            public int GetBuildingNo() {
                return mBuilding;
            }

            @Override
            public String GetHouseNo() {
                return mHouseNo;
            }

            @Override
            public int GetCoverImageId() {
                return mCoverImg;
            }

            @Override
            public int GetNewEventCount() {
                return mEventCount;
            }

            @Override
            public String GetEvenTime() {
                return mTime;
            }

            @Override
            public String GetDescription() {
                return mDesc;
            }

            @Override
            public String ListItemInfo2String() {
                return " house: " + mHouseId + ", property: " + mProperty + ", building: " + mBuilding +
                        ", house no: " + mHouseNo + ", cover image: " + mCoverImg + ", new event:" + mEventCount +
                        ", time:" + mTime + ", desc:" + mDesc;
            }
        }
    }
}
