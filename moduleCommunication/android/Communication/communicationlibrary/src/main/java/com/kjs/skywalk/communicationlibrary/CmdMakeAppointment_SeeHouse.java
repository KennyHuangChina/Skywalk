package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import com.kjs.skywalk.communicationlibrary.CommunicationBase;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by kenny on 2017/6/24.
 */

class CmdMakeAppointment_SeeHouse extends CommunicationBase {
//    private int     mHouseId    = 0;
//    private String  mPhone      = null;
//    private String  mTimeBgn    = null;
//    private String  mTimeEnd    = null;
//    private String  mDesc       = null;

    CmdMakeAppointment_SeeHouse(Context context, int house_id, String phone,
                                String time_bgn, String time_end, String desc) {
        super(context, CommunicationInterface.CmdID.CMD_APPOINT_SEE_HOUSE);
        mMethodType = "POST";
        mArgs = new Args(house_id, phone, time_bgn, time_end, desc);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/appointment/see/house/" + ((Args)mArgs).getId();
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("phone=" + ((Args)mArgs).getPhone());
        mRequestData += "&";
        mRequestData += ("pb=" + ((Args)mArgs).getTimeBegin());
        mRequestData += "&";
        mRequestData += ("pe=" + ((Args)mArgs).getTimeEnd());
        mRequestData += "&";
        mRequestData += ("desc=" + String2Base64(((Args)mArgs).getDesc()));

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return new ResAddResource(nErrCode, jObject);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsMakeAppointmentSeeHouse {
        private int     mHouseId    = 0;
        private String  mPhone      = null;
        private String  mTimeBgn    = null;
        private String  mTimeEnd    = null;
        private String  mDesc       = null;

        Args(int house_id, String phone, String time_bgn, String time_end, String desc) {
            super(house_id);
            mPhone      = phone;
            mTimeBgn    = time_bgn;
            mTimeEnd    = time_end;
            mDesc       = desc;
        }

        @Override
        public boolean checkArgs() {
            String Fn = "[checkArgs] ";
            if (!super.checkArgs()) {
                return false;
            }
            if (null == mTimeBgn || mTimeBgn.isEmpty()) {
                Log.e(TAG, Fn + "mTimeBgn:" + mTimeBgn);
                return false;
            }
            if (null == mTimeEnd || mTimeEnd.isEmpty()) {
                Log.e(TAG, Fn + "mTimeEnd:" + mTimeEnd);
                return false;
            }
            // do further checking of time string
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date time1;
            try {
                time1 = format.parse(mTimeBgn);
            } catch (ParseException e) {
//                e.printStackTrace();
                Log.e(TAG, Fn + "Invalid mTimeBgn: " + mTimeBgn);
                return false;
            }
            try {
                time1 = format.parse(mTimeEnd);
            } catch (ParseException e) {
//                e.printStackTrace();
                Log.e(TAG, Fn + "Invalid mTimeEnd: " + mTimeEnd);
                return false;
            }
            if (mDesc.isEmpty()) {
                Log.e(TAG, Fn + "mDesc:" + mDesc);
                return false;
            }

            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            return super.isEqual(arg2) &&
                    mPhone.equals(((Args)arg2).mPhone) &&
                    mTimeBgn.equals(((Args)arg2).mTimeBgn) &&
                    mTimeEnd.equals(((Args)arg2).mTimeEnd) &&
                    mDesc.equals(((Args)arg2).mDesc);
        }

        @Override
        public String getPhone() {
            return mPhone;
        }

        @Override
        public String getTimeBegin() {
            return mTimeBgn;
        }

        @Override
        public String getTimeEnd() {
            return mTimeEnd;
        }

        @Override
        public String getDesc() {
            return mDesc;
        }
    }
}
