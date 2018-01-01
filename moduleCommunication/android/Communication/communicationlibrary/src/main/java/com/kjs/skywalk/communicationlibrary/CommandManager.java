package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import static android.os.SystemClock.sleep;
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

    private ArrayList<CICommandListener>    mCmdListeners   = null;
    private ArrayList<CIProgressListener>   mProgListeners  = null;
    private ArrayList<CommunicationBase>    mCmdQueue       = new ArrayList<CommunicationBase>();
    private CommunicationBase               mCmdLogin       = null;
    private int                             mCmdSeq         = COMMAND_SEQ_BASE;

    private static final int            AGENT_STATUS_Unknow     = 0,
                                        AGENT_STATUS_NotLogin   = 1,
                                        AGENT_STATUS_Logining   = 2,
                                        AGENT_STATUS_Logined    = 3;
    private int                         mAgentStatus            = AGENT_STATUS_Unknow;
    private String                      mLoginUser              = null; // record the last login user
    private IApiResults.IGetUserInfo    mLoginUserInfo          = null; // represent the current login user

    private CommandManager(Context context, CICommandListener CmdListener, CIProgressListener ProgListener) {
        mContext        = context;
        mCmdListeners   = new ArrayList<CICommandListener>();
        mProgListeners  = new ArrayList<CIProgressListener>();

        mUtils= new MyUtils(context);
//        mCookieManager = SKCookieManager.getManager(context);

        // Get login user
        loadLoginUser();
    }

    public static synchronized CommandManager getCmdMgrInstance(Context context) {
        if (null == mCmdMgr) {
            mCmdMgr = new CommandManager(context, null, null);
        }

        return mCmdMgr;
    }

    public  int Register(CICommandListener cmdListener, CIProgressListener progListener) {
        String Fn = "[Register] ";
        if (null == cmdListener || null == progListener) {
            Log.e(TAG, Fn + "Invalid input, cmdListener:" + cmdListener + ", progListener:" + progListener);
            return -1;
        }

        synchronized(mCmdListeners) {
            for (CICommandListener cl : mCmdListeners) {
                if (cl == cmdListener) {
                    Log.d(TAG, Fn + "cmdListener:" + cmdListener + " already registered");
                    return 0;
                }
            }
            mCmdListeners.add(cmdListener);
            mProgListeners.add(progListener);
            Log.d(TAG, Fn + "Register cmdListener:" + cmdListener + ", progListener:" + progListener);
            Log.d(TAG, Fn + "Total listener:" + mCmdListeners.size());
        }
        return 0;
    }

    public int Unregister(CICommandListener cmdListener, CIProgressListener progListener) {
        String Fn = "[Unregister] ";
        if (null == cmdListener || null == progListener) {
            Log.e(TAG, Fn + "Invalid input, cmdListener:" + cmdListener + ", progListener:" + progListener);
            return -1;
        }
        synchronized (mCmdListeners) {
            for (int n = 0; n < mCmdListeners.size(); n++) {
                if (cmdListener == mCmdListeners.get(n)) {
                    mCmdListeners.remove(n);
                    mProgListeners.remove(n);
                    Log.d(TAG, Fn + "Unregister cmdListener:" + cmdListener + ", progListener:" + progListener);
                    Log.d(TAG, Fn + "Total listener:" + mCmdListeners.size());
                    return 0;
                }
            }
        }
        Log.d(TAG, Fn + "cmdListener:" + cmdListener + ", progListener:" + progListener + " not exist");
        return 1;
    }

    private int onNotify(int cmdId, int cmdSeq, IApiResults.ICommon res) {
        synchronized (mCmdListeners) {
            for (CICommandListener cl : mCmdListeners) {
                cl.onCommandFinished(cmdId, cmdSeq, res);
            }
            return 0;
        }
    }

    /**
     *  load login user from shared preference
     *  @return String, user login name
     */
    private String loadLoginUser() {
        SharedPreferences sp = mContext.getSharedPreferences("AppSetting", Context.MODE_PRIVATE);
        mLoginUser = sp.getString("login-user", "");
        Log.d(TAG, "[loadLoginUser] mLoginUser: " + mLoginUser);
        return mLoginUser;
    }

    /**
     *  Store the login user into shared preference
     */
    private void storeLoginUser() {
        SharedPreferences sp = mContext.getSharedPreferences("AppSetting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("login-user", mLoginUser);
        editor.commit();
    }

    /**
     *  Put the command in queue
     *  @param cmd - command to queue
     *  @return command queued
     */
    private synchronized CommunicationBase queueCommand(CommunicationBase cmd) {
        String Fn = "[queueCommand] ";

        if (null == mCmdQueue) {
            mCmdQueue = new ArrayList<CommunicationBase>();
        }
        // Check if the command already exist in queue
        // This is a rough way, just check the command id
//        int nNewCmd = cmd.mAPI;
        for (CommunicationBase cmd_in_queue : mCmdQueue) {
//            if (cmd_in_queue.mAPI == nNewCmd) {
            if (cmd_in_queue.isSameCmd(cmd)) {
                Log.d(TAG, Fn + String.format("new command(0x%x) already exist", cmd.mAPI));
                return cmd_in_queue;    // give up the new command, use the command queued instead
            }
        }

        if (mCmdQueue.add(cmd)) {
            Log.d(TAG, Fn + String.format("new command(0x%x) queued, seq:%d", cmd.mAPI, cmd.getCmdSeq()));
            return cmd;
        }

        Log.e(TAG, Fn + String.format("Fail to queue new command(0x%x)", cmd.mAPI));
        return null;
    }

    private synchronized CommunicationBase removeCmd(int cmd, IApiResults.ICommon res) {
        String FN = "[removeCmd] ";

        if (cmd <= 0 && null == mCmdQueue) {
            return null;
        }

        CommunicationBase cmd2Remov = null;
        for (int i = 0; i < mCmdQueue.size(); i++) {
            CommunicationBase cmdThis = mCmdQueue.get(i);
            if (cmdThis.isCmdRes(cmd, res)) {
                cmd2Remov = mCmdQueue.remove(i);     // (cmdThis);
                break;
            }
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
            Log.e(TAG, FN + "mLoginUser not set");
            return CommunicationError.CE_COMMAND_ERROR_NOT_LOGIN;
        }

        CommunicationBase cmdRelogin = new CmdRelogin(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, mLoginUser);
        if (!cmdRelogin.checkParameter(pMap)) {
            Log.e(TAG, FN + "fail to check parameters for relogin");
            return -2;
        }
        cmdRelogin.setCmdSeq(getCommandSeq());
        int ret = cmdRelogin.doOperation(this, this);
        if (CommunicationError.CE_ERROR_NO_ERROR == ret) {
            mCmdLogin = cmdRelogin;
        }

        return ret;
    }

    private int getLoginUserInfoFromServer() {
        String Fn = "[getLoginUserInfoFromServer] ";
        Log.i(TAG, Fn);

        CommunicationBase cmdGetLoginUserInfo = new CmdGetLoginUserInfo(mContext);
//        HashMap<String, String> pMap = new HashMap<String, String>();
        int ret = cmdGetLoginUserInfo.doOperation(this, this);
        if (CommunicationError.CE_ERROR_NO_ERROR == ret) {
        }

        return ret;
    }

    int getCommandSeq() {
        mCmdSeq ++;
        return mCmdSeq;
    }

    /**
     *  Execute the command
     *  @param cmd : command to process
     *  @param map : argument list
     *  @return
     *      0x00000000                  CE_ERROR_NO_ERROR
     *      0x80000000 - 0x8fffffff     Error Codes
     *      0x90000000 and above        Unique command id (COMMAND_ID_BASE)
     */
    private synchronized CmdExecRes execute(CommunicationBase cmd, HashMap<String, String> map) {
        String Fn = "[execute] ";
        if (null == cmd) {
            return new CmdExecRes(CommunicationError.CE_COMMAND_ERROR_FATAL_ERROR, -1, null);
        }
        MyUtils.printContentInMap(map);
        Log.i(TAG, Fn + String.format("cmd:0x%x <%s>", cmd.mAPI, CmdID.GetCmdDesc(cmd.mAPI)));

        // Check parameters before processing this command
        if (!cmd.checkParameter(map)) {
            Log.w(TAG, Fn + "Fail to check parameters");
            return new CmdExecRes(CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT, -1, null);
        }
        cmd.setCmdSeq(getCommandSeq());

        // que the command into queue
//        cmd.SetBackupListener(mProgListener, mCmdListener);
        if (!isLoginCmd(cmd)) {
            if (null == (cmd = queueCommand(cmd))) {
                return new CmdExecRes(CommunicationError.CE_COMMAND_ERROR_INVALID_INPUT, -1, null);
            }
        } else {
            mCmdLogin = cmd;
        }

        int ret = CommunicationError.CE_ERROR_NO_ERROR;
        if (cmd.isNeedLogin() || isLoginCmd(cmd)) {    // commands that need to login first
            Log.d(TAG, Fn + String.format("command(%d)[%s] need to be logined, current status:%d", cmd.mAPI, CmdID.GetCmdDesc(cmd.mAPI), mAgentStatus));
            switch (mAgentStatus) {
                case AGENT_STATUS_Unknow:   // Check login status
                case AGENT_STATUS_NotLogin: // Send relogin command
                    if (!isLoginCmd(cmd)) {  // regular commands
                        // send relogin command first
                        if (CommunicationError.CE_ERROR_NO_ERROR != (ret = sendReloginCmd())) {
                            Log.e(TAG, Fn + String.format("Fail to send relogin command, mAgentStatus:%d, ret:%d", mAgentStatus, ret));
                            if (CommunicationError.CE_COMMAND_ERROR_NOT_LOGIN == ret) {
                                mLoginUserInfo = null;
                                Log.e(TAG, Fn + "user not login, notify user to login");
                                onNotify(CmdID.CMD_RELOGIN, cmd.getCmdSeq(), new ResBase(CmdRes.CMD_RES_NOT_LOGIN));
                                // Keep the command in queue, once user get logined, the commands will be processed automatically, one by one
                            } else {
                                Log.e(TAG, Fn + String.format("other error(0x%x), remove the command from queue", ret));
                                removeCmd(cmd.mAPI, null);
                            }
                            return new CmdExecRes(CommunicationError.CE_ERROR_NO_ERROR, cmd.getCmdSeq(), cmd.mArgs);   // ret;
                        }
                        mAgentStatus = AGENT_STATUS_Logining;
                    } else { // login commands
                        ret  = cmd.doOperation(this, this);
                    }
                    break;
                case AGENT_STATUS_Logining: // relogin command sent out, waiting for result
                    break;
                case AGENT_STATUS_Logined:  // Already logined, send command directly
                    ret  = cmd.doOperation(this, this);
            }
            if (CommunicationError.CE_ERROR_NO_ERROR != ret) {  // remove the command already cached
                removeCmd(cmd.mAPI, null);
            }
        } else {    // commands that do not need to login, send to server directly
            ret = cmd.doOperation(this, this);
            if (CommunicationError.CE_ERROR_NO_ERROR != ret) {  // remove the command already cached
                removeCmd(cmd.mAPI, null);
            }
        }

        if (CommunicationError.CE_ERROR_NO_ERROR != ret) {
            Log.e(TAG, Fn + "failed to execute command: " + cmd.GetApiName() + ", ret:" + ret);
            return new CmdExecRes(ret, -1, null);
        }

        return new CmdExecRes(CommunicationError.CE_ERROR_NO_ERROR, cmd.getCmdSeq(), cmd.mArgs);
    }

    @Override
    public void onCommandFinished(final int cmdId, final int cmdSeq, IApiResults.ICommon res) {
        String Fn = "[onCommandFinished] ";
        Log.i(TAG, Fn + String.format("cmd:0x%x <%s>, %d", cmdId, CmdID.GetCmdDesc(cmdId), cmdSeq));

        switch (cmdId) {
            case CmdID.CMD_LOGIN_BY_SMS:
            case CmdID.CMD_LOGIN_BY_PASSWORD :
            case CmdID.CMD_RELOGIN: {
                if (res.GetErrCode() == CmdRes.CMD_RES_NOERROR) {
                    // send a command automatically to get login user info from server
                    getLoginUserInfoFromServer();

                    Log.d(TAG, Fn + String.format("Login success, try to resent all (%d) commands in queue", mCmdQueue.size()));
                    mAgentStatus = AGENT_STATUS_Logined;
                    for (int n = 0; n < mCmdQueue.size(); n++) {
                        CommunicationBase cmd = mCmdQueue.get(n);
                        cmd.doOperation(this, this);    // mCmdListener, mProgListener);
                        sleep(100);
                    }
                } else {
                    Log.e(TAG, Fn + String.format("silent login failed(0x%x), notify user to login", res.GetErrCode()));
                    mAgentStatus = AGENT_STATUS_NotLogin;
                    mLoginUserInfo = null;
                }

                // notify UI no matter command succeeded or failed
                CICommandListener cmdListener = null;
                if (null != mCmdLogin) {
                    onNotify(cmdId, cmdSeq, res);
                }
//                if (null != mCmdLogin &&
//                    null != (cmdListener = mCmdLogin.GetBackupCommandListener())) {
//                    // Notify UI
//                    cmdListener.onCommandFinished(cmdId, cmdSeq, res);
//                }
                mCmdLogin = null;
                break;
            }
            case CmdID.CMD_LOG_OUT: {
                mAgentStatus = AGENT_STATUS_NotLogin;
                mLoginUserInfo = null;
                // remove it from command queue
                CommunicationBase cmd = removeCmd(cmdId, res);
                if (null != cmd) {
                    onNotify(cmdId, cmdSeq, res);
                }
                break;
            }
            case CmdID.CMD_GET_LOGIN_USER_INFO: {
                mLoginUserInfo = (ResGetUserInfo)res;
                Log.d(TAG, Fn + "Login user info:" + ((IApiResults.ICommon)mLoginUserInfo).DebugString());
                break;
            }
            default: { // all other commands
                int nCmdRes = res.GetErrCode();
                if (CmdRes.CMD_RES_NOT_LOGIN == nCmdRes) {
                    Log.e(TAG, Fn + "Command need login, post a silent login command");
                    mAgentStatus = AGENT_STATUS_NotLogin;
                    mLoginUserInfo = null;
                    if (CommunicationError.CE_ERROR_NO_ERROR != sendReloginCmd()) {
                        Log.e(TAG, Fn + "Fail to send relogin command, notify user to login");
                        onNotify(CmdID.CMD_RELOGIN, mCmdLogin.getCmdSeq(), new ResBase(CmdRes.CMD_RES_NOT_LOGIN));
                        return;
                    }
                    mAgentStatus = AGENT_STATUS_Logining;
                } else { // command succeed or failed by other reasons
                    if (CmdRes.CMD_RES_NOERROR == nCmdRes) {
                        Log.d(TAG, Fn + String.format("command(0x%x %s, %d) processing succeeded, notify user",
                                                        cmdId, CmdID.GetCmdDesc(cmdId), cmdSeq));
                    } else {
                        Log.e(TAG, Fn + String.format("command(0x%x %s, %d) processing failed(0x%x), notify user",
                                                        cmdId, CmdID.GetCmdDesc(cmdId), cmdSeq, nCmdRes));
                        if (CommunicationError.IsNetworkError(nCmdRes)) {
                            Log.e(TAG, Fn + String.format("Network error:0x%x, %s", nCmdRes, CommunicationError.getErrorDescription(nCmdRes)));
                            mLoginUserInfo = null;
                        }
                    }
                    // remove it from command queue
                    CommunicationBase cmd = removeCmd(cmdId, res);
                    if (null == cmd) {
                        Log.w(TAG, Fn + String.format("not found command(0x%x, seq:%d) in queue", cmdId, cmdSeq));
                    }
                    onNotify(cmdId, cmdSeq, res);
                }
            }
        }
    }

    @Override
    public void onProgressChanged(int command, String percent, HashMap<String, String> map) {

    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      -- Command APIs --
    //
    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public CmdExecRes GetSmsCode(String userName) {
        CommunicationBase op = new CmdGetSmsCode(mContext, userName);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetUserInfo(int uid) {
        CommunicationBase op = new CmdGetUserInfo(mContext, uid);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public IApiResults.IGetUserInfo GetLoginUserInfo() {
//        if (bForceUpdate) {
//            mLoginUserInfo = null;
//            getLoginUserInfoFromServer();
//        }
        return mLoginUserInfo;
    }

    @Override
    public CmdExecRes GetUserSalt(String userName) {
        CommunicationBase op = new CmdGetUserSalt(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, userName);
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes LoginByPassword(String user, String pass, String rand, String salt) {
        if (null != user && !user.isEmpty()) {
            mLoginUser = user;
            storeLoginUser();
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
    public CmdExecRes LoginBySms(String user, String smsCode) {
        if (null != user && !user.isEmpty()) {
            mLoginUser = user;
            storeLoginUser();
        }

        CommunicationBase op = new CmdLoginBySms(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, user);
        pMap.put(CommunicationParameterKey.CPK_SMS_CODE, smsCode);
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes Logout() {
        mLoginUserInfo = null;
        CommunicationBase op = new CmdLogout(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes ResetLoginPass(String user, String pass, String sms, String salt, String rand) {
        CommunicationBase op = new CmdResetLoginPassword(mContext, user, pass, sms, salt, rand);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetAgencyList(int begin, int cnt) {
        CommunicationBase op = new CmdGetAgencyList(mContext, begin, cnt);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes MofidyAgency(int agency, int rank_pro, int rank_att, int begin_year) {
        CommunicationBase op = new CmdModifyAgency(mContext, agency, rank_pro, rank_att, begin_year);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public String GetCookie(String url) {
        SKCookieManager mCookieManager = SKCookieManager.getManager(mContext);
        return mCookieManager.getCookie(url);
    }

    @Override
    public CmdExecRes Relogin(String userName) {
        if (null != userName && !userName.isEmpty()) {
            mLoginUser = userName;
            storeLoginUser();
        }

        CommunicationBase op = new CmdRelogin(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, userName);
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes CommandTest() {
        CommunicationBase op = new CommandTest(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetBriefPublicHouseInfo(int houseId) {
        CommunicationBase op = new CmdGetBriefPublicHouseInfo(mContext, houseId);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetHouseInfo(int houseId, boolean bBackend) {
        CommunicationBase op = new CmdGetHouseInfo(mContext, houseId, bBackend);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes CommitHouseByOwner(HouseInfo houseInfo, int agency) {
        CommunicationBase op = new CmdCommitHouseByOwner(mContext, houseInfo, agency);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes AmendHouse(HouseInfo houseInfo) {
        CommunicationBase op = new CmdAmendHouse(mContext, houseInfo);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes RecommendHouse(int house_id, int action) {
        CommunicationBase op = new CmdRecommendHouse(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id));
        pMap.put(CommunicationParameterKey.CPK_HOUSE_RECOMMENT_ACT, String.valueOf(action));
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes SetHouseCoverImg(int house_id, int img_id) {
        CommunicationBase op = new CmdSetHouseCoverImg(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id));
        pMap.put(CommunicationParameterKey.CPK_HOUSE_COVER_IMG, String.valueOf(img_id));
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes SetHousePrice(int house_id, int rental_tag, int rental_bottom, boolean bIncPropFee, int price_tag, int price_bottom) {
        CommunicationBase op = new CmdSetHousePrice(mContext, house_id, rental_tag, rental_bottom, bIncPropFee, price_tag, price_bottom);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetHousePrice(int house_id, int begin, int count) {
        CommunicationBase op = new CmdGetHousePrices(mContext, house_id, begin, count);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes CertificateHouse(int house_id, boolean bPass, String sCertComment) {
        CommunicationBase op = new CmdCertificateHouse(mContext, house_id, bPass, sCertComment);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetHouseCertHist(int house_id) {
        CommunicationBase op = new CmdGetHouseCertHist(mContext, house_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes RecommitHouseCertification(int house_id, String comments) {
        CommunicationBase op = new CmdRecommitHouseCertification(mContext, house_id, comments);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetBehalfHouses(int type, int begin, int cnt) {
        CommunicationBase op = new CmdGetBehalfHouses(mContext, type, begin, cnt);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetHouseDigestList(int type, int begin, int cnt, HouseFilterCondition filter, ArrayList<Integer> sort) {
        CommunicationBase op = new CmdGetHouseList(mContext, type, begin, cnt, filter, sort);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetHouseShowtime(int house_id) {
        CommunicationBase op = new CmdGetHouseShowTime(mContext, house_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes SetHouseShowtime(int house_id, int pw, int pv, String pd) {
        CommunicationBase op = new CmdSetHouseShowtime(mContext, house_id, pw, pv, pd);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public String GetLandlordHouseSubmitConfirmUrl(int property, String building_no, String house_no) {
        String Fn = "[GetLandlordHouseSubmitConfirmUrl] ";
        if (property <= 0) {
            Log.e(TAG, Fn + String.format("property: %d", property));
            return null;
        }
        if (null == building_no || building_no.isEmpty()) {
            Log.e(TAG, Fn + String.format("building_no: %d", building_no));
            return null;
        }
        if (null == house_no || house_no.isEmpty()) {
            Log.e(TAG, Fn + String.format("house_no: %d", house_no));
            return null;
        }

        String Url = ServerURL.mServerUri +
                        String.format("/v1/house/LandlordSubmitHouseContract?ett=%d&bn=%s&hn=%s", property, building_no, house_no);
        return Url;
    }

    @Override
    public CmdExecRes AssignHouseAgency(int house, int agent) {
        CommunicationBase op = new AssignHouseAgency(mContext, house, agent);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetPropertyListByName(String sName, int nBegin, int nCount) {
        CommunicationBase op = new CmdGetPropertyList(mContext, sName, nBegin, nCount);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes AddProperty(String sName, String sAddr, String sDesc) {
        CommunicationBase op = new CmdAddProperty(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_NAME, sName);
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_ADDR, sAddr);
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_DESC, sDesc);
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetPropertyInfo(int nPropId) {
        CommunicationBase op = new CmdGetPropertyInfo(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(nPropId));
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes ModifyPropertyInfo(int nPropId, String sName, String sAddr, String sDesc) {
        CommunicationBase op = new CmdModifyPropertyInfo(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(nPropId));
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_NAME, sName);
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_ADDR, sAddr);
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_DESC, sDesc);
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes AddDeliverable(String sName) {
        CommunicationBase op = new CmdAddDeliverable(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_NAME, sName);
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetDeliverableList() {
        CommunicationBase op = new CmdGetDeliverableList(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes ModifyDeliverable(int dev_id, String sName) {
        CommunicationBase op = new CmdModifyDeliverable(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(dev_id));
        pMap.put(CommunicationParameterKey.CPK_NAME, sName);
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes AddHouseDeliverable(int house_id, int deliverable_id, int qty, String sDesc) {
        CommunicationBase op = new CmdAddHouseDeliverable(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id));
        pMap.put(CommunicationParameterKey.CPK_DELIVERABLE_ID, String.valueOf(deliverable_id));
        pMap.put(CommunicationParameterKey.CPK_QTY, String.valueOf(qty));
        pMap.put(CommunicationParameterKey.CPK_DESC, sDesc);
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetHouseDeliverables(int house_id) {
        CommunicationBase op = new CmdGetHouseDeliverables(mContext, house_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(house_id)); // "2");
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes AddFacilityType(String sTypeName) {
        CommunicationBase op = new CmdAddFacilityType(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_NAME, sTypeName);
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes EditFacility(int id, int nType, String sName, String sIcon) {
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
    public CmdExecRes EditFacilityType(int typeId, String sTypeName) {
        CommunicationBase op = new CmdModifyFacilityType(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(typeId));
        pMap.put(CommunicationParameterKey.CPK_NAME, sTypeName);
        return execute(op, pMap);
    }

   @Override
   public CmdExecRes GetFacilityTypeList() {
       CommunicationBase op = new CmdGetFacilityTypeList(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes AddFacility(int nType, String sName, String sIcon) {
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
    public CmdExecRes GetFacilityList(int nType) {
        CommunicationBase op = new CmdGetFacilityList(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_TYPE, String.valueOf(nType));
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes AddHouseFacility(int house, ArrayList<FacilityItem> list) {
        CommunicationBase op = new CmdAddHouseFacility(mContext, house, list);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes EditHouseFacility(int hfid, int fid, int qty, String desc) {
        CommunicationBase op = new CmdModifyHouseFacility(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, String.valueOf(hfid));
        pMap.put(CommunicationParameterKey.CPK_FACILITY_ID, String.valueOf(fid));
        pMap.put(CommunicationParameterKey.CPK_QTY, String.valueOf(qty));
        pMap.put(CommunicationParameterKey.CPK_DESC, desc);
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetHouseFacilityList(int house) {
        CommunicationBase op = new CmdGetHouseFacilityList(mContext, house);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes AddPicture(int house, int type, int refId, String desc, String file) {
        CommunicationBase op = new CmdAddPicture(mContext, file, house, type, refId, desc);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes DelePicture(int pic) {
        CommunicationBase op = new CmdDelePicture(mContext, pic);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetPicUrls(int pic, int size) {
        CommunicationBase op = new CmdGetPictureUrls(mContext, pic, size);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetHousePics(int house, int type, int size) {
        CommunicationBase op = new CmdGetHousePicList(mContext, house, type, size);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetUserPics(int user, int type, int size) {
        CommunicationBase op = new CmdGetUserPicList(mContext, user, type, size);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetHouseNewEvent() {
        CommunicationBase op = new CmdGetHouseNewEvent(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetHouseEventProcList(int event_id) {
        CommunicationBase op = new CmdGetHouseEventProcList(mContext, event_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetSysMsgList(int posi_bgn, int fetch_cnt, boolean ido, boolean nmo) {
        CommunicationBase op = new CmdGetSysMsgList(mContext, posi_bgn, fetch_cnt, ido, nmo);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes ModifyHouseEvent(int event_id, String desc) {
        CommunicationBase op = new CmdModifyHouseEvent(mContext, event_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_DESC, String.valueOf(desc));
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetNewMsgCount() {
        CommunicationBase op = new CmdGetNewMsgCount(mContext);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetSysMsgInfo(int msg_id) {
        CommunicationBase op = new CmdGetSysMsgInfo(mContext, msg_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes ReadNewMsg(int msg_id) {
        CommunicationBase op = new CmdNewMsgRead(mContext, msg_id);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes MakeAppointment_SeeHouse(int house, String phone, String time_begin, String time_end, String desc) {
        CommunicationBase op = new CmdMakeAppointment_SeeHouse(mContext, house, phone, time_begin, time_end, desc);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetHouseSeeAppointmentList(int house_id, int begin, int cnt) {
        CommunicationBase op = new CmdGetAppointmentListHouseSee(mContext, house_id, begin, cnt);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetHouseList_AppointSee(int begin, int cnt) {
        CommunicationBase op = new CmdGetHouseList_AppointSee(mContext, begin, cnt);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes MakeAppointmentAction(int apid, int act, String time_begin, String time_end, String comments) {
        CommunicationBase op = new CmdMakeAppointmentAction(mContext, apid, act, time_begin, time_end, comments);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetAppointmentInfo(int apid) {
        CommunicationBase op = new CmdGetAppointmentInfo(mContext, apid);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes AssignAppointmentReceptionist(int aid, int receptionist) {
        CommunicationBase op = new CmdAssignAppointmentReceptionist(mContext, aid, receptionist);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }

    @Override
    public CmdExecRes GetUserHouseWatchList(int begin, int cnt) {
        CommunicationBase op = new CmdGetUserHouseWatchList(mContext, begin, cnt);
        HashMap<String, String> pMap = new HashMap<String, String>();
        return execute(op, pMap);
    }
}
