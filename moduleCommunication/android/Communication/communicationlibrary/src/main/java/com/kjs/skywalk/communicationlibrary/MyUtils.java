package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Jackie on 2017/1/20.
 */

class MyUtils {
    private Context mContext = null;

    MyUtils(Context context) {
        mContext = context;
    }

    public static void printInputParameters(HashMap<String, String> map) {
        Log.i(InternalDefines.TAG_COMMUNICATION_UTILS, "input parameters:");
        Iterator it = map.keySet().iterator();
        while(it.hasNext()) {
            String key = (String)it.next();
            Log.i(InternalDefines.TAG_COMMUNICATION_UTILS, key + ": " + map.get(key));
        }
    }
}
