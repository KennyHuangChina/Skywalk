package com.kjs.skywalk.app_android;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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

    public static void copyAssets( Context context, String pathInAssets, String dstPath ) {
        File dir = new File(dstPath + File.separator + pathInAssets);
        if(dir.exists() && dir.isDirectory()) {
            return;
        }
        dir.mkdir();

        String[] files = null;
        try {
            files = context.getAssets().list(pathInAssets);
        } catch (IOException e) {
            kjsLogUtil.e("Failed to get asset file list." + e);
        }

        for(String filename : files) {
            InputStream in;
            OutputStream out;
            try {
                in = context.getAssets().open(pathInAssets + File.separator + filename);
                File outFile = new File( dstPath + File.separator + pathInAssets + File.separator + filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch(IOException e) {
                kjsLogUtil.e("Failed to get asset file list." + e);
            }
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static ArrayList<String> getTestPicList(Context context) {
        ArrayList<String> fileLst = new ArrayList<>();
        File file = new File(context.getCacheDir().getAbsolutePath() + File.separator + "testPics" + File.separator);
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                fileLst.add(f.getAbsolutePath());
            }
        }

        return fileLst;
    }

    // change SharedPreferences save path
    private static void changeSharedPreferencesPath(Context context) {
        Field field;
        try {
            field = ContextWrapper.class.getDeclaredField("mBase");
            field.setAccessible(true);

            Object obj = field.get(context);

            field = obj.getClass().getDeclaredField("mPreferencesDir");
            field.setAccessible(true);

            File file = new File("/sdcard/sailortest");
            if (!file.exists()) {
                file.mkdir();
            }

            field.set(obj, file);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // show toast info
    ///////////////////////////////////////////////////////////////////////////

    public static void showToast_resEntryName(Context context, View v) {
        String strVName = v.getResources().getResourceEntryName(v.getId());
//        Toast.makeText(context, "res entry name: " + strVName, Toast.LENGTH_SHORT).show();
        Snackbar.make(v, "res entry name: " + strVName, Snackbar.LENGTH_SHORT).show();
    }

    public static void showToast_resId(Context context, View v) {
//        Toast.makeText(context, String.format("res id: %d(%#x)", v.getId(), v.getId()), Toast.LENGTH_SHORT).show();
        Snackbar.make(v, String.format("res id: %d(%#x)", v.getId(), v.getId()), Snackbar.LENGTH_SHORT).show();
    }

    public static void showToast_resIag(Context context, View v) {
//        Toast.makeText(context, "res tag: " + v.getTag(), Toast.LENGTH_SHORT).show();
        Snackbar.make(v, "res tag: " + v.getTag(), Snackbar.LENGTH_SHORT).show();
    }

    public static void showToast_info(Context context, View v, String info) {
//        Toast.makeText(context, "res tag: " + v.getTag(), Toast.LENGTH_SHORT).show();
        Snackbar.make(v, info, Snackbar.LENGTH_SHORT).show();
    }
}
