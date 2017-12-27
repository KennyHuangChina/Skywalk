package com.kjs.skywalk.communicationlibrary;

import android.util.Log;

import static com.kjs.skywalk.communicationlibrary.IApiArgs.AGENT_HOUSE_ALL;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.AGENT_HOUSE_END;

/**
 * Created by kenny on 2017/12/27.
 */

class ApiArgsGetBehalfHouseList extends ApiArgsBase implements IApiArgs.IArgsGetBehalfList {
    private int mType       = IApiArgs.AGENT_HOUSE_ALL;    // AGENT_HOUSE_xxx
    private int mBegin      = 0;
    private int mFetchCnt   = 0;

    public ApiArgsGetBehalfHouseList(int type, int bgn, int cnt) {
        mType       = type;
        mBegin      = bgn;
        mFetchCnt   = cnt;
    }

    @Override
    public boolean checkArgs() {
        if (mType < AGENT_HOUSE_ALL || mType > AGENT_HOUSE_END) {
            Log.e(TAG, "mType:" + mType);
            return false;
        }
        if (mBegin < 0) {
            Log.e(TAG, "begin:" + mBegin);
            return false;
        }
        if (mFetchCnt < 0) {
            Log.e(TAG, "mFetchCnt:" + mFetchCnt);
            return false;
        }
        return true;
    }

    @Override
    public int getType() {
        return mType;
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
