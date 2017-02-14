package com.kjs.skywalk.app_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

    public static final Bitmap loadImageFromUrl(String imgUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    ///////////////////////////////////////////////////////////////////////////
    // show toast info
    ///////////////////////////////////////////////////////////////////////////

    public static void showToast_resEntryName(Context context, View v) {
        String strVName = v.getResources().getResourceEntryName(v.getId());
        Toast.makeText(context, "res entry name: " + strVName, Toast.LENGTH_SHORT).show();
    }

    public static void showToast_resId(Context context, View v) {
        Toast.makeText(context, String.format("res id: %d(%#x)", v.getId(), v.getId()), Toast.LENGTH_SHORT).show();
    }

    public static void showToast_resIag(Context context, View v) {
        Toast.makeText(context, "res tag: " + v.getTag(), Toast.LENGTH_SHORT).show();
    }
}
