package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/20.
 */

class CmdGetHouseEventList extends CommunicationBase {
    private int mHouseId    = 0;
    private int mStat       = 0;    // event status. 0 - all events; 1 - new events; 2 - unclosed event; 3 - closed events
    private int mType       = 0;    // event type. 0 means all kind of events
    private int mBegin      = 0;
    private int mFetchCnt   = 0;
    private int mIDO        = 0;    // if only fetch event id. true - fetch evvent id / false - fetch whole event info

    CmdGetHouseEventList(Context context, int houseId) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_EVENT_LST);
        TAG = "CmdGetHouseEventList";
        mMethodType = "GET";
        mHouseId = houseId;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/event/list/house/" + mHouseId;
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        String mArgu = "";
        if (mStat > 0) {
            mArgu = ("stat=" + mStat);
        }
        if (mType > 0) {
            if (!mArgu.isEmpty()) {
                mArgu += "&";
            }
            mArgu += ("type=" + mType);
        }
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
        if (mIDO > 0) {
            if (!mArgu.isEmpty()) {
                mArgu += "&";
            }
            mArgu += ("ido=" + mIDO);
        }
        mRequestData += mArgu;

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_EVENT_STAT) ||
                !map.containsKey(CommunicationParameterKey.CPK_EVENT_TYPE) ||
                !map.containsKey(CommunicationParameterKey.CPK_LIST_BEGIN) ||
                !map.containsKey(CommunicationParameterKey.CPK_LIST_CNT) ||
                !map.containsKey(CommunicationParameterKey.CPK_EVENT_IDO)) {
            return false;
        }

        try {
            mStat = Integer.parseInt(map.get(CommunicationParameterKey.CPK_EVENT_STAT));
            if (mStat < 0 | mStat > 3) {
                Log.e(TAG, "mStat:" + mStat);
                return false;
            }
            mType = Integer.parseInt(map.get(CommunicationParameterKey.CPK_EVENT_TYPE));
            mBegin = Integer.parseInt(map.get(CommunicationParameterKey.CPK_LIST_BEGIN));
            if (mBegin < 0) {
                Log.e(TAG, "mBegin:" + mBegin);
                return false;
            }
            mFetchCnt = Integer.parseInt(map.get(CommunicationParameterKey.CPK_LIST_CNT));
            if (mFetchCnt < 0) {
                Log.e(TAG, "mFetchCnt:" + mFetchCnt);
                return false;
            }
            boolean bIDO = Boolean.parseBoolean(map.get(CommunicationParameterKey.CPK_EVENT_IDO));
            mIDO = bIDO ? 1 : 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseEventList result = new ResGetHouseEventList(nErrCode, jObject);
        return result ;
    }
}
