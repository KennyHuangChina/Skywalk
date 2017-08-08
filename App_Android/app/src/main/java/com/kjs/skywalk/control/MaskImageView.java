package com.kjs.skywalk.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.kjs.skywalk.app_android.R;

/**
 * Created by sailor.zhou on 2017/8/8.
 */

public class MaskImageView extends ImageView {
    public MaskImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaskImage, 0, 0);
        int imageResId = typedArray.getResourceId(R.styleable.MaskImage_image, 0);
        int maskResId = typedArray.getResourceId(R.styleable.MaskImage_mask, 0);
        if (imageResId == 0 || maskResId == 0)
            return;

        Bitmap imgOrg = BitmapFactory.decodeResource(getResources(), imageResId);
        Bitmap mask = BitmapFactory.decodeResource(getResources(), maskResId);
        Bitmap imgResult = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(imgResult);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(imgOrg, 0, 0, null);
        canvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        this.setImageBitmap(imgResult);
        this.setScaleType(ScaleType.CENTER);

        typedArray.recycle();
    }
}
