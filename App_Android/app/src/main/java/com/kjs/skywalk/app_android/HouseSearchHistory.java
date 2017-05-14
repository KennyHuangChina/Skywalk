package com.kjs.skywalk.app_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by admin on 2017/5/14.
 */

public class HouseSearchHistory {
    private Context mContext = null;
    private ArrayList<String> mList = new ArrayList<String>();
    private int mMaxRecord = 5;
    private final String SharedFile = "HouseSearchHistory";
    private final String TAG = "HouseSearchHistory";

    HouseSearchHistory(Context context) {
        mContext = context;

        loadHistory();
    }

    public int getCount() {
        return mList.size();
    }

    public void addHistory(String history) {
        if(history == null || history.isEmpty()) {
            return;
        }

        if(mList.size() >= mMaxRecord) {
            mList.remove(mMaxRecord - 1);
        }

        mList.add(history);

        saveHistory();
    }

    public void cleanHistory() {
        mList.clear();

        saveHistory();
    }

    public boolean searched(String string) {
        for(int i = 0; i < mList.size(); i ++) {
            if(mList.get(i).contains(string)) {
                return true;
            }
        }

        return false;
    }

    private void loadHistory() {
        SharedPreferences sharedData = mContext.getSharedPreferences(SharedFile, 0);
        String data = sharedData.getString("History", null);

        if(data == null) {
            return;
        }

        String s[] = data.split(";");
        for(int i = 0; i < s.length; i ++) {
            if(s[i].isEmpty()) {
                continue;
            }

            mList.add(s[i]);
        }

        for(int j = 0; j < mList.size(); j ++) {
            Log.i(TAG, mList.get(j));
        }
    }

    private void saveHistory() {
        if(mList.size() == 0) {
            return;
        }

        SharedPreferences.Editor sharedData = mContext.getSharedPreferences(SharedFile, 0).edit();
        String history = "";
        for(int i = 0; i < mList.size(); i ++) {
            history += mList.get(i);
            history += ";";
        }

        sharedData.putString("History", history);
        sharedData.commit();
    }
}
