package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.support.v4.*;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.FileNameMap;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Jackie on 2017/1/17.
 */

class HttpConnector {
    private String TAG = "HttpConnector"; // getLocalClassName();
    private Context mContext = null;
    private String mStringURL = "";
    private String mStringServerURL = "";
    private int mErrorCode = 0;
    private String mErrorDescription = "";
    private String mToken = "";
    private String mMethod = "POST";
    private String mRequestData = "";
    private String mUploadFile = "";
    private URL mURL = null;
    private HttpURLConnection mConnection = null;
    private boolean mReadCookie = false;
    private boolean mWriteCookie = false;
    private SKCookieManager mCookieManager = null;

    private static final String BOUNDARY_STRING = "skywalk_http_boundary";
    private static final String BOUNDARY_START = "--" + BOUNDARY_STRING + "\r\n";
    private static final String BOUNDARY_END = "--" + BOUNDARY_STRING + "--\r\n";

    public HttpConnector(Context context) {
        mContext = context;
        mCookieManager = SKCookieManager.getManager(context);
    }
    private JSONObject mJsonObj = null;

    public void setURL(String serverURL, String url) {
        mStringServerURL = serverURL;
        mStringURL = url;
    }

    public void setRequestMethod(String method) {
        if(!method.equals("POST") && !method.equals("GET") && !method.equals("PUT") && !method.equals("DELETE")) {
            return;
        }

        mMethod = method;
    }

    public void setUploadFile(String file) {
        mUploadFile = file;
    }

    public void setRequestData(String strRequest) {
        if(strRequest == null) {
            mRequestData = "";
        } else {
            mRequestData = strRequest;
        }

        Log.i(TAG, "Request Data: " + mRequestData);
    }

    public void setReadCookie(boolean read) {
        mReadCookie = read;
    }

    public void setWriteCookie(boolean write) {
        mWriteCookie = write;
    }

    public int connect() {
        mErrorCode = 0;
        mErrorDescription = "";

        int nRetCode = InternalDefines.ERROR_CODE_OK;
        if(mStringServerURL == null || mStringServerURL.isEmpty()) {
            return InternalDefines.ERROR_CODE_HTTP_INVALID_URL;
        }
        if(mStringURL == null || mStringURL.isEmpty()) {
            return InternalDefines.ERROR_CODE_HTTP_INVALID_URL;
        }

        String url = mStringServerURL + mStringURL ;
        if (mMethod.equals("GET") && !mRequestData.isEmpty()) {
            // Kenny: only the "Get" http request could put the arguments in request URL
            url += "?" + mRequestData;
        }

        try {
            mURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return InternalDefines.ERROR_CODE_HTTP_CONNECTION;
        }

        try {
            mConnection = (HttpURLConnection) mURL.openConnection();
            mConnection.setConnectTimeout(5 * 1000);
            if (mConnection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsUrlConnection = (HttpsURLConnection)mConnection;
                httpsUrlConnection.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostName, SSLSession session) {
                        if (!mStringServerURL.startsWith("https://")) {
                            return false;
                        }

                        URL serverUrl;
                        try {
                            serverUrl = new URL(mStringServerURL);
                        } catch (MalformedURLException e) {
                            return false;
                        }
                        String serverHost = serverUrl.getHost();
                        return hostName.compareToIgnoreCase(serverHost) == 0 ? true : false;
                    }
                });

