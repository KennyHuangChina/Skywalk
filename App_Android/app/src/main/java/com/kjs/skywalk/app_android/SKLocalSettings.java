package com.kjs.skywalk.app_android;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/22.
 */

public class SKLocalSettings {
    public static final int SETTING_TYPE_SEARCH_CONDITION = 0;

    private static Context mContext = null;
    private static SKLocalSettings mLocalSettings = null;
    private static String mSPFile = "AdvancedSearchCondition";

    private static String PREF_UISETTINGS = "PrefUiSettings";

    private static SearchConditionPrice mSearchConditionSettings = null;

    public SKLocalSettings(Context context) {
        mContext = context;

        mSearchConditionSettings = new SearchConditionPrice(context);
    }

    public static synchronized SKLocalSettings getInstance(Context context) {
        if(mLocalSettings == null) {
            mLocalSettings = new SKLocalSettings(context);
        }

        return mLocalSettings;
    }

    public static Object get(int what) {
        Object obj = null;
        switch (what) {
            case SETTING_TYPE_SEARCH_CONDITION:
                return mSearchConditionSettings;
        }

        return obj;
    }

    public class SearchConditionPrice {
        private Context mContext = null;
        private final String TAG = "SearchConditionSettings";

        public static final int PRICE_NOLIMIT = 0;
        public static final int PRICE_100 = 1;
        public static final int PRICE_100_150 = 2;
        public static final int PRICE_150_200 = 3;
        public static final int PRICE_200_300 = 4;

        public int mPriceTypeFlag = 0; //0: preset 1: user defined
        public int mPriceIndex = 0;
        public int mMinPrice = 0;
        public int mMaxPrice = 0;

        public SearchConditionPrice(Context context) {
            mContext = context;
            SharedPreferences sp = context.getSharedPreferences(mSPFile, 0);
            //get price setting
            mPriceTypeFlag = sp.getInt("PriceTypeFlag", 0);
            Log.i(TAG, "Price Type Flag = " + mPriceTypeFlag);
            if(mPriceTypeFlag == 0) {
                mPriceIndex = sp.getInt("PriceIndex", 0);
                Log.i(TAG, "Price Index = " + mPriceIndex);
            } else if(mPriceTypeFlag == 1) {
                mMinPrice = sp.getInt("MinPrice", 0);
                mMinPrice = sp.getInt("MaxPrice", 300);
                Log.i(TAG, "Min Price = " + mMinPrice);
                Log.i(TAG, "Max Price = " + mMaxPrice);
            }
        }

        public void save() {
            SharedPreferences.Editor editor = mContext.getSharedPreferences(mSPFile, 0).edit();
            //save price setting
            editor.putInt("PriceTypeFlag", mPriceTypeFlag);
            if(mPriceTypeFlag == 0) {
                editor.putInt("PriceIndex", mPriceIndex);
            } else if(mPriceTypeFlag == 1) {
                editor.putInt("MinPrice", mMinPrice);
                editor.putInt("MaxPrice", mMaxPrice);
            }


            editor.commit();
        }
    }

    // UI Settings
    public static final String UISettingsKey_LoginMode = "login_mode";       // telephone account
    public static final String UISettingsKey_LoginStatus = "login_status";       // telephone account
    public static final String UISettingsKey_BrowsingHistory = "browsing_history";       // browsing_history

    public static void UISettings_set(Context context, String key, String value) {
        SharedPreferences.Editor editor;
        try {
            SharedPreferences sysPref = context.getSharedPreferences(PREF_UISETTINGS, 0);
            editor = sysPref.edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String UISettings_get(Context context, String key, String defaultVaule) {
        SharedPreferences sysPref = context.getSharedPreferences(PREF_UISETTINGS, 0);
        return sysPref.getString(key, defaultVaule);
    }

    public static void UISettings_set(Context context, String key, boolean bValue) {
        SharedPreferences.Editor editor;
        try {
            SharedPreferences sysPref = context.getSharedPreferences(PREF_UISETTINGS, 0);
            editor = sysPref.edit();
            editor.putBoolean(key, bValue);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean UISettings_get(Context context, String key, boolean bDefaultVaule) {
        SharedPreferences sysPref = context.getSharedPreferences(PREF_UISETTINGS, 0);
        return sysPref.getBoolean(key, bDefaultVaule);
    }

    // 浏览记录
    public static List<String> browsing_history_read(Context context) {
        String history = UISettings_get(context, UISettingsKey_BrowsingHistory, "");
        String[] historys = history.split(";");
        List<String> list = new ArrayList<>();
        if (historys.length > 0) {
            for (int i = 0; i < historys.length; i++) {
                list.add(historys[i]);
            }
        }

        return list;
    }

    public static int BROWSING_HISTORY_LENGTH = 3;
    public static void browsing_history_insert(Context context, String value) {
        String history = UISettings_get(context, UISettingsKey_BrowsingHistory, "");
        if (history.isEmpty()) {
            UISettings_set(context, UISettingsKey_BrowsingHistory, value);
            return;
        }

        String[] historys = history.split(";");
        for (int i = 0; i < historys.length; i++) {
            if (historys[i].equals(value))
                return;     // repeat
        }

        if (historys.length < BROWSING_HISTORY_LENGTH) {
            UISettings_set(context, UISettingsKey_BrowsingHistory, value + ";" + history);
        } else {
            UISettings_set(context, UISettingsKey_BrowsingHistory, value + ";" + history.substring(0, history.lastIndexOf(";")));
        }
    }
}
