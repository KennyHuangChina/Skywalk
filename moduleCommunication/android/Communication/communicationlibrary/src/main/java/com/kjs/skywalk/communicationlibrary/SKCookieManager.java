package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.util.Log;
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

    public void setCookie(String url, String value) {
        Log.i(InternalDefines.TAG_COMMUNICATION_COOKIE_MANAGER, "setCookie: ");
        Log.i(InternalDefines.TAG_COMMUNICATION_COOKIE_MANAGER, "\tURL->" + url);
        Log.i(InternalDefines.TAG_COMMUNICATION_COOKIE_MANAGER, "\tCookie->" + value);
        if(url == null || value == null) {
            return;
        }

        mManager.setCookie(url, value);
    }

    public String getCookie(String url) {
        Log.i(InternalDefines.TAG_COMMUNICATION_COOKIE_MANAGER, "getCookie: ");
        Log.i(InternalDefines.TAG_COMMUNICATION_COOKIE_MANAGER, "\tURL->" + url);
        String cookie = mManager.getCookie(url);
        Log.i(InternalDefines.TAG_COMMUNICATION_COOKIE_MANAGER, "\tCookie->" + cookie);
        return cookie;
    }
}
