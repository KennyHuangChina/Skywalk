package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.*;

/**
 * Created by kenny on 2017/3/2.
 */

class CmdGetHouseList extends CommunicationBase {

    CmdGetHouseList(Context context, int type, int bgn, int cnt, HouseFilterCondition filter, ArrayList<Integer> sort) {
        super(context, CommunicationInterface.CmdID.CMD_GET_HOUSE_DIGEST_LIST);
        mNeedLogin  = false;
        mArgs       = new ApiArgsGetHouseDigestList(type, bgn, cnt, filter, sort);
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
        ApiArgsGetHouseDigestList args = (ApiArgsGetHouseDigestList)mArgs;
        mRequestData = ("type=" + args.getType());
        mRequestData += "&";
        mRequestData += ("bgn=" + args.getBeginPosi());
        mRequestData += "&";
        mRequestData += ("cnt=" + args.getFetchCnt());

        if (null != args.getFilters()) {
            // rental
            int nOp = args.getFilters().mRental.GetOp();
            if (nOp > 0) {
                if (!mRequestData.isEmpty()) {
                    mRequestData += "&";
                }
                String strVal = args.getFilters().mRental.GetValuesString();
                mRequestData += String.format("rtop=%d&rt=%s", nOp, strVal);
            }

            // Livingroom
            nOp = args.getFilters().mLivingroom.GetOp();
            if (nOp >= 0) {
                if (!mRequestData.isEmpty()) {
                    mRequestData += "&";
                }
                String strVal = args.getFilters().mLivingroom.GetValuesString();
                mRequestData += String.format("lvop=%d&lr=%s", nOp, strVal);
            }

            // Bedroom
            nOp = args.getFilters().mBedroom.GetOp();
            if (nOp >= 0) {
                if (!mRequestData.isEmpty()) {
                    mRequestData += "&";
                }
                String strVal = args.getFilters().mBedroom.GetValuesString();
                mRequestData += String.format("berop=%d&ber=%s", nOp, strVal);
            }

            // Bathroom min
            nOp = args.getFilters().mBathroom.GetOp();
            if (nOp >= 0) {
                if (!mRequestData.isEmpty()) {
                    mRequestData += "&";
                }
                String strVal = args.getFilters().mBathroom.GetValuesString();
                mRequestData += String.format("barop=%d&bar=%s", nOp, strVal);
            }

            // Acreage min
            nOp = args.getFilters().mAcreage.GetOp();
            if (nOp >= 0) {
                if (!mRequestData.isEmpty()) {
                    mRequestData += "&";
                }
                String strVal = args.getFilters().mAcreage.GetValuesString();
                mRequestData += String.format("acop=%d&ac=%s", nOp, strVal);
            }

            // Property
            nOp = args.getFilters().mProperty.GetOp();
            if (nOp >= 0) {
                if (!mRequestData.isEmpty()) {
                    mRequestData += "&";
                }
                String strVal = args.getFilters().mProperty.GetValuesString();
                mRequestData += String.format("pop=%d&prop=%s", nOp, strVal);
            }
        }

        String sort = "";
        if (null != args.getSorts()) {
            for (int n = 0; n < args.getSorts().size(); n++) {
                if (!sort.isEmpty()) {
                    sort += ",";
                }
                sort += String.format("%d", args.getSorts().get(n).intValue());
            }
            if (!sort.isEmpty()) {
                if (!mRequestData.isEmpty()) {
                    mRequestData += "&";
                }
                mRequestData += String.format("sort=%s", sort);
            }
        }

        Log.d(TAG, "mRequestData: " + mRequestData);
    }
}
