package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/14.
 */

class CmdGetFacilityList extends CommunicationBase {

    CmdGetFacilityList(Context context, int type) {
        super(context, CommunicationInterface.CmdID.CMD_GET_FACILITY_LIST);
        mNeedLogin = false;
        mArgs = new Args(type);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/facilitys";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("type=" + ((Args)mArgs).getType());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResFacilityList res = new ResFacilityList(nErrCode, jObject);
        return res;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsGetFacilityList {
        private int mType = -1;  // 0 means all type

        Args(int type) {
            mType = type;
        }

        @Override
        public boolean checkArgs() {
            if (mType < 0) {
                Log.e(TAG, "[checkArgs] mType:" + mType);
                return false;
            }
            return super.checkArgs();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (mType != ((Args)arg2).mType) {
                return false;
            }
            return true;
        }

        @Override
        public int getType() {
            return mType;
        }
    }
}
