package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/10/1.
 */

class CmdRecommitHouseCertification extends CommunicationBase {

    CmdRecommitHouseCertification(Context context, int house, String comments) {
        super(context, CommunicationInterface.CmdID.CMD_RECOMMIT_HOUSE_CERTIFICATON);
        mMethodType = "POST";
        mArgs = new Args(house, comments);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/recert", ((Args)mArgs).getId());
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("cc=" + String2Base64(((Args)mArgs).getComments()));
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return new ResAddResource(nErrCode, jObject);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsRecommitHouseCertify {
        private String mComments = null;

        Args(int house, String comment) {
            super(house);
            mComments = comment;
        }

        @Override
        public boolean checkArgs() {
            if (!super.checkArgs()) {
                return false;
            }
            if (null == mComments || mComments.isEmpty()) {
                Log.e(TAG, "[checkArgs] mComments:" + mComments);
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (!mComments.equals(((Args)arg2).mComments)) {
                return false;
            }
            return true;
        }

        @Override
        public String getComments() {
            return mComments;
        }
    }
}
