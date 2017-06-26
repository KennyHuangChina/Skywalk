package com.kjs.skywalk.control;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kjs.skywalk.app_android.ClassDefine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sailor.zhou on 2017/6/26.
 */

public class LinearLayout_AdaptiveText extends LinearLayout {
    private Context mContext;
    private int mActScreenWidth = 0;
    private int mActScreenHeight = 0;
    private static int DEFAULT_PADDING_LEFT = 25;
    private static int DEFAULT_PADDING_RIGHT = DEFAULT_PADDING_LEFT;
    private static int DEFAULT_PADDING_TOP = 18;
    private static int DEFAULT_PADDING_BOTTOM = DEFAULT_PADDING_TOP;
    private static int DEFAULT_MARGIN = 11;

    private int mPaddingLeft = DEFAULT_PADDING_LEFT;
    private int mPaddingRight = DEFAULT_PADDING_RIGHT;
    private int mPaddingTop = DEFAULT_PADDING_TOP;
    private int mPaddingBottom = DEFAULT_PADDING_BOTTOM;
    private int mMarginLeft = DEFAULT_MARGIN;
    private int mMarginRight = DEFAULT_MARGIN;
    private int mMarginTop = DEFAULT_MARGIN;
    private int mMarginBottom = DEFAULT_MARGIN;

    public LinearLayout_AdaptiveText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinearLayout_AdaptiveText(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        mActScreenWidth = metric.widthPixels;
        mActScreenHeight = metric.heightPixels;

        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.parseColor("#500000ff"));
    }

    public void setTextList(ArrayList<String> list) {
        removeAllViews();

        int totalUsed = 0;
        while (totalUsed < list.size()) {
            List<String> tmpList = list.subList(totalUsed, list.size());
            int count = addChildrens(tmpList);
            totalUsed += count;
        }
    }

    private int addChildrens(List<String> list) {
        LinearLayout layout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        int totalWidth = 0;
        int count = 0;
        for(int i = 0; i < list.size(); i ++) {
            ClassDefine.TextItem item = createTextItem(list.get(i));
            if((totalWidth + item.mTextViewWidth <= (mActScreenWidth - 100)) || (i == 0 && item.mTextViewWidth > (mActScreenWidth - 100))) {
                layout.addView(item.mView);
                totalWidth += item.mTextViewWidth;
                count += 1;
            } else {
                break;
            }
        }

        addView(layout);
        return count;
    }

    private ClassDefine.TextItem createTextItem(String text) {
        ClassDefine.TextItem item = new ClassDefine.TextItem();
        TextView textView = new TextView(mContext);
        textView.setText(text);

        textView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(mMarginLeft, mMarginTop, mMarginRight, mMarginBottom);
        textView.setLayoutParams(layoutParams);

        float size = textView.getPaint().measureText(text);
        item.mView = textView;
        item.mTextViewWidth = (int)size + mPaddingLeft + mPaddingRight + mMarginLeft + mMarginRight;
        return item;
    }

}
