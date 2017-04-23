package com.kjs.skywalk.app_android;

import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.Apartment.Activity_ApartmentDetail;
import com.kjs.skywalk.app_android.Apartment.fragmentApartment;
import com.kjs.skywalk.app_android.Homepage.fragmentHomePage;
import com.kjs.skywalk.app_android.Message.fragmentMsg;
import com.kjs.skywalk.app_android.Private.fragmentPrivate;
import com.kjs.skywalk.control.BadgeView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.iwf.photopicker.PhotoPreview;

public class MainActivity extends AppCompatActivity {
    private fragmentHomePage mFragHomePage = null;
    private fragmentApartment mFragApartment = null;
    private fragmentMsg mFragMsg = null;
    private fragmentPrivate mFragPrivate = null;
    private TextView mTvHomePage;
    private TextView mTvApartment;
    private TextView mTvMsg;
    private TextView mTvPrivate;
    private BadgeView mBvMsg;
    private BadgeView mBvMsgInTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // copy file from "asset/testPics/" to "cache" dir
        commonFun.copyAssets(this, "testPics", getCacheDir().getAbsolutePath());

        // bind
        mTvHomePage = (TextView) findViewById(R.id.tv_homepage);
        mTvApartment = (TextView) findViewById(R.id.tv_apartment);
        mTvMsg = (TextView) findViewById(R.id.tv_msg);
        mTvPrivate = (TextView) findViewById(R.id.tv_private);

        // set default to display homepage
        mTvHomePage.setSelected(true);

        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        mFragHomePage = new fragmentHomePage();
        fragTransaction.replace(R.id.fl_container, mFragHomePage);
        fragTransaction.commit();
    }

    private void setTabMenuSelected(View v) {
        mTvHomePage.setSelected(false);
        mTvApartment.setSelected(false);
        mTvMsg.setSelected(false);
        mTvPrivate.setSelected(false);
        v.setSelected(true);
    }

    // set in activity_main.xml
    public void onTabMenuClick(View v) {
        setTabMenuSelected(v);
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.tv_homepage:
                if (mFragHomePage == null) {
                    mFragHomePage = new fragmentHomePage();
                }
                fragTransaction.replace(R.id.fl_container, mFragHomePage);
            break;

            case R.id.tv_apartment:
                if (mFragApartment == null) {
                    mFragApartment = new fragmentApartment();
                }
                fragTransaction.replace(R.id.fl_container, mFragApartment);
                break;

            case R.id.tv_msg:
                if (mFragMsg == null) {
                    mFragMsg = new fragmentMsg();
                }
                fragTransaction.replace(R.id.fl_container, mFragMsg);
                break;

            case R.id.tv_private:
                if (mFragPrivate == null) {
                    mFragPrivate = new fragmentPrivate();
                }
                fragTransaction.replace(R.id.fl_container, mFragPrivate);
                break;
        }
        fragTransaction.commit();
    }

    // set in activity_main.xml
    public void onClickResponse(View v) {
        //commonFun.showToast_resEntryName(this, v);

        switch (v.getId()) {
            case R.id.tv_rent:
            {
//                if (mBvMsg != null) {
//                    mBvMsg.hide();
//                }
//
//                if (mBvMsgInTab != null) {
//                    mBvMsgInTab.hide();
//                }

                Intent intent = new Intent(this, Activity_HouseholdDeliverables.class);
                startActivity(intent);
            }
            break;

            case R.id.tv_apartment:
            {
//                Intent intent = new Intent(this, Activity_ApartmentDetail.class);
//                startActivity(intent);

                Intent intent = new Intent(this, Activity_HouseholdAppliances.class);
                startActivity(intent);

                setNewMessageCount(5);
            }
            break;

            case R.id.iv_title:
            {
//                startActivity(new Intent(this, Activity_ImagePreview.class));

                ArrayList<String> images = commonFun.getTestPicList(this);
                PhotoPreview.builder()
                        .setPhotos(images)
                        .setCurrentItem(0)
                        .start(this);
            }
            break;

            case R.id.im_message:
            {
                showTestMenu(v);
            }
            break;
        }
    }

    public void onSearchConditionFilterItemClicked(View view) {
        mFragApartment.searchConditionFilterItemClicked(view);
    }

    public void onSearchConditionHouseTypeItemClicked(View view) {
        mFragApartment.searchConditionHouseTypeItemClicked(view);
    }

    public void onSearchConditionPriceItemClicked(View view) {
        mFragApartment.searchConditionPriceItemClicked(view);
    }

    private void setNewMessageCount(int count) {
        if (mBvMsg == null) {
            ImageView imMessage = (ImageView)findViewById(R.id.im_message);
            mBvMsg = new BadgeView(this, imMessage);
            mBvMsg.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            mBvMsg.setText(Integer.toString(count));
            mBvMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8.0f);
//            mBvMsg.setBackgroundResource(R.drawable.red_circle);
            mBvMsg.show(true);
        }

        if (mBvMsgInTab == null) {
            TextView tvMsgInTab = (TextView) findViewById(R.id.tv_msg);
            mBvMsgInTab = new BadgeView(this, tvMsgInTab);
            mBvMsgInTab.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            mBvMsgInTab.setText(Integer.toString(count));
            mBvMsgInTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8.0f);
            mBvMsgInTab.show(true);
        }

        // test notification
        regNotification();
