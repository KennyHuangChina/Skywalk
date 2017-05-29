package com.kjs.skywalk.communicationlibrary;

//import ResList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/2/23.
 */

class ResHousePublicBriefInfo extends ResBase implements IApiResults.IHouseDigest, IApiResults.IResultList {
    private int     mHouseId        = 0;
    private String  mPropertyName   = "";
    private String  mPropertyAddr   = "";
    private int     mBedrooms       = 0;
    private int     mLivingRooms    = 0;
    private int     mBathrooms      = 0;
    private int     mAcreage        = 0;
    private int     mRental         = 0;
    private int     mPricing        = 0;
    private int     mCoverImg       = 0;
    private TagList mTagList        = null;

    ResHousePublicBriefInfo(int nErrCode, JSONObject obj) {
        super(nErrCode);
        mTagList = new TagList();
        parse(obj);
    }

    protected int parseResult(JSONObject obj) {
        JSONObject objDigest = null;
        try {
            objDigest = obj.getJSONObject("HouseDigest");
            // parse house digest info
            mHouseId        = objDigest.getInt("Id");
            mPropertyName   = objDigest.getString("Property");
            mPropertyAddr   = objDigest.getString("PropertyAddr");
            mBedrooms       = objDigest.getInt("Bedrooms");
            mLivingRooms    = objDigest.getInt("Livingrooms");
            mBathrooms      = objDigest.getInt("Bathrooms");
            mAcreage        = objDigest.getInt("Acreage");
            mRental         = objDigest.getInt("Rental");
            mPricing        = objDigest.getInt("Pricing");
            mCoverImg       = objDigest.getInt("CoverImg");

        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        // parse house tag list
        return mTagList.parseList(objDigest);
    }

    @Override
    public int GetHouseId() { return mHouseId; }
    @Override
    public String GetProperty() { return mPropertyName; }
    @Override
    public String GetPropertyAddr() { return mPropertyAddr; }
    @Override
    public int GetBedrooms() { return mBedrooms; }
    @Override
    public int GetLivingrooms() { return mLivingRooms; }
    @Override
    public int GetBathrooms() { return mBathrooms; }
    @Override
    public int GetAcreage() { return mAcreage; }
    @Override
    public int GetRental() { return mRental; }
    @Override
    public int GetPricing() { return mPricing; }
    @Override
    public int GetCoverImage() { return mCoverImg; }

    @Override
    public String DebugString() {
        super.DebugString();

        mString += (" house id: " + mHouseId + "\n");
        mString += (" property: " + mPropertyName + "\n");
        mString += (" property address: " + mPropertyAddr + "\n");
        mString += (" Bedrooms: " + mBedrooms + "\n");
        mString += (" LivingRooms: " + mLivingRooms + "\n");
        mString += (" Bathrooms: " + mBathrooms + "\n");
        mString += (" Acreage: " + mAcreage + "\n");
        mString += (" Rental: " + mRental + "\n");
        mString += (" Pricing: " + mPricing + "\n");
        mString += (" CoverImg: " + mCoverImg + "\n");

        if (null != mTagList) {
            mString += mTagList.DebugList();
        }

        return mString;
    }

    @Override
    public int GetTotalNumber() {
        return 0;
    }

    @Override
    public int GetFetchedNumber() {
        return 0;
    }

    @Override
    public ArrayList<Object> GetList() {
        return null;
    }

    class TagList extends ResList {
        TagList() {
            mForceGetList = true;   // list without properties: total & fetched
        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("Tags");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    HouseTag newProp = new HouseTag(array.getJSONObject(n));
                    if (null == newProp) {
                        return -2;
                    }
                    mList.add(newProp);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return -3;
            }

            return 0;
        }

        class HouseTag implements IApiResults.IHouseTag, InternalDefines.IListItemInfoInner {
            int mTagId = 0;
            String mTagName = "";

            HouseTag(JSONObject obj) {
                super();
                try {
                    mTagId = obj.getInt("TagId");
                    mTagName = obj.getString("TagDesc");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int GetTagId() { return mTagId; }
            @Override
            public String GetName() { return mTagName; }

            public String toString() {
                String str = "" + mTagId + ": " + mTagName + "\n";
                return str;
            }
            @Override
            public String ListItemInfo2String() {
                return "" + mTagId + ": " + mTagName + "\n";
            }
        }
    }
}

