package com.kjs.skywalk.app_android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kjs.skywalk.app_android.kjsLogUtil;

/**
 * Created by admin on 2017/10/15.
 */

public class ImageCacheDBHelper extends SQLiteOpenHelper {
    private static final int mVersion = 1;
    private static final String mTableName = "images";

    public ImageCacheDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ImageCacheDBHelper(Context context, String path){
        super(context, path, null, mVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " +
                mTableName +
                "(" +
                "_hid integer, _pid integer, _type integer, _md5 varchar(64), _path1 varchar(100), _path2 varchar(100), _path3 varchar(100), _path4 varchar(100)" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        kjsLogUtil.i("image cache database update: " + oldVersion + "-->" + newVersion);
        dropTable(db);
    }

    private void dropTable(SQLiteDatabase db) {
        String sql = "drop table " + mTableName + ";";
        db.execSQL(sql);
    }

    public String getTableName() {
        return mTableName;
    }
}
