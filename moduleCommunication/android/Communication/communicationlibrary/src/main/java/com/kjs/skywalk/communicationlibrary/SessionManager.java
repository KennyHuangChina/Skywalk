package com.kjs.skywalk.communicationlibrary;

import android.content.Context;

/**
 * Created by Jackie on 2017/1/23.
 */

class SessionManager {
    private static SessionManager mManager = null;
    private static Context mContext = null;

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

    }

}
