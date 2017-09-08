package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/5/20.
 */

class ResGetHouseEventList extends ResBase implements IApiResults.IResultList {
    private EventList mEventList = null;

    ResGetHouseEventList(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mEventList = new EventList();
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
        // parse house event list
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

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //
    class EventList extends ResList {
//        EventList() {
//        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("EventLst");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    EventInfo newItem = new EventInfo(array.getJSONObject(n));
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

        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        class EventInfo extends SysMsgInfo implements InternalDefines.IListItemInfoInner {

            EventInfo(JSONObject jsonObject) {
                super(jsonObject);
            }
            @Override
            public String ListItemInfo2String() {
                String dbString = "";
                dbString += " id: " + mMsgId;
                dbString += "  Property: " + mProperty;
                dbString += ", " + mBuilding + "栋 " + mHouseNo + "室";
                // TODO:
//                dbString += ", Sender: " + mSender + ", Receiver:" + mReceiver;
//                dbString += ", Create: " + mCreateTime;
//                dbString += ", Read: " + mReadTime;
//                dbString += ", Type: " + mType;
//                dbString += ", Desc: " + mDesc;
//                dbString += ", Proc: " + mProcCount;

                return dbString;
            }
        }
    }
}
