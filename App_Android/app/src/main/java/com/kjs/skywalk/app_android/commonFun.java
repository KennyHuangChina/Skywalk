package com.kjs.skywalk.app_android;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kjs.skywalk.app_android.Apartment.Activity_ApartmentDetail;

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
import java.util.List;

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

    public static Bitmap getScaleBitmapFromLocal(String path, int dstW, int dstH) {
        Bitmap bmp = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true; // allowing the caller to query the bitmap without having to allocate the memory for its pixels.
        BitmapFactory.decodeFile(path, opt);
        int picW = opt.outWidth;
        int picH = opt.outHeight;

        opt.inSampleSize = 1;
        if (picW > dstW || picH > dstH) {
            if (picW * dstH > picH * dstW) {
                opt.inSampleSize = picH / dstH;
            } else {
                opt.inSampleSize = picW / dstW;
            }
        }
        opt.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, opt);

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

    public static Drawable getDrawableFromUrl(Context context, String imgUrl) {
        Bitmap bitmap = loadImageFromUrl(imgUrl);
        BitmapDrawable bd = null;

        if (bitmap != null) {
            bd = new BitmapDrawable(context.getResources(), bitmap);
        }

        return bd;
    }

    public static void copyAssets(Context context, String pathInAssets, String dstPath) {
        File dir = new File(dstPath + File.separator + pathInAssets);
        if (dir.exists() && dir.isDirectory()) {
            return;
        }
        dir.mkdir();

        String[] files = null;
        try {
            files = context.getAssets().list(pathInAssets);
        } catch (IOException e) {
            kjsLogUtil.e("Failed to get asset file list." + e);
        }

        for (String filename : files) {
            InputStream in;
            OutputStream out;
            try {
                in = context.getAssets().open(pathInAssets + File.separator + filename);
                File outFile = new File(dstPath + File.separator + pathInAssets + File.separator + filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                kjsLogUtil.e("Failed to get asset file list." + e);
            }
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
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

    public static String getTextOnSearchView(SearchView v) {
        int id = v.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) v.findViewById(id);
        return textView.getText().toString();
    }

    public static void hideSoftKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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

    ///////////////////////////////////////////////////////////////////////////
    // translate tag id
    ///////////////////////////////////////////////////////////////////////////
    public static void setHouseTagStyleById(TextView textview, String text, int tagId) {
        switch (tagId) {
            case 1:
            {
                textview.setBackgroundResource(R.drawable.rounded_rect_background1);
                textview.setTextColor(Color.parseColor("#32BE84"));
                break;
            }
            case 2:
            {
                textview.setBackgroundResource(R.drawable.rounded_rect_background2);
                textview.setTextColor(Color.parseColor("#4CA3FF"));
                break;
            }
            case 3:
            {
                textview.setBackgroundResource(R.drawable.rounded_rect_background3);
                textview.setTextColor(Color.parseColor("#FF3F29"));
                break;
            }
            case 4:
            {
                textview.setBackgroundResource(R.drawable.rounded_rect_background4);
                textview.setTextColor(Color.parseColor("#8D7DEF"));
                break;
            }
            case 5:
            {
                textview.setBackgroundResource(R.drawable.rounded_rect_background5);
                textview.setTextColor(Color.parseColor("#EA0233"));
                break;
            }
            default:
            {
                textview.setBackgroundResource(R.drawable.rounded_rect_background1);
                textview.setTextColor(Color.parseColor("#32BE84"));
                break;
            }
        }

        // base setting
        textview.setText(text);
        textview.setTextSize(11);       // setTextSize --- sp   getTextSize --- pixel
        textview.setGravity(Gravity.CENTER);
        textview.setPadding(30, 15, 30, 15);        // must set padding after setBackgroundResource
    }


    public static void displayImageByURL(Context context, String URL, ImageView view, int placeHolderResourceId, int errorResourceId) {
        if (context == null || URL == null || URL.isEmpty() || view == null) {
            return;
        }

        Glide.with(context).load(URL).placeholder(placeHolderResourceId).error(errorResourceId).into(view);
    }

    public static void displayImageByURL(Context context, String URL, ImageView view) {
        displayImageByURL(context, URL, view, R.drawable.touxiang, R.drawable.touxiang);
    }

    public static void startActivityWithHouseId(Context context, Class<?> ActivityClass, int houseId) {
        Intent intent = new Intent(context, ActivityClass);
        intent.putExtra(ClassDefine.IntentExtraKeyValue.KEY_HOUSE_ID, houseId);
//        Bundle bundle = new Bundle();
//        bundle.putInt(ClassDefine.IntentExtraKeyValue.KEY_HOUSE_ID, houseId);
//        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class TextDefine {
        private String mText;
        private int mTextSize;          // pixel
        private int mTextColor;

        public TextDefine(String text, int textSize, int textColor) {
            mTextSize = textSize;
            mTextColor = textColor;
            mText = text;
        }
    }
    // get SpannableString
    public static SpannableStringBuilder getSpannableString(List<TextDefine> textDefines) {
        int start = 0;
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        for (TextDefine textDef : textDefines) {
            spannableString.append(textDef.mText);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(textDef.mTextColor);
            AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(textDef.mTextSize);
            spannableString.setSpan(colorSpan, start, start + textDef.mText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(sizeSpan, start, start + textDef.mText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            start += textDef.mText.length();
        }

        return spannableString;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
}
