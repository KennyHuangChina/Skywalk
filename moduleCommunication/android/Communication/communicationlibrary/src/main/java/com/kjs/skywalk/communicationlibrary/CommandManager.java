package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.*;

/**
 * Created by kenny on 2017/3/14.
 */

public class CommandManager implements ICommand, CICommandListener, CIProgressListener {
    final String TAG = getClass().getSimpleName();

    private static CommandManager mCmdMgr   = null;

    private Context             mContext        = null;
//    private SKCookieManager     mCookieManager  = null;
    private MyUtils             mUtils          = null;

    private CICommandListener               mCmdListener    = null;
    private CIProgressListener              mProgListener   = null;
    private ArrayList<CommunicationBase>    mCmdQueue       = new ArrayList<CommunicationBase>();
    private CommunicationBase               mCmdLogin       = null;

    private static final int    AGENT_STATUS_Unknow     = 0,
                                AGENT_STATUS_NotLogin   = 1,
                                AGENT_STATUS_Logining   = 2,
                                AGENT_STATUS_Logined    = 3;
    private int     mAgentStatus    = AGENT_STATUS_Unknow;
    private String  mLoginUser      = "15306261804";

    private CommandManager(Context context, CICommandListener CmdListener, CIProgressListener ProgListener) {
        mContext = context;
        mCmdListener  = CmdListener;
        mProgListener = ProgListener;

        mUtils= new MyUtils(context);
//        mCookieManager = SKCookieManager.getManager(context);
    }
    public static synchronized CommandManager getCmdMgrInstance(Context context, CICommandListener CmdListener, CIProgressListener ProgListener) {
        if (null == mCmdMgr) {
            mCmdMgr = new CommandManager(context, CmdListener, ProgListener);
        }
        mCmdMgr.mCmdListener  = CmdListener;
        mCmdMgr.mProgListener = ProgListener;

        return mCmdMgr;
    }

    private synchronized boolean queueCommand(CommunicationBase cmd) {
        if (null == mCmdQueue) {
            mCmdQueue = new ArrayList<CommunicationBase>();
        }
        // TODO: check if the command already exist in queue
        return mCmdQueue.add(cmd);
    }

    private synchronized CommunicationBase removeCmd(int cmd) {
        String FN = "[removeCmd] ";

        if (cmd <= 0 && null == mCmdQueue) {
            return null;
        }

        CommunicationBase cmd2Remov = null;
        for (int i = 0; i < mCmdQueue.size(); i++) {
            CommunicationBase cmdThis = mCmdQueue.get(i);
            if (cmdThis.mAPI != cmd) {
                continue;
            }
            cmd2Remov = mCmdQueue.remove(i);     // (cmdThis);
            break;
        }

        return cmd2Remov;
    }


    private boolean isLoginCmd(CommunicationBase cmd) {
        return CmdID.CMD_RELOGIN == cmd.mAPI || CmdID.CMD_LOGIN_BY_PASSWORD == cmd.mAPI
                || CmdID.CMD_LOGIN_BY_SMS == cmd.mAPI /*|| CmdID.CMD_GET_USER_SALT == cmd.mAPI*/;
    }

    private int sendReloginCmd() {
        String FN = "[sendReloginCmd] ";
        Log.d(TAG, "mLoginUser:" + mLoginUser);
        if (null == mLoginUser || mLoginUser.isEmpty()) {
            return -1;
        }

        CommunicationBase cmdRelogin = new CmdRelogin(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, mLoginUser);
        if (!cmdRelogin.checkParameter(pMap)) {
            return -2;
        }
        cmdRelogin.SetBackupListener(mProgListener, mCmdListener);
        mCmdLogin = cmdRelogin;

        return cmdRelogin.doOperation(this, this);
    }

