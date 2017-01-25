package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;

/**
 * Created by Jackie on 2017/1/25.
 */

class SKCookieManager {
    private static SKCookieManager mSKManager = null;
    private Context mContext = null;
    private CookieManager mManager = null;

    private SKCookieManager(Context context) {
        mContext = context;
        mManager = CookieManager.getInstance();
    }

    public static synchronized SKCookieManager getManager(Context context) {
        if(mSKManager == null) {
            mSKManager = new SKCookieManager(context);
        }

        return mSKManager;
    }


}
