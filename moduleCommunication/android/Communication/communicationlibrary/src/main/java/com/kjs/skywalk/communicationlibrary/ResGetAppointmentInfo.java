package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kenny on 2017/9/24.
 */

class ResGetAppointmentInfo extends ResBase implements IApiResults.IAppointmentInfo, IApiResults.IResultList {

    private ResHouseTitleInfo   mHouseTitle         = null;
    private Date                mScheduleDate       = null;
    private Date                mScheduleTimeBegin  = null;
    private Date                mScheduleTimeEnd    = null;
    private String              mSubscriber         = null;
    private String              mSubscriberPhone    = null;
    private String              mReceptionist       = null;
    private String              mReceptionistPhone  = null;
    private String              mAppointDesc        = null;
    private Date                mSubscribeTime      = null;
    private ActList             mActList            = null;

    ResGetAppointmentInfo(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mActList = new ActList();
        mHouseTitle = new ResHouseTitleInfo();
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();
        mString += mHouseTitle.DebugString() + "\n";
        mString += String.format(" Schedule: %s %s -> %s\n", ScheduleDate().toString(), ScheduleBeginTime().toString(), ScheduleEndTime().toString());
        mString += String.format(" Subscriber: %s phone: %s\n", Subscriber(), SubscriberPhone());
        mString += String.format(" Receptionist: %s phone: %s\n", Receptionist(), ReceptionistPhone());
        mString += String.format(" Description: %s \n", AppointmentDesc());
        mString += String.format(" Subscribe Time: %s \n", SubscribeTime().toString());
        if (null != mActList) {
            mString += mActList.DebugList();
        }
        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        if (null == mHouseTitle) {
            return -1;
        }

        DateFormat df_date      = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_schedule  = new SimpleDateFormat("hh:mm");
        DateFormat df_time      = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            JSONObject objAptm = obj.getJSONObject("Appointment");

            if (0 != mHouseTitle.parse(objAptm.getJSONObject("House"))) {
                return -6;
            }
            String strTmp = objAptm.getString("Date");
            if (null == strTmp || strTmp.isEmpty()) {
                return -3;
            }
            mScheduleDate = df_date.parse(strTmp);

            strTmp = objAptm.getString("TimeBegin");
            if (null == strTmp || strTmp.isEmpty()) {
                return -4;
            }
            mScheduleTimeBegin = df_schedule.parse(strTmp);

            strTmp = objAptm.getString("TimeEnd");
            if (null == strTmp || strTmp.isEmpty()) {
                return -5;
            }
            mScheduleTimeEnd = df_schedule.parse(strTmp);

            mSubscriber         = objAptm.getString("Subscriber");
            mSubscriberPhone    = objAptm.getString("SubscriberPhone");
            mReceptionist       = objAptm.getString("Receptionist");
            mReceptionistPhone  = objAptm.getString("ReceptionistPhone");
            mAppointDesc        = objAptm.getString("ApmtDesc");

            strTmp = objAptm.getString("SubscribeTime");
            if (null == strTmp || strTmp.isEmpty()) {
                return -6;
            }
            mSubscribeTime = df_time.parse(strTmp);

            // action list
            if (null != mActList) {
                mActList.parseList(objAptm);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return -2;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return super.parseResult(obj);
    }

    @Override
    public String Property() {
        if (null == mHouseTitle) {
            return null;
        }
        return mHouseTitle.Property();
    }

    @Override
    public String BuildingNo() {
        if (null == mHouseTitle) {
            return null;
        }
        return mHouseTitle.BuildingNo();
    }

    @Override
    public String HouseNo() {
        if (null == mHouseTitle) {
            return null;
        }
        return mHouseTitle.HouseNo();
    }

    @Override
    public int Livingrooms() {
        if (null == mHouseTitle) {
            return -1;
        }
        return mHouseTitle.Livingrooms();
    }

    @Override
    public int Bedrooms() {
        if (null == mHouseTitle) {
            return -1;
        }
        return mHouseTitle.Bedrooms();
    }

    @Override
    public int Bathrooms() {
        if (null == mHouseTitle) {
            return -1;
        }
        return mHouseTitle.Bathrooms();
    }

    @Override
    public Date ScheduleDate() {
        return mScheduleDate;
    }

    @Override
    public Date ScheduleBeginTime() {
        return mScheduleTimeBegin;
    }

    @Override
    public Date ScheduleEndTime() {
        return mScheduleTimeEnd;
    }

    @Override
    public String Subscriber() {
        return mSubscriber;
    }

    @Override
    public String SubscriberPhone() {
        return mSubscriberPhone;
    }

    @Override
    public String Receptionist() {
        return mReceptionist;
    }

    @Override
    public String ReceptionistPhone() {
        return mReceptionistPhone;
    }

    @Override
    public String AppointmentDesc() {
        return mAppointDesc;
    }

    @Override
    public Date SubscribeTime() {
        return mSubscribeTime;
    }

    @Override
    public int GetTotalNumber() {
        if (null == mActList) {
            return -1;
        }
        return mActList.GetTotalNumber();
    }

    @Override
    public int GetFetchedNumber() {
        if (null == mActList) {
            return -1;
        }
        return mActList.GetFetchedNumber();
    }

    @Override
    public ArrayList<Object> GetList() {
        if (null == mActList) {
            return null;
        }
        return mActList.GetList();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    class ActList extends ResList {
        ActList() {
            mForceGetList = true;   // list without properties: total & fetched
        }

        @Override
        public int parseListItems(JSONObject obj) {
            try {
                JSONArray array = obj.getJSONArray("Acts");
                if (null == array) {
                    return -1;
                }
                for (int n = 0; n < array.length(); n++) {
                    AppointmentAct newItem = new AppointmentAct(array.getJSONObject(n));
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
   }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    class AppointmentAct implements IApiResults.IAppointmentAct, InternalDefines.IListItemInfoInner {
        private int     mId         = 0;
        private int     mAct        = 0;
        private String  mWho        = null;
        private Date    mWhen       = null;
        private Date    mTimeBgn    = null;
        private Date    mTimeEnd    = null;
        private String  mComment    = null;

        AppointmentAct(JSONObject obj) {
            try {
                mId = obj.getInt("Id");
                mAct = obj.getInt("Act");
                mWho = obj.getString("Who");

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                mWhen = df.parse(obj.getString("When"));

                String strTmp = obj.getString("TimeBegin");
                if (null != strTmp && !strTmp.isEmpty()) {
                    mTimeBgn = df.parse(strTmp);
                }

                strTmp = obj.getString("TimeEnd");
                if (null != strTmp && !strTmp.isEmpty()) {
                    mTimeEnd = df.parse(strTmp);
                }

                mComment = obj.getString("Comment");

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int Id() {
            return mId;
        }

        @Override
        public int Act() {
            return mAct;
        }

        @Override
        public String Who() {
            return mWho;
        }

        @Override
        public Date When() {
            return mWhen;
        }

        @Override
        public Date PeriodBegin() {
            return mTimeBgn;
        }

        @Override
        public Date PeriodEnd() {
            return mTimeEnd;
        }

        @Override
        public String Comment() {
            return mComment;
        }

        @Override
        public String ListItemInfo2String() {
            return String.format("id:%d, act:%d, who:%s, when:%s, schedule:%s - %s, comments:%s\n",
                    Id(), Act(), Who(), When().toString(), PeriodBegin(), PeriodEnd(), Comment());
        }
    }
}

