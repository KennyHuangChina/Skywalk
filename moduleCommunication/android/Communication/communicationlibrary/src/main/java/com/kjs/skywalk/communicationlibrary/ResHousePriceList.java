package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by kenny on 2017/6/10.
 */

class ResHousePriceList extends ResBase implements IApiResults.IResultList {
    private HousePriceList mList = null;

    ResHousePriceList(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mList = new HousePriceList();
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
        // parse price list
        if (null != mList) {
            return mList.parseList(obj);
        }
        return super.parseResult(obj);
    }

    @Override
    public int GetTotalNumber() {
        return (null != mList) ? mList.GetTotalNumber() : 0;
    }

    @Override
    public int GetFetchedNumber() {
        return (null != mList) ? mList.GetFetchedNumber() : 0;
    }

    @Override
    public ArrayList<Object> GetList() {
        return (null != mList) ? mList.GetList() : null;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //
    class HousePriceList extends ResList {
        HousePriceList () {
            //mForceGetList = true;   // list without properties: total & fetched
        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("Prices");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    HousePriceItem newItem = new HousePriceItem(array.getJSONObject(n));
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
        class HousePriceItem implements IApiResults.IHousePriceInfo, InternalDefines.IListItemInfoInner {
            private int     mId                 = 0;
            private int     mRentalTag          = 0;
            private int     mRentalMin          = 0;
            private boolean mRentalIncPropFee   = false;
            private int     mSellingPriceTag    = 0;
            private int     mSellingPriceMin    = 0;
            private String  mWho                = null;
            private String  mWhen               = null;

            HousePriceItem(JSONObject obj) {
                try {
                    mId                 = obj.getInt("Id");
                    mRentalTag          = obj.getInt("RentalTag");
                    mRentalMin          = obj.getInt("RentalMin");
                    mRentalIncPropFee   = obj.getBoolean("PropFee");
                    mSellingPriceTag    = obj.getInt("SalePriceTag");
                    mSellingPriceMin    = obj.getInt("SalePriceMin");
                    mWho                = obj.getString("Who");
                    mWhen               = obj.getString("When");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String ListItemInfo2String() {
                return String.format("id: %d, Rental(%d, %d, %b), Selling(%d, %d), %s, %s",
                        Id(), RentalTag(), RentalMin(), RentalIncPropFee(),
                        SellingPriceTag(), SellingPriceMin(), Who(), When());
            }

            @Override
            public int Id() {
                return mId;
            }

            @Override
            public int RentalTag() {
                return mRentalTag;
            }

            @Override
            public int RentalMin() {
                return mRentalMin;
            }

            @Override
            public boolean RentalIncPropFee() {
                return mRentalIncPropFee;
            }

            @Override
            public int SellingPriceTag() {
                return mSellingPriceTag;
            }

            @Override
            public int SellingPriceMin() {
                return mSellingPriceMin;
            }

            @Override
            public String Who() {
                return mWho;
            }

            @Override
            public String When() {
                return mWhen;
            }
        }
    }
}
