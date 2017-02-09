package com.kjs.skywalk.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sailor.zhou on 2017/1/13.
 */

public class TextView_DrawableRightCenter extends TextView {
    public TextView_DrawableRightCenter(Context context) {
        super(context);
    }

    public TextView_DrawableRightCenter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView_DrawableRightCenter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables(); // l, t, r, b
        if (drawables != null) {
            Drawable dRight = drawables[2];
            if (dRight != null) {
                float txtWidth = getPaint().measureText(getText().toString());
                int dPadding = getCompoundDrawablePadding();
                int dWidth = dRight.getIntrinsicWidth();
                float contentWidth = txtWidth + dWidth + dPadding;
                setPadding(0, 0, (int)(getWidth() - contentWidth), 0);
                canvas.translate((getWidth() - contentWidth) / 2, 0);
            }
        }
        super.onDraw(canvas);
    }
}
