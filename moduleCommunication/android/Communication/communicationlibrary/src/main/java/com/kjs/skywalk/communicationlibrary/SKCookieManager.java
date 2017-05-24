package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Jackie on 2017/1/25.
 */

class SKCookieManager {

    private static SKCookieManager mSKManager = null;
    private Context mContext = null;

    private SKCookieManager(Context context) {
        mContext = context;
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

        SharedPreferences sp= mContext.getSharedPreferences("Cookies", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Session-Cookie", value);
        editor.commit();
    }

    public String getCookie(String url) {
        Log.i(InternalDefines.TAG_COMMUNICATION_COOKIE_MANAGER, "getCookie: ");
        Log.i(InternalDefines.TAG_COMMUNICATION_COOKIE_MANAGER, "\tURL->" + url);
        SharedPreferences sp= mContext.getSharedPreferences("Cookies", Context.MODE_PRIVATE);
        String cookie = sp.getString("Session-Cookie", "");
        Log.i(InternalDefines.TAG_COMMUNICATION_COOKIE_MANAGER, "\tCookie->" + cookie);
        return cookie;
    }

    /*
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
    */
}
