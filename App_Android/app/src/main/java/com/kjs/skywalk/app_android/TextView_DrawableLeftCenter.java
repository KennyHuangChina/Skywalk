package com.kjs.skywalk.app_android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sailor.zhou on 2017/1/13.
 */

public class TextView_DrawableLeftCenter extends TextView {
    public TextView_DrawableLeftCenter(Context context) {
        super(context);
    }

    public TextView_DrawableLeftCenter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView_DrawableLeftCenter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables(); // l, t, r, b
        if (drawables != null) {
            Drawable dLeft = drawables[0];
            if (dLeft != null) {
                float txtWidth = getPaint().measureText(getText().toString());
                int dPadding = getCompoundDrawablePadding();
                int dWidth = dLeft.getIntrinsicWidth();
                float contentWidth = txtWidth + dWidth + dPadding;
                canvas.translate((getWidth() - contentWidth) / 2, 0);
            }
        }
        super.onDraw(canvas);
    }
}
