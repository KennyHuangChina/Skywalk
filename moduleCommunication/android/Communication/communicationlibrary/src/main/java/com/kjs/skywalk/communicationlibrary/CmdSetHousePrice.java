package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/6/7.
 */

class CmdSetHousePrice extends CommunicationBase {

    CmdSetHousePrice(Context context, int house, int rt, int rm, boolean rpf, int spt, int spm) {
        super(context, CommunicationInterface.CmdID.CMD_SET_HOUSE_PRICE);
        mMethodType = "POST";
        mArgs = new Args(house, rt, rm, rpf, spt, spm);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/price", ((Args)mArgs).getId());
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("r_tp=" + ((Args)mArgs).getRentalTag());
        mRequestData += "&";
        mRequestData += ("r_bp=" + ((Args)mArgs).getRentalMin());
        mRequestData += "&";
        mRequestData += ("pf=" + ((Args)mArgs).includePropertyFee());
        mRequestData += "&";
        mRequestData += ("p_tp=" + ((Args)mArgs).getSellingPriceTag());
        mRequestData += "&";
        mRequestData += ("p_bp=" + ((Args)mArgs).getSellingPriceMin());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResAddResource result = new ResAddResource(nErrCode, jObject);
        return result;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsSetHousePrice {
        private int     mRentalTag      = 0;        // Rental, tag price
        private int     mRentalBottom   = 0;        // Rental, bottom price
        private boolean mPropertyFee    = false;    // if the rental involve the property fee
        private int     mPriceTag       = 0;        // Selling price, tag price
        private int     mPriceBottom    = 0;        // Selling price, bottom price

        Args(int house, int rt, int rm, boolean rpf, int spt, int spm) {
            super(house);
            mRentalTag      = rt;
            mRentalBottom   = rm;
            mPropertyFee    = rpf;
            mPriceTag       = spt;
            mPriceBottom    = spm;
        }

        @Override
        public boolean checkArgs() {
            if (!super.checkArgs()) {
                return false;
            }
            if (mRentalTag < 0) {
                Log.e(TAG, "mRentalTag: " + mRentalTag);
                return false;
            }
            if (mRentalBottom < 0) {
                Log.e(TAG, "mRentalBottom: " + mRentalBottom);
                return false;
            }
            if (mRentalBottom > mRentalTag) {
                Log.e(TAG, String.format("mRentalBottom(%d) > mRentalTag(%d)", mRentalBottom, mRentalTag));
                return false;
            }
            if (mPriceTag < 0) {
                Log.e(TAG, "mPriceTag: " + mPriceTag);
                return false;
            }
            if (mPriceBottom < 0) {
                Log.e(TAG, "mPriceBottom: " + mPriceBottom);
                return false;
            }
            if (mPriceBottom > mPriceTag) {
                Log.e(TAG, String.format("mPriceBottom(%d) > mPriceTag(%d)", mPriceBottom, mPriceTag));
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            Args arg_chk = (Args)arg2;
            if (mRentalTag != arg_chk.mRentalTag || mRentalBottom != arg_chk.mRentalBottom ||
                    mPropertyFee != arg_chk.mPropertyFee || mPriceTag != arg_chk.mPriceTag || mPriceBottom != arg_chk.mPriceBottom) {
                return false;
            }
            return true;
        }

        @Override
        public int getRentalTag() {
            return mRentalTag;
        }

        @Override
        public int getRentalMin() {
            return mRentalBottom;
        }

        @Override
        public boolean includePropertyFee() {
            return mPropertyFee;
        }

        @Override
        public int getSellingPriceTag() {
            return mPriceTag;
        }

        @Override
        public int getSellingPriceMin() {
            return mPriceBottom;
        }
    }
}
