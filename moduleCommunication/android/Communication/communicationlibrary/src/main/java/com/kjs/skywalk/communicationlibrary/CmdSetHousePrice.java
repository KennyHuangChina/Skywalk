package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/6/7.
 */

class CmdSetHousePrice extends CommunicationBase {
    private int     mHouse          = 0;
    private int     mRentalTag      = 0;        // Rental, tag price
    private int     mRentalBottom   = 0;        // Rental, bottom price
    private boolean mPropertyFee    = false;    // if the rental involve the property fee
    private int     mPriceTag       = 0;        // Selling price, tag price
    private int     mPriceBottom    = 0;        // Selling price, bottom price

    CmdSetHousePrice(Context context, int house) {
        super(context, CommunicationInterface.CmdID.CMD_SET_HOUSE_PRICE);
        mMethodType = "POST";
        mHouse = house;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/price", mHouse);
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("r_tp=" + mRentalTag);
        mRequestData += "&";
        mRequestData += ("r_bp=" + mRentalBottom);
        mRequestData += "&";
        mRequestData += ("pf=" + mPropertyFee);
        mRequestData += "&";
        mRequestData = ("p_tp=" + mPriceTag);
        mRequestData += "&";
        mRequestData += ("p_bp=" + mPriceBottom);
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!map.containsKey(CommunicationParameterKey.CPK_HOUSE_RENTAL_TAG) ||
                !map.containsKey(CommunicationParameterKey.CPK_HOUSE_RENTAL_BOTTOM) ||
                !map.containsKey(CommunicationParameterKey.CPK_HOUSE_RENTAL_PROPFEE) ||
                !map.containsKey(CommunicationParameterKey.CPK_HOUSE_RPRICE_TAG) ||
                !map.containsKey(CommunicationParameterKey.CPK_HOUSE_PRICE_BOTTOM)) {
            return false;
        }

        try {
            mRentalTag = Integer.parseInt(map.get(CommunicationParameterKey.CPK_HOUSE_RENTAL_TAG));
            if (mRentalTag < 0) {
                Log.e(TAG, "mRentalTag: " + mRentalTag);
                return false;
            }
            mRentalBottom = Integer.parseInt(map.get(CommunicationParameterKey.CPK_HOUSE_RENTAL_BOTTOM));
            if (mRentalBottom < 0) {
                Log.e(TAG, "mRentalBottom: " + mRentalBottom);
                return false;
            }
            mPropertyFee = Boolean.parseBoolean(map.get(CommunicationParameterKey.CPK_HOUSE_RENTAL_PROPFEE));

            mPriceTag = Integer.parseInt(map.get(CommunicationParameterKey.CPK_HOUSE_RPRICE_TAG));
            if (mPriceTag < 0) {
                Log.e(TAG, "mPriceTag: " + mPriceTag);
                return false;
            }
            mPriceBottom = Integer.parseInt(map.get(CommunicationParameterKey.CPK_HOUSE_PRICE_BOTTOM));
            if (mPriceBottom < 0) {
                Log.e(TAG, "mPriceBottom: " + mPriceBottom);
                return false;
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        return true;    // super.checkParameter(map);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        return super.doParseResult(nErrCode, jObject);
    }
}
