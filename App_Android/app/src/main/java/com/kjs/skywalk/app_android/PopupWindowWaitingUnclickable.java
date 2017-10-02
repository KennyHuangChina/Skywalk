package com.kjs.skywalk.app_android;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by Jackie on 2017/2/10.
 */

public class PopupWindowWaitingUnclickable extends PopupWindow {
    private Context mContext = null;
    private View mView = null;
    private int mWidth = 0;
    private int mHeight = 0;

    public PopupWindowWaitingUnclickable(Context context, int width, int height) {
        super(context);
        mContext = context;
        mWidth = width;
        mHeight = height;

        mView = LayoutInflater.from(context).inflate(R.layout.widget_waiting, null);
        setContentView(mView);
        setFocusable(false);
        setTouchable(true);
        setBackgroundDrawable(null);
        setWidth(width);
        setHeight(height);
        kjsLogUtil.i("Screen Width: " + width + " Screen Height: " + height);
    }

    public void updateProgressText(String text) {
        TextView view = (TextView)mView.findViewById(R.id.textViewInfo);

        view.setText(text);
    }

    public void show(View parent) {
        if(isShowing()) {
            return;
        }

        showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public void hide() {
        dismiss();
    }
}
