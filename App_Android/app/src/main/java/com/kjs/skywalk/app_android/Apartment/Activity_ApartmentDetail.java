package com.kjs.skywalk.app_android.Apartment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.kjs.skywalk.app_android.Activity_HouseholdDeliverables;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.control.ExpandedView;
import com.kjs.skywalk.control.SliderView;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPreview;

public class Activity_ApartmentDetail extends AppCompatActivity {

    private ArrayList<String> mImageLst;

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
        mImageLst = commonFun.getTestPicList(this);
        sView.setImages(mImageLst, mSvListener);
    }

    private SliderView.SliderViewListener mSvListener = new SliderView.SliderViewListener() {
        @Override
        public void onImageClick(int pos, View view) {
            PhotoPreview.builder()
                    .setPhotos(mImageLst)
                    .setCurrentItem(pos)
                    .start(Activity_ApartmentDetail.this);
        }

        @Override
        public void onImageDisplay(final String imgUrl, final ImageView imageView) {

            String tag = (String) imageView.getTag();
//            int resId = getResources().getIdentifier("decorate" + tag, "drawable", getPackageName());
//            kjsLogUtil.i(String.format("tag is %s, resId is %#x", tag, resId));
//            imageView.setImageResource(resId);

             kjsLogUtil.i(String.format("clicked tag: %s", tag));
            imageView.setImageBitmap(commonFun.getBitmapFromLocal(tag));




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
            case  R.id.fab_back:
            {
                finish();
            }
            break;

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
            }
            break;

            case R.id.tv_yijia:
            {
                // 议价
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

        final TextView tvDateSelector = (TextView) mOrderDlg.findViewById(R.id.tv_date_selector);
        tvDateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateSelectorDlg();
            }
        });

        // set date selector check status
        Boolean isOtherDaysChecked = ((RadioButton)mOrderDlg.findViewById(R.id.rb_otherdays)).isChecked();
        tvDateSelector.setEnabled(isOtherDaysChecked);

        RadioGroup radioGroup = (RadioGroup) mOrderDlg.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                // log
                if (radioGroup.findViewById(i) instanceof RadioButton) {
                    RadioButton radBtn = (RadioButton) radioGroup.findViewById(i);
                    commonFun.showToast_info(Activity_ApartmentDetail.this, radioGroup, "selected : " + radBtn.getText());
                } else {
                    commonFun.showToast_info(Activity_ApartmentDetail.this, radioGroup, "selected id: " + i);
                }
                //

                switch (i)
                {
                    case R.id.rb_today:
                        tvDateSelector.setEnabled(false);
                        break;

                    case R.id.rb_tomorrow:
                        tvDateSelector.setEnabled(false);
                        break;

                    case R.id.rb_aftertomorrow:
                        tvDateSelector.setEnabled(false);
                        break;

                    case R.id.rb_otherdays:
                        tvDateSelector.setEnabled(true);
                        break;
                }

            }
        });

        // initialize Bandwidth spinner
        Spinner spTimeSelector = (Spinner)mOrderDlg.findViewById(R.id.sp_timeselector);
        ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(this, R.layout.spinner_item);
        adapterTime.add("8:30 - 9:00");
        adapterTime.add("9:00 - 9:30");
        adapterTime.add("9:30 - 10:00");
        adapterTime.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spTimeSelector.setAdapter(adapterTime);
        spTimeSelector.setSelection(1);




//        Window window = mOrderDlg.getWindow();
//        window.getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);

//        mOrderDlg.getWindow().getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager windowManager = getWindowManager();
//        DisplayMetrics outMetrics = new DisplayMetrics();;
//        windowManager.getDefaultDisplay().getMetrics(outMetrics);
//        WindowManager.LayoutParams lp = mOrderDlg.getWindow().getAttributes();
//        lp.width = outMetrics.widthPixels;
//        mOrderDlg.getWindow().setAttributes(lp);

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

    private AlertDialog mDateSelectorDlg;
    private void showDateSelectorDlg() {
        if (mDateSelectorDlg == null) {
            mDateSelectorDlg = new AlertDialog.Builder(this).create();
        }
        mDateSelectorDlg.show();
        mDateSelectorDlg.setContentView(R.layout.dialog_fangyuanxinxi_dateselector);

        TextView tvBack = (TextView) mDateSelectorDlg.findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateSelectorDlg.dismiss();
            }
        });

        TextView tvConfirm = (TextView) mDateSelectorDlg.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateSelectorDlg.dismiss();
            }
        });
    }
}
