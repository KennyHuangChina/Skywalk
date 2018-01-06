package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/6/22.
 */

class CmdGetAppointmentListHouseSee extends CommunicationBase {
//
//    private int     mHouseId        = 0;
//    private int     mBeginPosi      = 0;
//    private int     mFetchCount     = 0;

    CmdGetAppointmentListHouseSee(Context context, int house_id, int begin, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_APPOINT_HOUSE_SEE_LST);
        mNeedLogin  = false;
        mArgs = new Args(house_id, begin, cnt);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/appointment/house/%d/seelist", ((Args)mArgs).getHouseId());
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData += ("bgn=" + ((Args)mArgs).getBeginPosi());
        mRequestData += "&";
        mRequestData += ("fCnt=" + ((Args)mArgs).getFetchCnt());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return new ResGetAppointmentList(nErrCode, jObject);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgFetchList implements IApiArgs.IArgsGetHouseSeeAppointmentList {
        private int mHouseId = 0;

        Args(int house, int begin, int cnt) {
            super(begin, cnt);
            mHouseId = house;
        }

        @Override
        public boolean checkArgs() {
            if (!super.checkArgs()) {
                return false;
            }
            if (mHouseId <= 0) {
                Log.e(TAG,  "[checkArgs] mHouseId:" + mHouseId);
                return false;
            }

            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            return super.isEqual(arg2) && (mHouseId == ((Args)arg2).mHouseId);
        }

        @Override
        public int getHouseId() {
            return mHouseId;
        }
    }
}
