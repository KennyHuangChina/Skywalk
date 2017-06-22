package com.kjs.skywalk.app_android.Apartment;

import android.content.Intent;
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
import com.kjs.skywalk.app_android.SKBaseActivity;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.IApiResults;
import com.kjs.skywalk.control.ExpandedView;
import com.kjs.skywalk.control.SliderView;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPreview;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_DIGEST_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_PROPERTY_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_USER_INFO;

public class Activity_ApartmentDetail extends SKBaseActivity {

    private ArrayList<String> mImageLst;
    private int mHouseId = -1;
    private CommandManager mCmdMgr = null;

    private TextView mTvApartmentName;
    private TextView mTvApartmentStatus;
    private TextView mTvApartmentAddr;
    private TextView mTvApartmentNo;
    private TextView mTvApartmentLastModifyDate;
    private TextView mTvApartmentHuXing;
    private TextView mTvApartmentAcreage;
    private TextView mTvApartmentYuYue;

    private TextView mTvApartment_floor;
    private TextView mTvApartment_fangxing;
    private TextView mTvApartment_chanquan;
    private TextView mTvApartment_decoration;
    private TextView mTvApartment_jungong;
    private TextView mTvApartment_buydate;

    private TextView mTvApartment_username;
    private TextView mTvApartment_telephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__apartment_detail);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_share);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTvApartmentName = (TextView) findViewById(R.id.tv_apartment_name);
        mTvApartmentStatus = (TextView) findViewById(R.id.tv_apartment_status);
        mTvApartmentAddr = (TextView) findViewById(R.id.tv_apartment_addr);

        mTvApartmentNo = (TextView) findViewById(R.id.tv_apartment_no);
        mTvApartmentLastModifyDate = (TextView) findViewById(R.id.tv_apartment_last_modify_date);
        mTvApartmentHuXing = (TextView) findViewById(R.id.tv_apartment_huxing);
        mTvApartmentAcreage = (TextView) findViewById(R.id.tv_apartment_acreage);
        mTvApartmentYuYue = (TextView) findViewById(R.id.tv_apartment_yuyue);

        mTvApartment_floor = (TextView) findViewById(R.id.tv_apartment_floor);
        mTvApartment_fangxing = (TextView) findViewById(R.id.tv_apartment_fangxing);
        mTvApartment_chanquan = (TextView) findViewById(R.id.tv_apartment_chanquan);
        mTvApartment_decoration = (TextView) findViewById(R.id.tv_apartment_decoration);
        mTvApartment_jungong = (TextView) findViewById(R.id.tv_apartment_jungong);
        mTvApartment_buydate = (TextView) findViewById(R.id.tv_apartment_buydate);

        mTvApartment_username = (TextView) findViewById(R.id.tv_apartment_username);
        mTvApartment_telephone = (TextView) findViewById(R.id.tv_apartment_telephone);


        SliderView sView = (SliderView) findViewById(R.id.sv_view);
        mImageLst = commonFun.getTestPicList(this);
        sView.setImages(mImageLst, mSvListener);

        Bundle bundle = getIntent().getExtras();
        mHouseId = bundle.getInt("houseId");

        mCmdMgr = new CommandManager(this, this, this);
        mCmdMgr.GetHouseInfo(mHouseId, false);
        kjsLogUtil.i("GetHouseInfo: " + mHouseId);

//        小区物业信息，GetPropertyInfo
//        mCmdMgr.GetPropertyInfo(2);

//        代理员信息，GetUserInfo
//        mCmdMgr.GetUserInfo(1);

//        房屋设施 -> GetHouseFacilityList
        mCmdMgr.GetHouseFacilityList(mHouseId);
        }

        @Override
        public void onCommandFinished(int command, IApiResults.ICommon iResult) {
            kjsLogUtil.i("Activity_ApartmentDetail::onCommandFinished");
            if (null == iResult) {
                kjsLogUtil.w("result is null");
                return;
            }
            kjsLogUtil.i(String.format("[command: %d] --- %s", command, iResult.DebugString()));
            if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                return;
            }

            if (command == CMD_GET_HOUSE_INFO) {
                updateHouseInfo((IApiResults.IGetHouseInfo)iResult);
            }

            if (command == CMD_GET_PROPERTY_INFO) {
                // CMD_GET_PROPERTY_INFO,   IApiResults.IPropertyInfo
                updateHousePropertyInfo((IApiResults.IPropertyInfo)iResult);
            }

            if (command == CMD_GET_USER_INFO) {
                // CMD_GET_USER_INFO,       IApiResults.IGetUserInfo
                updateHouseUserInfo((IApiResults.IGetUserInfo)iResult);
            }

        }

        private void updateHouseInfo(final IApiResults.IGetHouseInfo IHouseInfo) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvApartmentNo.setText("房屋编号： NO." + IHouseInfo.HouseId());
                    mTvApartmentLastModifyDate.setText("最后更新： " + IHouseInfo.ModifyDate());
                    mTvApartmentHuXing.setText(String.format("%d室%d厅%d卫", IHouseInfo.Bedrooms(), IHouseInfo.Livingrooms(), IHouseInfo.Bathrooms()));
                    mTvApartmentAcreage.setText(String.format("%d 平米", IHouseInfo.Acreage() / 100));
                    mTvApartmentYuYue.setText("");
                    mTvApartmentStatus.setText(translateRentStatus(IHouseInfo.RentStat()));

                    mTvApartment_floor.setText(String.format("%d/%d F", IHouseInfo.Floorthis(), IHouseInfo.FloorTotal()));
                    mTvApartment_fangxing.setText(IHouseInfo.FloorDesc());
                    mTvApartment_chanquan.setText("");
                    mTvApartment_decoration.setText(IHouseInfo.DecorateDesc());
                    mTvApartment_jungong.setText("");
                    mTvApartment_buydate.setText(String.format("%s 年", IHouseInfo.BuyDate()));

                    mCmdMgr.GetPropertyInfo(IHouseInfo.ProId());
                    mCmdMgr.GetUserInfo(IHouseInfo.Agency());

                }
            });
        }

        private void updateHousePropertyInfo(final IApiResults.IPropertyInfo propertyInfo) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvApartmentName.setText(propertyInfo.GetName());
                    mTvApartmentAddr.setText(propertyInfo.GetAddress());
                }
            });
        }

    private void updateHouseUserInfo(final IApiResults.IGetUserInfo UserInfo) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvApartment_username.setText("代理员：" + UserInfo.GetName());
                mTvApartment_telephone.setText("联系电话：" + UserInfo.GetPhoneNo());
//                UserInfo.GetHead()
            }
        });
    }

    private String translateRentStatus(int status) {
//        1: wait for rent, 2: rented, 3: Due, open for ordering
        switch (status) {
            case 1:
                return "待租";
            case 2:
                return "已租";
            case 3:
                return "到期";
        }
        return "";
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

            case  R.id.fab_share:
            {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, "我发现有个房子不错，点击查看");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(Intent.createChooser(intent, "分享房源"));
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
