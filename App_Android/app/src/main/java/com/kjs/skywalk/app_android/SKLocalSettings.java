package com.kjs.skywalk.app_android;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by admin on 2017/2/22.
 */

public class SKLocalSettings {
    public static final int SETTING_TYPE_SEARCH_CONDITION = 0;

    private static Context mContext = null;
    private static SKLocalSettings mLocalSettings = null;
    private static String mSPFile = "AdvancedSearchCondition";

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
}
