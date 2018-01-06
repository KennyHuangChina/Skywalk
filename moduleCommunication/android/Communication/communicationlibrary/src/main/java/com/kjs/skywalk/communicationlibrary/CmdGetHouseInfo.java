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
        mCommandURL = "/v1/house/" + ((Args)mArgs).getId();
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
    class Args extends ApiArgsObjId implements IApiArgs.IArgsGetHouseInfo {
        private boolean mbBackend;          // if fetch real, complete house info for back-end using

        public Args(int house, boolean backend) {
            super(house);
            mbBackend = backend;
        }

        @Override
        public boolean checkArgs() {
            return super.checkArgs();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            return super.isEqual(arg2) && (mbBackend == ((Args)arg2).mbBackend);
        }

        @Override
        public boolean isBackendUse() {
            return mbBackend;
        }
    }
}
