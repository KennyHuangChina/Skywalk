package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/1.
 */

class CmdGetHouseInfo extends CommunicationBase {

    CmdGetHouseInfo(Context context, int house_id, boolean bBackEnd) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO);
        mNeedLogin  = bBackEnd ? true : false;
        mArgs = new Args(house_id, bBackEnd);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/" + ((Args)mArgs).getHouseId();
        if (((Args)mArgs).mbBackend) {
            mCommandURL += "?be=1";
        }
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseInfo result = new ResGetHouseInfo(nErrCode, jObject);
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      -- Arguments List --
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsGetHouseInfo {
        private int     mHouseId;           // house id
        private boolean mbBackend;          // if fetch real, complete house info for back-end using

        public Args(int house, boolean backend) {
            mHouseId    = house;
            mbBackend   = backend;
        }

        @Override
        public boolean checkArgs() {
            if (mHouseId <= 0) {
                Log.e(TAG, "mHouseId: " + mHouseId);
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            String Fn = "[isEqual] ";
            if (!super.isEqual(arg2)) {
                return false;
            }

            Args args_chk = (Args)arg2;
            if (mHouseId != args_chk.mHouseId || mbBackend != args_chk.mbBackend) {
                return false;
            }
            return true;
        }

        @Override
        public int getHouseId() {
            return mHouseId;
        }

        @Override
        public boolean isBackendUse() {
            return mbBackend;
        }
    }
}
