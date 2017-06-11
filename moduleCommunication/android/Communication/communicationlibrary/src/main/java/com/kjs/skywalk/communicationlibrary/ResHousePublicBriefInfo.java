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
    private HouseDigestInfo mDigestInfo = null;

    ResHousePublicBriefInfo(int nErrCode, JSONObject obj) {
        super(nErrCode/*, obj*/);
        parse(obj);
    }

    protected int parseResult(JSONObject obj) {
        JSONObject objDigest = null;
        try {
            objDigest = obj.getJSONObject("HouseDigest");
            mDigestInfo = new HouseDigestInfo(objDigest);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public int GetHouseId() { return (null != mDigestInfo) ? mDigestInfo.mHouseId : 0; }
    @Override
    public String GetProperty() { return (null != mDigestInfo) ? mDigestInfo.mPropertyName : null; }
    @Override
    public String GetPropertyAddr() { return (null != mDigestInfo) ? mDigestInfo.mPropertyAddr : null; }
    @Override
    public int GetBedrooms() { return (null != mDigestInfo) ? mDigestInfo.mBedrooms : 0; }
    @Override
    public int GetLivingrooms() { return (null != mDigestInfo) ? mDigestInfo.mLivingRooms : 0; }
    @Override
    public int GetBathrooms() { return (null != mDigestInfo) ? mDigestInfo.mBathrooms : 0; }
    @Override
    public int GetAcreage() { return (null != mDigestInfo) ? mDigestInfo.mAcreage : 0; }
    @Override
    public int GetRental() { return (null != mDigestInfo) ? mDigestInfo.mRental : 0; }
    @Override
    public int GetPricing() { return (null != mDigestInfo) ? mDigestInfo.mPricing : 0; }
    @Override
    public int GetCoverImage() { return (null != mDigestInfo) ? mDigestInfo.mCoverImg : 0; }

    @Override
    public String GetCoverImageUrlM() { return (null == mDigestInfo) ? null : mDigestInfo.mCovImgUrl_m; }

    @Override
    public String DebugString() {
        super.DebugString();
        if (null != mDigestInfo) {
            mString += mDigestInfo.DebugString();
        }

        return mString;
    }

    @Override
    public int GetTotalNumber() { return (null == mDigestInfo) ? 0 : mDigestInfo.GetTotalNumber(); }

    @Override
    public int GetFetchedNumber() { return (null == mDigestInfo) ? 0 : mDigestInfo.GetFetchedNumber(); }

    @Override
    public String GetCoverImageUrlS() { return (null == mDigestInfo) ? null : mDigestInfo.mCovImgUrl_s; }

    @Override
    public ArrayList<Object> GetList() { return (null == mDigestInfo) ? null : mDigestInfo.GetList(); }
}

class HouseDigestInfo implements IApiResults.IResultList {
    public int     mHouseId        = 0;
    public String  mPropertyName   = "";
    public String  mPropertyAddr   = "";
    public int     mBedrooms       = 0;
    public int     mLivingRooms    = 0;
    public int     mBathrooms      = 0;
    public int     mAcreage        = 0;
    public int     mRental         = 0;
    public int     mPricing        = 0;
    public int     mCoverImg       = 0;
    public String  mCovImgUrl_s    = null;
    public String  mCovImgUrl_m    = null;
    public TagList mTagList        = null;

    HouseDigestInfo(JSONObject objDigest) {
        mTagList = new TagList();
        parse(objDigest);
    }

    private int parse(JSONObject objDigest) {
        try {
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
            mCovImgUrl_s    = objDigest.getString("CovImgUrlS");
            mCovImgUrl_m    = objDigest.getString("CovImgUrlM");
            if (null != mCovImgUrl_s && !mCovImgUrl_s.isEmpty()) {
                mCovImgUrl_s = CUtilities.PicFullUrl(mCovImgUrl_s);
            }
            if (null != mCovImgUrl_m && !mCovImgUrl_m.isEmpty()) {
                mCovImgUrl_m = CUtilities.PicFullUrl(mCovImgUrl_m);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        // parse house tag list
        return mTagList.parseList(objDigest);
    }

    @Override
    public int GetTotalNumber() { return (null != mTagList) ? mTagList.GetTotalNumber() : 0; }

    @Override
    public int GetFetchedNumber() { return (null != mTagList) ? mTagList.GetFetchedNumber() : 0; }

    @Override
    public ArrayList<Object> GetList() { return (null != mTagList) ? mTagList.GetList() : null; }

    public String DebugString() {
        String sDebugString = "";
        sDebugString += (" house id: " + mHouseId + "\n");
        sDebugString += (" property: " + mPropertyName + "\n");
        sDebugString += (" property address: " + mPropertyAddr + "\n");
        sDebugString += (" Bedrooms: " + mBedrooms + "\n");
        sDebugString += (" LivingRooms: " + mLivingRooms + "\n");
        sDebugString += (" Bathrooms: " + mBathrooms + "\n");
        sDebugString += (" Acreage: " + mAcreage + "\n");
        sDebugString += (" Rental: " + mRental + "\n");
        sDebugString += (" Pricing: " + mPricing + "\n");
        sDebugString += (" CoverImg: " + mCoverImg + "\n");
        sDebugString += (" Image URL(s): " + mCovImgUrl_s + "\n");
        sDebugString += (" Image URL(m): " + mCovImgUrl_m + "\n");

        if (null != mTagList) {
            sDebugString += mTagList.DebugList();
        }

        return sDebugString;
    }

    /*
    *       class TagList
     */
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

        /*
        *       class HouseTag
         */
        class HouseTag implements IApiResults.IHouseTag, InternalDefines.IListItemInfoInner {
            private int     mTagId      = 0;
            private String  mTagName    = "";

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
