package com.kjs.skywalk.communicationlibrary;

import android.util.Log;

import com.kjs.skywalk.communicationlibrary.IApiArgs;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.*;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.*;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.*;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/12/27.
 */

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
    public boolean isEqual(IArgsBase arg2) {
        String Fn = "[isEqual] ";
        if (null == arg2) {
            Log.e(TAG, Fn + "Input args is NULL");
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

    private boolean isSortTypeExist(int type) {
        for (Integer sort : mSort) {
            if (sort == type) {
                return true;
            }
        }
        return false;
    }
}
