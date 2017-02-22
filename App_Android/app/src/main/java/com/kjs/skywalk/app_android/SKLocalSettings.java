package com.kjs.skywalk.app_android;

import android.animation.ObjectAnimator;
import android.content.Context;

/**
 * Created by admin on 2017/2/22.
 */

public class SKLocalSettings {
    public static final int SETTING_TYPE_SEARCH_CONDITION = 0;

    private static String mFileSearchCondition = "SPSearchCondition";
    private static Context mContext = null;
    private static SKLocalSettings mLocalSettings = null;

    private static SearchConditionSettings mSearchConditionSettings = null;

    public SKLocalSettings(Context context) {
        mContext = context;

        mSearchConditionSettings = new SearchConditionSettings(context);
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



    public static void set(Object obj) {
        if(obj == null) {
            return;
        }
        BaseSettingOperation settings = (BaseSettingOperation)obj;
        settings.save();
    }

    public interface Operation {
        void save();
    }

    public class BaseSettingOperation implements Operation{
        @Override
        public void save() {

        }
    }

    public class SearchConditionSettings extends BaseSettingOperation {
        private Context mContext = null;
        public SearchConditionSettings(Context context) {

        }
        public void save() {
            
        }
    }
}
