package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import static com.kjs.skywalk.communicationlibrary.IApiArgs.*;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdGetBehalfHouses extends CommunicationBase {

    CmdGetBehalfHouses(Context context, int type, int bgn, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_GET_BEHALF_HOUSE_LIST);
        mArgs = new ApiArgsGetBehalfHouseList(type, bgn, cnt);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/behalf";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        ApiArgsGetBehalfHouseList args = (ApiArgsGetBehalfHouseList)mArgs;
        mRequestData = ("type=" + args.getType());
        mRequestData += "&";
        mRequestData += ("bgn=" + args.getBeginPosi());
        mRequestData += "&";
        mRequestData += ("cnt=" + args.getFetchCnt());
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseList result = new ResGetHouseList(nErrCode, jObject);
        return result;
    }

    //////////////////////////////////////////////////////////////////////////////////
    //
    //
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
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            ApiArgsGetBehalfHouseList arg2chk = (ApiArgsGetBehalfHouseList)arg2;
            if (mType != arg2chk.mType || mBegin != arg2chk.mBegin || mFetchCnt != mFetchCnt) {
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
}
