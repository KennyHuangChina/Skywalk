package com.kjs.skywalk.app_android;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by Jackie on 2017/2/10.
 */

class PopupWindowWaiting extends PopupWindow {
    private Context mContext = null;
    private View mView = null;

    public PopupWindowWaiting(Context context) {
        super(context);
        mContext = context;

        mView = LayoutInflater.from(context).inflate(R.layout.popup_window_waiting, null);
        setContentView(mView);
        setFocusable(false);
        setTouchable(false);
        setBackgroundDrawable(null);
        setWidth(480);
        setHeight(320);
    }

}
