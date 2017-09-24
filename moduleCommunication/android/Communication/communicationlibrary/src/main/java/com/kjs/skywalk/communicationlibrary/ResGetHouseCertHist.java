package com.kjs.skywalk.communicationlibrary;

import android.text.LoginFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kenny on 2017/9/18.
 */

class ResGetHouseCertHist extends ResBase implements IApiResults.IResultList {

    private HouseCertList mList = null;

    ResGetHouseCertHist(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mList = new HouseCertList();
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
        // parse house cert list
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

    ////////////////////////////////////////////////////////////////////////////////////////////
    //
    class HouseCertList extends ResList {

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("CertHist");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    HouseCertInfo newItem = new HouseCertInfo(array.getJSONObject(n));
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
        class HouseCertInfo implements IApiResults.IHouseCertInfo, InternalDefines.IListItemInfoInner {
            private int     mCertId     = 0;
            private int     mUserId     = 0;
            private String  mUsername   = null;
            private String  mUserPhone  = null;
            private Date    mCertTime   = null;
            private int     mCertStat   = -1;
            private String  mCertText   = null;

            HouseCertInfo(JSONObject obj) {
                try {
                    mCertId     = obj.getInt("Id");
                    mUserId     = obj.getInt("Uid");
                    mUsername   = obj.getString("User");
                    mUserPhone  = obj.getString("Phone");
                    mCertStat   = obj.getInt("Stat");
                    mCertText   = obj.getString("CertTxt");

                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    mCertTime = format.parse(obj.getString("Time"));

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String ListItemInfo2String() {
                return String.format("id:%d, User(%d) %s (Tel: %s), time:%s state:%d, text: %s",
                                    CertId(), UserId(), UserName(), UserPhone(), CertTime().toString(), CertStat(), CertDesc());
            }

            @Override
            public int CertId() {
                return mCertId;
            }

            @Override
            public int UserId() {
                return mUserId;
            }

            @Override
            public String UserName() {
                return mUsername;
            }

            @Override
            public String UserPhone() {
                return mUserPhone;
            }

            @Override
            public Date CertTime() {
                return mCertTime;
            }

            @Override
            public int CertStat() {
                return mCertStat;
            }

            @Override
            public String CertDesc() {
                return mCertText;
            }
        }
    }
}
