package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/12/16.
 */

class AssignHouseAgency extends CommunicationBase {

    AssignHouseAgency(Context context, int house, int agent) {
        super(context, CommunicationInterface.CmdID.CMD_ASSIGN_HOUSE_AGENT);
        mMethodType = "PUT";
        mArgs = new Args(house, agent);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/assignagency", ((Args)mArgs).getId());
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("aid=" + ((Args)mArgs).getAgent());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsAssignHouseAgency {
        protected int mAgent = 0;

        Args(int house, int agent) {
            super(house);
            mAgent = agent;
        }

        @Override
        public boolean checkArgs() {
            if (!super.checkArgs()) {
                return false;
            }
            if (mAgent <= 0) {
                Log.e(TAG, "[checkArgs] mAgent:" + mAgent);
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (mAgent != ((Args)arg2).mAgent) {
                return false;
            }
            return true;
        }

        @Override
        public int getAgent() {
            return mAgent;
        }
    }
}
