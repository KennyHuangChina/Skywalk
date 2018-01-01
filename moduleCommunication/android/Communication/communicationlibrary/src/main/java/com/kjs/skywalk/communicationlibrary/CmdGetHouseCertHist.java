package com.kjs.skywalk.communicationlibrary;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/9/18.
 */

class CmdGetHouseCertHist extends CommunicationBase {

    CmdGetHouseCertHist(Context context, int house_id) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_CERTIFY_HIST);
        mArgs = new Args(house_id);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/certhist", ((Args)mArgs).getHouse());
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseCertHist res = new ResGetHouseCertHist(nErrCode, jObject);
        return res;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsGetHouseCertHist {
        private int mHouseId = 0;

        Args(int house) {
            mHouseId = house;
        }

        @Override
        public int getHouse() {
            return mHouseId;
        }

        @Override
        public boolean checkArgs() {
            if (mHouseId <= 0) {
                Log.e(TAG, String.format("mHouseId:%d", mHouseId));
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }

            if (mHouseId != ((Args)arg2).mHouseId) {
                return false;
            }
            return true;
        }
    }
}
