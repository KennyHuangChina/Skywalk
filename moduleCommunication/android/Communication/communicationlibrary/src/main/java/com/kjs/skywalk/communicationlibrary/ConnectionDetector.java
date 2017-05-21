package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by kenny on 2017/2/27.
 */

class ConnectionDetector {
    final String TAG = getClass().getSimpleName();
    private Context mContext;

    public ConnectionDetector(Context context){
        this.mContext = context;
    }

    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivity) {
            return false;
        }
/*
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (null == info) {
            return false;
        }

        for (int i = 0; i < info.length; i++) {
            if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
*/
        NetworkInfo.State wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (wifi == NetworkInfo.State.CONNECTED) {
            Log.i(TAG, "WiFi is connected");
            return true;
        }
        Log.w(TAG, "WiFi is not connected");

        NetworkInfo.State mobile = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (mobile == NetworkInfo.State.CONNECTED) {
            Log.i(TAG, "mobile is connected");
            return true;
        }
        Log.w(TAG, "mobile is not connected");

        return false;
    }
}
