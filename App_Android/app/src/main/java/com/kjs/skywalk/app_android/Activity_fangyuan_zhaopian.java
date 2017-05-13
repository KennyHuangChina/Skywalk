package com.kjs.skywalk.app_android;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;

public class Activity_fangyuan_zhaopian extends AppCompatActivity {
    ViewPager mVPHuXing;
    ViewPager mVpFangJianJieGou;
    ViewPager mVpJiaJuYongPin;
    ViewPager mVpDianQi;
    TextView mTvStatus1;
    TextView mTvStatus2;
    TextView mTvStatus3;
    TextView mTvStatus4;
    ArrayList<ClassDefine.PicList> mHuXingPicLst;
    ArrayList<ClassDefine.PicList> mFangJianJieGouPicLst;
    ArrayList<ClassDefine.PicList> mJiaJuYongPinPicLst;
    ArrayList<ClassDefine.PicList> mDianQiPicLst;

    boolean mIsPicSelectMode = false;
    int mPhotoPickerHostId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                onPhotoPickerReturn(photos);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangyuan_zhaopian);

        // 户型图
        ((TextView) findViewById(R.id.tv_name_picgroup1)).setText("户型图");
        mTvStatus1 = (TextView) findViewById(R.id.tv_status_picgroup1);
        mVPHuXing = (ViewPager) findViewById(R.id.vp_huxing);
        mHuXingPicLst = new ArrayList<> (
                Arrays.asList(
                        new ClassDefine.PicList("户型图一", "", R.drawable.huxingtu1, false),
                        new ClassDefine.PicList("户型图二", "", R.drawable.huxingtu2, false),
                        new ClassDefine.PicList("户型图三", "", R.drawable.huxingtu2, false),
                        new ClassDefine.PicList("户型图四", "", R.drawable.huxingtu2, false),
                        new ClassDefine.PicList("户型图五", "", R.drawable.huxingtu2, false),
                        new ClassDefine.PicList("户型图六", "", R.drawable.huxingtu2, false)
             )
        );
        fillPicGroupInfo(mTvStatus1, mVPHuXing, mHuXingPicLst);

        //        // 房间结构
        ((TextView) findViewById(R.id.tv_name_picgroup2)).setText("房间结构");
        mTvStatus2 = (TextView) findViewById(R.id.tv_status_picgroup2);
        mVpFangJianJieGou = (ViewPager) findViewById(R.id.vp_fangjianjiegou);
        mFangJianJieGouPicLst = new ArrayList<> (
                Arrays.asList(
                        new ClassDefine.PicList("房间结构图一", "", R.drawable.huxingtu1, false),
                        new ClassDefine.PicList("房间结构图二", "", R.drawable.huxingtu2, false)
                )
        );
        fillPicGroupInfo(mTvStatus2, mVpFangJianJieGou, mFangJianJieGouPicLst);

        //        // 家居用品
        ((TextView) findViewById(R.id.tv_name_picgroup3)).setText("家居用品");
        mTvStatus3 = (TextView) findViewById(R.id.tv_status_picgroup3);
        mVpJiaJuYongPin = (ViewPager) findViewById(R.id.vp_jiajuyongpin);
        mJiaJuYongPinPicLst = new ArrayList<> (
                Arrays.asList(
                        new ClassDefine.PicList("家居用品图一", "", R.drawable.huxingtu1, false),
                        new ClassDefine.PicList("家居用品图二", "", R.drawable.huxingtu2, false)
                )
        );
        fillPicGroupInfo(mTvStatus3, mVpJiaJuYongPin, mJiaJuYongPinPicLst);

        //        // 电器
        ((TextView) findViewById(R.id.tv_name_picgroup4)).setText("电器");
        mTvStatus4 = (TextView) findViewById(R.id.tv_status_picgroup4);
        mVpDianQi = (ViewPager) findViewById(R.id.vp_dianqi);
        mDianQiPicLst = new ArrayList<> ();
