package com.kjs.skywalk.app_android;

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


    boolean mIsPicSelectMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangyuan_zhaopian);

        // 户型图
        mVPHuXing = (ViewPager) findViewById(R.id.vp_huxing);
        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> huXingPicLst = new ArrayList<fragmentFangYuanZhaoPianGroup.PicList> (
                Arrays.asList(
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图一", R.drawable.huxingtu1, false),
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图二", R.drawable.huxingtu2, false),
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图三", R.drawable.huxingtu2, false),
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图四", R.drawable.huxingtu2, false),
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图五", R.drawable.huxingtu2, false),
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图六", R.drawable.huxingtu2, false)
             )
        );
        fillPicGroupInfo(mVPHuXing, huXingPicLst);

        //        // 房间结构
        mVpFangJianJieGou = (ViewPager) findViewById(R.id.vp_fangjianjiegou);
        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> fangJianJieGouPicLst = new ArrayList<> (
                Arrays.asList(
                        new fragmentFangYuanZhaoPianGroup.PicList("房间结构图一", R.drawable.huxingtu1, false),
                        new fragmentFangYuanZhaoPianGroup.PicList("房间结构图二", R.drawable.huxingtu2, false)
                )
        );
        fillPicGroupInfo(mVpFangJianJieGou, fangJianJieGouPicLst);

        //        // 家居用品
        mVpJiaJuYongPin = (ViewPager) findViewById(R.id.vp_jiajuyongpin);
        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> jiaJuYongPinPicLst = new ArrayList<> (
                Arrays.asList(
                        new fragmentFangYuanZhaoPianGroup.PicList("家居用品图一", R.drawable.huxingtu1, false),
                        new fragmentFangYuanZhaoPianGroup.PicList("家居用品图二", R.drawable.huxingtu2, false)
                )
        );
        fillPicGroupInfo(mVpJiaJuYongPin, jiaJuYongPinPicLst);

        //        // 电器
        mVpDianQi = (ViewPager) findViewById(R.id.vp_dianqi);
        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> dianQiPicLst = new ArrayList<> (
                Arrays.asList(
                        new fragmentFangYuanZhaoPianGroup.PicList("电器图一", R.drawable.huxingtu1, false),
                        new fragmentFangYuanZhaoPianGroup.PicList("电器图二", R.drawable.huxingtu2, false)
                )
        );
        fillPicGroupInfo(mVpDianQi, dianQiPicLst);

    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title:
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
            case R.id.iv_huxing_photopicker:
            {
                PhotoPicker.builder()
                        .start(Activity_fangyuan_zhaopian.this);
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
    }

    private void fillPicGroupInfo(ViewPager viewPager, ArrayList<fragmentFangYuanZhaoPianGroup.PicList> picLst) {
        List<Fragment> fragLst = new ArrayList<Fragment>();
        int lstCnt = picLst.size() % 2 == 0 ? picLst.size() / 2 : picLst.size() / 2 + 1;
        for (int i = 0; i <lstCnt; i++) {
            fragmentFangYuanZhaoPianGroup fragment = new fragmentFangYuanZhaoPianGroup();
            ArrayList<fragmentFangYuanZhaoPianGroup.PicList> list = new ArrayList();
            list.add(picLst.get(i * 2));
            list.add(picLst.get(i * 2  + 1));

            fragment.setPicList(list);
            fragLst.add(fragment);
        }
        viewPager.setAdapter(new PicFragStatePageAdapter(getSupportFragmentManager(), fragLst));
        viewPager.setCurrentItem(0);
    }
}
