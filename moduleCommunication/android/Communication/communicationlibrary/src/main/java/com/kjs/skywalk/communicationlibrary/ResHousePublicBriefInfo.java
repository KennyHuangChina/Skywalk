package com.kjs.skywalk.communicationlibrary;

//import ResList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/23.
 */

public class ResHousePublicBriefInfo extends ResList {
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


    ResHousePublicBriefInfo(JSONObject obj) {
        mForceGetList = true;
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
        JSONArray array = null;
        try {
            array = obj.getJSONArray("Tags");
            if (null == array) {
                return -1;
            }
            for (int i = 0; i < array.length(); i++) {
                HouseTag newTag = new HouseTag(array.getJSONObject(i));
                if (null == newTag) {
                    return -2;
                }
                mList.add(newTag);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String toString() {
        super.toString();

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

    protected String ListString() {
        for (int i = 0; i < mList.size(); i++) {
            mString += "   Tag(" + (i + 1) + ") -- " + mList.get(i).toString();
        }
        return "";
    }

    public class HouseTag {
        int mTagId = 0;
        String mTagName = "";

        HouseTag(JSONObject obj) {
            try {
                mTagId = obj.getInt("TagId");
                mTagName = obj.getString("TagDesc");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public int GetId() { return mTagId; }
        public String GetName() { return mTagName; }

        public String toString() {
            String str = "" + mTagId + ": " + mTagName + "\n";
            return str;
        }
    }
}