package com.kjs.skywalk.app_android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sailor.zhou on 2017/11/11.
 */

public class ProfileDBHelper extends SQLiteOpenHelper {
    private static final String TAG = ProfileDBHelper.class.getSimpleName();
    private static final int VERSION = 1;
    private String mTableName;

    public ProfileDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        Log.i(TAG, "ProfileDBHelper(Context, String, CursorFactory)");
    }

    public ProfileDBHelper(Context context, String user) {
        super(context, "/sdcard/profile.db", null, VERSION);
        mTableName = user + "_profile";
        Log.i(TAG, "ProfileDBHelper(Context), mTableName: " + mTableName);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "onUpgrade() " + oldVersion + " ==> " + newVersion);
        dropTable(db);
        createTable(db);
    }

    private void createTable(SQLiteDatabase db) {
//        String sql = "CREATE TABLE IF NOT EXISTS " +
//                mTableName +
//                "(" +
//                "id integer, create_time varchar(16), type varchar(16), body varchar(100), property varchar(100), building_no varchar(16) house_no varchar(16)" +
//                ")";
        String sql = "CREATE TABLE IF NOT EXISTS " +
                mTableName +
                "(" +
                "id integer primary key, create_time varchar(16), read_time varchar(16), type varchar(16), body varchar(100), property varchar(100), building_no varchar(16), house_no varchar(16)" +
                ")";
        db.execSQL(sql);
    }

    private void dropTable(SQLiteDatabase db) {
        db.execSQL("drop table " + mTableName);
    }

    public String getTableName() {
        return mTableName;
    }
}
