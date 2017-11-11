package com.kjs.skywalk.app_android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;

/**
 * Created by sailor.zhou on 2017/11/11.
 */

public class ProfileDBOperator {
    private static ProfileDBOperator mOperator = null;
    private Context mContext = null;
    private ProfileDBHelper mProfileDBHelper = null;

    private ProfileDBOperator(Context context, String user) {
        mContext = context;
        mProfileDBHelper = new ProfileDBHelper(context, user);
    }

    public static ProfileDBOperator getOperator(Context context, String user) {
        if (mOperator == null) {
            mOperator = new ProfileDBOperator(context, user);
        }

        return mOperator;
    }

    // IApiResults.ISysMsgInfo msgInfo
    public void update(ArrayList<Object> list) {
        for (Object object : list) {
            IApiResults.ISysMsgInfo msgInfo = (IApiResults.ISysMsgInfo) object;
            String sql_add = "insert into " + mProfileDBHelper.getClass() + " (id, create_time, type, body, property, building_no, house_no)values (?,?,?,?,?,?,?)";
            SQLiteDatabase db = mProfileDBHelper.getWritableDatabase();
            db.execSQL(sql_add, new Object[] { msgInfo.MsgId(), msgInfo.CreateTime(), msgInfo.MsgType(), msgInfo.MsgBody(), msgInfo.Property(), msgInfo.BuildingNo(), msgInfo.HouseNo()});
            db.close();
        }

    }
}
