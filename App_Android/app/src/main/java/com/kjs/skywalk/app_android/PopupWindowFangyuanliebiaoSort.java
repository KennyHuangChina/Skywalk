package com.kjs.skywalk.app_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Jackie on 2017/2/10.
 */

class PopupWindowFangyuanliebiaoSort extends PopupWindow {
    private Context mContext = null;
    private View mView = null;

    public PopupWindowFangyuanliebiaoSort(Context context) {
        super(context);
        mContext = context;

        mView = LayoutInflater.from(context).inflate(R.layout.popup_window_fangyuanliebiao_sort, null);
        setContentView(mView);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public View getView() {
        return mView;
    }
}
