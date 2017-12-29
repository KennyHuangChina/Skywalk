package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.*;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.*;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.*;

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

    /////////////////////////////////////////////////////////////////////////////////////
    //
    //
    class ApiArgsGetHouseDigestList extends ApiArgsBase implements IApiArgs.IArgsGetHouseDigestList {

        private int mType       = IApiArgs.GET_HOUSE_DIG_LST_TYPE_ALL;    // GET_HOUSE_DIG_LST_TYPE_XXX
        private int mBegin      = 0;
        private int mFetchCnt   = 0;

        private HouseFilterCondition  mFilter = null;
        private ArrayList<Integer>    mSort   = null;

        public ApiArgsGetHouseDigestList(int type, int bgn, int cnt,
                                         CommunicationInterface.HouseFilterCondition filter,
                                         ArrayList<Integer> sort) {
            mType       = type;
            mBegin      = bgn;
            mFetchCnt   = cnt;
            mFilter     = filter;
            mSort       = sort;
        }

        @Override
        public int getType() {
            return mType;
        }

        @Override
        public int getBeginPosi() {
            return mBegin;
        }

        @Override
        public int getFetchCnt() {
            return mFetchCnt;
        }

        @Override
        public HouseFilterCondition getFilters() {
            return mFilter;
        }

        @Override
        public ArrayList<Integer> getSorts() {
            return mSort;
        }

        @Override
        public boolean checkArgs() {
            if (mType < GET_HOUSE_DIG_LST_TYPE_BEGIN || mType > GET_HOUSE_DIG_LST_TYPE_END) {
                Log.e(TAG, "mType:" + mType);
                return false;
            }
            if (mBegin < 0) {
                Log.e(TAG, "begin:" + mBegin);
                return false;
            }
            if (mFetchCnt < 0) {
                Log.e(TAG, "mFetchCnt:" + mFetchCnt);
                return false;
            }

            if (null != mSort && mSort.size() > 0) {
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
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            String Fn = "[isEqual] ";
            if (!super.isEqual(arg2)) {
                return false;
            }

            ApiArgsGetHouseDigestList arg2chk = (ApiArgsGetHouseDigestList)arg2;
            if (arg2chk.mType != mType) {
                return false;
            }
            if (arg2chk.mBegin != mBegin) {
                return false;
            }
            if (arg2chk.mFetchCnt != mFetchCnt) {
                return false;
            }
            Log.w(TAG, Fn + "TODO: do checking for filter and sort");
            return true;
        }

        @Override
        public String toString() {
//        return super.toString();
            String str = "type:" + mType + ", begin:" + mBegin + ", to fetch:" + mFetchCnt + "\n";
            return str;
        }

        private boolean isSortTypeExist(int type) {
            for (Integer sort : mSort) {
                if (sort == type) {
                    return true;
                }
            }
            return false;
        }
    }
}
