package com.kjs.skywalk.communicationlibrary;

import android.util.Log;

/**
 * Created by kenny on 2018/1/2.
 */

class ApiArgHouseId extends ApiArgsBase implements IApiArgs.IArgsHouseId {
    private int mHouseId = -1;

    ApiArgHouseId(int house) {
        mHouseId = house;
    }

    @Override
    public boolean checkArgs() {
        if (mHouseId <= 0) {
            Log.e(TAG, "[checkArgs] mHouseId:" + mHouseId);
            return false;
        }
        return super.checkArgs();
    }

    @Override
    public boolean isEqual(IApiArgs.IArgsBase arg2) {
        if (!super.isEqual(arg2)) {
            return false;
        }
        if (mHouseId != ((ApiArgHouseId)arg2).mHouseId) {
            return false;
        }
        return true;
    }

    @Override
    public int getHouseId() {
        return mHouseId;
    }
}
