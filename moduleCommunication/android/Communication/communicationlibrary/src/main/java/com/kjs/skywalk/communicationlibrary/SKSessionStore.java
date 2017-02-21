package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Jackie on 2017/2/20.
 */

class SKSessionStore {
    private static SKSessionStore mSKManager = null;
    private static Context mContext = null;

    private SKSessionStore(Context context) {
        mContext = context;
    }

    public static synchronized SKSessionStore getInstance(Context context) {
        if(mSKManager == null) {
            mSKManager = new SKSessionStore(context);
        }

        return mSKManager;
    }

    public static void save(String session) {
        // empty session means clear session locally saved
        if(session == null /*|| session.isEmpty()*/) {
            return;
        }

        Log.i(InternalDefines.TAG_COMMUNICATION_SESSION_STORE, "Save Session: " + session);
        SharedPreferences sp = mContext.getSharedPreferences("Session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("SessionId", session);
        editor.commit();
    }

    public static String get() {
        SharedPreferences sp= mContext.getSharedPreferences("Session", Context.MODE_PRIVATE);
        String session = sp.getString("SessionId", "");
        Log.i(InternalDefines.TAG_COMMUNICATION_SESSION_STORE, "\tGet SessionId->" + session);

        return session;
    }
}
