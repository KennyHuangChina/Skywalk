package com.kjs.skywalk.communicationlibrary;

import android.util.Log;

/**
 * Created by kenny on 2017/12/27.
 */

public class ApiArgsBase implements IApiArgs.IArgsBase {
    protected String TAG = getClass().getSimpleName();

    @Override
    public boolean checkArgs() {
        Log.w(TAG, "[checkArgs] please override this method to check if the arguments correct");
        return false;
    }

    @Override
    public boolean isEqual(IApiArgs.IArgsBase arg2) {
//        Log.w(TAG, "[checkArgs] please override this method to check if the arguments equal");
        if (null == arg2) {
            return false;
        }
        return true;
    }
}
