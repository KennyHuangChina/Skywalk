package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import com.kjs.skywalk.communicationlibrary.CommunicationBase;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;

import java.util.HashMap;

/**
 * Created by kenny on 2017/6/21.
 */

class CmdSetHouseShowtime extends CommunicationBase {

    CmdSetHouseShowtime(Context context, int house_id, int pw, int pv, String pd) {
        super(context, CommunicationInterface.CmdID.CMD_SET_HOUSE_SHOWTIME);
        mMethodType = "PUT";
        mArgs = new Args(house_id, pw, pv, pd);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/showtime", ((Args)mArgs).getId());
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("prdw=" + ((Args)mArgs).getPeriodOfWorkingDay());
        mRequestData += "&";
        mRequestData += ("prdv=" + ((Args)mArgs).getPeriodOfVacation());
        mRequestData += "&";
        mRequestData += ("desc=" + ((Args)mArgs).getPeriodDesc());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsSetHouseShowtime {
        private int     mPeriodW    = -1;   // period for working day
        private int     mPeriodV    = -1;   // period for weekend and vacation
        private String  mPeriodDesc = null; // period description

        Args(int house, int pw, int pv, String desc) {
            super(house);
            mPeriodW    = pw;
            mPeriodV    = pv;
            mPeriodDesc = desc;
        }

        @Override
        public boolean checkArgs() {
            if (!super.checkArgs()) {
                return false;
            }
            if (mPeriodW < 0 || mPeriodW > 7) {
                Log.e(TAG, "mPeriodW: " + mPeriodW);
                return false;
            }
            if (mPeriodV < 0 || mPeriodV > 7) {
                Log.e(TAG, "mPeriodV: " + mPeriodV);
                return false;
            }

            return true;    // super.checkParameter(map);
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }

            Args ac = (Args)arg2;
            if (mPeriodW != ac.mPeriodW || mPeriodV != ac.mPeriodV) {
                return false;
            }
            if (null == mPeriodDesc && null == ac.mPeriodDesc) {
                return true;
            }
            if (null != mPeriodDesc && null != ac.mPeriodDesc && mPeriodDesc.equals(ac.mPeriodDesc)) {
                return true;
            }
            return false;
        }

        @Override
        public int getPeriodOfWorkingDay() {
            return mPeriodW;
        }

        @Override
        public int getPeriodOfVacation() {
            return mPeriodV;
        }

        @Override
        public String getPeriodDesc() {
            return mPeriodDesc;
        }
    }
}
