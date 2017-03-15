package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CIProgressListener;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CICommandListener;

import org.json.JSONObject;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.*;

/**
 * Created by Jackie on 2017/1/20.
 */

class CommunicationBase implements  InternalDefines.DoOperation,
                                    InternalDefines.CheckParameter,
                                    InternalDefines.CreateResult,
                                    InternalDefines.BeforeConnect,
                                    InternalDefines.AfterConnect,
                                    InternalDefines.ConnectFailed,
                                    InternalDefines.IApiName {
    protected   String  TAG         = "CommunicationBase";
    private     int     mAPI        = CmdID.CMD_TEST;
    protected   Context mContext    = null;
    protected   String  mMethodType = "";
    protected   String  mServerURL  = "";
    protected   String  mCommandURL = "";
    protected   String  mRequestData = "";

    protected MyUtils               mUtils              = null;
    protected CIProgressListener    mProgressListener   = null;
    protected CICommandListener     mCommandListener    = null;

    // common header items
    protected String            mSessionID      = "";
    protected int               mVersion        = 0;      // API version number
    protected SKCookieManager   mCookieManager  = null;
    protected String            mRadom          = "";

    CommunicationBase(Context context, int cmdId) {
        Log.i(TAG, "Communication Base Constructor");
        mContext = context;
        mAPI = cmdId;
        mVersion = 1;   // please set it in sub-class if its version is not 1
        mUtils = new MyUtils(context);
        mServerURL = ServerURL.mServerUri;

        mCookieManager = SKCookieManager.getManager(context);
    }

    @Override
    public int doOperation(HashMap<String, String> map, CICommandListener commandListener, CIProgressListener progressListener) {
        Log.i(TAG, "Communication Base: doOperation");
        mCommandListener = commandListener;
        mProgressListener = progressListener;

        if (null == getRequestURL()) {
            Log.e(TAG, "No URL set");
            return CommunicationError.CE_COMMAND_ERROR_FATAL_ERROR;
        }
        generateRequestData();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // check network status
                ConnectionDetector cd = new ConnectionDetector(mContext);
                if (!cd.isConnectingToInternet()) {
                    IApiResults.ICommon result = doParseResult(InternalDefines.ERROR_CODE_NETWORK_NOTAVAILABLE, null);
                    mCommandListener.onCommandFinished(mAPI, result);
                    return;
                }

                String returnCode = "";
                int retValue = InternalDefines.ERROR_CODE_OK;
                HttpConnector http = new HttpConnector(mContext);
                http.setURL(mServerURL, mCommandURL);
                http.setRequestMethod(mMethodType);
                http.setRequestData(mRequestData);

                doBeforeConnect(http);
                if ((retValue = http.connect()) != InternalDefines.ERROR_CODE_OK) {
                    String strError = InternalDefines.getErrorDescription(retValue);
                    Log.e(TAG, "Fail to connect to server. error: " +  strError);
                    returnCode = "" + retValue;
                    mCommandListener.onCommandFinished(mAPI, null);
                    doConnectFailed(http);
                    return;
                }

                retValue = http.sendRequest();
                http.disconnect();

                if (retValue != InternalDefines.ERROR_CODE_OK) {
                    String strError = http.getErrorDescription();   // InternalDefines.getErrorDescription(retValue);
                    Log.e(TAG, "Fail to send request to server. error: " +  strError);
//                    returnCode = "[" + retValue + "] " + http.getErrorCode();
                    int nErrCode = http.getErrorCode();
                    JSONObject jObject = http.getJsonObject();
                    IApiResults.ICommon result = doParseResult(nErrCode, jObject);
                    mCommandListener.onCommandFinished(mAPI, result);
                    doConnectFailed(http);
                    return;
                }

                JSONObject jObject = http.getJsonObject();
                if (jObject == null) {
//                    String strError = InternalDefines.getErrorDescription(InternalDefines.ERROR_CODE_HTTP_REQUEST_FAILED);
//                    returnCode = "" + InternalDefines.ERROR_CODE_HTTP_REQUEST_FAILED;
                    mCommandListener.onCommandFinished(mAPI, null);
                    doConnectFailed(http);
                    return;
                }

                IApiResults.ICommon result = doParseResult(InternalDefines.ERROR_CODE_OK, jObject);

                String strError = InternalDefines.getErrorDescription(InternalDefines.ERROR_CODE_OK);
                returnCode = "" + InternalDefines.ERROR_CODE_OK;
                mCommandListener.onCommandFinished(mAPI, result);

                doAfterConnect(http);

            }
        }).start();

        return CommunicationError.CE_ERROR_NO_ERROR;
    }

    @Override
    public String getRequestURL() {
        return null;
    }

    @Override
    public void generateRequestData() {
        mRequestData = "";
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        return false;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResBase result = new ResBase(nErrCode, jObject);
        return result;
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

    protected String generateRandom() {
        String strRand = "";
        Random rdm = new Random(System.currentTimeMillis());
        int intRd = Math.abs((int)(rdm.nextDouble() * 1000000));
        strRand = "" + intRd;
        Log.i(TAG, "strRand:" + strRand);

        return strRand;
    }

    // Convert string to BASE64
    protected String String2Base64(String str) {
        byte[] bstr = new byte[0];
        try {
            bstr = str.getBytes(("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        return Base64.encodeToString(bstr, Base64.URL_SAFE);
    }

    // Convert string to URL safe
    String getUrlSafeString(String str) {
        // A StringBuffer Object
        StringBuffer sb = new StringBuffer();
        sb.append(str);
        String Utf8String = "";
        String UrlsafeString = "";
        try {
            Utf8String = new String(sb.toString().getBytes("UTF-8"));
            UrlsafeString = URLEncoder.encode(Utf8String, "UTF-8");
            System.out.println("utf-8 编码：" + UrlsafeString) ;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // return to String Formed
        return UrlsafeString;
    }

    @Override
    public String GetApiName() {
        return "" + mAPI;
    }
}
