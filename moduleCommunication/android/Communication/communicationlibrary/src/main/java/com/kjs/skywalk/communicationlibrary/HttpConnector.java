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
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Jackie on 2017/1/17.
 */

class HttpConnector {
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
    private String mResponseString = "";

    private static final String BOUNDARY_STRING = "skywalk_http_boundary";
    private static final String BOUNDARY_START = "--" + BOUNDARY_STRING + "\r\n";
    private static final String BOUNDARY_END = "--" + BOUNDARY_STRING + "--\r\n";

    public HttpConnector(Context context) {
        mContext = context;
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
        if(!mRequestData.isEmpty()) {
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
                Log.i(InternalDefines.TAG_HTTPConnector, "Connect with http mode");
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

    public int sendRequest(String strRequestData) {
        if(mConnection == null) {
            return InternalDefines.ERROR_CODE_HTTP_CONNECTION;
        }

        mJsonObj = null;

        mResponseString = "";

        try {
            if(mMethod.equals("POST")) {
                File srcFile = null;
                boolean uploadFile = false;
                if(!mUploadFile.isEmpty()) {
                    srcFile = new File(mUploadFile);
                    if(srcFile.exists()) {
                        uploadFile = true;
                    }
                }

                mConnection.setRequestMethod(mMethod);
                mConnection.setDoOutput(true);
                mConnection.setUseCaches(false);
                mConnection.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARY_STRING);
//                mConnection.setFixedLengthStreamingMode(strRequestData.length());
                DataOutputStream wr = new DataOutputStream (mConnection.getOutputStream());

                String list[] = strRequestData.split("&");
                for(int i= 0; i < list.length; i ++) {
                    String tmp[] = list[i].split("=");
                    if (tmp.length == 2) {
                        String name = tmp[0];
                        String value = tmp[1];
                        wr.writeBytes(BOUNDARY_START);
                        String strLine = String.format("Content-Disposition: form-data; name=\"%s\"\r\n\r\n%s\r\n", name, value);
                        Log.i(InternalDefines.TAG_HTTPConnector, strLine);
                        wr.writeBytes(strLine);
                    }
                }

                if(uploadFile) {
                    DataInputStream fileIS;
                    try {
                        wr.writeBytes(BOUNDARY_START);
                        //String strTmp = String.format("Content-Disposition:form-data;name=\"data\";filename=\"%s\"\r\nContent-Type:application/x-zip-compressed\r\n\r\n", srcFile.getName());
                        String strTmp = String.format("Content-Disposition:form-data;name=\"data\";filename=\"%s\"\r\n", srcFile.getName());
                        wr.writeBytes(strTmp);
                        strTmp = String.format("Content-Type:application/x-zip-compressed\r\n\r\n");
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
            } else if(mMethod.equals("GET")) {
                mConnection.setRequestMethod(mMethod);
                mConnection.setDoOutput(false);
            }

            InputStream in = new BufferedInputStream(mConnection.getInputStream());
            byte[] response = new byte[256];
            while (true) {
                int ret;
                try {
                    ret = in.read(response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    break;
                }
                if (ret > 0) {
                    String temp = new String(response, 0, ret);
                    mResponseString += temp;
                } else {
                    break;
                }
            }

            try {
                JSONObject json = new JSONObject(mResponseString);
                mErrorCode = json.getInt("ErrCode");
                mErrorDescription = json.getString("ErrString");
                if(json.has("Token")) {
                    mToken = json.getString("Token");
                } else {
                    mToken = "";
                }

                mJsonObj = json;
            } catch (JSONException e) {
                e.printStackTrace();
                mErrorCode = -1;
                mErrorDescription = "Unknown Error";
                return InternalDefines.ERROR_CODE_HTTP_REQUEST_FAILED;
            }
        } catch (UnknownHostException eHost) {
            eHost.printStackTrace();
            return InternalDefines.ERROR_CODE_HTTP_UNKNOWN_HOST;
        } catch (IOException e) {
            e.printStackTrace();
            InputStream in = new BufferedInputStream(mConnection.getErrorStream());
            byte[] response = new byte[256];
            int ret = 0;
            try {
                ret = in.read(response);
            } catch (IOException e1) {
                e1.printStackTrace();
                mErrorCode = -1;
                mErrorDescription = "Unknown Error";
            }
            if (ret > 0) {  // server rejected this device to register
                try {
                    JSONObject json = new JSONObject(new String(response, 0, ret));
                    mErrorCode = json.getInt("ErrCode");
                    mErrorDescription = json.getString("ErrString");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    try {
                        mErrorCode = mConnection.getResponseCode();
                        mErrorDescription = mConnection.getResponseMessage();
                    } catch (IOException e2) {
                        mErrorCode = -1;
                        mErrorDescription = "Unknown Error";
                    }
                }
            }
            return InternalDefines.ERROR_CODE_HTTP_REQUEST_FAILED;
        }

        return InternalDefines.ERROR_CODE_OK;
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
