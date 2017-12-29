package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/31.
 */

class CmdGetAgencyList extends CommunicationBase {

    CmdGetAgencyList(Context context, int begin, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_GET_AGENCY_LIST);
//        mNeedLogin  = false;
        mArgs = new Args(begin, cnt);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/AgencyList";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData += ("bgn=" + ((Args)mArgs).getBegin());
        mRequestData += "&";
        mRequestData += ("cnt=" + ((Args)mArgs).getFetchCnt());

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetAgencyList res = new ResGetAgencyList(nErrCode, jObject);
        return res;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsGetAgentList {
        private int mBegin  = 0;
        private int mCnt    = 0;

        Args(int begin, int cnt) {
            mBegin = begin;
            mCnt = cnt;
        }

        @Override
        public boolean checkArgs() {
            if (mBegin < 0) {
                Log.e(TAG, "mBegin:" + mBegin);
                return false;
            }
            if (mCnt < 0) {
                Log.e(TAG, "mCnt:" + mCnt);
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            Args arg_chk = (Args)arg2;
            if (mBegin != arg_chk.mBegin || mCnt != arg_chk.mCnt) {
                return  false;
            }

            return true;
        }

        @Override
        public int getBegin() {
            return mBegin;
        }

        @Override
        public int getFetchCnt() {
            return mCnt;
        }
    }
}
