package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/20.
 */

class CmdGetSysMsgList extends CommunicationBase {
    private int mBegin      = 0;
    private int mFetchCnt   = 0;
    private boolean mIDO    = false;    // if only fetch event id. true - fetch evvent id / false - fetch whole event info

    CmdGetSysMsgList(Context context, int bgn, int cnt, boolean ido) {
        super(context, CommunicationInterface.CmdID.CMD_GET_SYSTEM_MSG_LST);
        mBegin      = bgn;
        mFetchCnt   = cnt;
        mIDO        = ido;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/sysmsg/lst";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        String mArgu = "";
        if (mBegin > 0) {
            if (!mArgu.isEmpty()) {
                mArgu += "&";
            }
            mArgu += ("bgn=" + mBegin);
        }
        if (mFetchCnt > 0) {
            if (!mArgu.isEmpty()) {
                mArgu += "&";
            }
            mArgu += ("cnt=" + mFetchCnt);
        }
        if (!mIDO) {
            if (!mArgu.isEmpty()) {
                mArgu += "&";
            }
            mArgu += "ff=1";
        }
        mRequestData += mArgu;

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mBegin < 0) {
            Log.e(TAG, "mBegin:" + mBegin);
            return false;
        }
        if (mFetchCnt < 0) {
            Log.e(TAG, "mFetchCnt:" + mFetchCnt);
            return false;
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetSysMsgList result = new ResGetSysMsgList(nErrCode, jObject);
        return result ;
    }
}
