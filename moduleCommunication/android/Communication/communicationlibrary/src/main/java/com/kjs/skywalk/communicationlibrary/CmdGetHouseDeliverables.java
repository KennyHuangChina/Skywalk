package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdGetHouseDeliverables extends CommunicationBase {

    CmdGetHouseDeliverables(Context context, int house) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_DELIVERABLES);
        mArgs = new Args(house);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/accessory/house/%d/deliverables", ((Args)mArgs).getHouse());
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResHouseDeliverables res = new ResHouseDeliverables(nErrCode, jObject);
        return res;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsGetHouseDeliverables {
        private int mHouseId = 0;

        Args(int house) {
            mHouseId = house;
        }

        @Override
        public boolean checkArgs() {
            if (mHouseId <= 0) {
                Log.e(TAG, "mHouseId:" + mHouseId);
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (mHouseId != ((Args)arg2).getHouse()) {
                return false;
            }
            return true;
        }

        @Override
        public int getHouse() {
            return mHouseId;
        }
    }
}