    private synchronized int execute(CommunicationBase cmd, HashMap<String, String> map) {
        if (null == cmd) {
            return CommunicationError.CE_COMMAND_ERROR_FATAL_ERROR;
        }
        if (null == mCmdListener || null == mProgListener) {
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }
        MyUtils.printContentInMap(map);

        if (!cmd.checkParameter(map)) {
            Log.w(TAG, "Fail to check parameters");
            return CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT;
        }

        int ret = CommunicationError.CE_ERROR_NO_ERROR;
        if (cmd.isNeedLogin() || isLoginCmd(cmd)) {    // commands that need to login first
            Log.d(TAG, String.format("command(%d) need to be logined, current status:%d", cmd.mAPI, mAgentStatus));
            cmd.SetBackupListener(mProgListener, mCmdListener);
            if (!isLoginCmd(cmd)) {
                queueCommand(cmd);  // que the command into command queue
            } else {
                mCmdLogin = cmd;
            }
            switch (mAgentStatus) {
                case AGENT_STATUS_Unknow:   // Check login status
                case AGENT_STATUS_NotLogin: // Send relogin command
                    if (!isLoginCmd(cmd)) {
                        // send relogin command first
                        if (CommunicationError.CE_ERROR_NO_ERROR != (ret = sendReloginCmd())) {
                            Log.e(TAG, "Fail to send relogin command, notify user to login");
                            if (null != mCmdListener) {
                                ResBase res = new ResBase(CmdRes.CMD_RES_NOT_LOGIN);
                                mCmdListener.onCommandFinished(CmdID.CMD_RELOGIN, res);
                            }
//                            removeCmd(cmd.mAPI/*, ""*/);
                            return 0;   // ret;
                        }
                        mAgentStatus = AGENT_STATUS_Logining;
                    } else {
                        ret  = cmd.doOperation(this, this);
                    }
                case AGENT_STATUS_Logining: // relogin command send out, waiting for result
                    break;
                case AGENT_STATUS_Logined:  // Already logined
                    ret  = cmd.doOperation(this, this);
            }
        } else {    // commands that do not need to login
            ret = cmd.doOperation(mCmdListener, mProgListener);
        }

        if (ret != CommunicationError.CE_ERROR_NO_ERROR) {
            Log.e(TAG, "failed to execute command: " + cmd.GetApiName());
        }

        return ret;
    }

