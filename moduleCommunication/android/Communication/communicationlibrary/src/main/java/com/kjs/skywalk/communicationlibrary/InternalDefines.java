package com.kjs.skywalk.communicationlibrary;

/**
 * Created by Jackie on 2017/1/17.
 */

class InternalDefines {

    //Log Tags defines
    public static final String TAG_HTTPConnector = "HTTPConnector";

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
}
