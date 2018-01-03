package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by kenny on 2017/6/10.
 */

class CmdGetHousePrices extends CommunicationBase {

    CmdGetHousePrices(Context context, int house, int begin, int count) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_PRICE);
        mArgs = new Args(house, begin, count);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = String.format("/v1/house/%d/price", ((Args)mArgs).getHouseId());
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("bgn=" + ((Args)mArgs).getBeginPosi());
        mRequestData += ("&cnt=" + ((Args)mArgs).getFetchCnt());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResHousePriceList res = new ResHousePriceList(nErrCode, jObject);
        return res;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgFetchList implements IApiArgs.IArgsGetHousePriceHist {
        private int mHouse = 0;

        Args(int house, int begin, int cnt) {
            super(begin, cnt);
            mHouse = house;
        }

        @Override
        public boolean checkArgs() {
            if (mHouse <= 0) {
                Log.e(TAG, "[checkArgs] mHouse:" + mHouse);
                return false;
            }
            return super.checkArgs();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (mHouse != ((Args)arg2).mHouse) {
                return false;
            }
            return true;
        }

        @Override
        public int getHouseId() {
            return mHouse;
        }
    }
}
