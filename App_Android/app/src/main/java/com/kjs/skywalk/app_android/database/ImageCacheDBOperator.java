package com.kjs.skywalk.app_android.database;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by admin on 2017/10/18.
 */

public class ImageCacheDBOperator {
    private static ImageCacheDBOperator mOperator = null;
    private Context mContext = null;
    private ImageCacheDBHelper mHelper = null;

    private ImageCacheDBOperator(Context context) {
        mContext = context;
        mHelper = new ImageCacheDBHelper(context);
    }

    public static ImageCacheDBOperator getOperator(Context context) {
        if(mOperator == null) {
            mOperator = new ImageCacheDBOperator(context);
        }

        return mOperator;
    }

    //上传成功后增加新图片，picture id & original picture md5
    public void addItem(int id, String md5) {

    }

    //通过md5检查图片是否有更新
    public boolean checkChange(int id, String md5) {
        return true;
    }

    //如果md5发生变化，表示图片已经更新，清除缓存
    //问题：图片更新， 先删除在更新id变化？
    public void cleanCache(int id) {

    }

    //images[0]: big images[1]: middle
    public void updateCache(int id, ArrayList<String> images) {

    }

    //删除某个id相关内容
    public void removeItem(int id) {

    }
}
