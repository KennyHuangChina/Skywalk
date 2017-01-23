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
    private CommunicationBase mOperation = null;

    private MyUtils mUtils = null;

    public CommunicationManager(Context context) {
        mContext = context;
        mUtils= new MyUtils(context);
    }

    public int execute(String command, HashMap<String, String> map, CICommandListener commandListener, CIProgressListener progressListener) {
        if(command == null || commandListener == null) {
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }
        if(command.isEmpty() || map.isEmpty()) {
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }

        MyUtils.printInputParameters(map);

        mOperation = createOperation(command);
        if(mOperation == null) {
            return CommunicationError.CE_COMMAND_ERROR_FATAL_ERROR;
        }

        if(!mOperation.checkParameter(map)) {
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }

        int ret = mOperation.doOperation(map, commandListener, progressListener);
        if(ret != CommunicationError.CE_ERROR_NO_ERROR) {
            Log.i(InternalDefines.TAG_COMMUNICATION_MANAGER, "failed to execute command: " + command);
        }

        return ret;
    }

    private CommunicationBase createOperation(String command) {
        CommunicationBase operation = null;
        if(command.equals(CommunicationCommand.CC_GET_BRIEF_PUBLIC_HOUSE_INFO)) {
            operation = new GetBriefPublicHouseInfo(mContext);
        }

        return operation;
    }

}
