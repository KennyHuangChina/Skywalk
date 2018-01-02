package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/6/10.
 */

class CmdModifyAgency extends CommunicationBase {

    CmdModifyAgency(Context context, int agency, int rank_prof, int rank_atti, int begin_year) {
        super(context, CommunicationInterface.CmdID.CMD_MODIFY_AGENCY);
        mMethodType = "PUT";
        mArgs = new Args(agency, rank_prof, rank_atti, begin_year);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/Agency/" + ((Args)mArgs).getAencyId();
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("rp=" + ((Args)mArgs).getRankProf());
        mRequestData += ("&ra=" + ((Args)mArgs).getRankAttitude());
        mRequestData += ("&by=" + ((Args)mArgs).getWorkingYears());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true;    // return super.checkParameter(map);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsModifyAgencyInfo {
        private int mAgencyId   = 0;
        private int mRankProf   = 0;    // values 0 ~ 50, means 0.0 ~5.0
        private int mRankAtti   = 0;    // values 0 ~ 50, means 0.0 ~5.0
        private int mWorkYears  = 0;

        Args(int agency, int rank_prof, int rank_atti, int wy) {
            mAgencyId   = agency;
            mRankProf   = rank_prof;
            mRankAtti   = rank_atti;
            mWorkYears  = wy;
        }

        @Override
        public boolean checkArgs() {
            String Fn = "[checkArgs] ";
            if (mAgencyId <= 0) {
                Log.e(TAG, Fn + "mAgencyId:" + mAgencyId);
                return false;
            }
            if (mRankProf < 0 || mRankProf > 50) {
                Log.e(TAG, Fn + "mRankProf:" + mRankProf);
                return false;
            }
            if (mRankAtti < 0 || mRankAtti > 50) {
                Log.e(TAG, Fn + "mRankAtti:" + mRankAtti);
                return false;
            }
            if (mWorkYears < 0 || mWorkYears > 60) {
                Log.e(TAG, Fn + "mWorkYears:" + mWorkYears);
                return false;
            }
            return true;    // super.checkArgs();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            Args ac = (Args)arg2;
            if (mAgencyId != ac.mAgencyId || mRankProf != ac.mRankProf ||
                    mRankAtti != ac.mRankAtti || mWorkYears != ac.mWorkYears) {
                return false;
            }
            return true;
        }

        @Override
        public int getAencyId() {
            return mAgencyId;
        }

        @Override
        public int getRankProf() {
            return mRankProf;
        }

        @Override
        public int getRankAttitude() {
            return mRankAtti;
        }

        @Override
        public int getWorkingYears() {
            return mWorkYears;
        }
    }
}
