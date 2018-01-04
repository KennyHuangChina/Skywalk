package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/3/12.
 */

class CmdAddHouseDeliverable extends CommunicationBase {

    CmdAddHouseDeliverable(Context context, int house, int did, int qty, String desc) {
        super(context, CommunicationInterface.CmdID.CMD_ADD_HOUSE_DELIVERABLE);
        mMethodType = "POST";
        mArgs = new Args(house, did, qty, desc);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/house/" + ((Args)mArgs).getId() + "/deliverable";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("did=" + ((Args)mArgs).getDeliverableId());
        mRequestData += "&";
        mRequestData += ("qty=" + ((Args)mArgs).getDeliverableQty());
        mRequestData += "&";
        mRequestData += ("desc=" + ((Args)mArgs).getDesc());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResAddResource res = new ResAddResource(nErrCode, jObject);
        return res;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsAddHouseDeliverable {
        private int     mDeliverable    = 0;    // deliverable id
        private int     mDelivQty       = 0;    // deliverable quantity
        private String  mDesc           = null; // deliverable description

        Args(int house, int did, int qty, String desc) {
            super(house);
            mDeliverable = did;
            mDelivQty    = qty;
            mDesc        = desc;
        }

        @Override
        public boolean checkArgs() {
            if (mDeliverable <= 0) {
                Log.e(TAG, "[checkArgs] mDeliverable:" + mDeliverable);
                return false;
            }
            if (mDelivQty <= 0) {
                Log.e(TAG, "[checkArgs] mDelivQty:" + mDelivQty);
                return false;
            }
            if (null == mDesc || mDesc.isEmpty()) {
                Log.e(TAG, "[checkArgs] mDesc:" + mDesc);
                return false;
            }
            mDesc = String2Base64(mDesc);
            return super.checkArgs();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            return super.isEqual(arg2);
        }

        @Override
        public int getDeliverableId() {
            return mDeliverable;
        }

        @Override
        public int getDeliverableQty() {
            return mDelivQty;
        }

        @Override
        public String getDesc() {
            return mDesc;
        }
    }
}
