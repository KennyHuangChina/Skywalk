package com.kjs.skywalk.app_android;

import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        String strVName = v.getResources().getResourceEntryName(v.getId());
        Toast.makeText(this, "click: " + strVName, Toast.LENGTH_SHORT).show();

        switch (v.getId()) {
            case R.id.tv_rent:
            {
                if (mBvMsg != null) {
                    mBvMsg.hide();
                }

                if (mBvMsgInTab != null) {
                    mBvMsgInTab.hide();
                }
            }
            break;

            case R.id.tv_apartment:
            {
                setNewMessageCount(5);
            }
            break;
        }
    }

    private void setNewMessageCount(int count) {
        if (mBvMsg == null) {
            ImageView imMessage = (ImageView)findViewById(R.id.im_message);
            mBvMsg = new BadgeView(this, imMessage);
            mBvMsg.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            mBvMsg.setText(Integer.toString(count));
            mBvMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8.0f);
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
    }
}
