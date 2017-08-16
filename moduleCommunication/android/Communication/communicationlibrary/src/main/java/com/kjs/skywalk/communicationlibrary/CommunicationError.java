package com.kjs.skywalk.communicationlibrary;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/20.
 */

public class CommunicationError {
    public static final int   CE_ERROR_NO_ERROR                         = 0x00000000;
    public static final int   CE_COMMAND_ERROR_INVALID_INPUT            = 0x80000001;
    public static final int   CE_COMMAND_ERROR_FATAL_ERROR              = 0x80000002;

    //==Http Connection Error Code Defines==
    public static final int   CE_ERROR_HTTP_INVALID_URL                 = 0x80001000;
    public static final int   CE_ERROR_HTTP_CONNECTION                  = 0x80001001;
    public static final int   CE_ERROR_HTTP_CONNECTION_SSL              = 0x80001002;
    public static final int   CE_ERROR_HTTP_REQUEST_FAILED              = 0x80001003;
    public static final int   CE_ERROR_HTTP_SOURCE_FILE_NOT_FOUND       = 0x80001004;
    public static final int   CE_ERROR_HTTP_UNKNOWN_HOST                = 0x80001005;
    public static final int   CE_ERROR_CONNECTION_REFUSED               = 0x80001006;
    public static final int   CE_ERROR_SCOCKET_TIMEOUT                  = 0x80001007;
    public static final int   CE_ERROR_NETWORK_UNREACH                  = 0x80001008;
    public static final int   CE_ERROR_HOST_UNREACH                     = 0x80001009;
    public static final int   CE_ERROR_PROTOCOL                         = 0x8000100A;
    public static final int   CE_ERROR_NETWORK_NOTAVAILABLE             = 0x8000100B;

    //Error Description
    private static HashMap<Integer, String> errorMap;

    static {
        errorMap = new HashMap<Integer, String>();
        errorMap.put(CE_ERROR_HTTP_INVALID_URL,           "CE_ERROR_HTTP_INVALID_URL: invalid http url");
        errorMap.put(CE_ERROR_HTTP_CONNECTION,            "CE_ERROR_HTTP_CONNECTION: error http connection");
        errorMap.put(CE_ERROR_HTTP_CONNECTION_SSL,        "CE_ERROR_HTTP_CONNECTION_SSL: error https connection");
        errorMap.put(CE_ERROR_HTTP_REQUEST_FAILED,        "CE_ERROR_HTTP_REQUEST_FAILED: sending http request failed");
        errorMap.put(CE_ERROR_HTTP_SOURCE_FILE_NOT_FOUND, "CE_ERROR_HTTP_SOURCE_FILE_NOT_FOUND: http resource not found");
        errorMap.put(CE_ERROR_HTTP_UNKNOWN_HOST,          "CE_ERROR_HTTP_UNKNOWN_HOST: unknown host");
        errorMap.put(CE_ERROR_CONNECTION_REFUSED,         "CE_ERROR_CONNECTION_REFUSED: connection get refused");
        errorMap.put(CE_ERROR_SCOCKET_TIMEOUT,            "CE_ERROR_SCOCKET_TIMEOUT: socket timeout");
        errorMap.put(CE_ERROR_NETWORK_UNREACH,            "CE_ERROR_NETWORK_UNREACH: network is unreachable");
        errorMap.put(CE_ERROR_HOST_UNREACH,               "CE_ERROR_HOST_UNREACH: host is unreachalbe");
        errorMap.put(CE_ERROR_PROTOCOL,                   "CE_ERROR_PROTOCOL: inalid protocol");
        errorMap.put(CE_ERROR_NETWORK_NOTAVAILABLE,       "CE_ERROR_NETWORK_NOTAVAILABLE: network is now not availiable");
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
