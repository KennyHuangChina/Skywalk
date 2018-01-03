package com.kjs.skywalk.app_android.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.Message.AdapterMessage;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by sailor.zhou on 2017/11/11.
 */

public class ProfileDBOperator {
    private static ProfileDBOperator mOperator = null;
    private Context mContext = null;
    private ProfileDBHelper mProfileDBHelper = null;

    private ProfileDBOperator(Context context, String user) {
        mContext = context;
        String db_path = String.format("/sdcard/user_%s/profile.db", user);
        mProfileDBHelper = new ProfileDBHelper(context, db_path);
    }

    public static ProfileDBOperator getOperator(Context context, String user) {
        if (mOperator == null) {
            mOperator = new ProfileDBOperator(context, user);
        }

        return mOperator;
    }

    // IApiResults.ISysMsgInfo msgInfo
    // insert or update
    public void update(ArrayList<Object> list) {
        for (Object object : list) {
            IApiResults.ISysMsgInfo msgInfo = (IApiResults.ISysMsgInfo) object;
            String sql_add = "replace into " + mProfileDBHelper.getTableName() + " (msg_id, house_id, ref_id, type, create_time, read_time, body, property, building_no, house_no)values (?,?,?,?,?,?,?,?,?,?)";
            SQLiteDatabase db = mProfileDBHelper.getWritableDatabase();
            db.execSQL(sql_add, new Object[] { msgInfo.MsgId(), msgInfo.HouseId(), msgInfo.RefId(), msgInfo.MsgType(), msgInfo.CreateTime(), msgInfo.ReadTime(), msgInfo.MsgBody(), msgInfo.Property(), msgInfo.BuildingNo(), msgInfo.HouseNo()});
            db.close();
        }
    }

    public int getMessageCount() {
        SQLiteDatabase db = mProfileDBHelper.getReadableDatabase();
        Cursor cursor = db.query(mProfileDBHelper.getTableName(), null, null, null, null, null, null);
        int count = cursor.getCount();

        return count;
    }

    public ArrayList<ClassDefine.MessageInfo> getMessageListFromDB() {
        SQLiteDatabase db = mProfileDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From " + mProfileDBHelper.getTableName() + " ORDER BY msg_id DESC", null);

        ArrayList<ClassDefine.MessageInfo> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ClassDefine.MessageInfo info = new ClassDefine.MessageInfo(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2))
                        , Integer.parseInt(cursor.getString(3)), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
                list.add(info);
                kjsLogUtil.i(String.format(Locale.CHINA, "mMsgId: %d, mCreate_time:%s, mMsgType:%d", info.mMsgId, info.mCreate_time, info.mMsgType));
            } while (cursor.moveToNext());
        }

        return list;
    }
}
