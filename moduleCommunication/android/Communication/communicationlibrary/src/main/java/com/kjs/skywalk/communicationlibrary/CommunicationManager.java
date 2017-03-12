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
    final String TAG = "CommunicationManager";

    private Context mContext = null;
    private CommunicationBase mOperation = null;
    private SKCookieManager mCookieManager = null;

    private MyUtils mUtils = null;

    public CommunicationManager(Context context) {
        mContext = context;
        mUtils= new MyUtils(context);
        mCookieManager = SKCookieManager.getManager(context);
    }

    public int execute(String command, HashMap<String, String> map, CICommandListener commandListener, CIProgressListener progressListener) {
        if(command == null || commandListener == null || map== null) {
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }
        if(command.isEmpty()) {
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }

        MyUtils.printContentInMap(map);

        mOperation = createOperation(command);
        if(mOperation == null) {
            return CommunicationError.CE_COMMAND_ERROR_FATAL_ERROR;
        }

        if(!mOperation.checkParameter(map)) {
            Log.w(TAG, "Fail to check parameters");
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
        if (command.equals(CommunicationCommand.CC_GET_BRIEF_PUBLIC_HOUSE_INFO)) {
            operation = new CmdGetBriefPublicHouseInfo(mContext, command);
        } else if (command.equals(CommunicationCommand.CC_LOG_IN_BY_PASSWORD)) {
            operation = new CmdLoginByPassword(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_LOG_IN_BY_SMS)) {
            operation = new CmdLoginBySms(mContext, command);
        } else if (command.equals(CommunicationCommand.CC_LOG_OUT)) {
            operation = new CmdLogout(mContext, command);
        } else if (command.equals(CommunicationCommand.CC_TEST)) {
            operation = new CommandTest(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_USER_SALT)) {
            operation = new CmdGetUserSalt(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_RELOGIN)) {
            operation = new CmdRelogin(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_SMS_CODE)) {
            operation = new CmdGetSmsCode(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_USER_INFO)) {
            operation = new CmdGetUserInfo(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_HOUSE_LIST)) {
            operation = new CmdGetHouseList(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_BEHALF_HOUSE_LIST)) {
            operation = new CmdGetBehalfHouses(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_GET_HOUSE_INFO)) {
            operation = new CmdGetHouseInfo(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_COMMIT_HOUSE_BY_OWNER)) {
            operation = new CmdCommitHouseByOwner(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_AMEND_HOUSE)) {
            operation = new CmdAmendHouse(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_CERT_HOUSE)) {
            operation = new CmdCertificateHouse(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_SET_HOUSE_COVER_IMAGE)) {
            operation = new CmdSetHouseCoverImg(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_RECOMMEND_HOUSE)) {
            operation = new CmdRecommendHouse(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_ADD_PROPERTY)) {
            operation = new CmdAddProperty(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_MODIFY_PROPERTY)) {
            operation = new CmdModifyPropertyInfo(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_PROPERTY_LIST)) {
            operation = new CmdGetPropertyList(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_GET_PROPERTY_INFO)) {
            operation = new CmdGetPropertyInfo(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_ADD_DELIVERABLE)) {
            operation = new CmdAddDeliverable(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_GET_DELIVERABLE_LIST)) {
            operation = new CmdGetDeliverableList(mContext, command);
        } else if (command.equals/*IgnoreCase*/(CommunicationCommand.CC_ADD_HOUSE_DELIVERABLE)) {
            operation = new CmdAddHouseDeliverable(mContext, command);
        } else {
            Log.e(TAG, "Unknown command:" + command);
        }

        return operation;
    }

}
