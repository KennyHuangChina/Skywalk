package com.kjs.skywalk.app_android.Apartment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.kjsLogUtil;

/**
 * Created by admin on 2017/2/9.
 */

class PopupWindowSearchConditionHouseType extends PopupWindow {
    private Context mContext = null;
    private View mView = null;
    public PopupWindowSearchConditionHouseType(Context context) {
        super(context);
        mContext = context;

        mView = LayoutInflater.from(context).inflate(R.layout.popup_window_search_condition_house_type,null);
        setContentView(mView);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        Button buttonOk = (Button)mView.findViewById(R.id.buttonOK);
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
        selectViewByIndex(0);
    }

    private void save() {
        //save current condition
    }
    private void cleanSelection() {
        LinearLayout container = (LinearLayout)mView.findViewById(R.id.houseTypeContainer);
        for(int i = 0; i < container.getChildCount(); i ++) {
            View v = container.getChildAt(i);
            v.setSelected(false);
        }
    }

    private int getSelectedItemCount() {
        View v0 = (View)mView.findViewById(R.id.textViewHouseType0);
        View v1 = (View)mView.findViewById(R.id.textViewHouseType1);
        View v2 = (View)mView.findViewById(R.id.textViewHouseType2);
        View v3 = (View)mView.findViewById(R.id.textViewHouseType3);
        View v4 = (View)mView.findViewById(R.id.textViewHouseType4);
        View v5 = (View)mView.findViewById(R.id.textViewHouseType5);

        int count = 0;
        if(v0.isSelected()) {
            count ++;
        }
        if(v1.isSelected()) {
            count ++;
        }
        if(v2.isSelected()) {
            count ++;
        }
        if(v3.isSelected()) {
            count ++;
        }
        if(v4.isSelected()) {
            count ++;
        }
        if(v5.isSelected()) {
            count ++;
        }

        return count;
    }

    public void onItemClicked(View view) {
        view.setSelected(!view.isSelected());

        if(getSelectedItemCount() == 0) {
            View v = (View)mView.findViewById(R.id.textViewHouseType0);
            v.setSelected(true);

            return;
        }

        View v = (View)mView.findViewById(R.id.textViewHouseType0);
        if(view.getId() != R.id.textViewHouseType0) {
            if(v.isSelected()) {
                v.setSelected(false);
            }
        } else {
            if(v.isSelected()) {
                cleanSelection();
                v.setSelected(true);
            }
        }
    }

    private void selectViewByIndex(int index) {
        LinearLayout container = (LinearLayout)mView.findViewById(R.id.houseTypeContainer);
        for(int i = 0; i < container.getChildCount(); i ++) {
            View v = container.getChildAt(i);
            Object obj = v.getTag();
            if(obj == null) {
                continue;
            }

            int tag = Integer.valueOf((String)obj);
            if(tag == index) {
                v.setSelected(true);
            }
        }
    }

}
