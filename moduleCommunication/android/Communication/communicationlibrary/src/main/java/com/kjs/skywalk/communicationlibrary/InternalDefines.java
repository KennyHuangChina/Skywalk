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
        String getRequestURL();
        void generateRequestData();
    }
    public interface CheckParameter {
        boolean checkParameter(HashMap<String, String> map);
    }
    public interface CreateResult {
        IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject);
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

    interface IListInner {
        int parseListItems(JSONObject obj);
        String ListItem2String(Object item);
    }

    interface IListItemInfoInner {
        String ListItemInfo2String();
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
    public static final int     ERROR_CODE_CONNECTION_REFUSED               = 0x80001006;
    public static final int     ERROR_CODE_SCOCKET_TIMEOUT                  = 0x80001007;
    public static final int     ERROR_CODE_NETWORK_UNREACH                  = 0x80001008;
    public static final int     ERROR_CODE_HOST_UNREACH                     = 0x80001009;
    public static final int     ERROR_CODE_PROTOCOL                         = 0x8000100A;
    public static final int     ERROR_CODE_NETWORK_NOTAVAILABLE             = 0x8000100B;

    //Error Description
    private static HashMap<Integer, String> errorMap;

    static {
        errorMap = new HashMap<Integer, String>();
        errorMap.put(ERROR_CODE_HTTP_INVALID_URL, "ERROR_CODE_HTTP_INVALID_URL: invalid http url");
        errorMap.put(ERROR_CODE_HTTP_CONNECTION, "ERROR_CODE_HTTP_CONNECTION: error http connection");
        errorMap.put(ERROR_CODE_HTTP_CONNECTION_SSL, "ERROR_CODE_HTTP_CONNECTION_SSL: error https connection");
        errorMap.put(ERROR_CODE_HTTP_REQUEST_FAILED, "ERROR_CODE_HTTP_REQUEST_FAILED: sending http request failed");
        errorMap.put(ERROR_CODE_HTTP_SOURCE_FILE_NOT_FOUND, "ERROR_CODE_HTTP_SOURCE_FILE_NOT_FOUND: http resource not found");
        errorMap.put(ERROR_CODE_HTTP_UNKNOWN_HOST, "ERROR_CODE_HTTP_UNKNOWN_HOST: unknown host");
        errorMap.put(ERROR_CODE_CONNECTION_REFUSED, "ERROR_CODE_CONNECTION_REFUSED: connection get refused");
        errorMap.put(ERROR_CODE_SCOCKET_TIMEOUT, "ERROR_CODE_SCOCKET_TIMEOUT: socket timeout");
        errorMap.put(ERROR_CODE_NETWORK_UNREACH, "ERROR_CODE_NETWORK_UNREACH: network is unreachable");
        errorMap.put(ERROR_CODE_HOST_UNREACH, "ERROR_CODE_HOST_UNREACH: host is unreachalbe");
        errorMap.put(ERROR_CODE_PROTOCOL, "ERROR_CODE_PROTOCOL: inalid protocol");
        errorMap.put(ERROR_CODE_NETWORK_NOTAVAILABLE, "ERROR_CODE_NETWORK_NOTAVAILABLE: network is now not availiable");
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
