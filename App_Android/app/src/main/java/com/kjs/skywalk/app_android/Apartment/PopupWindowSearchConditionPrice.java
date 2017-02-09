package com.kjs.skywalk.app_android.Apartment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.kjs.skywalk.app_android.R;

/**
 * Created by Jackie on 2017/2/9.
 */

public class PopupWindowSearchConditionPrice extends PopupWindow {
    private Context mContext = null;

    public PopupWindowSearchConditionPrice(Context context) {
        super(context);
        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_search_condition_price,null);
        setContentView(view);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
