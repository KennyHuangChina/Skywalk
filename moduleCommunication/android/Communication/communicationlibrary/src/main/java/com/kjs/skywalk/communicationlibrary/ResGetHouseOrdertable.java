package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/6/22.
 */

class ResGetHouseOrdertable extends ResBase implements IApiResults.IResultList {

    private OrderTable mList = null;

    ResGetHouseOrdertable(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mList = new OrderTable();
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
        // parse order table
        return mList.parseList(obj);
    }

    @Override
    public int GetTotalNumber()  {
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

    ///////////////////////////////////////////////////////////////////////////////////////////
    //
    class OrderTable extends ResList {

        OrderTable() {
            mForceGetList = false;
        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("Orders");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    OrderTableItem newItem = new OrderTableItem(array.getJSONObject(n));
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
        class OrderTableItem implements IApiResults.IHouseOrdertable, InternalDefines.IListItemInfoInner {
            private int mOrderId = 0;
            private int mHouseId = 0;
            private String mSubscriber = null;
            private String mOrderTime = null;
            private String mCloseTime = null;

            OrderTableItem(JSONObject obj) {
                try {
                    mOrderId = obj.getInt("Id");
                    mHouseId = obj.getInt("House");
                    mSubscriber = obj.getString("Subscriber");
                    mOrderTime = obj.getString("OrderTime");
                    mCloseTime = obj.getString("CloseTime");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int OrderId() {
                return mOrderId;
            }

            @Override
            public int HouseId() {
                return mHouseId;
            }

            @Override
            public String Subscriber() {
                return mSubscriber;
            }

            @Override
            public String OrderTime() {
                return mOrderTime;
            }

            @Override
            public String CloseTime() {
                return mCloseTime;
            }

            @Override
            public String ListItemInfo2String() {
                return " id: " + OrderId() + ", House: " + HouseId() + ", Subscriber: " + Subscriber() +
                        ", Order: " + OrderTime() + ", Close: " + CloseTime();
            }
        }
    }
}
