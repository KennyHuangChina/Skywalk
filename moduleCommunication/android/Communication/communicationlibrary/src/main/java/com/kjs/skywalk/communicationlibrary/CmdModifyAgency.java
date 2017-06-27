package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/6/10.
 */

class CmdModifyAgency extends CommunicationBase {
    private int mAgencyId   = 0;
    private int mRankProf   = 0;
    private int mRankAtti   = 0;
    private int mBeginYear  = 0;

    CmdModifyAgency(Context context, int agency, int rank_prof, int rank_atti, int begin_year) {
        super(context, CommunicationInterface.CmdID.CMD_MODIFY_AGENCY);
        mMethodType = "PUT";

        mRankProf   = rank_prof;
        mRankAtti   = rank_atti;
        mBeginYear  = begin_year;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/Agency/" + mAgencyId;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("rp=" + mRankProf);
        mRequestData += ("&ra=" + mRankAtti);
        mRequestData += ("&by=" + mBeginYear);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true;    // return super.checkParameter(map);
    }
}
