package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdAddFacilityType extends CommunicationBase {

    CmdAddFacilityType(Context context, String name) {
        super(context, CommunicationInterface.CmdID.CMD_ADD_FACILITY_TYPE);
        mMethodType = "POST";
        mArgs = new Args(name);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/facility/type";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("name=" + ((Args)mArgs).getName());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResAddResource result = new ResAddResource(nErrCode, jObject);
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsAddFacility {
        private String mName = null;

        Args(String name) {
            mName = name;
        }

        @Override
        public boolean checkArgs() {
            if (null == mName || mName.isEmpty()) {
                Log.e(TAG, "[checkArgs] mName:" + mName);
                return false;
            }
            mName = String2Base64(mName);
            return true;    // super.checkArgs();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            return super.isEqual(arg2);
        }

        @Override
        public String getName() {
            return mName;
        }
    }
}