    @Override
    public void onCommandFinished(int command, IApiResults.ICommon res) {
        switch (command) {
            case CmdID.CMD_LOGIN_BY_SMS:
            case CmdID.CMD_LOGIN_BY_PASSWORD :
            case CmdID.CMD_RELOGIN: {
                if (res.GetErrCode() == CmdRes.CMD_RES_NOERROR) {
                    Log.d(TAG, "Login success, resent all commands in queue");
                    mAgentStatus = AGENT_STATUS_Logined;
                    for (int n = 0; n < mCmdQueue.size(); n++) {
                        CommunicationBase cmd = mCmdQueue.get(n);
                        cmd.doOperation(this, this);    // mCmdListener, mProgListener);
                    }
                } else { // if (res.GetErrCode() == CmdRes.CMD_RES_NOT_LOGIN) {
                    Log.e(TAG, "silent login failed, notify user to login");
                }
                CICommandListener cmdListener = null;
                if (null != mCmdLogin &&
                    null != (cmdListener = mCmdLogin.GetBackupCommandListener())) {
                    // Notify UI
                    cmdListener.onCommandFinished(command, res);
                }
                mCmdLogin = null;
                break;
            }
            case CmdID.CMD_LOG_OUT: {
                mAgentStatus = AGENT_STATUS_NotLogin;
                break;
            }
            default: {
                int nCmdRes = res.GetErrCode();
                switch (nCmdRes) {
                    case CmdRes.CMD_RES_NOT_LOGIN: {
                        Log.e(TAG, "Command need login, post a silent login command");
                        mAgentStatus = AGENT_STATUS_NotLogin;
                        if (CommunicationError.CE_ERROR_NO_ERROR != sendReloginCmd()) {
                            Log.e(TAG, "Fail to send relogin command, notify user to login");
                            return;
                        }
                        mAgentStatus = AGENT_STATUS_Logining;
                        break;
                    }
                    default: {  // command succeed or failed
                        if (CmdRes.CMD_RES_NOERROR == nCmdRes) {
                            Log.d(TAG, "command processing succeeded, notify user");
                        } else {
                            Log.e(TAG, "command processing failed, notify user");
                        }
                        // remove it from command queue
                        CommunicationBase cmd           = removeCmd(command);
                        CICommandListener cmdListener   = null;
                        if (null != cmd &&
                            null != (cmdListener = cmd.GetBackupCommandListener())) {
                            // Notify UI
                            cmdListener.onCommandFinished(command, res);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onProgressChanged(int command, String percent, HashMap<String, String> map) {

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
    public int GetLoginUserInfo() {
        CommunicationBase op = new CmdGetLoginUserInfo(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
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
        if (null != user && !user.isEmpty()) {
            mLoginUser = user;
        }

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
        if (null != user && !user.isEmpty()) {
            mLoginUser = user;
        }

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
    public int ResetLoginPass(String user, String pass, String sms, String salt, String rand) {
        CommunicationBase op = new CmdResetLoginPassword(mContext, user, pass, sms, salt, rand);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetAgencyList(int begin, int cnt) {
        CommunicationBase op = new CmdGetAgencyList(mContext, begin, cnt);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int MofidyAgency(int agency, int rank_pro, int rank_att, int begin_year) {
        CommunicationBase op = new CmdModifyAgency(mContext, agency, rank_pro, rank_att, begin_year);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int Relogin(String userName) {
        if (null != userName && !userName.isEmpty()) {
            mLoginUser = userName;
        }

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
        CommunicationBase op = new CmdGetBriefPublicHouseInfo(mContext, houseId);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetHouseInfo(int houseId, boolean bBackend) {
        CommunicationBase op = new CmdGetHouseInfo(mContext, houseId, bBackend);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int CommitHouseByOwner(HouseInfo houseInfo, int agency) {
        CommunicationBase op = new CmdCommitHouseByOwner(mContext, houseInfo, agency);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int AmendHouse(HouseInfo houseInfo) {
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
    public int SetHousePrice(int house_id, int rental_tag, int rental_bottom, boolean bIncPropFee, int price_tag, int price_bottom) {
        CommunicationBase op = new CmdSetHousePrice(mContext, house_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_HOUSE_RENTAL_TAG,     String.valueOf(rental_tag));
        pMap.put(CommunicationParameterKey.CPK_HOUSE_RENTAL_BOTTOM,  String.valueOf(rental_bottom));
        pMap.put(CommunicationParameterKey.CPK_HOUSE_RENTAL_PROPFEE, String.valueOf(bIncPropFee));
        pMap.put(CommunicationParameterKey.CPK_HOUSE_RPRICE_TAG,     String.valueOf(price_tag));
        pMap.put(CommunicationParameterKey.CPK_HOUSE_PRICE_BOTTOM,   String.valueOf(price_bottom));
        return execute(op, pMap);
    }

    @Override
    public int GetHousePrice(int house_id, int begin, int count) {
        CommunicationBase op = new CmdGetHousePrices(mContext, house_id, begin, count);
        HashMap<String, String> pMap = new HashMap<String, String>();
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
        CommunicationBase op = new CmdGetBehalfHouses(mContext, type, begin, cnt);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetHouseDigestList(int type, int begin, int cnt, HouseFilterCondition filter, ArrayList<Integer> sort) {
        CommunicationBase op = new CmdGetHouseList(mContext, type, begin, cnt, filter, sort);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_HOUSE_TYPE, "" + type);
        pMap.put(CommunicationParameterKey.CPK_LIST_BEGIN, "" + begin);
        pMap.put(CommunicationParameterKey.CPK_LIST_CNT, "" + cnt);
        return execute(op, pMap);
    }

    @Override
    public int GetHouseShowtime(int house_id) {
        CommunicationBase op = new CmdGetHouseShowTime(mContext, house_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int SetHouseShowtime(int house_id, int pw, int pv, String pd) {
        CommunicationBase op = new CmdSetHouseShowtime(mContext, house_id, pw, pv, pd);
        HashMap<String, String> pMap = new HashMap<String, String>();
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
    public int EditFacility(int id, int nType, String sName, String sIcon) {
        CommunicationBase op = new CmdModifyFacility(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(id));
        pMap.put(CommunicationParameterKey.CPK_TYPE, String.valueOf(nType));
        pMap.put(CommunicationParameterKey.CPK_NAME, sName);
        if (null != sIcon && !sIcon.isEmpty()) {
            pMap.put(CommunicationParameterKey.CPK_IMG_FILE, sIcon);
        }
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
    public int AddFacility(int nType, String sName, String sIcon) {
        CommunicationBase op = new CmdAddFacility(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_TYPE, String.valueOf(nType));
        pMap.put(CommunicationParameterKey.CPK_NAME, sName);
        if (null != sIcon && !sIcon.isEmpty()) {
            pMap.put(CommunicationParameterKey.CPK_IMG_FILE, sIcon);
        }
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
    public int AddHouseFacility(int house, ArrayList<FacilityItem> list) {
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
    public int AddPicture(int house, int type, int refId, String desc, String file) {
        CommunicationBase op = new CmdAddPicture(mContext, house);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(refId));
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

    @Override
    public int MakeAppointment_SeeHouse(int house, String phone, String time_begin, String time_end, String desc) {
        CommunicationBase op = new CmdMakeAppointment_SeeHouse(mContext, house, phone, time_begin, time_end, desc);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetHouseSeeAppointmentList(int house_id, int begin, int cnt) {
        CommunicationBase op = new CmdGetAppointmentListHouseSee(mContext, house_id, begin, cnt);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetHouseList_AppointSee(int begin, int cnt) {
        CommunicationBase op = new CmdGetHouseList_AppointSee(mContext, begin, cnt);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public int GetUserHouseWatchList(int begin, int cnt) {
        CommunicationBase op = new CmdGetUserHouseWatchList(mContext, begin, cnt);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }
}
