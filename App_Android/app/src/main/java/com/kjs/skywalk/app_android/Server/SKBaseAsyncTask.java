package com.kjs.skywalk.app_android.Server;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.kjs.skywalk.app_android.Activity_PasswordReset;
import com.kjs.skywalk.app_android.Activity_Search;
import com.kjs.skywalk.app_android.Activity_login;
import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.HashMap;

import static com.kjs.skywalk.app_android.ClassDefine.ServerError.SERVER_NEED_LOGIN;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdRes.CMD_RES_NOERROR;

/**
 * Created by admin on 2017/8/2.
 */

public class SKBaseAsyncTask extends AsyncTask<Integer, Void, Integer> implements CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{

    protected boolean mResultGot = false;
    protected Context mContext = null;

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

    CommunicationInterface.CIProgressListener mProgressListener = new CommunicationInterface.CIProgressListener() {
        @Override
        public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {
        }
    };

    @Override
    public void onCommandFinished(int command, IApiResults.ICommon result) {
        int errorCode = result.GetErrCode();
        String description = result.GetErrDesc();
        kjsLogUtil.e("error code: " + errorCode + "-->" + description);
        if(errorCode == CMD_RES_NOERROR) {
            return;
        }

        int error = ClassDefine.ServerError.getErrorType(errorCode);
        switch (error) {
            case SERVER_NEED_LOGIN:
                doLogIn();
                break;
        }
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }

    protected void doLogIn() {
        Intent intent = new Intent(mContext, Activity_login.class);
        mContext.startActivity(intent);
    }
}
