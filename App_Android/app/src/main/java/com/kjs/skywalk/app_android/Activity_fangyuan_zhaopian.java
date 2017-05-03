package com.kjs.skywalk.app_android;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kjs.skywalk.app_android.Homepage.fragmentHomePage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Activity_fangyuan_zhaopian extends AppCompatActivity {
    ViewPager mVPHuXing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangyuan_zhaopian);

        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();

        // 户型图
        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> huXingPicLst = new ArrayList<fragmentFangYuanZhaoPianGroup.PicList> (
                Arrays.asList(
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图一", R.drawable.huxingtu1),
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图二", R.drawable.huxingtu2),
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图三", R.drawable.huxingtu2),
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图四", R.drawable.huxingtu2),
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图五", R.drawable.huxingtu2),
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图六", R.drawable.huxingtu2)
             )
        );
//        fragmentFangYuanZhaoPianGroup.Group huXingGroup = new fragmentFangYuanZhaoPianGroup.Group("户型图", huXingPicLst);

//        fragmentFangYuanZhaoPianGroup fraHuXing = new fragmentFangYuanZhaoPianGroup();
////        fraHuXing.setGroup(huXingGroup);
//        fraHuXing.setPicList(huXingPicLst);
//        fragTransaction.replace(R.id.fl_huxing, fraHuXing);

        mVPHuXing = (ViewPager) findViewById(R.id.vp_huxing);
        List<Fragment> fragLstHuXing = new ArrayList<Fragment>();
        int lstCnt = huXingPicLst.size() % 2 == 0 ? huXingPicLst.size() / 2 : huXingPicLst.size() / 2 + 1;
        for (int i = 0; i <lstCnt; i++) {
            fragmentFangYuanZhaoPianGroup fragment = new fragmentFangYuanZhaoPianGroup();
            ArrayList<fragmentFangYuanZhaoPianGroup.PicList> list = new ArrayList();
            list.add(huXingPicLst.get(i * 2));
            list.add(huXingPicLst.get(i * 2  + 1));

            fragment.setPicList(list);
            fragLstHuXing.add(fragment);
        }
        mVPHuXing.setAdapter(new PicFragStatePageAdapter(getSupportFragmentManager(), fragLstHuXing));
        mVPHuXing.setCurrentItem(1);


//        // 房间结构
//        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> fangJianJieGouLst = new ArrayList<fragmentFangYuanZhaoPianGroup.PicList> (
//                Arrays.asList(
//                        new fragmentFangYuanZhaoPianGroup.PicList("房间结构图一", R.drawable.huxingtu1),
//                        new fragmentFangYuanZhaoPianGroup.PicList("房间结构图二", R.drawable.huxingtu2)
//                )
//        );
//        fragmentFangYuanZhaoPianGroup.Group fangJianJieGouGroup = new fragmentFangYuanZhaoPianGroup.Group("房间结构", fangJianJieGouLst);
//
//        fragmentFangYuanZhaoPianGroup fraFangJianJieGou = new fragmentFangYuanZhaoPianGroup();
//        fraFangJianJieGou.setGroup(fangJianJieGouGroup);
//        fragTransaction.replace(R.id.fl_fangjianjiegou, fraFangJianJieGou);
//
//        // 家居用品
//        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> jiaJuYongPinLst = new ArrayList<fragmentFangYuanZhaoPianGroup.PicList> (
//                Arrays.asList(
//                        new fragmentFangYuanZhaoPianGroup.PicList("家居用品图一", R.drawable.huxingtu1),
//                        new fragmentFangYuanZhaoPianGroup.PicList("家居用品图二", R.drawable.huxingtu2)
//                )
//        );
//        fragmentFangYuanZhaoPianGroup.Group jiaJuYongPinGroup = new fragmentFangYuanZhaoPianGroup.Group("家居用品", jiaJuYongPinLst);
//
//        fragmentFangYuanZhaoPianGroup fraJiaJuYongPin = new fragmentFangYuanZhaoPianGroup();
//        fraJiaJuYongPin.setGroup(jiaJuYongPinGroup);
//        fragTransaction.replace(R.id.fl_jiajuyongpin, fraJiaJuYongPin);
//
//        // 电器
//        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> dianQiLst = new ArrayList<fragmentFangYuanZhaoPianGroup.PicList> (
//                Arrays.asList(
//                        new fragmentFangYuanZhaoPianGroup.PicList("电器图一", R.drawable.huxingtu1),
//                        new fragmentFangYuanZhaoPianGroup.PicList("电器图二", R.drawable.huxingtu2)
//                )
//        );
//        fragmentFangYuanZhaoPianGroup.Group dianQiGroup = new fragmentFangYuanZhaoPianGroup.Group("电器", dianQiLst);
//
//        fragmentFangYuanZhaoPianGroup fraDianQi = new fragmentFangYuanZhaoPianGroup();
//        fraDianQi.setGroup(dianQiGroup);
//        fragTransaction.replace(R.id.fl_dianqi, fraDianQi);
//        //

        fragTransaction.commit();
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
}
