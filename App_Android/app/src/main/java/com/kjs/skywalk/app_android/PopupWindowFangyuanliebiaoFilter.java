package com.kjs.skywalk.app_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupWindow;

import java.util.ArrayList;

/**
 * Created by Jackie on 2017/2/10.
 */

class PopupWindowFangyuanliebiaoFilter extends PopupWindow {
    private Context mContext = null;
    private View mView = null;

    public PopupWindowFangyuanliebiaoFilter(Context context) {
        super(context);
        mContext = context;

        mView = LayoutInflater.from(context).inflate(R.layout.popup_window_fangyuanliebiao_filter, null);
        setContentView(mView);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public View getView() {
        return mView;
    }

    public ArrayList<Integer> getSelectedRooms() {
        ArrayList<Integer> list = new ArrayList<>();
        CheckBox room1 = (CheckBox)mView.findViewById(R.id.radio1);
        CheckBox room2 = (CheckBox)mView.findViewById(R.id.radio2);
        CheckBox room3 = (CheckBox)mView.findViewById(R.id.radio3);
        CheckBox room4 = (CheckBox)mView.findViewById(R.id.radio4);
        CheckBox room5 = (CheckBox)mView.findViewById(R.id.radio5);

        if(room1.isChecked()) {
            list.add(1);
        }
        if(room2.isChecked()) {
            list.add(2);
        }
        if(room3.isChecked()) {
            list.add(3);
        }
        if(room4.isChecked()) {
            list.add(4);
        }
        if(room5.isChecked()) {
            list.add(5);
        }

        return list;
    }
}
