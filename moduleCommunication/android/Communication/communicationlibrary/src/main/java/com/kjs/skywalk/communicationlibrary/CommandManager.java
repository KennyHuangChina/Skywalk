package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
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
    public int GetSmsCode(String userName) {
        mOperation = new CmdGetSmsCode(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, userName);
        return execute(pMap);
    }

    @Override
    public int GetUserInfo(int uid) {
        mOperation = new CmdGetUserInfo(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, "" + uid);
        return execute(pMap);
    }

    @Override
    public int GetUserSalt(String userName) {
        mOperation = new CmdGetUserSalt(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, userName);
        return execute(pMap);
    }

    @Override
    public int LoginByPassword(String user, String pass, String rand, String salt) {
        mOperation = new CmdLoginByPassword(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, user);
        pMap.put(CommunicationParameterKey.CPK_PASSWORD, pass);
        pMap.put(CommunicationParameterKey.CPK_RANDOM, rand);
        pMap.put(CommunicationParameterKey.CPK_USER_SALT, salt);
        return execute(pMap);
    }

    @Override
    public int LoginBySms(String user, String smsCode) {
        mOperation = new CmdLoginBySms(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, user);
        pMap.put(CommunicationParameterKey.CPK_SMS_CODE, smsCode);
        return execute(pMap);
    }

    @Override
    public int Logout() {
        mOperation = new CmdLogout(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(pMap);
    }

    @Override
    public int Relogin(String userName) {
        mOperation = new CmdRelogin(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, userName);
        return execute(pMap);
    }

    @Override
    public int CommandTest() {
        mOperation = new CommandTest(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(pMap);
    }

    @Override
    public int GetBriefPublicHouseInfo(int houseId) {
        mOperation = new CmdGetBriefPublicHouseInfo(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, "" + houseId);
        return execute(pMap);
    }

    @Override
    public int GetHouseInfo(int houseId, boolean bPrivtInfo) {
        mOperation = new CmdGetHouseInfo(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(houseId));
        pMap.put(CommunicationParameterKey.CPK_PRIVATE_INFO, String.valueOf(bPrivtInfo));
        return execute(pMap);
    }

    @Override
    public int CommitHouseByOwner(CommunicationInterface.HouseInfo houseInfo, int agency) {
        mOperation = new CmdCommitHouseByOwner(mContext, houseInfo, agency);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(pMap);
    }

    @Override
    public int AmendHouse(CommunicationInterface.HouseInfo houseInfo) {
        mOperation = new CmdAmendHouse(mContext, houseInfo);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(pMap);
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
    public int SetHouseCoverImg(int house_id, int img_id) {
        mOperation = new CmdSetHouseCoverImg(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id));
        pMap.put(CommunicationParameterKey.CPK_HOUSE_COVER_IMG, String.valueOf(img_id));
        return execute(pMap);
    }

    @Override
    public int CertificateHouse(int house_id, boolean bPass, String sCertComment) {
        mOperation = new CmdCertificateHouse(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id));
        pMap.put(CommunicationParameterKey.CPK_HOUSE_CERT_COMMENT, sCertComment);
        pMap.put(CommunicationParameterKey.CPK_HOUSE_CERT_PASS, String.valueOf(bPass));
        return execute(pMap);
    }

    @Override
    public int GetBehalfHouses(int type, int begin, int cnt) {
        mOperation = new CmdGetBehalfHouses(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_HOUSE_TYPE, "" + type);
        pMap.put(CommunicationParameterKey.CPK_LIST_BEGIN, "" + begin);
        pMap.put(CommunicationParameterKey.CPK_LIST_CNT, "" + cnt);
        return execute(pMap);
    }

    @Override
    public int GetHouseList(int type, int begin, int cnt) {
        mOperation = new CmdGetHouseList(mContext, "GetHouseList");
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_HOUSE_TYPE, "" + type);
        pMap.put(CommunicationParameterKey.CPK_LIST_BEGIN, "" + begin);
        pMap.put(CommunicationParameterKey.CPK_LIST_CNT, "" + cnt);
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
    public int ModifyDeliverable(int dev_id, String sName) {
        mOperation = new CmdModifyDeliverable(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(dev_id));
        pMap.put(CommunicationParameterKey.CPK_NAME, sName);
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
        mOperation = new CmdGetHouseDeliverables(mContext, house_id);
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
    public int EditFacility(int id, int nType, String sName) {
        mOperation = new CmdModifyFacility(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(id));
        pMap.put(CommunicationParameterKey.CPK_TYPE, String.valueOf(nType));
        pMap.put(CommunicationParameterKey.CPK_NAME, sName);
        return execute(pMap);
    }

    @Override
    public int EditFacilityType(int typeId, String sTypeName) {
        mOperation = new CmdModifyFacilityType(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(typeId));
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

    @Override
    public int AddHouseFacility(int house, ArrayList<CommunicationInterface.FacilityItem> list) {
        mOperation = new CmdAddHouseFacility(mContext, house, list);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(pMap);
    }

    @Override
    public int EditHouseFacility(int hfid, int fid, int qty, String desc) {
        mOperation = new CmdModifyHouseFacility(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(hfid));
        pMap.put(CommunicationParameterKey.CPK_FACILITY_ID, String.valueOf(fid));
        pMap.put(CommunicationParameterKey.CPK_QTY, String.valueOf(qty));
        pMap.put(CommunicationParameterKey.CPK_DESC, desc);
        return execute(pMap);
    }

    @Override
    public int GetHouseFacilityList(int house) {
        mOperation = new CmdGetHouseFacilityList(mContext, house);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(pMap);
    }
}
