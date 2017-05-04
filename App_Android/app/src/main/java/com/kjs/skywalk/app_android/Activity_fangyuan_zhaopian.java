package com.kjs.skywalk.app_android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Activity_fangyuan_zhaopian extends AppCompatActivity {
    ViewPager mVPHuXing;

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
        ViewPager vpFangJianJieGou = (ViewPager) findViewById(R.id.vp_fangjianjiegou);
        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> fangJianJieGouPicLst = new ArrayList<> (
                Arrays.asList(
                        new fragmentFangYuanZhaoPianGroup.PicList("房间结构图一", R.drawable.huxingtu1, false),
                        new fragmentFangYuanZhaoPianGroup.PicList("房间结构图二", R.drawable.huxingtu2, false)
                )
        );
        fillPicGroupInfo(vpFangJianJieGou, fangJianJieGouPicLst);

        //        // 家居用品
        ViewPager vpJiaJuYongPin = (ViewPager) findViewById(R.id.vp_jiajuyongpin);
        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> jiaJuYongPinPicLst = new ArrayList<> (
                Arrays.asList(
                        new fragmentFangYuanZhaoPianGroup.PicList("家居用品图一", R.drawable.huxingtu1, false),
                        new fragmentFangYuanZhaoPianGroup.PicList("家居用品图二", R.drawable.huxingtu2, false)
                )
        );
        fillPicGroupInfo(vpJiaJuYongPin, jiaJuYongPinPicLst);

        //        // 电器
        ViewPager vpDianQi = (ViewPager) findViewById(R.id.vp_dianqi);
        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> dianQiPicLst = new ArrayList<> (
                Arrays.asList(
                        new fragmentFangYuanZhaoPianGroup.PicList("电器图一", R.drawable.huxingtu1, false),
                        new fragmentFangYuanZhaoPianGroup.PicList("电器图二", R.drawable.huxingtu2, false)
                )
        );
        fillPicGroupInfo(vpDianQi, dianQiPicLst);

    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title:
            {
                finish();
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
