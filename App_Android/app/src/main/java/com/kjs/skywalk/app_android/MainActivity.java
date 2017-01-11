package com.kjs.skywalk.app_android;

import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private fragmentHomePage mFragHomePage = null;
    private fragmentApartment mFragApartment = null;
    private fragmentMsg mFragMsg = null;
    private fragmentPrivate mFragPrivate = null;
    private TextView mTvHomePage;
    private TextView mTvApartment;
    private TextView mTvMsg;
    private TextView mTvPrivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
