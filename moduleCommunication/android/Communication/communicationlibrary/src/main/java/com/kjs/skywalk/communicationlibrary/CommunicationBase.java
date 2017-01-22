package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CIProgressListener;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CICommandListener;
/**
 * Created by Jackie on 2017/1/20.
 */

class CommunicationBase implements InternalDefines.DoOperation {
    protected String TAG = "";
    protected Context mContext = null;
    protected String mMethodType = "";
    protected String mServerURL = "";
    protected String mCommandURL = "";

    protected MyUtils mUtils = null;
    protected CIProgressListener mProgressListener = null;
    protected CICommandListener mCommandListener = null;

    CommunicationBase(Context context) {
        Log.i(TAG, "Communication Base Constructor");
        mContext = context;
        mUtils = new MyUtils(context);
        mServerURL = ServerURL.mServerUri;
    }

    public interface CheckParameter {
        boolean checkParameter(HashMap<String, String> map);
    }

    @Override
    public int doOperation(HashMap<String, String> map, CICommandListener commandListener, CIProgressListener progressListener) {
        Log.i(TAG, "Communication Base: doOperation");
        mCommandListener = commandListener;
        mProgressListener = progressListener;
        return 0;
    }
}
