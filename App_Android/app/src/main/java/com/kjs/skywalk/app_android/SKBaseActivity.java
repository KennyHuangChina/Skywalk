package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by Jackie on 2017/5/27.
 */

public class SKBaseActivity extends Activity {
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
}
