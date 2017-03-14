package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by kenny on 2017/3/14.
 */

public class CommandManager implements CommunicationInterface.ICommand {
    final String TAG = "CommandManager";

    private Context             mContext        = null;
    private CommunicationBase   mOperation      = null;
    private SKCookieManager     mCookieManager  = null;
    private MyUtils             mUtils          = null;

    private CommunicationInterface.CICommandListener    mCmdListener  = null;
    private CommunicationInterface.CIProgressListener   mProgListener = null;

    public CommandManager(Context context,
                          CommunicationInterface.CICommandListener CmdListener,
                          CommunicationInterface.CIProgressListener ProgListener) {
        mContext = context;
        mCmdListener  = CmdListener;
        mProgListener = ProgListener;

        mUtils= new MyUtils(context);
        mCookieManager = SKCookieManager.getManager(context);
    }

    private int execute(HashMap<String, String> map) {
        if (null == mOperation) {
            return CommunicationError.CE_COMMAND_ERROR_FATAL_ERROR;
        }
        if (null == mCmdListener || null == mProgListener) {
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }
        MyUtils.printContentInMap(map);

        if (!mOperation.checkParameter(map)) {
            Log.w(TAG, "Fail to check parameters");
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }

        int ret = mOperation.doOperation(map, mCmdListener, mProgListener);
        if (ret != CommunicationError.CE_ERROR_NO_ERROR) {
            Log.i(InternalDefines.TAG_COMMUNICATION_MANAGER, "failed to execute command: " + mOperation.GetApiName());
        }

        return ret;
    }

    @Override
    public int RecommendHouse(int house_id, int action) {
        mOperation = new CmdRecommendHouse(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id));
        pMap.put(CommunicationParameterKey.CPK_HOUSE_RECOMMENT_ACT, String.valueOf(action));
        return execute(pMap);
    }

    @Override
    public int GetPropertyListByName(String sName, int nBegin, int nCount) {
        mOperation = new CmdGetPropertyList(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_NAME, sName);
        pMap.put(CommunicationParameterKey.CPK_LIST_BEGIN, String.valueOf(nBegin));
        pMap.put(CommunicationParameterKey.CPK_LIST_CNT, String.valueOf(nCount));
        return execute(pMap);
    }

    @Override
    public int AddProperty(String sName, String sAddr, String sDesc) {
        mOperation = new CmdAddProperty(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_NAME, sName);
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_ADDR, sAddr);
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_DESC, sDesc);
        return execute(pMap);
    }

    @Override
    public int GetPropertyInfo(int nPropId) {
        mOperation = new CmdGetPropertyInfo(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(nPropId));
        return execute(pMap);
    }

    @Override
    public int ModifyPropertyInfo(int nPropId, String sName, String sAddr, String sDesc) {
        mOperation = new CmdModifyPropertyInfo(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(nPropId));
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_NAME, sName);
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_ADDR, sAddr);
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_DESC, sDesc);
        return execute(pMap);
    }

    @Override
    public int AddDeliverable(String sName) {
        mOperation = new CmdAddDeliverable(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_NAME, sName);
        return execute(pMap);
    }

    @Override
    public int GetDeliverableList() {
        mOperation = new CmdGetDeliverableList(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(pMap);
    }

    @Override
    public int AddHouseDeliverable(int house_id, int deliverable_id, int qty, String sDesc) {
        mOperation = new CmdAddHouseDeliverable(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id));
        pMap.put(CommunicationParameterKey.CPK_DELIVERABLE_ID, String.valueOf(deliverable_id));
        pMap.put(CommunicationParameterKey.CPK_QTY, String.valueOf(qty));
        pMap.put(CommunicationParameterKey.CPK_DESC, sDesc);
        return execute(pMap);
    }

    @Override
    public int GetHouseDeliverables(int house_id) {
        mOperation = new CmdGetHouseDeliverables(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id)); // "2");
        return execute(pMap);
    }

    @Override
    public int AddFacilityType(String sTypeName) {
        mOperation = new CmdAddFacilityType(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_NAME, sTypeName);
        return execute(pMap);
    }

   @Override
    public int GetFacilityTypeList() {
        mOperation = new CmdGetFacilityTypeList(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(pMap);
    }

    @Override
    public int AddFacility(int nType, String sName) {
        mOperation = new CmdAddFacility(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_TYPE, String.valueOf(nType));
        pMap.put(CommunicationParameterKey.CPK_NAME, sName);
        return execute(pMap);
    }

    @Override
    public int GetFacilityList(int nType) {
        mOperation = new CmdGetFacilityList(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_TYPE, String.valueOf(nType));
        return execute(pMap);
    }
}
