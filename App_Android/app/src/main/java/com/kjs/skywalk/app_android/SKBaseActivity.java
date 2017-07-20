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

import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.HashMap;

import static com.kjs.skywalk.app_android.ClassDefine.ServerError.SERVER_CONNECTION_ERROR;

/**
 * Created by Jackie on 2017/5/27.
 */

public class SKBaseActivity extends AppCompatActivity
        implements CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{
    private PopupWindowWaiting mWaitingWindow = null;

    protected int mHouseId = 0;
    protected String mHouseLocation = "";

    public int mActScreenWidth = 0;
    public int mActScreenHeight = 0;

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
            mHouseId = getIntent().getIntExtra(ClassDefine.IntentExtraKeyValue.KEY_HOUSE_ID, 0);
            mHouseLocation = getIntent().getStringExtra(ClassDefine.IntentExtraKeyValue.KEY_HOUSE_LOCATION);
            Log.i(getClass().getSimpleName().toString(), "House Id: " + mHouseId);
            Log.i(getClass().getSimpleName().toString(), "House Location: " + mHouseLocation);
        }

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mActScreenWidth = metric.widthPixels;
        mActScreenHeight = metric.heightPixels;
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
    public void onCommandFinished(int i, IApiResults.ICommon iCommon) {
        kjsLogUtil.i("SKBaseActivity::onCommandFinished");
        int errorCode = ClassDefine.ServerError.getErrorType(iCommon.GetErrCode());
        switch (errorCode) {
            case SERVER_CONNECTION_ERROR: {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processConnectionError();
                    }
                });
                break;
            }
        }
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {
        kjsLogUtil.i("SKBaseActivity::onProgressChanged");
    }
}
