package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kenny on 2017/9/24.
 */

class ResGetAppointmentInfo extends ResBase implements IApiResults.IAppointmentInfo {

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

    ResGetAppointmentInfo(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        mHouseTitle = new ResHouseTitleInfo();
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();
//        mString += "  user id: " + mId + "\n";
//        mString += "  user name: " + mName + "\n";
//        mString += "  phone: " + mPhoneNo + "\n";
//        mString += "  ID: " + mIdNo + "\n";
//        mString += "  header potrait: " + mHeadPotrait + "\n";
//        mString += "  role: " + mRole + " (" + mRoleDesc + ")\n";
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
            if (0 != mHouseTitle.parse(obj.getJSONObject("House"))) {
                return -6;
            }
            String strTmp = obj.getString("Date");
            if (null == strTmp || strTmp.isEmpty()) {
                return -3;
            }
            mScheduleDate = df_date.parse(strTmp);

            strTmp = obj.getString("TimeBegin");
            if (null == strTmp || strTmp.isEmpty()) {
                return -4;
            }
            mScheduleTimeBegin = df_schedule.parse(strTmp);

            strTmp = obj.getString("TimeEnd");
            if (null == strTmp || strTmp.isEmpty()) {
                return -4;
            }
            mScheduleTimeEnd = df_schedule.parse(strTmp);

            mSubscriber         = obj.getString("Subscriber");
            mSubscriberPhone    = obj.getString("SubscriberPhone");
            mReceptionist       = obj.getString("Receptionist");
            mReceptionistPhone  = obj.getString("ReceptionistPhone");
            mAppointDesc        = obj.getString("ApmtDesc");

            strTmp = obj.getString("SubscribeTime");
            if (null == strTmp || strTmp.isEmpty()) {
                return -5;
            }
            mSubscribeTime = df_time.parse(strTmp);

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
}

