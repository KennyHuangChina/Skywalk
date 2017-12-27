package com.kjs.skywalk.communicationlibrary;

import android.util.Log;

import static com.kjs.skywalk.communicationlibrary.IApiArgs.*;

/**
 * Created by kenny on 2017/12/27.
 */

class ApiArgsGetXPiclst extends ApiArgsBase implements IApiArgs.IArgsGetXPicLst {

    private int mXId  = 0;
    private int mType = -1;     // ref to PIC_TYPE_SUB_HOUSE_xxx
    private int mSize = -1;     // ref to

    public ApiArgsGetXPiclst(int xid, int type, int size) {
        mXId    = xid;
        mType   = type;
        mSize   = size;
    }

    @Override
    public boolean checkArgs() {
        String Fn = "[checkArgs] ";
//        return super.checkArgs();
        if (mXId <= 0) {
            Log.e(TAG, "xid:" + mXId);
            return false;
        }
        if (mType < PIC_TYPE_SUB_HOUSE_BEGIN || mType > PIC_TYPE_SUB_HOUSE_END) {
            Log.e(TAG, "type:" + mType);
            return false;
        }
        if (mSize < PIC_SIZE_ALL || mSize > PIC_SIZE_Large) {
            Log.e(TAG, "mType:" + mType);
            return false;
        }

        return true;
    }

    @Override
    public boolean isEqual(IApiArgs.IArgsBase arg2) {
        if (!super.isEqual(arg2)) {
            return false;
        }
        ApiArgsGetXPiclst arg2_chk = (ApiArgsGetXPiclst)arg2;
        if (mXId != arg2_chk.mXId || mType != arg2_chk.mType || mSize != arg2_chk.mSize) {
            return false;
        }

        return true;
    }

    @Override
    public int getXId() {
        return mXId;
    }

    @Override
    public int getSubType() {
        return mType;
    }

    @Override
    public int getSize() {
        return mSize;
    }
}
