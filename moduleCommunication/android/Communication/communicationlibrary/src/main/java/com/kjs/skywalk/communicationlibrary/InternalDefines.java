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
        int doOperation(CICommandListener commandListener, CIProgressListener progressListener);
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
    }

    interface IListItemInfoInner {
        String ListItemInfo2String();
    }

    interface IApiName {
        String GetApiName();
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
}
