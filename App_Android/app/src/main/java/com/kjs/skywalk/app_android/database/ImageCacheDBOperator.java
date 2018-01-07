package com.kjs.skywalk.app_android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kjs.skywalk.app_android.ClassDefine;

import java.util.ArrayList;

import static com.kjs.skywalk.communicationlibrary.IApiArgs.PIC_SIZE_Large;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.PIC_SIZE_Moderate;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.PIC_SIZE_Small;

/**
 * Created by admin on 2017/10/18.
 */

public class ImageCacheDBOperator {
    private static ImageCacheDBOperator mOperator = null;
    private Context mContext = null;
    private ImageCacheDBHelper mHelper = null;

    private ImageCacheDBOperator(Context context, String user) {
        mContext = context;
        String db_path = String.format("/sdcard/skywalk/image_cache_%s.db", user);
        mHelper = new ImageCacheDBHelper(context, db_path);
    }

    public static ImageCacheDBOperator getOperator(Context context, String user) {
        if(mOperator == null) {
            mOperator = new ImageCacheDBOperator(context, user);
        }

        return mOperator;
    }

    public void addItem(int hid, ClassDefine.PictureInfo picInfo) {
        if(hid <= 0 || picInfo == null) {
            return;
        }

        String sql_add = "replace into " + mHelper.getTableName() + " (_hid, _pid, _type, _md5, _path1, _path2, _path3, _path4)values (?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(sql_add, new Object[] { hid, picInfo.mId, picInfo.mType, picInfo.mCheckSum, picInfo.smallPicUrl, picInfo.middlePicUrl, picInfo.largePicUrl, ""});
        db.close();
    }

    //通过md5检查图片是否有更新
    public boolean checkChange(int id, String md5) {
        return true;
    }

    //images[0]: big images[1]: middle
    public void updateCache(int id, ArrayList<String> images) {

    }

    //删除某个id相关内容
    public void removeItem(int id) {

    }

    public ClassDefine.PictureInfo getCachedPicture(int hid, int pid, int type, int size) {
        ClassDefine.PictureInfo pic = new ClassDefine.PictureInfo();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String cmd = "SELECT * FROM " + mHelper.getTableName() + " WHERE" + " _hid=? AND _pid=? AND _type=?";
        Cursor cursor = db.rawQuery(cmd, new String[] {String.valueOf(hid), String.valueOf(pid), String.valueOf(type)});
        int count = cursor.getCount();
        if(count == 0) {
            return null;
        }
        if(cursor.moveToFirst()) {
            if(size == PIC_SIZE_Small) {
                pic.mId = pid;
                pic.largePicUrl = null;
                pic.middlePicUrl = null;
                pic.smallPicUrl = cursor.getString(4);
                pic.mType = type;
            } else if(size == PIC_SIZE_Moderate) {
                pic.mId = pid;
                pic.largePicUrl = null;
                pic.middlePicUrl = cursor.getString(5);
                pic.smallPicUrl = null;
                pic.mType = type;
            } else if(size == PIC_SIZE_Large) {
                pic.mId = pid;
                pic.largePicUrl = cursor.getString(6);
                pic.middlePicUrl = null;
                pic.smallPicUrl = null;
                pic.mType = type;
            } else {
                return null;
            }
        }
        return pic;
    }

    public boolean isPictureCached(int hid, ClassDefine.PictureInfo picInfo) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String cmd = "SELECT * FROM " + mHelper.getTableName() + " WHERE" + " _hid=?";
        Cursor cursor = db.rawQuery(cmd, new String[] {String.valueOf(hid)});
        int count = cursor.getCount();
        if(count == 0) {
            return false;
        }
        if(cursor.moveToFirst()) {
            do {
                int houseId = cursor.getInt(0);
                int picId = cursor.getInt(1);
                int type = cursor.getInt(2);
                String md5 = cursor.getString(3);
                if(houseId == hid && picId == picInfo.mId && type == picInfo.mType && md5.equals(picInfo.mCheckSum)) {
                    return true;
                }
            } while(cursor.moveToNext());
        }
        return false;
    }
}
