package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.kjs.skywalk.communicationlibrary.CmdExecRes;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.app_android.ClassDefine.ServerError.SERVER_CONNECTION_ERROR;
import static com.kjs.skywalk.app_android.ClassDefine.ServerError.SERVER_NEED_LOGIN;
import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_RELOGIN;

/**
 * Created by Jackie on 2017/5/27.
 */

public class SKBaseActivity extends AppCompatActivity
        implements CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{
    private PopupWindowWaiting mWaitingWindow = null;

    protected int                       mHouseId        = 0;
    protected String                    mHouseLocation  = "";
    protected String                    mPropertyName   = "";
    protected int                       mPropertyId     = 0;
    protected String                    mBuildingNo     = "";
    protected String                    mRoomNo         = "";
    protected int                       mUserId         = 0;
    protected String                    mUserName       = "";
    protected String                    mUserPhone      = "";
    public IApiResults.IGetUserInfo     mLoginUserInfo  = null;
    private ArrayList<CmdExecRes>       mCmdList        = null;

    public int mActScreenWidth  = 0;
    public int mActScreenHeight = 0;

    public SKBaseActivity() {
        mCmdList = new ArrayList<CmdExecRes>();
    }

    protected boolean StoreCommand(CmdExecRes res) {
        synchronized (mCmdList) {
            if (mCmdList.add(res)) {
                kjsLogUtil.d("store command seq: " + res.mCmdSeq);
                return true;
            }
        }
        kjsLogUtil.e("Fail to store command seq: " + res.mCmdSeq);
        return false;
    }

    protected CmdExecRes RetrieveCommand(int cmdSeq) {
        synchronized (mCmdList) {
            for (int n = 0; n< mCmdList.size(); n++) {
                CmdExecRes res = mCmdList.get(n);
                if (res.mCmdSeq == cmdSeq) {
                    kjsLogUtil.d("Retrieve command seq: " + cmdSeq);
                    return mCmdList.remove(n);
                }
            }
        }
        kjsLogUtil.w("command seq: " + cmdSeq + " not found");
        return null;
    }

    protected void showWaiting(final View v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mWaitingWindow == null) {
                    mWaitingWindow = new PopupWindowWaiting(SKBaseActivity.this);
                    mWaitingWindow.setWidth(mActScreenWidth);
                    mWaitingWindow.setHeight(mActScreenHeight);
                }
                mWaitingWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });
    }

    protected void hideWaiting() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mWaitingWindow != null) {
                    mWaitingWindow.dismiss();
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        if(intent != null) {
            mHouseId        = getIntent().getIntExtra(ClassDefine.IntentExtraKeyValue.KEY_HOUSE_ID, 0);
            mHouseLocation  = getIntent().getStringExtra(ClassDefine.IntentExtraKeyValue.KEY_HOUSE_LOCATION);
            Log.i(getClass().getSimpleName().toString(), "House Id: " + mHouseId);
            Log.i(getClass().getSimpleName().toString(), "House Location: " + mHouseLocation);

            mPropertyName   = getIntent().getStringExtra(ClassDefine.IntentExtraKeyValue.KEY_PROPERTY_NAME);
            mPropertyId     = getIntent().getIntExtra(ClassDefine.IntentExtraKeyValue.KEY_PROPERTY_ID, 0);
            mBuildingNo     = getIntent().getStringExtra(ClassDefine.IntentExtraKeyValue.KEY_BUILDING_NO);
            mRoomNo         = getIntent().getStringExtra(ClassDefine.IntentExtraKeyValue.KEY_ROOM_NO);

            mUserId         = getIntent().getIntExtra(ClassDefine.IntentExtraKeyValue.KEY_USER_ID, 0);
            mUserName       = getIntent().getStringExtra(ClassDefine.IntentExtraKeyValue.KEY_USER_NAME);
            mUserPhone      = getIntent().getStringExtra(ClassDefine.IntentExtraKeyValue.KEY_USER_PHONE);
        }

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mActScreenWidth     = metric.widthPixels;
        mActScreenHeight    = metric.heightPixels;
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister Listener
        CommandManager.getCmdMgrInstance(this).Unregister(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register Listener
        CommandManager.getCmdMgrInstance(this).Register(this, this);
        // update login user info
        IsLogined();
    }

    protected void processConnectionError() {
        kjsLogUtil.i("process connection error");
        ClassDefine.NetworkErrorDialog dialog = new ClassDefine.NetworkErrorDialog(this);
        dialog.showDialog(true);
    }

    protected void processLogin() {
        startActivity(new Intent(this, Activity_login.class));
    }


    @Override
    public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
        kjsLogUtil.i("[" + CommunicationInterface.CmdID.GetCmdDesc(command) + "]" + "SKBaseActivity::onCommandFinished");
        int errorCode = iResult.GetErrCode();
        if(errorCode == CE_ERROR_NO_ERROR) {
            return;
        }

        if(CommunicationError.IsNetworkError(errorCode)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    processConnectionError();
                }
            });

            return;
        }

        if(SERVER_NEED_LOGIN == ClassDefine.ServerError.getErrorType(errorCode)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    processLogin();
                }
            });

            return;
        } else {
            kjsLogUtil.i("error: " + iResult.GetErrDesc());
            if(command == CMD_RELOGIN) {
                kjsLogUtil.i("re-login failed: " + "show log in activity.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processLogin();
                    }
                });
            }
        }
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {
        kjsLogUtil.i("SKBaseActivity::onProgressChanged");
    }

    protected boolean IsLogined() {
        mLoginUserInfo = CommandManager.getCmdMgrInstance(this).GetLoginUserInfo();
        kjsLogUtil.d(String.format("Login status: %s", (null == mLoginUserInfo) ? "Not Login" : mLoginUserInfo.GetName()));
        return null != mLoginUserInfo;
    }
}
