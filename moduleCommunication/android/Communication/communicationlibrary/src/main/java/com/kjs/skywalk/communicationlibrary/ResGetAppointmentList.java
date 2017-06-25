package com.kjs.skywalk.communicationlibrary;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kenny on 2017/6/22.
 */

class ResGetAppointmentList extends ResBase implements IApiResults.IResultList {

    private AppointmentList mList = null;

    ResGetAppointmentList(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mList = new AppointmentList();
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
    class AppointmentList extends ResList {

        AppointmentList() {
            mForceGetList = false;
        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("Appointments");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    Appointment newItem = new Appointment(array.getJSONObject(n));
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
        class Appointment implements IApiResults.IAppointment, InternalDefines.IListItemInfoInner {
            private int     mId             = 0;
            private int     mType           = 0;
            private String  mTypeDesc       = null;
            private int     mHouse          = 0;
            private String  mPhone          = null;
            private String  mSubscriber     = null;
            private Date    mAppointTimeBeg = null;
            private Date    mAppointTimeEnd = null;
            private String  mAppointDesc    = null;
            private Date    mSubscribeTime  = null;
            private Date    mCloseTime      = null;

            Appointment(JSONObject obj) {
                String appointmentPeriodBegin   = null;
                String appointmentPeriodEnd     = null;
                String subscribeTime            = null;
                String closeTime                = null;

                try {
                    mId                     = obj.getInt("Id");
                    mType                   = obj.getInt("ApomtType");
                    mTypeDesc               = obj.getString("TypeDesc");
                    mHouse                  = obj.getInt("House");
                    mPhone                  = obj.getString("Phone");
                    mSubscriber             = obj.getString("Subscriber");
                    appointmentPeriodBegin  = obj.getString("ApomtTimeBgn");
                    appointmentPeriodEnd    = obj.getString("ApomtTimeEnd");
                    mAppointDesc            = obj.getString("ApomtDesc");
                    subscribeTime           = obj.getString("SubscribTime");
                    closeTime               = obj.getString("CloseTime");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                if (null != appointmentPeriodBegin && !appointmentPeriodBegin.isEmpty()) {
                    try {
                        mAppointTimeBeg = format.parse(appointmentPeriodBegin);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (null != appointmentPeriodEnd && !appointmentPeriodEnd.isEmpty()) {
                    try {
                        mAppointTimeEnd = format.parse(appointmentPeriodEnd);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (null != subscribeTime && !subscribeTime.isEmpty()) {
                    try {
                        mSubscribeTime = format.parse(subscribeTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (null != closeTime && !closeTime.isEmpty()) {
                    try {
                        mCloseTime = format.parse(closeTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public int AppointId() {
                return mId;
            }

            @Override
            public int AppointType() {
                return mType;
            }

            @Override
            public String TypeDesc() {
                return mTypeDesc;
            }

            @Override
            public int HouseId() {
                return mHouse;
            }

            @Override
            public String Phone() {
                return mPhone;
            }

            @Override
            public String AppointDesc() {
                return mAppointDesc;
            }

            @Override
            public String Subscriber() {
                return mSubscriber;
            }

            @Override
            public Date AppointTimeBgn() {
                return mAppointTimeBeg;
            }

            @Override
            public Date AppointTimeEnd() {
                return mAppointTimeEnd;
            }

            @Override
            public Date SubscribeTime() {
                return mSubscribeTime;
            }

            @Override
            public Date CloseTime() {
                return mCloseTime;
            }

            @Override
            public String ListItemInfo2String() {
                String strInfo = "id: " + AppointId() + "\n";
                strInfo += "Type: " + AppointType() + ", " + TypeDesc() + "\n";
                strInfo += "Desc: " + AppointDesc() + "\n";
                strInfo += "House: " + HouseId() + "\n";
                strInfo += "Phone: " + Phone() + "\n";
                strInfo += "Subscriber: " + Subscriber() + "\n";
                strInfo += "Appoint time: " + AppointTimeBgn().toString() + " - " + mAppointTimeEnd.toString() + "\n";
                strInfo += "Subscribe time: " + SubscribeTime().toString() + "\n";
                strInfo += "Close time: " + (null != CloseTime()? CloseTime().toString() : "") + "\n";

                return strInfo ;
            }
        }
    }
}
