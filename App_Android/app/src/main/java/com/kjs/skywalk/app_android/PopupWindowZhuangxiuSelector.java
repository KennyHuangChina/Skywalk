package com.kjs.skywalk.app_android;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Jackie on 2017/2/10.
 */

class PopupWindowZhuangxiuSelector extends PopupWindow {
    private Context mContext = null;
    private View mView = null;

    public PopupWindowZhuangxiuSelector(Context context) {
        super(context);
        mContext = context;

        mView = LayoutInflater.from(context).inflate(R.layout.popup_window_zhuangxiu_selector, null);
        setContentView(mView);
        setFocusable(true);
        setTouchable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        setAnimationStyle(R.style.popupAnimation);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView viewMaopi =(TextView)mView.findViewById(R.id.textViewMaopi);

        mView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                TextView view = (TextView)v;
                String text = view.getText().toString();
                Log.i(getClass().getSimpleName().toString(), text);
                dismiss();
                return true;
            }
        });
    }

    public View getView() {
        return mView;
    }
}
