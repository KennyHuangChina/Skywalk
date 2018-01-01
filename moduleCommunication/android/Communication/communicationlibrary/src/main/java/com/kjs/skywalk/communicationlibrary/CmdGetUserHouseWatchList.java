package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/8/1.
 */

class CmdGetUserHouseWatchList extends CommunicationBase {

    CmdGetUserHouseWatchList(Context context, int bgn, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_GET_USER_HOUSE_WATCH_LIST);
        mArgs = new Args(bgn, cnt);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/housewatch/user";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("bgn=" + ((Args)mArgs).getBegin());
        mRequestData += "&";
        mRequestData += ("cnt=" + ((Args)mArgs).getFetchCnt());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseList result = new ResGetHouseList(nErrCode, jObject);
        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsGetUserHouseWatchList {
        private int mBeginPosi  = 0;
        private int mFetchCount = 0;

        Args(int begin, int cnt) {
            mBeginPosi  = begin;
            mFetchCount = cnt;
        }

        @Override
        public int getBegin() {
            return mBeginPosi;
        }

        @Override
        public int getFetchCnt() {
            return mFetchCount;
        }

        @Override
        public boolean checkArgs() {
            if (mBeginPosi < 0) {
                Log.e(TAG, "mBeginPosi: " + mBeginPosi);
                return false;
            }
            if (mFetchCount < 0) {
                Log.e(TAG, "mFetchCount: " + mFetchCount);
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (mBeginPosi != ((Args)arg2).mBeginPosi || mFetchCount != ((Args)arg2).mFetchCount) {
                return false;
            }
            return true;
        }
    }
}