//        Notification notifation= new Notification.Builder(this)
//                .setContentTitle("通知标题")
//                .setContentText("通知内容")
//                .setSmallIcon(R.drawable.message)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.message))
//                .build();
//        NotificationManager manger= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        manger.notify(0, notifation);
    }

    private void regNotification() {
        int type = 3;

        Intent intentClick = new Intent(this, NotificationBroadcastReceiver.class);
        intentClick.setAction(NotificationBroadcastReceiver.NBR_NOTI_CLICKED);
        intentClick.putExtra(NotificationBroadcastReceiver.NBR_TYPE, type);
        PendingIntent pendingIntentClick = PendingIntent.getBroadcast(this, 0, intentClick, PendingIntent.FLAG_ONE_SHOT);

        Intent intentCancel = new Intent(this, NotificationBroadcastReceiver.class);
        intentCancel.setAction(NotificationBroadcastReceiver.NBR_NOTI_CANCELLED);
        intentCancel.putExtra(NotificationBroadcastReceiver.NBR_TYPE, type);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 0, intentCancel, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.message)
                .setContentTitle("通知标题")
                .setContentText("通知内容")
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntentClick)
                .setDeleteIntent(pendingIntentCancel);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(type, builder.build());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting1:
            {
                startActivity(new Intent(this, Activity_rentals_expiration.class));
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    private void showTestMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_setting1:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_rentals_expiration.class));
                    }
                    break;

                    case R.id.action_setting2:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_rentals_progress_yijia.class));
                    }
                    break;

                    case R.id.action_setting3:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_rentals_progress_contracted.class));
                    }
                    break;

                    case R.id.action_setting4:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_Zushouweituo_Fangyuanxinxi.class));
                    }
                    break;

                    case R.id.action_setting5:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_fangyuan_guanli.class));
                    }
                    break;

                    case R.id.action_setting100:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_rentals_jiaofang_chaobiao.class));
                        break;
                    }

                    case R.id.action_setting101:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_Weituoqueren.class));
                        break;
                    }

                    case R.id.action_setting102:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_Zushouweituo_Finish.class));
                        break;
                    }

                    case R.id.action_setting103:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_Zushouweituo_Xuanzedaili.class));
                        break;
                    }

                    case R.id.action_setting104:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_Zushouweituo_SelectService.class));
                        break;
                    }

                    case R.id.action_setting105:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_shenhe_zhengjian.class));
                        break;
                    }
                    case R.id.action_setting106:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_Zushouweituo_Kanfangshijian.class));
                        break;
                    }
                    case R.id.action_setting107:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_Search_NoHistory.class));
                        break;
                    }
                    case R.id.action_setting108:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_Search_Add.class));
                        break;
                    }
                    case R.id.action_setting109:
                    {
                        startActivity(new Intent(MainActivity.this, Activity_Zushouweituo_Kanfangshijian.class));
                        break;
                    }
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
