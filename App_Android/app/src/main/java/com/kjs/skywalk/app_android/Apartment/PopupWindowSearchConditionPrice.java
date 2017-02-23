package com.kjs.skywalk.app_android.Apartment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.SKLocalSettings;

/**
 * Created by Jackie on 2017/2/9.
 */

class PopupWindowSearchConditionPrice extends PopupWindow {
    private Context mContext = null;
    private SKLocalSettings mLocalSettings = null;
    private SKLocalSettings.SearchConditionPrice mSettings = null;
    private View mView = null;

    public PopupWindowSearchConditionPrice(Context context) {
        super(context);
        mContext = context;
//        mLocalSettings = SKLocalSettings.getInstance(context);
//        mSettings = (SKLocalSettings.SearchConditionSettings)mLocalSettings.get(SKLocalSettings.SETTING_TYPE_SEARCH_CONDITION);

        mView = LayoutInflater.from(context).inflate(R.layout.popup_window_search_condition_price, null);
        setContentView(mView);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                //mSettings.save();
            }
        });

        Button buttonOk = (Button)mView.findViewById(R.id.buttonOK);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mSettings.mPriceTypeFlag = 1;
                dismiss();
            }
        });

        init();
    }

    public void setCurrentSelection(View v) {
        mSettings.mPriceTypeFlag = 0;
        Object obj = v.getTag();
        if(obj == null) {
            return;
        }

        int tag = Integer.valueOf((String)obj);
        mSettings.mPriceIndex = tag;
    }

    private void cleanSelection() {
        LinearLayout container = (LinearLayout)mView.findViewById(R.id.price_container);
        for(int i = 0; i < container.getChildCount(); i ++) {
            View v = container.getChildAt(i);
            v.setSelected(false);
        }
    }

    private void selectViewByIndex(int index) {
        LinearLayout container = (LinearLayout)mView.findViewById(R.id.price_container);
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

    private void init() {
        //initialize selected condition
        cleanSelection();
        selectViewByIndex(0);
        /*
        if(mSettings.mPriceTypeFlag == 0) {
            int index = mSettings.mPriceIndex;
            selectViewByIndex(index);
        } else if(mSettings.mPriceTypeFlag == 1) {
            EditText textMin = (EditText)mView.findViewById(R.id.editTextLowPrice);
            EditText textMax = (EditText)mView.findViewById(R.id.editTextHighPrice);

            textMin.setText(mSettings.mMinPrice);
            textMax.setText(mSettings.mMaxPrice);
        }
        */
    }
}
