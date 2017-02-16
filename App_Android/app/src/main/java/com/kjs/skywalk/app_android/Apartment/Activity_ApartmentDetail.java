package com.kjs.skywalk.app_android.Apartment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.kjs.skywalk.app_android.Activity_HouseholdDeliverables;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.control.ExpandedView;
import com.kjs.skywalk.control.SliderView;

import java.util.ArrayList;

//http://www.cnblogs.com/yangqiangyu/p/5143590.html
//https://github.com/singwhatiwanna/banner/tree/master/app/src/main
public class Activity_ApartmentDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__apartment_detail);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SliderView sView = (SliderView) findViewById(R.id.sv_view);
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        sView.setImages(list, mSvListener);
    }

    private SliderView.SliderViewListener mSvListener = new SliderView.SliderViewListener() {
        @Override
        public void onImageClick(int pos, View view) {
            commonFun.showToast_resIag(Activity_ApartmentDetail.this, view);
        }

        @Override
        public void onImageDisplay(final String imgUrl, final ImageView imageView) {

            String tag = (String) imageView.getTag();
            int resId = getResources().getIdentifier("decorate" + tag, "drawable", getPackageName());
            kjsLogUtil.i(String.format("tag is %s, resId is %#x", tag, resId));

            imageView.setImageResource(resId);



//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    final Bitmap bitmap = commonFun.loadImageFromUrl(imgUrl);
//                    if (bitmap != null) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                imageView.setImageBitmap(bitmap);
//                            }
//                        });
//                    }
//                }
//            }).start();
        }
    };

    public void onViewClick(View v) {
//        commonFun.showToast_resId(this, v);
        switch (v.getId()) {
            case R.id.tv_price_analysis:
            {
                ExpandedView priceView = (ExpandedView) findViewById(R.id.ev_price_analysis);
                if (priceView.getVisibility() == View.VISIBLE) {
                    priceView.collapse();
                    ((TextView)findViewById(R.id.tv_price_analysis)).setText("价格分析 v");
                } else {
                    priceView.expand();
                    ((TextView)findViewById(R.id.tv_price_analysis)).setText("价格分析 ^");
                }
            }
            break;

            case R.id.tv_order:
            {
                // 预约看房
                showOrderDlg();
            }
            break;

            case R.id.tv_zufang:
            {
                // 我要租房
                showYiJiaDlg();
            }
            break;
        }
    }

    private AlertDialog mOrderDlg;
    private void showOrderDlg() {
        if (mOrderDlg == null) {
            mOrderDlg = new AlertDialog.Builder(this).create();
        }
        mOrderDlg.show();
        mOrderDlg.setContentView(R.layout.dialog_apartment_order);

        TextView tvBack = (TextView) mOrderDlg.findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderDlg.dismiss();
            }
        });

        TextView tvConfirm = (TextView) mOrderDlg.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderDlg.dismiss();
            }
        });
    }

    private AlertDialog mYiJiaDlg;
    private void showYiJiaDlg() {
        if (mYiJiaDlg == null) {
            mYiJiaDlg = new AlertDialog.Builder(this).create();
        }
        mYiJiaDlg.show();
        mYiJiaDlg.setContentView(R.layout.dialog_apartment_yijia);

        TextView tvBack = (TextView) mYiJiaDlg.findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYiJiaDlg.dismiss();
            }
        });

        TextView tvConfirm = (TextView) mYiJiaDlg.findViewById(R.id.tv_commit);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYiJiaDlg.dismiss();
            }
        });
    }
}
