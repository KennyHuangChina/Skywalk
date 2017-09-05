package com.kjs.skywalk.app_android.Apartment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    public ArrayList<Integer> getRoomSelection() {
        TextView v0 = (TextView) mView.findViewById(R.id.textViewHouseType0);
        TextView v1 = (TextView)mView.findViewById(R.id.textViewHouseType1);
        TextView v2 = (TextView)mView.findViewById(R.id.textViewHouseType2);
        TextView v3 = (TextView)mView.findViewById(R.id.textViewHouseType3);
        TextView v4 = (TextView)mView.findViewById(R.id.textViewHouseType4);
        TextView v5 = (TextView)mView.findViewById(R.id.textViewHouseType5);

        ArrayList<Integer> list = new ArrayList<>();
        if(v0.isSelected()) {
            return list;
        }

        if(v1.isSelected()) {
            list.add(1);
        }
        if(v2.isSelected()) {
            list.add(2);
        }
        if(v3.isSelected()) {
            list.add(3);
        }
        if(v4.isSelected()) {
            list.add(4);
        }
        if(v5.isSelected()) {
            list.add(5);
        }

        return list;
    }

    private void select(int index) {
        TextView v0 = (TextView) mView.findViewById(R.id.textViewHouseType0);
        TextView v1 = (TextView)mView.findViewById(R.id.textViewHouseType1);
        TextView v2 = (TextView)mView.findViewById(R.id.textViewHouseType2);
        TextView v3 = (TextView)mView.findViewById(R.id.textViewHouseType3);
        TextView v4 = (TextView)mView.findViewById(R.id.textViewHouseType4);
        TextView v5 = (TextView)mView.findViewById(R.id.textViewHouseType5);

        TextView v;
        switch (index) {
            case 0: {
                v = v0;
                break;
            }
            case 1: {
                v = v1;
                break;
            }
            case 2: {
                v = v2;
                break;
            }
            case 3: {
                v = v3;
                break;
            }
            case 4: {
                v = v4;
                break;
            }
            case 5: {
                v = v5;
                break;
            }
            default:
                return;
        }

        commonFun.setTextViewDrawableRight(mContext, v, R.drawable.select4_check);
        v.setTextColor(ContextCompat.getColor(mContext, R.color.colorFontSelected));
        v.setSelected(true);
    }

    private void unselect(int index) {
        TextView v0 = (TextView) mView.findViewById(R.id.textViewHouseType0);
        TextView v1 = (TextView)mView.findViewById(R.id.textViewHouseType1);
        TextView v2 = (TextView)mView.findViewById(R.id.textViewHouseType2);
        TextView v3 = (TextView)mView.findViewById(R.id.textViewHouseType3);
        TextView v4 = (TextView)mView.findViewById(R.id.textViewHouseType4);
        TextView v5 = (TextView)mView.findViewById(R.id.textViewHouseType5);

        TextView v;
        switch (index) {
            case 0: {
                v = v0;
                break;
            }
            case 1: {
                v = v1;
                break;
            }
            case 2: {
                v = v2;
                break;
            }
            case 3: {
                v = v3;
                break;
            }
            case 4: {
                v = v4;
                break;
            }
            case 5: {
                v = v5;
                break;
            }
            default:
                return;
        }

        commonFun.cleanTextViewDrawable(v);
        v.setTextColor(Color.rgb(0, 0, 0));
        v.setSelected(false);
    }

    private void init() {
        //initialize selected condition
        cleanSelection();
        select(0);
    }

    private void save() {
        //save current condition
    }
    private void cleanSelection() {
        unselect(0);
        unselect(1);
        unselect(2);
        unselect(3);
        unselect(4);
        unselect(5);
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
        Object obj = view.getTag();
        if(obj == null) {
            return;
        }

        int tag = Integer.valueOf((String)obj);
        if(tag == 0) {
            if(!view.isSelected()) {
                cleanSelection();
                select(0);
                return;
            }
        }

        if(view.isSelected()) {
            unselect(tag);
        } else {
            select(tag);
        }

        if(getSelectedItemCount() == 0) {
            select(0);
            return;
        }

        View v = (View)mView.findViewById(R.id.textViewHouseType0);
        if(view.getId() != R.id.textViewHouseType0) {
            if(v.isSelected()) {
                unselect(0);
            }
        }
    }

}
