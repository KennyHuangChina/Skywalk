package com.kjs.skywalk.communicationlibrary;

import android.util.Log;

/**
 * Created by kenny on 2018/1/4.
 */

class ApiArgPropertyInfo extends ApiArgsBase implements IApiArgs.IArgsAddProperty {
    private String mPropName = null;
    private String mPropAddr = null;
    private String mPropDesc = null;

    ApiArgPropertyInfo(String name, String addr, String desc) {
        mPropName = name;
        mPropAddr = addr;
        mPropDesc = desc;
    }

    @Override
    public boolean checkArgs() {
        String Fn = "[checkArgs] ";
//            return super.checkArgs();
        if (null == mPropName || mPropName.isEmpty()) {
            Log.e(TAG, Fn + "mPropName:" + mPropName);
            return false;
        }
        mPropName = CommunicationBase.String2Base64(mPropName);

        if (null == mPropAddr || mPropAddr.isEmpty()) {
            Log.e(TAG, Fn + "mPropAddr:" + mPropAddr);
            return false;
        }
        mPropAddr = CommunicationBase.String2Base64(mPropAddr);

        if (null == mPropDesc || mPropDesc.isEmpty()) {
            Log.e(TAG, Fn + "mPropDesc:" + mPropDesc);
            return false;
        }
        mPropDesc = CommunicationBase.String2Base64(mPropDesc);

        return true;
    }

    @Override
    public boolean isEqual(IApiArgs.IArgsBase arg2) {
        if (!super.isEqual(arg2)) {
            return false;
        }
        if (!mPropName.equals(((ApiArgPropertyInfo)arg2).mPropName)) {
            return false;
        }
        if (!mPropAddr.equals(((ApiArgPropertyInfo)arg2).mPropAddr)) {
            return false;
        }
        if (!mPropDesc.equals(((ApiArgPropertyInfo)arg2).mPropDesc)) {
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return mPropName;
    }

    @Override
    public String getAddress() {
        return mPropAddr;
    }

    @Override
    public String getDesc() {
        return mPropDesc;
    }
}
