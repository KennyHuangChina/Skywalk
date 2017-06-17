package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/5/31.
 */

class ResGetAgencyList extends ResBase implements IApiResults.IResultList {

    private AgencyList mAgencyList = null;

    ResGetAgencyList(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mAgencyList = new AgencyList();
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();
        if (null != mAgencyList) {
            mString += mAgencyList.DebugList();
        }
        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        // parse agency list
        return mAgencyList.parseList(obj);
    }

    @Override
    public int GetTotalNumber() {
        if (null != mAgencyList) {
            return mAgencyList.GetTotalNumber();
        }
        return 0;
    }

    @Override
    public int GetFetchedNumber() {
        if (null != mAgencyList) {
            return mAgencyList.GetFetchedNumber();
        }
        return 0;
    }

    @Override
    public ArrayList<Object> GetList() {
        if (null != mAgencyList) {
            return mAgencyList.GetList();
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //
    class AgencyList extends ResList {
//        AgencyList() {
//        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("Agencys");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    AgencyInfo newItem = new AgencyInfo(array.getJSONObject(n));
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
        class AgencyInfo implements IApiResults.IAgencyInfo, InternalDefines.IListItemInfoInner {
			private int     mId			    = 0;    // agency id
			private String  mName			= "";   // agency name
            private int     mSex			= 0;	// agency sex. 0 - male / 1 - female
            private String	mIDNo			= "";   // agency ID card
            private int     mProfessional	= 0;    // professional rank. 0 ~ 50 (0.0 ~ 5.0)
            private int     mAttitude       = 0;    // attitude rank. 0 ~50 (0.0 ~ 5.0)
            private String  mPhone          = "";   // agency phone number
            private String  mPortrait       = "";   // agency head portrait picture URL

            AgencyInfo(JSONObject obj) {
                try {
                    mId             = obj.getInt("Id");
                    mName           = obj.getString("Name");
                    mSex            = obj.getInt("Sex");
                    mIDNo           = obj.getString("IdNo");
                    mProfessional   = obj.getInt("Professional");
                    mAttitude       = obj.getInt("Attitude");
                    mPortrait       = obj.getString("Portrait");
                    mPhone          = obj.getString("Phone");
                    if (!mPortrait.isEmpty()) {
                        mPortrait = PicFullUrl(mPortrait);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int Id() {
                return mId;
            }

            @Override
            public String Name() {
                return mName;
            }

            @Override
            public String Phone() {
                return mPhone;
            }

            @Override
            public String Portrait() {
                return mPortrait;
            }

            @Override
            public int Sex() {
                return mSex;
            }

            @Override
            public String IdNo() {
                return mIDNo;
            }

            @Override
            public int RankProf() {
                return mProfessional;
            }

            @Override
            public int RankAtti() {
                return mAttitude;
            }

            @Override
            public String ListItemInfo2String() {
                return " id: " + Id() + ", name: " + Name() + ", sex: " + (0 == Sex() ? "男" : "女")
                        + ", ID card: " + IdNo() + ", phone:" + Phone() + ", portrait:" + Portrait()
                        + ", rank professional:" + RankProf() + ", rank attitude:" + RankAtti();
            }
        }
    }

}
