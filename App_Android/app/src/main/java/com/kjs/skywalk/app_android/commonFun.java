package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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
    public static void setHouseTagStyleByIdWithoutSetting(TextView textview, String text, int tagId) {
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

        textview.setText(text);
        textview.setGravity(Gravity.CENTER);
    }
    public static void setHouseTagStyleById(TextView textview, String text, int tagId) {
        setHouseTagStyleByIdWithoutSetting(textview, text, tagId);
        // base setting
        textview.setTextSize(11);       // setTextSize --- sp   getTextSize --- pixel
        textview.setPadding(30, 15, 30, 15);        // must set padding after setBackgroundResource
    }


    public static void displayImageByURL(Context context, String URL, ImageView view, int placeHolderResourceId, int errorResourceId) {
        if (context == null || URL == null || view == null) {
            return;
        }

        Glide.with(context).load(URL).placeholder(placeHolderResourceId).error(errorResourceId).into(view);
    }

    public static void displayImageByURL(Context context, String URL, ImageView view) {
        displayImageByURL(context, URL, view, R.drawable.touxiang, R.drawable.touxiang);
    }

    public static void displayImageWithMask(Context context, ImageView view, int srcImageResId, int maskImageResId) {
        Bitmap srcBmp = BitmapFactory.decodeResource(context.getResources(), srcImageResId);
        Bitmap maskBmp = BitmapFactory.decodeResource(context.getResources(), maskImageResId);
        displayImageWithMask(context, view, srcBmp, maskBmp);
    }

    public static void displayImageWithMask(final Context context, final ImageView view, String srcImageUrl, final int maskImageResId) {
        Glide.with(context).load(srcImageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Bitmap maskBmp = BitmapFactory.decodeResource(context.getResources(), maskImageResId);
                displayImageWithMask(context, view, resource, maskBmp);
            }
        });
    }

    public static void displayImageWithMask(Context context, ImageView view, Bitmap srcBmp, Bitmap maskBmp) {
        Bitmap imgResult = Bitmap.createBitmap(maskBmp.getWidth(), maskBmp.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(imgResult);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(srcBmp, 0, 0, null);
        canvas.drawBitmap(maskBmp, 0, 0, paint);
        paint.setXfermode(null);
        view.setImageBitmap(imgResult);
        view.setScaleType(ImageView.ScaleType.CENTER);
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

    // get house type
    public static String getHouseTypeString(int bedrooms, int livingrooms, int bathrooms) {
        String houseType = "";

        if (bedrooms != 0) {
            houseType += "" + bedrooms + "室";
        }

        if (livingrooms != 0) {
            houseType += "" + livingrooms + "厅";
        }

        if (bathrooms != 0) {
            houseType += "" + bathrooms + "卫";
        }
        return houseType;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Rect getScreenResolution(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density  = dm.density;
        int densityDPI = dm.densityDpi;

        int screenWidthDip = dm.widthPixels;
        int screenHeightDip = dm.heightPixels;

        int screenWidth  = (int)(dm.widthPixels * density + 0.5f);      // 屏幕宽（px，如：480px）
        int screenHeight = (int)(dm.heightPixels * density + 0.5f);     // 屏幕高（px，如：800px）

        kjsLogUtil.i("Screen Width: " + screenWidth + " Screen Height: " + screenHeight);

        Rect rc = new Rect();
        rc.right = dm.widthPixels;
        rc.bottom = dm.heightPixels;

        return rc;
    }

    public static int getStatusBarHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    // password verify
    public static boolean verifyPassword(String paasword) {
        // 检测密码由6-16个字母和数字组成，可以是纯数字或纯字母
        boolean bMatch = paasword.matches("^[0-9a-zA-Z]{6,16}$");

        // 检测密码由6-16个字母和数字组成，不可以是纯数字或纯字母
//        boolean bMatch = paasword.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");

        return bMatch;
    }

    public void showSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public void hideSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    //update text view drawable
    public static void setTextViewDrawableLeft(Context context, TextView view, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
        view.setTextColor(ContextCompat.getColor(context, R.color.colorFontSelected));
        view.setSelected(true);
    }
    public static void setTextViewDrawableTop(Context context, TextView view, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, drawable, null, null);
        view.setTextColor(ContextCompat.getColor(context, R.color.colorFontSelected));
        view.setSelected(true);
    }
    public static void setTextViewDrawableRight(Context context, TextView view, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setTextColor(ContextCompat.getColor(context, R.color.colorFontSelected));
        view.setSelected(true);
    }
    public static void setTextViewDrawableBottom(Context context, TextView view, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, null, drawable);
        view.setTextColor(ContextCompat.getColor(context, R.color.colorFontSelected));
        view.setSelected(true);
    }
    public static void cleanTextViewDrawable(TextView view) {
        view.setCompoundDrawables(null, null, null, null);
    }

    public static String getErrorDescriptionByErrorCode(int errorCode) {
        String error = "";
        switch (errorCode) {
            case 0x451:
                error = "房源重复";
                break;
        }

        return error;
    }
}
