package com.kjs.skywalk.app_android;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kjs.skywalk.app_android.Homepage.fragmentHomePage;

import java.util.ArrayList;
import java.util.Arrays;

public class Activity_fangyuan_zhaopian extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangyuan_zhaopian);

        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();

        // 户型图
        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> huXingPicLst = new ArrayList<fragmentFangYuanZhaoPianGroup.PicList> (
                Arrays.asList(
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图一", R.drawable.huxingtu1),
                        new fragmentFangYuanZhaoPianGroup.PicList("户型图二", R.drawable.huxingtu2)
                )
        );
        fragmentFangYuanZhaoPianGroup.Group huXingGroup = new fragmentFangYuanZhaoPianGroup.Group("户型图", huXingPicLst);

        fragmentFangYuanZhaoPianGroup fraHuXing = new fragmentFangYuanZhaoPianGroup();
        fraHuXing.setGroup(huXingGroup);
        fragTransaction.replace(R.id.fl_huxing, fraHuXing);

        // 房间结构
        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> fangJianJieGouLst = new ArrayList<fragmentFangYuanZhaoPianGroup.PicList> (
                Arrays.asList(
                        new fragmentFangYuanZhaoPianGroup.PicList("房间结构图一", R.drawable.huxingtu1),
                        new fragmentFangYuanZhaoPianGroup.PicList("房间结构图二", R.drawable.huxingtu2)
                )
        );
        fragmentFangYuanZhaoPianGroup.Group fangJianJieGouGroup = new fragmentFangYuanZhaoPianGroup.Group("房间结构", fangJianJieGouLst);

        fragmentFangYuanZhaoPianGroup fraFangJianJieGou = new fragmentFangYuanZhaoPianGroup();
        fraFangJianJieGou.setGroup(fangJianJieGouGroup);
        fragTransaction.replace(R.id.fl_fangjianjiegou, fraFangJianJieGou);

        // 家居用品
        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> jiaJuYongPinLst = new ArrayList<fragmentFangYuanZhaoPianGroup.PicList> (
                Arrays.asList(
                        new fragmentFangYuanZhaoPianGroup.PicList("家居用品图一", R.drawable.huxingtu1),
                        new fragmentFangYuanZhaoPianGroup.PicList("家居用品图二", R.drawable.huxingtu2)
                )
        );
        fragmentFangYuanZhaoPianGroup.Group jiaJuYongPinGroup = new fragmentFangYuanZhaoPianGroup.Group("家居用品", jiaJuYongPinLst);

        fragmentFangYuanZhaoPianGroup fraJiaJuYongPin = new fragmentFangYuanZhaoPianGroup();
        fraJiaJuYongPin.setGroup(jiaJuYongPinGroup);
        fragTransaction.replace(R.id.fl_jiajuyongpin, fraJiaJuYongPin);

        // 电器
        ArrayList<fragmentFangYuanZhaoPianGroup.PicList> dianQiLst = new ArrayList<fragmentFangYuanZhaoPianGroup.PicList> (
                Arrays.asList(
                        new fragmentFangYuanZhaoPianGroup.PicList("电器图一", R.drawable.huxingtu1),
                        new fragmentFangYuanZhaoPianGroup.PicList("电器图二", R.drawable.huxingtu2)
                )
        );
        fragmentFangYuanZhaoPianGroup.Group dianQiGroup = new fragmentFangYuanZhaoPianGroup.Group("电器", dianQiLst);

        fragmentFangYuanZhaoPianGroup fraDianQi = new fragmentFangYuanZhaoPianGroup();
        fraDianQi.setGroup(dianQiGroup);
        fragTransaction.replace(R.id.fl_dianqi, fraDianQi);
        //

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
}
