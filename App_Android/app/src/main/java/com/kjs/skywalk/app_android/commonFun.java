package com.kjs.skywalk.app_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import java.io.File;

/**
 * Created by sailor.zhou on 2017/2/2.
 */

public class commonFun {
    public static Bitmap getBitmapFromLocal(String path) {
        Bitmap bmp = null;

        File file = new File(path);
        if (file.exists()) {
            try {
                bmp = BitmapFactory.decodeFile(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bmp;
    }

    public static Drawable getDrawableFromLocal(Context context, String path) {
        BitmapDrawable bd = null;
        Bitmap bmp = getBitmapFromLocal(path);
        if (bmp != null) {
            bd = new BitmapDrawable(context.getResources(), bmp);
        }

        return bd;
    }

    public static void showToast_resEntryName(Context context, View v) {
        String strVName = v.getResources().getResourceEntryName(v.getId());
        Toast.makeText(context, "res entry name: " + strVName, Toast.LENGTH_SHORT).show();
    }

    public static void showToast_resId(Context context, View v) {
        Toast.makeText(context, "res id: " + v.getId(), Toast.LENGTH_SHORT).show();
    }

    public static void showToast_resIag(Context context, View v) {
        Toast.makeText(context, "res tag: " + v.getTag(), Toast.LENGTH_SHORT).show();
    }
}
