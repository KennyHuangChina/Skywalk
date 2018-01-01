package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/20.
 */

class CmdGetSysMsgList extends CommunicationBase {

    CmdGetSysMsgList(Context context, int bgn, int cnt, boolean ido, boolean nmo) {
        super(context, CommunicationInterface.CmdID.CMD_GET_SYSTEM_MSG_LST);
        mArgs = new Args(bgn, cnt, ido, nmo);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/sysmsg/lst";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        String mArgu = "";
        Args args = (Args)mArgs;
        if (args.getBeginPosi() > 0) {
            if (!mArgu.isEmpty()) {
                mArgu += "&";
            }
            mArgu += ("bgn=" + args.getBeginPosi());
        }
        if (args.getFetchCnt() > 0) {
            if (!mArgu.isEmpty()) {
                mArgu += "&";
            }
            mArgu += ("cnt=" + args.getFetchCnt());
        }
        if (!args.isIdOnly()) {
            if (!mArgu.isEmpty()) {
                mArgu += "&";
            }
            mArgu += "ff=1";
        }
        if (args.isNewMsgOnly()) {
            if (!mArgu.isEmpty()) {
                mArgu += "&";
            }
            mArgu += "nm=1";
        }
        mRequestData += mArgu;

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetSysMsgList result = new ResGetSysMsgList(nErrCode, jObject);
        return result ;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //
    //      -- API Arguments --
    //
    //////////////////////////////////////////////////////////////////////////////////////////
    class Args extends ApiArgFetchList implements IApiArgs.IArgsGetMsgList {
        private boolean mIDO  = false;    // if only fetch message id. true - fetch evvent id / false - fetch whole message info
        private boolean mNMO  = false;    // if only fetch new message. true - only fetch new messages / false - fetch all messages

        Args(int bgn, int cnt, boolean ido, boolean nmo) {
            super(bgn, cnt);
            mIDO = ido;
            mNMO = nmo;
        }

        @Override
        public boolean checkArgs() {
            if (!super.checkArgs()) {
                return false;
            }
            // DO further checking here
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            Args arg_chk = (Args)arg2;
            if (mIDO != arg_chk.mIDO || mNMO != arg_chk.mNMO) {
                return false;
            }
            return true;
        }

        @Override
        public boolean isIdOnly() {
            return mIDO;
        }

        @Override
        public boolean isNewMsgOnly() {
            return mNMO;
        }
    }
}
