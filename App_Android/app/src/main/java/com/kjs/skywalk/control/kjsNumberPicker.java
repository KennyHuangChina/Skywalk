package com.kjs.skywalk.control;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

/**
 * Created by sailor.zhou on 2017/3/21.
 */

public class kjsNumberPicker extends NumberPicker {
    public kjsNumberPicker(Context context) {
        super(context);
    }

    public kjsNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public kjsNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setDividerColor(this);
    }

    @Override
    public void addView(View child) {
        addView(child, null);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        addView(child, -1, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setTextAttr(child);
    }

    public void setTextAttr(View view) {
        if(view instanceof EditText) {
            ((EditText)view).setTextColor(Color.parseColor("#242224"));
            ((EditText)view).setTextSize(14);
        }
    }

    public void setDividerColor() {
        setDividerColor(Color.parseColor("#EFEFEF"));
    }

    public void setDividerColor(int color) {
        NumberPicker picker = this;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for(Field pField : pickerFields) {
            if (pField.getName().equals("mSelectionDivider")) {
                pField.setAccessible(true);
                try {
                    pField.set(picker, new ColorDrawable(color));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
