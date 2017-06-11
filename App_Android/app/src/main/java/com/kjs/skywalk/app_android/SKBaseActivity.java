package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by Jackie on 2017/5/27.
 */

public class SKBaseActivity extends AppCompatActivity {
    private PopupWindowWaiting mWaitingWindow = null;

    protected void showWaiting(final View v) {
        if(mWaitingWindow == null) {
            mWaitingWindow = new PopupWindowWaiting(this);
            DisplayMetrics dm = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(dm);
            mWaitingWindow.setWidth(dm.widthPixels);
            mWaitingWindow.setHeight(dm.heightPixels);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWaitingWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });
    }

    protected void hideWaiting() {
        if(mWaitingWindow != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWaitingWindow.dismiss();
                }
            });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
