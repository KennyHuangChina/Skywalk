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
    final String TAG = getClass().getSimpleName();

    private Context             mContext        = null;
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

    private int execute(CommunicationBase operation, HashMap<String, String> map) {
        if (null == operation) {
            return CommunicationError.CE_COMMAND_ERROR_FATAL_ERROR;
        }
        if (null == mCmdListener || null == mProgListener) {
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }
        MyUtils.printContentInMap(map);

        if (!operation.checkParameter(map)) {
            Log.w(TAG, "Fail to check parameters");
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }

        int ret = operation.doOperation(map, mCmdListener, mProgListener);
        if (ret != CommunicationError.CE_ERROR_NO_ERROR) {
            Log.i(InternalDefines.TAG_COMMUNICATION_MANAGER, "failed to execute command: " + operation.GetApiName());
        }

        return ret;
    }

    @Override
    public int GetSmsCode(String userName) {
        CommunicationBase op = new CmdGetSmsCode(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, userName);
        return execute(op, pMap);
    }

    @Override
    public int GetUserInfo(int uid) {
        CommunicationBase op = new CmdGetUserInfo(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, "" + uid);
        return execute(op, pMap);
    }

    @Override
    public int GetUserSalt(String userName) {
        CommunicationBase op = new CmdGetUserSalt(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, userName);
        return execute(op, pMap);
    }

    @Override
    public int LoginByPassword(String user, String pass, String rand, String salt) {
        CommunicationBase op = new CmdLoginByPassword(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, user);
        pMap.put(CommunicationParameterKey.CPK_PASSWORD, pass);
        pMap.put(CommunicationParameterKey.CPK_RANDOM, rand);
        pMap.put(CommunicationParameterKey.CPK_USER_SALT, salt);
        return execute(op, pMap);
    }

    @Override
    public int LoginBySms(String user, String smsCode) {
        CommunicationBase op = new CmdLoginBySms(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, user);
        pMap.put(CommunicationParameterKey.CPK_SMS_CODE, smsCode);
        return execute(op, pMap);
    }

    @Override
    public int Logout() {
        CommunicationBase op = new CmdLogout(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int Relogin(String userName) {
        CommunicationBase op = new CmdRelogin(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, userName);
        return execute(op, pMap);
    }

    @Override
    public int CommandTest() {
        CommunicationBase op = new CommandTest(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetBriefPublicHouseInfo(int houseId) {
        CommunicationBase op = new CmdGetBriefPublicHouseInfo(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, "" + houseId);
        return execute(op, pMap);
    }

    @Override
    public int GetHouseInfo(int houseId, boolean bPrivtInfo) {
        CommunicationBase op = new CmdGetHouseInfo(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(houseId));
        pMap.put(CommunicationParameterKey.CPK_PRIVATE_INFO, String.valueOf(bPrivtInfo));
        return execute(op, pMap);
    }

    @Override
    public int CommitHouseByOwner(CommunicationInterface.HouseInfo houseInfo, int agency) {
        CommunicationBase op = new CmdCommitHouseByOwner(mContext, houseInfo, agency);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int AmendHouse(CommunicationInterface.HouseInfo houseInfo) {
        CommunicationBase op = new CmdAmendHouse(mContext, houseInfo);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int RecommendHouse(int house_id, int action) {
        CommunicationBase op = new CmdRecommendHouse(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id));
        pMap.put(CommunicationParameterKey.CPK_HOUSE_RECOMMENT_ACT, String.valueOf(action));
        return execute(op, pMap);
    }

    @Override
    public int SetHouseCoverImg(int house_id, int img_id) {
        CommunicationBase op = new CmdSetHouseCoverImg(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id));
        pMap.put(CommunicationParameterKey.CPK_HOUSE_COVER_IMG, String.valueOf(img_id));
        return execute(op, pMap);
    }

    @Override
    public int CertificateHouse(int house_id, boolean bPass, String sCertComment) {
        CommunicationBase op = new CmdCertificateHouse(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id));
        pMap.put(CommunicationParameterKey.CPK_HOUSE_CERT_COMMENT, sCertComment);
        pMap.put(CommunicationParameterKey.CPK_HOUSE_CERT_PASS, String.valueOf(bPass));
        return execute(op, pMap);
    }

    @Override
    public int GetBehalfHouses(int type, int begin, int cnt) {
        CommunicationBase op = new CmdGetBehalfHouses(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_HOUSE_TYPE, "" + type);
        pMap.put(CommunicationParameterKey.CPK_LIST_BEGIN, "" + begin);
        pMap.put(CommunicationParameterKey.CPK_LIST_CNT, "" + cnt);
        return execute(op, pMap);
    }

    @Override
    public int GetHouseList(int type, int begin, int cnt) {
        CommunicationBase op = new CmdGetHouseList(mContext, "GetHouseList");
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_HOUSE_TYPE, "" + type);
        pMap.put(CommunicationParameterKey.CPK_LIST_BEGIN, "" + begin);
        pMap.put(CommunicationParameterKey.CPK_LIST_CNT, "" + cnt);
        return execute(op, pMap);
    }

    @Override
    public int GetPropertyListByName(String sName, int nBegin, int nCount) {
        CommunicationBase op = new CmdGetPropertyList(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_NAME, sName);
        pMap.put(CommunicationParameterKey.CPK_LIST_BEGIN, String.valueOf(nBegin));
        pMap.put(CommunicationParameterKey.CPK_LIST_CNT, String.valueOf(nCount));
        return execute(op, pMap);
    }

    @Override
    public int AddProperty(String sName, String sAddr, String sDesc) {
        CommunicationBase op = new CmdAddProperty(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_NAME, sName);
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_ADDR, sAddr);
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_DESC, sDesc);
        return execute(op, pMap);
    }

    @Override
    public int GetPropertyInfo(int nPropId) {
        CommunicationBase op = new CmdGetPropertyInfo(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(nPropId));
        return execute(op, pMap);
    }

    @Override
    public int ModifyPropertyInfo(int nPropId, String sName, String sAddr, String sDesc) {
        CommunicationBase op = new CmdModifyPropertyInfo(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(nPropId));
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_NAME, sName);
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_ADDR, sAddr);
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_DESC, sDesc);
        return execute(op, pMap);
    }

    @Override
    public int AddDeliverable(String sName) {
        CommunicationBase op = new CmdAddDeliverable(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_NAME, sName);
        return execute(op, pMap);
    }

    @Override
    public int GetDeliverableList() {
        CommunicationBase op = new CmdGetDeliverableList(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int ModifyDeliverable(int dev_id, String sName) {
        CommunicationBase op = new CmdModifyDeliverable(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(dev_id));
        pMap.put(CommunicationParameterKey.CPK_NAME, sName);
        return execute(op, pMap);
    }

    @Override
    public int AddHouseDeliverable(int house_id, int deliverable_id, int qty, String sDesc) {
        CommunicationBase op = new CmdAddHouseDeliverable(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id));
        pMap.put(CommunicationParameterKey.CPK_DELIVERABLE_ID, String.valueOf(deliverable_id));
        pMap.put(CommunicationParameterKey.CPK_QTY, String.valueOf(qty));
        pMap.put(CommunicationParameterKey.CPK_DESC, sDesc);
        return execute(op, pMap);
    }

    @Override
    public int GetHouseDeliverables(int house_id) {
        CommunicationBase op = new CmdGetHouseDeliverables(mContext, house_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id)); // "2");
        return execute(op, pMap);
    }

    @Override
    public int AddFacilityType(String sTypeName) {
        CommunicationBase op = new CmdAddFacilityType(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_NAME, sTypeName);
        return execute(op, pMap);
    }

    @Override
    public int EditFacility(int id, int nType, String sName) {
        CommunicationBase op = new CmdModifyFacility(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(id));
        pMap.put(CommunicationParameterKey.CPK_TYPE, String.valueOf(nType));
        pMap.put(CommunicationParameterKey.CPK_NAME, sName);
        return execute(op, pMap);
    }

    @Override
    public int EditFacilityType(int typeId, String sTypeName) {
        CommunicationBase op = new CmdModifyFacilityType(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(typeId));
        pMap.put(CommunicationParameterKey.CPK_NAME, sTypeName);
        return execute(op, pMap);
    }

   @Override
    public int GetFacilityTypeList() {
       CommunicationBase op = new CmdGetFacilityTypeList(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int AddFacility(int nType, String sName) {
        CommunicationBase op = new CmdAddFacility(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_TYPE, String.valueOf(nType));
        pMap.put(CommunicationParameterKey.CPK_NAME, sName);
        return execute(op, pMap);
    }

    @Override
    public int GetFacilityList(int nType) {
        CommunicationBase op = new CmdGetFacilityList(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_TYPE, String.valueOf(nType));
        return execute(op, pMap);
    }

    @Override
    public int AddHouseFacility(int house, ArrayList<CommunicationInterface.FacilityItem> list) {
        CommunicationBase op = new CmdAddHouseFacility(mContext, house, list);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int EditHouseFacility(int hfid, int fid, int qty, String desc) {
        CommunicationBase op = new CmdModifyHouseFacility(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(hfid));
        pMap.put(CommunicationParameterKey.CPK_FACILITY_ID, String.valueOf(fid));
        pMap.put(CommunicationParameterKey.CPK_QTY, String.valueOf(qty));
        pMap.put(CommunicationParameterKey.CPK_DESC, desc);
        return execute(op, pMap);
    }

    @Override
    public int GetHouseFacilityList(int house) {
        CommunicationBase op = new CmdGetHouseFacilityList(mContext, house);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int AddPicture(int house, int type, String desc, String file) {
        CommunicationBase op = new CmdAddPicture(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house));
        pMap.put(CommunicationParameterKey.CPK_TYPE, String.valueOf(type));
        pMap.put(CommunicationParameterKey.CPK_DESC, desc);
        pMap.put(CommunicationParameterKey.CPK_IMG_FILE, String.valueOf(file));
        return execute(op, pMap);
    }

    @Override
    public int DelePicture(int pic) {
        CommunicationBase op = new CmdDelePicture(mContext, pic);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetPicUrls(int pic, int size) {
        CommunicationBase op = new CmdGetPictureUrls(mContext, pic, size);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetHousePics(int house, int type) {
        CommunicationBase op = new CmdGetHousePicList(mContext, house, type);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetNewEventCount() {
        CommunicationBase op = new CmdGetNewEventCount(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetHouseNewEvent() {
        CommunicationBase op = new CmdGetHouseNewEvent(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }
    @Override
    public int ReadNewEvent(int event_id) {
        CommunicationBase op = new CmdNewEventRead(mContext, event_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetHouseEventInfo(int event_id) {
        CommunicationBase op = new CmdGetHouseEventInfo(mContext, event_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetHouseEventProcList(int event_id) {
        CommunicationBase op = new CmdGetHouseEventProcList(mContext, event_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetHouseEventList(int house_id, int stat, int type, int posi_bgn, int fetch_cnt, boolean bIDO) {
        CommunicationBase op = new CmdGetHouseEventList(mContext, house_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_EVENT_STAT, String.valueOf(stat));
        pMap.put(CommunicationParameterKey.CPK_EVENT_TYPE, String.valueOf(type));
        pMap.put(CommunicationParameterKey.CPK_LIST_BEGIN, String.valueOf(posi_bgn));
        pMap.put(CommunicationParameterKey.CPK_LIST_CNT, String.valueOf(fetch_cnt));
        pMap.put(CommunicationParameterKey.CPK_EVENT_IDO, String.valueOf(bIDO));
        return execute(op, pMap);
    }

    @Override
    public int ModifyHouseEvent(int event_id, String desc) {
        CommunicationBase op = new CmdModifyHouseEvent(mContext, event_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_DESC, String.valueOf(desc));
        return execute(op, pMap);
    }
}
