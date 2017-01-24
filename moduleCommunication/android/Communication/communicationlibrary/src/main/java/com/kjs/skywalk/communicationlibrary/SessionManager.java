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
    private static String mSessionId = "";

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
        mSessionId = "";
    }

    private static String parseSessionID(String cookie) {
        String sId = "";

        String list[] = cookie.split(";");
        String tmp = list[0];
        list = tmp.split("=");
        if(list.length == 2 && list[0].equals("SKSession")) {
            if(list[1] == null) {
                sId = "";
            } else {
                sId = list[1];
            }
        }

        return sId;
    }

    public static void setCookie(String cookie) {
        if(cookie == null) {
            return;
        }
        Log.i(InternalDefines.TAG_COMMUNICATION_SESSION_MANAGER, "Cookie: " + cookie);
        mCookie = cookie;
        mSessionId = parseSessionID(cookie);
    }

    public static String getCookie() {
        return mCookie;
    }
}
