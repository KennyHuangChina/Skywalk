package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by kenny on 2017/9/24.
 */

class CmdMakeAppointmentAction extends CommunicationBase {
//    private int     mAppointment = 0;
//    private int     mAct         = 0;
//    private String  mTimeBegin   = null;
//    private String  mTimeEnd     = null;
//    private String  mComments    = null;

    CmdMakeAppointmentAction(Context context, int aid, int act, String tb, String te, String actTxt) {
        super(context, CommunicationInterface.CmdID.CMD_MAKE_APPOINTMENT_ACTION);
        mMethodType = "POST";
        mArgs = new Args(aid, act, tb, te, actTxt);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/appointment/%d/act", ((Args)mArgs).getId());
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        if (((Args)mArgs).getAction() > 0) {
            mRequestData = ("act=" + ((Args)mArgs).getAction());
        }
        if (null != ((Args)mArgs).getBeginTime() && null != ((Args)mArgs).getEndTime()) {
            if (!mRequestData.isEmpty()) {
                mRequestData += "&";
            }
            mRequestData += String.format("tb=%s&te=%s", ((Args)mArgs).getBeginTime(), ((Args)mArgs).getEndTime());
        }
        if (null != ((Args)mArgs).getDesc()) {
            if (!mRequestData.isEmpty()) {
                mRequestData += "&";
            }
            mRequestData += ("ac=" + String2Base64(((Args)mArgs).getDesc()));
        }

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return new ResAddResource(nErrCode, jObject);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsMakeAppointmentAct {
//        private int     mAppointment = 0;
        private int     mAct         = 0;
        private String  mTimeBegin   = null;
        private String  mTimeEnd     = null;
        private String  mComments    = null;

        Args(int appointment, int act, String begin, String end, String desc) {
            super(appointment);
            mAct         = act;
            mTimeBegin   = begin;
            mTimeEnd     = end;
            mComments    = desc;
        }

        @Override
        public boolean checkArgs() {
            String Fn = "[checkArgs] ";
            if (!super.checkArgs()) {
                return false;
            }

            if (mAct < IApiArgs.APPOINTMENT_ACTION_Min || mAct > IApiArgs.APPOINTMENT_ACTION_Max) {
                Log.e(TAG, Fn + String.format("Invalid action:%d, (2 ~ 5)", mAct));
                return false;
            }

            if (null == mComments || mComments.isEmpty()) {
                Log.e(TAG, Fn + "Action comments not set");
                return false;
            }

            if (IApiArgs.APPOINTMENT_ACTION_Reschedule == mAct /*|| 1 == mAct*/) {
                if (null == mTimeBegin || mTimeBegin.isEmpty()) {
                    Log.e(TAG, Fn + "begin time not set");
                    mTimeBegin = null;
                    mTimeEnd = null;
                    return false;
                }
                if (null == mTimeEnd || mTimeEnd.isEmpty()) {
                    Log.e(TAG, Fn + "end time not set");
                    mTimeBegin = null;
                    mTimeEnd = null;
                    return false;
                }
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                try {
                    Date timeBgn = format.parse(mTimeBegin);
                    Date timeEnd = format.parse(mTimeEnd);
                } catch (ParseException e) {
                    e.printStackTrace();
                    mTimeBegin = null;
                    mTimeEnd = null;
                    return false;
                }
            } else {
                mTimeBegin  = null;
                mTimeEnd    = null;
            }

            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            return super.isEqual(arg2) &&
                    mAct == ((Args)arg2).mAct &&
                    (null == mTimeBegin || mTimeBegin.equals(((Args)arg2).mTimeBegin)) &&
                    (null == mTimeEnd || mTimeEnd.equals(((Args)arg2).mTimeEnd)) &&
                    mComments.equals(((Args)arg2).mComments);
        }

        @Override
        public int getAction() {
            return mAct;
        }

        @Override
        public String getBeginTime() {
            return mTimeBegin;
        }

        @Override
        public String getEndTime() {
            return mTimeEnd;
        }

        @Override
        public String getDesc() {
            return mComments;
        }
    }
}
