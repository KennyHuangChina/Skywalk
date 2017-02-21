package com.kjs.skywalk.app_android.Apartment;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;

import org.w3c.dom.Text;

/**
 * Created by Jackie on 2017/2/10.
 */

class PopupWindowSearchConditionFilter extends PopupWindow {
    private Context mContext = null;

    public PopupWindowSearchConditionFilter(Context context) {
        super(context);
        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_search_condition_filter, null);
        setContentView(view);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        TextView view1 = (TextView) view.findViewById(R.id.textView_jin_ri_ke_kan);
        view1.setSelected(true);
        TextView view2 = (TextView) view.findViewById(R.id.textView_50_70);
        view2.setSelected(true);
    }
}
