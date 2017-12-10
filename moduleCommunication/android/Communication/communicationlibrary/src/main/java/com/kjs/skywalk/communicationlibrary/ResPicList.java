package com.kjs.skywalk.communicationlibrary;

import android.nfc.Tag;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/5/14.
 */

class ResPicList extends ResBase implements IApiResults.IResultList {
    String TAG = getClass().getSimpleName();
    private PicInfoList mList = null;
    private     PicInfoList mList       = null;
    protected   int         mPicType    = -1;
    protected   int         mPicSubType = -1;

    ResPicList(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mList = new PicInfoList();
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
        //
        try {
            mPicType    = obj.getInt("PicType");
            mPicSubType = obj.getInt("SubType");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // parse picture list
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

    /////////////////////////////////////////////////////////////////////////////////////////////
    //
    class PicInfoList extends ResList {
        PicInfoList() {
            mForceGetList = true;   // list without properties: total & fetched
        }
        @Override
        public int parseListItems(JSONObject obj) {
            String Fn = "[PicInfoList.parseListItems] ";
            try {
                JSONArray array = obj.getJSONArray("Pics");
                if (null == array) {
                    Log.d(TAG, Fn + "No picture attached");
                    return 0;
                }
                for (int n = 0; n < array.length(); n++) {
                    PicInfo newItem = new PicInfo(array.getJSONObject(n));
                    if (null == newItem) {
                        return -2;
                    }
                    mList.add(newItem);
                }
                mTotal = array.length();
            } catch (JSONException e) {
                Throwable t = e.getCause();
                String s = e.getMessage();
                if (s.indexOf("Value null at Pics") >= 0) {
                    Log.d(TAG, Fn + "No picture attached");
                    return 0;
                }
                e.printStackTrace();
                return -3;
            }

            return 0;
        }

        /////////////////////////////////////////////////////////////////////////////////////////
        //
        class PicInfo implements IApiResults.IPicInfo, IApiResults.IPicUrls, InternalDefines.IListItemInfoInner {
            private int     mId         = 0;
            private String  mDesc       = "";
            private int     mType       = -1;
            private String  mChecksum   = "";
            private String  mUrlSmall   = null;
            private String  mUrlMiddle  = null;
            private String  mUrlLarge   = null;

            PicInfo(JSONObject obj) {
                try {
                    mId         = obj.getInt("Id");
                    mDesc       = obj.getString("Desc");
                    mType       = obj.getInt("SubType");
                    mChecksum   = obj.getString("Checksum");

                    JSONObject jsUrl = obj.getJSONObject("Urls");
                    mUrlSmall = jsUrl.getString("Url_s");
                    mUrlMiddle  = jsUrl.getString("Url_m");
                    mUrlLarge = jsUrl.getString("Url_l");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int GetId() {
                return mId;
            }

            @Override
            public String GetDesc() {
                return mDesc;
            }

            @Override
            public int GetType() {
                return mType;
            }

            @Override
            public String GetChecksum() {
                return mChecksum;
            }

            @Override
            public String ListItemInfo2String() {
                String strInfo = "";
                strInfo += String.format("id:%d, desc: %s, type:%s, checksum: %s\n",
                                        GetId(), GetDesc(), GetType(), GetChecksum());
                strInfo += "URLs\n";
                strInfo += "    small:    " + GetSmallPicture() + "\n";
                strInfo += "    moderate: " + GetMiddlePicture() + "\n";
                strInfo += "    Large:    " + GetLargePicture() + "\n";
                return strInfo;
            }

            @Override
            public int GetPicId() {
                return GetId();
            }

            @Override
            public String GetSmallPicture() {
                if (null == mUrlSmall || mUrlSmall.isEmpty()) {
                    return null;
                }
                return CUtilities.PicFullUrl(mUrlSmall);
            }

            @Override
            public String GetMiddlePicture() {
                if (null == mUrlMiddle || mUrlMiddle.isEmpty()) {
                    return null;
                }
                return CUtilities.PicFullUrl(mUrlMiddle);
            }

            @Override
            public String GetLargePicture() {
                if (null == mUrlLarge || mUrlLarge.isEmpty()) {
                    return null;
                }
                return CUtilities.PicFullUrl(mUrlLarge);
            }
        }
    }
}
