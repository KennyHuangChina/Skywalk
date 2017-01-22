package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import java.util.HashMap;

import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CIProgressListener;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CICommandListener;
/**
 * Created by Jackie on 2017/1/20.
 */

public class CommunicationManager {
    private Context mContext = null;
    private CICommandListener mCommandListener = null;
    private CIProgressListener mProgressListener = null;
    private CommunicationBase mOperation = null;

    private MyUtils mUtils = null;

    public CommunicationManager(Context context) {
        mContext = context;
        mUtils= new MyUtils(context);
    }

    public int get(String getWhat, HashMap<String, String> map, CICommandListener commandListener, CIProgressListener progressListener) {
        if(getWhat == null || commandListener == null) {
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }
        if(getWhat.isEmpty() || map.isEmpty()) {
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }

        mProgressListener = progressListener;

        MyUtils.printInputParameters(map);

        mOperation = createOperation(getWhat);
        if(mOperation == null) {
            return CommunicationError.CE_COMMAND_ERROR_FATAL_ERROR;
        }

        int ret = mOperation.doOperation(map, commandListener, progressListener);
        if(ret != CommunicationError.CE_ERROR_NO_ERROR) {
            Log.i(InternalDefines.TAG_COMMUNICATION_MANAGER, "failed to execute command: " + getWhat);
        }

        return ret;
    }

    private CommunicationBase createOperation(String which) {
        CommunicationBase operation = null;
        if(which.equals(CommunicationCommand.CC_GET_BRIEF_PUBLIC_HOUSE_INFO)) {
            operation = new GetBriefPublicHouseInfo(mContext);
        }

        return operation;
    }

}
