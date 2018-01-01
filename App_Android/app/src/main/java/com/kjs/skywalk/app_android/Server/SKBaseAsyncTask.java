package com.kjs.skywalk.app_android.Server;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.kjs.skywalk.app_android.Activity_PasswordReset;
import com.kjs.skywalk.app_android.Activity_Search;
import com.kjs.skywalk.app_android.Activity_login;
import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CmdExecRes;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.app_android.ClassDefine.ServerError.SERVER_NEED_LOGIN;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_ASSIGN_APPOINTMENT_RECEPTIONIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdRes.CMD_RES_NOERROR;

/**
 * Created by admin on 2017/8/2.
 */

public class SKBaseAsyncTask    extends     AsyncTask<Integer, Void, Integer>
                                implements  CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{

    protected boolean               mResultGot  = false;
    protected Context               mContext    = null;
    protected ArrayList<CmdExecRes> mCmdList    = new ArrayList<>();

    SKBaseAsyncTask(Context context) {
        mContext = context;

        // Register Listener
        CommandManager.getCmdMgrInstance(mContext).Register(this, this);
    }

    public void close() {
        // Unregister Listener
        CommandManager.getCmdMgrInstance(mContext).Unregister(this, this);
    }

    protected boolean StoreCommand(CmdExecRes cmdRes) {
        return commonFun.StoreCommand(mCmdList, cmdRes);
    }

    protected CmdExecRes RetrieveCommand(int cmdSeq) {
        return commonFun.RetrieveCommand(mCmdList, cmdSeq);
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        return null;
    }

    boolean waitResult(int nTimeoutMs) {
        if (nTimeoutMs < 100)
            return mResultGot;

        int wait_count = 0;
        while (wait_count < nTimeoutMs / 100) {
            if (mResultGot)
                return true;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wait_count++;
        }

        return false;
    }

    @Override
    public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon result) {
        if (null == result) {
            kjsLogUtil.w("result is null");
            mResultGot = true;
            return;
        }
        CmdExecRes cmd = RetrieveCommand(cmdSeq);
        if (null == cmd) {  // result is not we wanted
            return;
        }
        kjsLogUtil.i(String.format("[command: %d(%s)] --- %s", command, CommunicationInterface.CmdID.GetCmdDesc(command), result.DebugString()));

        int errCode = result.GetErrCode();
        if (CommunicationError.CE_ERROR_NO_ERROR != errCode) {
            kjsLogUtil.e("Command:" + command + " finished with error: " + errCode);
            mResultGot = true;
            switch (errCode) {
                case SERVER_NEED_LOGIN:
                    doLogIn();
                    break;
            }
            return;
        }

        onCommandSuccess(command, result);
    }

    protected void onCommandSuccess(final int command, final IApiResults.ICommon result) {
        kjsLogUtil.w("Please override this function in sub-class");
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }

    protected void doLogIn() {
        Intent intent = new Intent(mContext, Activity_login.class);
        mContext.startActivity(intent);
    }
}
