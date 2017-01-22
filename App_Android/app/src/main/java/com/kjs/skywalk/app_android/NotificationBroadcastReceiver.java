package com.kjs.skywalk.app_android;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sailor.zhou on 2017/1/22.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    public static final String NBR_NOTI_CLICKED = "notification_clicked";
    public static final String NBR_NOTI_CANCELLED = "notification_cancelled";
    public static final String NBR_TYPE =  "-1";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int type = intent.getIntExtra(NBR_TYPE, -1);
        Log.i(this.getClass().getSimpleName(), "receive action: " + action + ", type: " + type);

        if (type != -1) {
            NotificationManager notiMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notiMan.cancel(type);
        }

        if (action.equals(NBR_NOTI_CLICKED)) {
            Toast.makeText(context, "receive: " + action, Toast.LENGTH_SHORT).show();
        }

        if (action.equals(NBR_NOTI_CANCELLED)) {
            Toast.makeText(context, "receive: " + action, Toast.LENGTH_SHORT).show();
        }
    }
}
