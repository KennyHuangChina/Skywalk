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
    public int GetHouseId() { return (null != mDigestInfo) ? mDigestInfo.GetHouseId() : 0; }
    @Override
    public String GetProperty() { return (null != mDigestInfo) ? mDigestInfo.GetProperty() : null; }
    @Override
    public String GetPropertyAddr() { return (null != mDigestInfo) ? mDigestInfo.GetPropertyAddr() : null; }
    @Override
    public int GetBedrooms() { return (null != mDigestInfo) ? mDigestInfo.GetBedrooms() : 0; }
    @Override
    public int GetLivingrooms() { return (null != mDigestInfo) ? mDigestInfo.GetLivingrooms() : 0; }
    @Override
    public int GetBathrooms() { return (null != mDigestInfo) ? mDigestInfo.GetBathrooms() : 0; }
    @Override
    public int GetAcreage() { return (null != mDigestInfo) ? mDigestInfo.GetAcreage() : 0; }
    @Override
    public int GetRental() { return (null != mDigestInfo) ? mDigestInfo.GetRental() : 0; }
    @Override
    public int GetPricing() { return (null != mDigestInfo) ? mDigestInfo.GetPricing() : 0; }
    @Override
    public int GetCoverImage() { return (null != mDigestInfo) ? mDigestInfo.GetCoverImage() : 0; }

    @Override
    public String GetCoverImageUrlM() { return (null == mDigestInfo) ? null : mDigestInfo.GetCoverImageUrlM(); }

    @Override
    public int GetTotalNumber() { return (null == mDigestInfo) ? 0 : mDigestInfo.GetTotalNumber(); }

    @Override
    public int GetFetchedNumber() { return (null == mDigestInfo) ? 0 : mDigestInfo.GetFetchedNumber(); }

    @Override
    public String GetCoverImageUrlS() { return (null == mDigestInfo) ? null : mDigestInfo.GetCoverImageUrlS(); }

    @Override
    public ArrayList<Object> GetList() { return (null == mDigestInfo) ? null : mDigestInfo.GetList(); }

    @Override
    public String DebugString() {
        super.DebugString();
        if (null != mDigestInfo) {
            mString += mDigestInfo.DebugString();
        }

        return mString;
    }
}

class HouseDigestInfo implements IApiResults.IHouseDigest, IApiResults.IResultList {
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
    private String  mCovImgUrl_s    = null;
    private String  mCovImgUrl_m    = null;
    private TagList mTagList        = null;

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
        sDebugString += (" house id: " + GetHouseId() + "\n");
        sDebugString += (" property: " + GetProperty() + "\n");
        sDebugString += (" property address: " + GetPropertyAddr() + "\n");
        sDebugString += (" Bedrooms: " + GetBedrooms() + "\n");
        sDebugString += (" LivingRooms: " + GetLivingrooms() + "\n");
        sDebugString += (" Bathrooms: " + GetBathrooms() + "\n");
        sDebugString += (" Acreage: " + GetAcreage() + "\n");
        sDebugString += (" Rental: " + GetRental() + "\n");
        sDebugString += (" Pricing: " + GetPricing() + "\n");
        sDebugString += (" CoverImg: " + GetCoverImage() + "\n");
        sDebugString += (" Image URL(s): " + GetCoverImageUrlS() + "\n");
        sDebugString += (" Image URL(m): " + GetCoverImageUrlM() + "\n");

        if (null != mTagList) {
            sDebugString += mTagList.DebugList();
        }

        return sDebugString;
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
    public String GetCoverImageUrlS() { return mCovImgUrl_s; }

    @Override
    public String GetCoverImageUrlM() { return mCovImgUrl_m; }

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
