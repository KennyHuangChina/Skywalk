package com.kjs.skywalk.communicationlibrary;

/**
 * Created by kenny on 2017/12/28.
 */

class ApiArgsGetSmsCode extends ApiArgsBase implements IApiArgs.IArgsGetSmsCode {
    private String mUserName = "";

    public ApiArgsGetSmsCode(String user) {
        mUserName = user;
    }

    @Override
    public boolean isEqual(IApiArgs.IArgsBase arg2) {
        if (!super.isEqual(arg2)) {
            return false;
        }
        ApiArgsGetSmsCode args2chk = (ApiArgsGetSmsCode)arg2;
        if (null != mUserName && null != args2chk.mUserName && mUserName.equals(args2chk.mUserName)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkArgs() {
        if (null == mUserName || mUserName.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String getUserName() {
        return mUserName;
    }
}
