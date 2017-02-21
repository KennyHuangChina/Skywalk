package com.kjs.skywalk.app_android.Apartment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.kjsLogUtil;

/**
 * Created by admin on 2017/2/9.
 */

class PopupWindowSearchConditionHouseType extends PopupWindow {
    private Context mContext = null;

    public PopupWindowSearchConditionHouseType(Context context) {
        super(context);
        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_search_condition_house_type,null);
        setContentView(view);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        load();
        Button buttonOk = (Button)view.findViewById(R.id.buttonOK);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                dismiss();
            }
        });
        init();
    }

    private void init() {
        //initialize selected condition
    }

    private void save() {
        //save current condition
    }

    private void load() {
        //load saved condition
    }

}
