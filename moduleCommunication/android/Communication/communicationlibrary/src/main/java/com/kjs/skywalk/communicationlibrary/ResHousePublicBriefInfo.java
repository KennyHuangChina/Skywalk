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
    int mHouseId = 0;
    String mPropertyName = "";
    String mPropertyAddr = "";
    int mBedrooms = 0;
    int mLivingRooms = 0;
    int mBathrooms = 0;
    int mAcreage = 0;
    int mRental = 0;
    int mPricing = 0;
    int mCoverImg = 0;
    TagList mList = null;

    ResHousePublicBriefInfo(JSONObject obj) {
        super(obj);
//        mForceGetList = true;
        mList = new TagList();
        parse(obj);
    }

    protected int parse(JSONObject obj) {
        JSONObject objDigest = null;
        try {
            objDigest = obj.getJSONObject("HouseDigest");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
        if (null == objDigest) {
            return -1;
        }

        try {
            mHouseId = objDigest.getInt("Id");
            mPropertyName = objDigest.getString("Property");
            mPropertyAddr = objDigest.getString("PropertyAddr");
            mBedrooms = objDigest.getInt("Bedrooms");
            mLivingRooms = objDigest.getInt("Livingrooms");
            mBathrooms = objDigest.getInt("Bathrooms");
            mAcreage = objDigest.getInt("Acreage");
            mRental = objDigest.getInt("Rental");
            mPricing = objDigest.getInt("Pricing");
            mCoverImg = objDigest.getInt("CoverImg");

        } catch (JSONException e) {
            e.printStackTrace();
            return -2;
        }

        return super.parse(objDigest);
    }

    protected int parseList(JSONObject obj) {
//        JSONArray array = null;
//        try {
//            array = obj.getJSONArray("Tags");
//            if (null == array) {
//                return -1;
//            }
//            for (int i = 0; i < array.length(); i++) {
//                HouseTag newTag = new HouseTag(array.getJSONObject(i));
//                if (null == newTag) {
//                    return -2;
//                }
//                mList.add(newTag);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return 0;
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
        return mString;
    }

//    @Override
    protected String ListString() {
//        for (int i = 0; i < mList.size(); i++) {
//            mString += "   Tag(" + (i + 1) + ") -- " + mList.get(i).toString();
//        }
        return "";
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
            super();
        }

        class HouseTag implements IApiResults.IHouseTag {
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
            public int GetId() { return mTagId; }
            @Override
            public String GetName() { return mTagName; }

            public String toString() {
                String str = "" + mTagId + ": " + mTagName + "\n";
                return str;
            }
        }
    }
}

