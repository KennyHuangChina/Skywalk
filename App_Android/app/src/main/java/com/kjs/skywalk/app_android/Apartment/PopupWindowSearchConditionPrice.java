package com.kjs.skywalk.app_android.Apartment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.SKLocalSettings;
import com.kjs.skywalk.app_android.commonFun;

import org.w3c.dom.Text;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jackie on 2017/2/9.
 */

class PopupWindowSearchConditionPrice extends PopupWindow {
    private Context mContext = null;
    private SKLocalSettings mLocalSettings = null;
    private SKLocalSettings.SearchConditionPrice mSettings = null;
    private View mView = null;

    private final int MSG_DISMISS_WINDOW = 0;

    public int mMinPrice = 0;
    public int mMaxPrice = 0;

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
                EditText textMin = (EditText)mView.findViewById(R.id.editTextLowPrice);
                EditText textMax = (EditText)mView.findViewById(R.id.editTextHighPrice);
                String min = textMin.getText().toString();
                String max = textMax.getText().toString();

                if(min.isEmpty()) {
                    min = "0";
                }

                if(max.isEmpty()) {
                    max = "0";
                }
                mMinPrice = Integer.valueOf(min);
                mMaxPrice = Integer.valueOf(max);

//                mSettings.mPriceTypeFlag = 1;
                cleanSelection();
                mHandler.sendEmptyMessageDelayed(MSG_DISMISS_WINDOW, 200);
            }
        });

        init();
    }

    public void onItemClicked(View view) {
        ViewGroup parent = (ViewGroup)view.getParent();
        for(int i = 0; i < parent.getChildCount(); i ++) {
            View v = parent.getChildAt(i);
            Object obj = v.getTag();
            if(obj == null) {
                continue;
            }
            if(v.getId() == view.getId()) {
                select((TextView)v);
                setPrices(view);
                //mPopSearchConditionPrice.setCurrentSelection(v);
            } else {
                unselect((TextView)v);
            }
        }

        EditText textMin = (EditText)mView.findViewById(R.id.editTextLowPrice);
        EditText textMax = (EditText)mView.findViewById(R.id.editTextHighPrice);

        textMin.setText("");
        textMax.setText("");

        mHandler.sendEmptyMessageDelayed(MSG_DISMISS_WINDOW, 200);
    }

    private void setPrices(View v) {
        Object obj = v.getTag();
        if(obj == null) {
            return;
        }

        int tag = Integer.valueOf((String)obj);
        switch (tag) {
            case 0:
                mMinPrice = 0;
                mMaxPrice = 0;
                break;
            case 1:
                mMinPrice = 1;
                mMaxPrice = 1000;
                break;
            case 2:
                mMinPrice = 1000;
                mMaxPrice = 1500;
                break;
            case 3:
                mMinPrice = 1500;
                mMaxPrice = 2000;
                break;
            case 4:
                mMinPrice = 2000;
                mMaxPrice = 3000;
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_DISMISS_WINDOW:
                    dismiss();
                    break;
            }
        }
    };

    public void setCurrentSelection(View v) {
        mSettings.mPriceTypeFlag = 0;
        Object obj = v.getTag();
        if(obj == null) {
            return;
        }

        int tag = Integer.valueOf((String)obj);
        mSettings.mPriceIndex = tag;
    }

    private void unselect(TextView view) {
        view.setCompoundDrawables(null, null, null, null);
        view.setTextColor(Color.rgb(0, 0, 0));
        view.setSelected(false);
    }

    private void select(TextView view) {
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.select4_check);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setTextColor(ContextCompat.getColor(mContext, R.color.colorFontSelected));
        view.setSelected(true);
    }

    private void cleanSelection() {
        LinearLayout container = (LinearLayout)mView.findViewById(R.id.price_container);
        for(int i = 0; i < container.getChildCount(); i ++) {
            View v = container.getChildAt(i);
            Object obj = v.getTag();
            if(obj == null) {
                continue;
            }
            unselect((TextView)v);
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
                select((TextView)v);
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