//                Arrays.asList(
//                        new ClassDefine.PicList("电器图一", "", R.drawable.huxingtu1, false),
//                        new ClassDefine.PicList("电器图二", "", R.drawable.huxingtu2, false)
//                )
//        );
        fillPicGroupInfo(mTvStatus4, mVpDianQi, mDianQiPicLst);

    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
            {
                finish();
            }
            break;
            case R.id.tv_select:
            {
                mIsPicSelectMode = !mIsPicSelectMode;
                if (mIsPicSelectMode) {
                    ((TextView)v).setText("取消");
                } else {
                    ((TextView)v).setText("选择");
                }
                updateViewrPagerSelectMode(mIsPicSelectMode);

            }
            break;
        }
    }

    // refresh view pagers
    private void updateViewrPagerSelectMode(boolean isSelectMode) {
        ((PicFragStatePageAdapter) mVPHuXing.getAdapter()).updateSelectMode(isSelectMode);
        ((PicFragStatePageAdapter) mVpFangJianJieGou.getAdapter()).updateSelectMode(isSelectMode);
        ((PicFragStatePageAdapter) mVpJiaJuYongPin.getAdapter()).updateSelectMode(isSelectMode);
        ((PicFragStatePageAdapter) mVpDianQi.getAdapter()).updateSelectMode(isSelectMode);
    }

    public void onPhotoPickerClicked(View v) {
        switch (v.getId()) {
            case R.id.iv_photopicker_picgroup1:
            case R.id.iv_photopicker_picgroup2:
            case R.id.iv_photopicker_picgroup3:
            case R.id.iv_photopicker_picgroup4:
            {
                startPhotoPickerActivity(v);
            }
            break;

        }
    }

    class PicFragStatePageAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mLst;
        public PicFragStatePageAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            mLst = list;
        }

        @Override
        public Fragment getItem(int position) {
            return mLst.get(position);
        }

        @Override
        public int getCount() {
            return mLst.size();
        }

        public void updateSelectMode(boolean isSelectMode) {
            for (Fragment fragment : mLst) {
                if (fragment instanceof  fragmentFangYuanZhaoPianGroup) {
                    ((fragmentFangYuanZhaoPianGroup)fragment).updateSelectMode(isSelectMode);
                }
            }
        }

        public int getSelectCount() {
            int count = 0;
            for (Fragment fragment : mLst) {
                if (fragment instanceof  fragmentFangYuanZhaoPianGroup) {
                    count += ((fragmentFangYuanZhaoPianGroup)fragment).getSelectCount();
                }
            }

            return count;
        }
    }

    private void fillPicGroupInfo(TextView tvStatus, ViewPager viewPager, ArrayList<ClassDefine.PicList> picLst) {
        tvStatus.setText("(" + picLst.size() + ")");
        List<Fragment> fragLst = new ArrayList<Fragment>();
        int lstCnt = picLst.size() % 2 == 0 ? picLst.size() / 2 : picLst.size() / 2 + 1;
        for (int i = 0; i <lstCnt; i++) {
            fragmentFangYuanZhaoPianGroup fragment = new fragmentFangYuanZhaoPianGroup();
            fragment.setZhaoPianGroupCallback(mPicGroupCallback);
            ArrayList<ClassDefine.PicList> list = new ArrayList();
            list.add(picLst.get(i * 2));
            list.add(picLst.get(i * 2  + 1));

            fragment.setPicList(list);
            fragLst.add(fragment);
        }
        viewPager.setAdapter(new PicFragStatePageAdapter(getSupportFragmentManager(), fragLst));
        viewPager.setCurrentItem(0);
    }

    fragmentFangYuanZhaoPianGroup.ZhaoPianGroupCallback mPicGroupCallback = new fragmentFangYuanZhaoPianGroup.ZhaoPianGroupCallback() {

        @Override
        public void onPicSelectChanged() {
            int select_count = ((PicFragStatePageAdapter)mVPHuXing.getAdapter()).getSelectCount();
            int total = mHuXingPicLst.size();
            mTvStatus1.setText("(" + select_count + "/" + total + ")");

            select_count = ((PicFragStatePageAdapter)mVpFangJianJieGou.getAdapter()).getSelectCount();
            total = mFangJianJieGouPicLst.size();
            mTvStatus2.setText("(" + select_count + "/" + total + ")");

            select_count = ((PicFragStatePageAdapter)mVpJiaJuYongPin.getAdapter()).getSelectCount();
            total = mJiaJuYongPinPicLst.size();
            mTvStatus3.setText("(" + select_count + "/" + total + ")");

            select_count = ((PicFragStatePageAdapter)mVpDianQi.getAdapter()).getSelectCount();
            total = mDianQiPicLst.size();
            mTvStatus4.setText("(" + select_count + "/" + total + ")");
        }
    };

    private void startPhotoPickerActivity(View host_view) {
        mPhotoPickerHostId = host_view.getId();
        PhotoPicker.builder()
                .start(Activity_fangyuan_zhaopian.this);
    }

    private void onPhotoPickerReturn(List<String> photos) {
        ArrayList<ClassDefine.PicList> picList;
        PicFragStatePageAdapter adapter;
        switch (mPhotoPickerHostId) {
            case R.id.iv_photopicker_picgroup1:
            {
                picList = mHuXingPicLst;
                adapter = ((PicFragStatePageAdapter) mVPHuXing.getAdapter());
                break;
            }
            case R.id.iv_photopicker_picgroup2:
            {
                picList = mFangJianJieGouPicLst;
                adapter = ((PicFragStatePageAdapter) mVpFangJianJieGou.getAdapter());
                break;
            }
            case R.id.iv_photopicker_picgroup3:
            {
                picList = mJiaJuYongPinPicLst;
                adapter = ((PicFragStatePageAdapter) mVpJiaJuYongPin.getAdapter());
                break;
            }
            case R.id.iv_photopicker_picgroup4:
            {
                picList = mDianQiPicLst;
                adapter = ((PicFragStatePageAdapter) mVpDianQi.getAdapter());
                break;
            }
            default:
                return;
        }

        for (String path : photos) {
            ClassDefine.PicList item = new ClassDefine.PicList("电器图一", path, 0, false);
            picList.add(item);
        }

        fillPicGroupInfo(mTvStatus1, mVPHuXing, mHuXingPicLst);
        fillPicGroupInfo(mTvStatus2, mVpFangJianJieGou, mFangJianJieGouPicLst);
        fillPicGroupInfo(mTvStatus3, mVpJiaJuYongPin, mJiaJuYongPinPicLst);
        fillPicGroupInfo(mTvStatus4, mVpDianQi, mDianQiPicLst);

        adapter.notifyDataSetChanged();
    }
}
