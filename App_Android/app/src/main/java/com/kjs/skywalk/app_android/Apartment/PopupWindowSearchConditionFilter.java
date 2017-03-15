package com.kjs.skywalk.app_android.Apartment;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;

import org.w3c.dom.Text;

/**
 * Created by Jackie on 2017/2/10.
 */

class PopupWindowSearchConditionFilter extends PopupWindow {
    private Context mContext = null;
    private View mView = null;

    public PopupWindowSearchConditionFilter(Context context) {
        super(context);
        mContext = context;

        mView = LayoutInflater.from(context).inflate(R.layout.popup_window_search_condition_filter, null);
        setContentView(mView);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        TextView buttonOk = (TextView)mView.findViewById(R.id.textViewButtonOK);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView buttonDefault = (TextView)mView.findViewById(R.id.textViewButtonDefault);
        buttonDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanSelection();
            }
        });
    }

    private void cleanChildren(ViewGroup viewGroup) {
        for(int i = 0; i < viewGroup.getChildCount(); i ++) {
            View v = viewGroup.getChildAt(i);
            v.setSelected(false);
        }
    }

    private void cleanSelection() {
        ScrollView container = (ScrollView)mView.findViewById(R.id.scrollViewFilterContainer);
        cleanChildren(container);
    }
}
