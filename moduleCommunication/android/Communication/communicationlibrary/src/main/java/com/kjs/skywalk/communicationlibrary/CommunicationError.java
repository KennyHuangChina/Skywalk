package com.kjs.skywalk.communicationlibrary;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/20.
 */

public class CommunicationError {
    public static final int CE_ERROR_NO_ERROR                       = 0x00000000;
    public static final int CE_COMMAND_ERROR_INVALID_INPUT          = 0x80000001;
    public static final int CE_COMMAND_ERROR_FATAL_ERROR            = 0x80000002;
    public static final int CE_COMMAND_ERROR_NOT_LOGIN              = 0x80000003;

    //==Http Connection Error Code Defines==
    public static final int CE_ERROR_HTTP_BASE                      = 0x80001000,
                            CE_ERROR_HTTP_INVALID_URL               = CE_ERROR_HTTP_BASE + 1,
                            CE_ERROR_HTTP_CONNECTION                = CE_ERROR_HTTP_BASE + 2,
                            CE_ERROR_HTTP_CONNECTION_SSL            = CE_ERROR_HTTP_BASE + 3,
                            CE_ERROR_HTTP_REQUEST_FAILED            = CE_ERROR_HTTP_BASE + 4,
                            CE_ERROR_HTTP_SOURCE_FILE_NOT_FOUND     = CE_ERROR_HTTP_BASE + 5,
                            CE_ERROR_HTTP_UNKNOWN_HOST              = CE_ERROR_HTTP_BASE + 6,
                            CE_ERROR_CONNECTION_REFUSED             = CE_ERROR_HTTP_BASE + 7,
                            CE_ERROR_SCOCKET_TIMEOUT                = CE_ERROR_HTTP_BASE + 8,
                            CE_ERROR_NETWORK_UNREACH                = CE_ERROR_HTTP_BASE + 9,
                            CE_ERROR_HOST_UNREACH                   = CE_ERROR_HTTP_BASE + 10,
                            CE_ERROR_PROTOCOL                       = CE_ERROR_HTTP_BASE + 11,
                            CE_ERROR_NETWORK_NOTAVAILABLE           = CE_ERROR_HTTP_BASE + 12;

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

    public static boolean IsNetworkError(int errCode) {
        if ((errCode & 0xfffff000) == CE_ERROR_HTTP_BASE) {  // 0x80001000
            return true;
        }
        return false;
    }
}
