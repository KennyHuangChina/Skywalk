package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/5/18.
 */

class ResGetEventProcList extends ResBase implements IApiResults.IResultList {

    private EventProcList mEventList = null;

    ResGetEventProcList(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mEventList = new EventProcList();
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();
        if (null != mEventList) {
            mString += mEventList.DebugList();
        }
        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        // parse event proc list
        return mEventList.parseList(obj);
    }

    @Override
    public int GetTotalNumber() {
        if (null != mEventList) {
            return mEventList.GetTotalNumber();
        }
        return 0;
    }

    @Override
    public int GetFetchedNumber() {
        if (null != mEventList) {
            return mEventList.GetFetchedNumber();
        }
        return 0;
    }

    @Override
    public ArrayList<Object> GetList() {
        if (null != mEventList) {
            return mEventList.GetList();
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    //
    class EventProcList extends ResList {
//        EventProcList() {
//        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("ProcList");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    EventProcInfo newItem = new EventProcInfo(array.getJSONObject(n));
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

        ///////////////////////////////////////////////////////////////////////////////////////////
        //
        class EventProcInfo implements IApiResults.IEventProcInfo, InternalDefines.IListItemInfoInner {
            private int     mId         = 0;
            private String  mUser       = "";
            private String  mTime       = "";
            private String  mOperation  = "";
            private String  mDesciption = "";

            EventProcInfo(JSONObject obj) {
                try {
                    mId         = obj.getInt("Id");
                    mUser       = obj.getString("User");
                    mTime       = obj.getString("Time");
                    mOperation  = obj.getString("Op");
                    mDesciption = obj.getString("Desc");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int Id() {
                return mId;
            }

            @Override
            public String User() {
                return mUser;
            }

            @Override
            public String Time() {
                return mTime;
            }

            @Override
            public String Operation() {
                return mOperation;
            }

            @Override
            public String Desc() {
                return mDesciption;
            }

            @Override
            public String ListItemInfo2String() {
                return " id: " + mId + ", user: " + mUser + ", time: " + mTime + ", op:" + mOperation + ", desc: " + mDesciption;
            }
        }
    }
}