                // Create an SSLContext that uses our TrustManager
                SSLContext context = createSSLSocketContext();
                if (context == null) {
                    return InternalDefines.ERROR_CODE_HTTP_CONNECTION_SSL;
                }
                httpsUrlConnection.setSSLSocketFactory(context.getSocketFactory());
            } else {
                Log.i(TAG, "Connect with http mode");
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return nRetCode;
    }

    public int disconnect() {
        if(mConnection != null) {
            mConnection.disconnect();
        }

        mConnection = null;

        return InternalDefines.ERROR_CODE_OK;
    }

    public JSONObject getJsonObject() {
        return mJsonObj;
    }

    public String getToken(){
        return mToken;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorDescription() {
        return mErrorDescription;
    }

    public int sendRequest() {
        if(mConnection == null) {
            return InternalDefines.ERROR_CODE_HTTP_CONNECTION;
        }

        mJsonObj = null;
        if (mWriteCookie) {
            String cookie = mCookieManager.getCookie(mConnection.getURL().toString());
            Log.i(TAG, "Send Cookie: " + cookie);
            if (cookie != null && !cookie.isEmpty()) {
                mConnection.setRequestProperty("Cookie", cookie);
            }
        }

        try {
            if (mMethod.equals("POST") || mMethod.equals("PUT")) {
                File srcFile = null;
                String contentType = "";
                boolean uploadFile = false;
                if (!mUploadFile.isEmpty()) {
                    srcFile = new File(mUploadFile);
                    if (srcFile.exists()) {
                        contentType = CUtilities.getMimeType(srcFile.getName());
                        if (contentType.length() > 0) {
                            Log.d(TAG, "contentType:" + contentType);
                            uploadFile = true;
//                            String strType = contentType.substring(0, 4);
                            if (contentType.toLowerCase().startsWith("image")) {
                                // TODO: scal down the picture if its size exceed a certain range, for example, 1920 x 1080
//                                srcFile.
                            }
                        }
                    }
                }

                mConnection.setRequestMethod(mMethod);
                mConnection.setDoOutput(true);
                mConnection.setUseCaches(false);
                mConnection.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARY_STRING);
//                mConnection.setFixedLengthStreamingMode(strRequestData.length());
                DataOutputStream wr = new DataOutputStream(mConnection.getOutputStream());

                if (!mRequestData.isEmpty()) {
                    String list[] = mRequestData.split("&");
                    for (int i = 0; i < list.length; i++) {
                        String tmpString = list[i];
                        int nPos = tmpString.indexOf("=");
                        if (nPos >= 0) {
                            String str0 = tmpString.substring(0, nPos);
                            String str1 = tmpString.substring(nPos + 1, tmpString.length());
                            if (str0 != null && !str0.isEmpty()) {
                                wr.writeBytes(BOUNDARY_START);
                                String strLine = String.format("Content-Disposition: form-data; name=\"%s\"\r\n\r\n%s\r\n", str0, str1);
                                Log.i(TAG, strLine);
                                wr.writeBytes(strLine);
                            }
                        }
//                    String tmp[] = list[i].split("=");
//                    if (tmp.length == 2) {
//                        String name = tmp[0];
//                        String value = tmp[1];
//                        wr.writeBytes(BOUNDARY_START);
//                        String strLine = String.format("Content-Disposition: form-data; name=\"%s\"\r\n\r\n%s\r\n", name, value);
//                        Log.i(TAG, strLine);
//                        wr.writeBytes(strLine);
//                    }
                    }
                }

                if (uploadFile) {
                    DataInputStream fileIS;
                    try {
                        wr.writeBytes(BOUNDARY_START);
                        //String strTmp = String.format("Content-Disposition:form-data;name=\"data\";filename=\"%s\"\r\nContent-Type:application/x-zip-compressed\r\n\r\n", srcFile.getName());
                        String strTmp = String.format("Content-Disposition:form-data;name=\"file\";filename=\"%s\"\r\n", srcFile.getName());
                        wr.writeBytes(strTmp);

//                        strTmp = String.format("Content-Type:application/x-zip-compressed\r\n\r\n");
                        strTmp = String.format("Content-Type:%s\r\n\r\n", contentType);
                        wr.writeBytes(strTmp);

                        fileIS = new DataInputStream(new FileInputStream(srcFile));

                        int bytes = 0;
                        byte[] bufferOut = new byte[1024];
                        while ((bytes = fileIS.read(bufferOut)) != -1) {
                            wr.write(bufferOut, 0, bytes);
                        }

                        wr.writeBytes("\r\n");

                        fileIS.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return InternalDefines.ERROR_CODE_HTTP_SOURCE_FILE_NOT_FOUND;
                    }
                }

                wr.writeBytes(BOUNDARY_END);
                wr.writeBytes("\r\n");
                wr.flush();
            } else if (mMethod.equals("GET")) {
                mConnection.setRequestMethod(mMethod);
                mConnection.setDoOutput(false);
            }

            if (mReadCookie) {
                Map<String, List<String>> map = mConnection.getHeaderFields();
                String cookieFromServer = mConnection.getHeaderField("Set-Cookie");
                mCookieManager.setCookie(mConnection.getURL().toString(), cookieFromServer);
            }

            InputStream in = new BufferedInputStream(mConnection.getInputStream());
            mJsonObj = getResponsObject(in);
            return InternalDefines.ERROR_CODE_OK;

        } catch (ProtocolException e1) {
            e1.printStackTrace();
            mErrorCode = InternalDefines.ERROR_CODE_PROTOCOL;
        } catch (JSONException e) {
            e.printStackTrace();
            mErrorCode = InternalDefines.ERROR_CODE_HTTP_REQUEST_FAILED;
        } catch (UnknownHostException eHost) {
            eHost.printStackTrace();
            mErrorCode = InternalDefines.ERROR_CODE_HTTP_UNKNOWN_HOST;
        } catch (IOException e) {
            e.printStackTrace();
            if (-1 != (mErrorCode = checkIOException(e))) {
                return mErrorCode;
            }

            InputStream in = new BufferedInputStream(mConnection.getErrorStream());
            try {
                mJsonObj = getResponsObject(in);
            } catch (IOException e1) {
                e1.printStackTrace();
                mErrorCode = InternalDefines.ERROR_CODE_HTTP_REQUEST_FAILED;
            } catch (JSONException e1) {
                e1.printStackTrace();
                try {
                    mErrorCode = mConnection.getResponseCode();
//                    mErrorDescription = mConnection.getResponseMessage();
                } catch (IOException e2) {
                    e2.printStackTrace();
                    mErrorCode = InternalDefines.ERROR_CODE_HTTP_REQUEST_FAILED;
                }
            }
        }
        return mErrorCode;
    }

    private JSONObject getResponsObject(InputStream is) throws IOException, JSONException {
        String RespondString = "";
        byte[] response = new byte[1400];
        JSONObject json = null;

//        try {
            while (true) {
                int ret = is.read(response);
                if (ret > 0) {
                    String temp = new String(response, 0, ret);
                    RespondString += temp;
                } else {
                    break;
                }
            }

            if (RespondString.length() > 0) {
                json = new JSONObject(RespondString);
                mErrorCode = json.getInt("ErrCode");
                mErrorDescription = json.getString("ErrString");
            }
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return json;
    }

    private int checkIOException(IOException exception) {
        int nErrCode = -1;
        if (exception instanceof java.net.SocketTimeoutException) {
            nErrCode = InternalDefines.ERROR_CODE_SCOCKET_TIMEOUT;
        } else if (exception instanceof java.net.ConnectException) {
            String err = exception.getMessage();
            if (err.indexOf("ENETUNREACH") >= 0) {
                nErrCode = InternalDefines.ERROR_CODE_NETWORK_UNREACH;
            } else if (err.indexOf("EHOSTUNREACH") >= 0) {
                nErrCode = InternalDefines.ERROR_CODE_HOST_UNREACH;
            } else if (err.indexOf("ECONNREFUSED") >= 0) {
                nErrCode = InternalDefines.ERROR_CODE_CONNECTION_REFUSED;
            } else {
                Log.w(TAG, "error string: " + err);
            }
        } else if (exception instanceof java.io.FileNotFoundException) {
            // file not found, but this is real result returned by server, so we need to do
            //   further checking by respond string of request
        } else {
            Log.w(TAG, "Untyped exception");
        }

        return nErrCode;
    }

    private int getRawID(Context context, String name)
    {
        return context.getResources().getIdentifier(name, "raw", context.getPackageName());
    }

    private int getStringID(Context context, String name)
    {
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }

    private SSLContext createSSLSocketContext() {
        // checking if a special edition for JAR style lib used in Eclipse project
        String flavor = android.support.v4.BuildConfig.FLAVOR.toLowerCase();
        boolean productionJAR = (flavor.indexOf("jar") != -1);

        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance("BKS");
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return null;
        }
        // the bks file we generated above
        final InputStream inKeyStore;
        if (productionJAR) {
            int myStoreId = getRawID(mContext, "verification_lib_store");
            if (myStoreId == 0)
                return null;
            inKeyStore = mContext.getResources().openRawResource(myStoreId);
        } else {
            inKeyStore = mContext.getResources().openRawResource(R.raw.communication_lib_store);
        }
        try {
            if (productionJAR) {
                int storePassId = getStringID(mContext, "verification_lib_store_pass");
                String storePass = mContext.getResources().getString(storePassId);
                keyStore.load(inKeyStore, storePass.toCharArray());
            } else {
                String storePass = mContext.getResources().getString(R.string.communication_lib_store_pass);
                keyStore.load(inKeyStore, storePass.toCharArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (CertificateException e) {
            e.printStackTrace();
            return null;
        }
        try {
            inKeyStore.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        try {
            tmf.init(keyStore);
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return null;
        }

        // Create an SSLContext that uses our TrustManager
        SSLContext context;
        try {
            context = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        try {
            context.init(null, tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return null;
        }

        return context;
    }
}
