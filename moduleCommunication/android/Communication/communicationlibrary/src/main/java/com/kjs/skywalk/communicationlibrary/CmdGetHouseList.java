package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.*;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.*;

/**
 * Created by kenny on 2017/3/2.
 */

class CmdGetHouseList extends CommunicationBase {
    protected int mType       = 0;  // 0: all house; 1: recommend; 2: deducted; 3: new
    protected int mBeginPosi  = 0;
    protected int mFetchCount = 0;

    protected HouseFilterCondition  mFilter = null;
    protected ArrayList<Integer>    mSort   = null;

    CmdGetHouseList(Context context, int type, int bgn, int cnt, HouseFilterCondition filter, ArrayList<Integer> sort) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_DIGEST_LIST);
        mNeedLogin  = false;
        mType       = type;
        mBeginPosi  = bgn;
        mFetchCount = cnt;
        mFilter     = filter;
        mSort       = sort;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (mType < 0 || mType > 3) {
            Log.e(TAG, "mType:" + mType);
            return false;
        }
        if (mBeginPosi < 0) {
            Log.e(TAG, "mBeginPosi:" + mBeginPosi);
            return false;
        }
        if (mFetchCount < 0) {
            Log.e(TAG, "mFetchCount:" + mFetchCount);
            return false;
        }

        if (mSort.size() > 0) {
            if (isSortTypeExist(SORT_PUBLISH_TIME) && isSortTypeExist(SORT_PUBLISH_TIME_DESC)) {
                Log.e(TAG, "SORT_PUBLISH_TIME vs SORT_PUBLISH_TIME_DESC");
                return false;
            }
            if (isSortTypeExist(SORT_RENTAL) && isSortTypeExist(SORT_RENTAL_DESC)) {
                Log.e(TAG, "SORT_RENTAL vs SORT_RENTAL_DESC");
                return false;
            }
            if (isSortTypeExist(SORT_APPOINT_NUMB) && isSortTypeExist(SORT_APPOINT_NUMB_DESC)) {
                Log.e(TAG, "SORT_APPOINT_NUMB vs SORT_APPOINT_NUMB_DESC");
                return false;
            }
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetHouseList result = new ResGetHouseList(nErrCode, jObject);
        return result;
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/list";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("type=" + mType);
        mRequestData += "&";
        mRequestData += ("bgn=" + mBeginPosi);
        mRequestData += "&";
        mRequestData += ("cnt=" + mFetchCount);

        if (null != mFilter) {
            // rental
            int nOp = mFilter.mRental.GetOp();
            if (nOp > 0) {
                if (!mRequestData.isEmpty()) {
                    mRequestData += "&";
                }
                String strVal = mFilter.mRental.GetValuesString();
                mRequestData += String.format("rtop=%d&rt=%s", nOp, strVal);
            }

            // Livingroom
            nOp = mFilter.mLivingroom.GetOp();
            if (nOp >= 0) {
                if (!mRequestData.isEmpty()) {
                    mRequestData += "&";
                }
                String strVal = mFilter.mLivingroom.GetValuesString();
                mRequestData += String.format("lvop=%d&lr=%s", nOp, strVal);
            }

            // Bedroom
            nOp = mFilter.mBedroom.GetOp();
            if (nOp >= 0) {
                if (!mRequestData.isEmpty()) {
                    mRequestData += "&";
                }
                String strVal = mFilter.mBedroom.GetValuesString();
                mRequestData += String.format("berop=%d&ber=%s", nOp, strVal);
            }

            // Bathroom min
            nOp = mFilter.mBathroom.GetOp();
            if (nOp >= 0) {
                if (!mRequestData.isEmpty()) {
                    mRequestData += "&";
                }
                String strVal = mFilter.mBathroom.GetValuesString();
                mRequestData += String.format("barop=%d&bar=%s", nOp, strVal);
            }

            // Acreage min
            nOp = mFilter.mAcreage.GetOp();
            if (nOp >= 0) {
                if (!mRequestData.isEmpty()) {
                    mRequestData += "&";
                }
                String strVal = mFilter.mAcreage.GetValuesString();
                mRequestData += String.format("acop=%d&ac=%s", nOp, strVal);
            }
        }

        String sort = "";
        for (int n = 0; n < mSort.size(); n++) {
            if (!sort.isEmpty()) {
                sort += ",";
            }
            sort += String.format("%d", mSort.get(n).intValue());
        }
        if (!sort.isEmpty()) {
            if (!mRequestData.isEmpty()) {
                mRequestData += "&";
            }
            mRequestData += String.format("sort=%s", sort);
        }

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    private boolean isSortTypeExist(int type) {
        for (int n = 0; n < mSort.size(); n++) {
            if (mSort.get(n).intValue() == type) {
                return true;
            }
        }

        return false;
    }
}
