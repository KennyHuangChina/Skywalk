package com.kjs.skywalk.communicationlibrary;

import java.util.HashMap;

import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CIProgressListener;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CICommandListener;

import org.json.JSONObject;

/**
 * Created by Jackie on 2017/1/17.
 */

class InternalDefines {

    public interface DoOperation {
        int doOperation(HashMap<String, String> map, CICommandListener commandListener, CIProgressListener progressListener);
    }
    public interface CheckParameter {
        boolean checkParameter(HashMap<String, String> map);
    }
    public interface CreateResultMap {
        HashMap<String, String> doCreateResultMap(JSONObject jObject);
        IApiResults.ICommon doParseResult(JSONObject jObject);
    }
    public interface BeforeConnect {
        int doBeforeConnect(HttpConnector http);
    }
    public interface AfterConnect {
        int doAfterConnect(HttpConnector http);
    }
    public interface ConnectFailed {
        int doConnectFailed(HttpConnector http);
    }

    //Log Tags defines
    public static final String TAG_HTTPConnector                            = "HTTPConnector";
    public static final String TAG_COMMUNICATION_MANAGER                    = "CommunicationManager";
    public static final String TAG_COMMUNICATION_UTILS                      = "CommunicationUtils";
    public static final String TAG_COMMUNICATION_SESSION_MANAGER            = "SessionManager";
    public static final String TAG_COMMUNICATION_COOKIE_MANAGER             = "SKCookieManager";
    public static final String TAG_COMMUNICATION_SESSION_STORE              = "SKSessionStore";

    //Error Code Defines
    public static final int     ERROR_CODE_OK                               = 0x0;
    public static final String  ERROR_DESCRIPTION_OK                        = "No Error";

    //==Http Connection Error Code Defines==
    public static final int     ERROR_CODE_HTTP_INVALID_URL                 = 0x80001000;
    public static final int     ERROR_CODE_HTTP_CONNECTION                  = 0x80001001;
    public static final int     ERROR_CODE_HTTP_CONNECTION_SSL              = 0x80001002;
    public static final int     ERROR_CODE_HTTP_REQUEST_FAILED              = 0x80001003;
    public static final int     ERROR_CODE_HTTP_SOURCE_FILE_NOT_FOUND       = 0x80001004;
    public static final int     ERROR_CODE_HTTP_UNKNOWN_HOST                = 0x80001005;

    //Error Description
    private static HashMap<Integer, String> errorMap;

    static {
        errorMap = new HashMap<Integer, String>();
    }

    public static String getErrorDescription(int errCode) {
        if(!errorMap.containsKey(errCode)) {
            return "Undefined Error";
        }

        String description = errorMap.get(errCode);
        if(description == null) {
            description = "";
        }

        return description;
    }
}
