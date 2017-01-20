package com.kjs.skywalk.communicationlibrary;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/20.
 */

public class CommunicationManager {
    private Context mContext = null;
    private CommunicationInterface.CICommandListener mCommandListener = null;
    private CommunicationInterface.CIProgressListener mProgressListener = null;
    private CommunicationBase mOperation = null;

    CommunicationManager(Context context) {
        mContext = context;
    }

    public int get(String getWhat, HashMap<String, String> map, CommunicationInterface.CICommandListener commandListener) {
        if(getWhat == null || commandListener == null) {
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }
        if(getWhat.isEmpty() || map.isEmpty()) {
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }

        mOperation = createOperation(getWhat);
        if(mOperation == null) {
            return CommunicationError.CE_COMMAND_ERROR_FATAL_ERROR;
        }

        return 0;
    }

    public void setProgressListener(CommunicationInterface.CIProgressListener progressListener) {
        mProgressListener = progressListener;
    }

    private CommunicationBase createOperation(String which) {
        CommunicationBase operation = null;
        if(which.equals(CommunicationCommand.CC_GET_BRIEF_PUBLIC_HOUSE_INFO)) {
            operation = new GetBriefPublicHouseInfo(mContext);
        }

        return operation;
    }

}
