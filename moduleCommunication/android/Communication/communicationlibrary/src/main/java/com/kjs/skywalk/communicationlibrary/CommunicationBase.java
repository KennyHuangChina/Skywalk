package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CIProgressListener;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CICommandListener;

import org.json.JSONObject;

/**
 * Created by Jackie on 2017/1/20.
 */

class CommunicationBase implements InternalDefines.DoOperation, InternalDefines.CheckParameter,
        InternalDefines.CreateResultMap, InternalDefines.BeforeConnect, InternalDefines.AfterConnect, InternalDefines.ConnectFailed{
    protected String TAG = "CommunicationBase";
    protected String mAPI = "";
    protected Context mContext = null;
    protected String mMethodType = "";
    protected String mServerURL = "";
    protected String mCommandURL = "";
    protected String mRequestData = "";

    protected MyUtils mUtils = null;
    protected CIProgressListener mProgressListener = null;
    protected CICommandListener mCommandListener = null;

    // common header items
    protected String mSessionID = "";
    protected int    mVersion = 0;      // API version number
    protected SKCookieManager mCookieManager = null;

    CommunicationBase(Context context) {
        Log.i(TAG, "Communication Base Constructor");
        mContext = context;
        mUtils = new MyUtils(context);
        mServerURL = ServerURL.mServerUri;

        mCookieManager = SKCookieManager.getManager(context);
    }

    @Override
    public int doOperation(HashMap<String, String> map, CICommandListener commandListener, CIProgressListener progressListener) {
        Log.i(TAG, "Communication Base: doOperation");
        mCommandListener = commandListener;
        mProgressListener = progressListener;

        new Thread(new Runnable() {
            @Override
            public void run() {
                String returnCode = "";
                int retValue = InternalDefines.ERROR_CODE_OK;
                HttpConnector http = new HttpConnector(mContext);
                Log.i("(mServerURL:", mServerURL);
                Log.i("(mCommandURL:", mCommandURL);
                http.setURL(mServerURL, mCommandURL);
                http.setRequestMethod(mMethodType);
                http.setRequestData(mRequestData);
                doBeforeConnect(http);
                if((retValue = http.connect()) != InternalDefines.ERROR_CODE_OK) {
                    String strError = InternalDefines.getErrorDescription(retValue);
                    Log.e(TAG, "Can't connect to server. error: " +  strError);
                    returnCode = "" + retValue;
                    mCommandListener.onCommandFinished(mAPI, returnCode, strError, null);
                    doConnectFailed(http);
                    return;
                }

                if((retValue = http.sendRequest()) != InternalDefines.ERROR_CODE_OK) {
                    http.disconnect();
                    String strError = InternalDefines.getErrorDescription(retValue);
                    Log.e(TAG, "Can't connect to server. error: " +  strError);
                    returnCode = "" + retValue;
                    mCommandListener.onCommandFinished(mAPI, returnCode, strError, null);
                    doConnectFailed(http);
                    return;
                }

                http.disconnect();

                JSONObject jObject = http.getJsonObject();
                if(jObject == null) {
                    String strError = InternalDefines.getErrorDescription(InternalDefines.ERROR_CODE_HTTP_REQUEST_FAILED);
                    returnCode = "" + InternalDefines.ERROR_CODE_HTTP_REQUEST_FAILED;
                    mCommandListener.onCommandFinished(mAPI, returnCode, strError, null);
                    doConnectFailed(http);
                    return;
                }

                HashMap<String, String> map = doCreateResultMap(jObject);

                String strError = InternalDefines.getErrorDescription(InternalDefines.ERROR_CODE_OK);
                returnCode = "" + InternalDefines.ERROR_CODE_OK;
                mCommandListener.onCommandFinished(mAPI, returnCode, strError, map);

                doAfterConnect(http);

            }
        }).start();

        return 0;
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return false;
    }

    @Override
    public HashMap<String, String> doCreateResultMap(JSONObject jObject) {
        return null;
    }

    @Override
    public int doBeforeConnect(HttpConnector http) {
        http.setReadCookie(true);
        http.setWriteCookie(true);
        return 0;
    }

    @Override
    public int doAfterConnect(HttpConnector http) {
        return 0;
    }

    @Override
    public int doConnectFailed(HttpConnector http) {
        return 0;
    }
}
