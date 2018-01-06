package com.kjs.skywalk.communicationlibrary;

import android.util.Log;

/**
 * Created by kenny on 2018/1/4.
 */
/******************************************************************************************
 *
 */
class ApiArgsObjId extends ApiArgsBase implements IApiArgs.IArgsObjId {
    protected int mId = -1;

    ApiArgsObjId(int id) {
        mId = id;
    }

    @Override
    public boolean checkArgs() {
        if (mId <= 0) {
            Log.e(TAG, "[checkArgs] mId:" + mId);
            return false;
        }
        return true; // super.checkArgs();
    }

    @Override
    public boolean isEqual(IApiArgs.IArgsBase arg2) {
        if (!super.isEqual(arg2)) {
            return false;
        }
        if (mId != ((ApiArgsObjId)arg2).mId) {
            return false;
        }
        return true;
    }

    @Override
    public int getId() {
        return mId;
    }
}

/******************************************************************************************
 *
 */
class ApiArgsFacility extends ApiArgsBase implements IApiArgs.IArgsAddFacility {
    private String  mName   = null;
    private int     mType   = 0;
    private String  mPic    = null;

    ApiArgsFacility(int type, String name, String pic) {
        mType   = type;
        mName   = name;
        mPic    = pic;
    }

    @Override
    public boolean checkArgs() {
        if (mType <= 0) {
            Log.e(TAG, "[checkArgs] mType:" + mType);
            return false;
        }

        if (null == mName || mName.isEmpty()) {
            Log.e(TAG, "[checkArgs] mName:" + mName);
            return false;
        }
        mName = CommunicationBase.String2Base64(mName);

        if (null == mPic || mPic.length() == 0) {
            Log.e(TAG, "[checkArgs] picture file not assigned");
            return false;
        }
        if (!CUtilities.isPicture(mPic)) {
            Log.e(TAG, "[checkArgs] picture file not exist");
            return false;
        }

        return true;    // super.checkArgs();
    }

    @Override
    public boolean isEqual(IApiArgs.IArgsBase arg2) {
        if (!super.isEqual(arg2)) {
            return false;
        }

        if (mType != ((ApiArgsFacility)arg2).mType || !mName.equals(((ApiArgsFacility)arg2).mName)) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public String getPic() {
        return mPic;
    }
}

/******************************************************************************************
 *
 */
class FacilityItem implements IApiArgs.IFacilityItem {
    private String  TAG     = getClass().getSimpleName();
    public  int      mFId   = 0;    // facility id
    public  int      mQty   = 0;    // facility quantity
    public  String   mDesc  = null; // facility description

    public FacilityItem(int id, int qty, String desc) {
        mFId    = id;
        mQty    = qty;
        mDesc   = desc;
    }

    public boolean checkFacilityItem() {
        String Fn = "[checkFacilityItem] ";
        if (mFId <= 0) {
            Log.e(TAG, Fn + "mFId: " + mFId);
            return false;
        }
        if (mQty <= 0) {
            Log.e(TAG, Fn + "mQty: " + mQty);
            return false;
        }
        if (null == mDesc || mDesc.isEmpty()) {
            Log.e(TAG, Fn + "mDesc: " + mDesc);
            return false;
        }
        return true;
    }

    public boolean equal(FacilityItem fi) {
        if (mFId != fi.mFId || mQty != fi.mQty) {
            return false;
        }
        if (null != mDesc && null != fi.mDesc && mDesc.equals(fi.mDesc)) {
            return true;
        }
        return false;
    }

    @Override
    public int getFId() {
        return mFId;
    }

    @Override
    public int getFQty() {
        return mQty;
    }

    @Override
    public String getDesc() {
        return mDesc;
    }
}
