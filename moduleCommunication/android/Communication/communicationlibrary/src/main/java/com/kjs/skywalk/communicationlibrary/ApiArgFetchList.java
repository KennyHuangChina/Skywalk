package com.kjs.skywalk.communicationlibrary;

import android.util.Log;

/**
 * Created by kenny on 2018/1/1.
 */

class ApiArgFetchList extends ApiArgsBase implements IApiArgs.IArgsFetchList{
    protected int mBegin    = -1;
    protected int mFetchCnt = -1;

    ApiArgFetchList(int begin, int cnt) {
        mBegin      = begin;
        mFetchCnt   = cnt;
    }

    @Override
    public boolean checkArgs() {
        String Fn = "[checkArgs] ";
        if (mBegin < 0) {
            Log.e(TAG, Fn + "Invalid begin position:" + mBegin);
            return false;
        }
        if (mFetchCnt < 0) {
            Log.e(TAG, Fn + "Invalid fetch count:" + mFetchCnt);
            return false;
        }

        return true;
    }

    @Override
    public boolean isEqual(IApiArgs.IArgsBase arg2) {
        if (!super.isEqual(arg2)) {
            return false;
        }

        if (mBegin != ((ApiArgFetchList)arg2).mBegin || mFetchCnt != ((ApiArgFetchList)arg2).mFetchCnt) {
            return false;
        }
        return true;
    }

    @Override
    public int getBeginPosi() {
        return mBegin;
    }

    @Override
    public int getFetchCnt() {
        return mFetchCnt;
    }
}
