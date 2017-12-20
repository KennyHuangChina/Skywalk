package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdGetBehalfHouses extends CommunicationBase {
    protected   int mType       = CommunicationInterface.AGENT_HOUSE_ALL;  // 0: all; 1: to rent; 2: rented; 3: to sale; 4: wait for approve. ref to AGENT_HOUSE_xxx
    private     int mBegin      = 0;
    private     int mFetchCnt   = 0;

    CmdGetBehalfHouses(Context context, int type, int bgn, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_GET_BEHALF_HOUSE_LIST);
        mType       = type;
        mBegin      = bgn;
        mFetchCnt   = cnt;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/behalf";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("type=" + mType);
        mRequestData += "&";
        mRequestData += ("bgn=" + mBegin);
        mRequestData += "&";
        mRequestData += ("cnt=" + mFetchCnt);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mType < CommunicationInterface.AGENT_HOUSE_ALL || mType > CommunicationInterface.AGENT_HOUSE_TO_APPROVE) {
            Log.e(TAG, "mType:" + mType);
            return false;
        }

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
        ResGetHouseList result = new ResGetHouseList(nErrCode, jObject);
        return result;
    }

    @Override
    protected boolean isCmdEqual(CommunicationBase cmd2Chk) {
        CmdGetBehalfHouses cmdChk = (CmdGetBehalfHouses)cmd2Chk;
        if (mType != cmdChk.mType || mBegin != cmdChk.mBegin || mFetchCnt != cmdChk.mFetchCnt) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean checkCmdRes(IApiResults.ICommon res) {
        ResGetHouseList cmd = (ResGetHouseList)res;
        if (mType != cmd.mType || mBegin != cmd.mBegin || mFetchCnt != cmd.mFetchCnt) {
            return false;
        }
        return true;
    }
}
