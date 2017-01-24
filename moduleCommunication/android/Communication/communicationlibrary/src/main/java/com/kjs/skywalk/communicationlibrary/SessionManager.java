package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

/**
 * Created by Jackie on 2017/1/23.
 */

class SessionManager {
    private static SessionManager mManager = null;
    private static Context mContext = null;
    private static String mCookie = "";

    private SessionManager(Context context) {
        mContext = context;
    }

    public static SessionManager getManager(Context context) {
        if(mManager == null) {
            mManager = new SessionManager(context);
        }

        return mManager;
    }

    public static void reset() {
        mCookie = "";
    }

    public static void setCookie(String cookie) {
        Log.i(InternalDefines.TAG_COMMUNICATION_SESSION_MANAGER, "Cookie: " + cookie);
        mCookie = cookie;
    }

    public static String getCookie() {
        return mCookie;
    }

}
