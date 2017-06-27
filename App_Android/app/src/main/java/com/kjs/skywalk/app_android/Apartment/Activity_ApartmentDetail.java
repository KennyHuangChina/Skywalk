package com.kjs.skywalk.app_android.Apartment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.kjs.skywalk.app_android.Activity_HouseholdDeliverables;
import com.kjs.skywalk.app_android.Activity_Search;
import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.SKBaseActivity;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.IApiResults;
import com.kjs.skywalk.control.ExpandedView;
import com.kjs.skywalk.control.LinearLayout_AdaptiveText;
import com.kjs.skywalk.control.SliderView;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPreview;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_APPOINT_HOUSE_SEE_LST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_BRIEF_PUBLIC_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;
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
    private TextView mTvApartmentRentPrice;
    private TextView mTvApartmentPricing;

    private TextView mTvApartment_floor;
    private TextView mTvApartment_fangxing;
    private TextView mTvApartment_chanquan;
    private TextView mTvApartment_decoration;
    private TextView mTvApartment_jungong;
    private TextView mTvApartment_buydate;

    private TextView mTvApartment_username;
    private TextView mTvApartment_telephone;

    private LinearLayout mLl_housetag_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__apartment_detail);

        LinearLayout_AdaptiveText testLayout = (LinearLayout_AdaptiveText) findViewById(R.id.lla_test);
        ArrayList<String> testList = new ArrayList<>();
        testList.add("字符串1");
        testList.add("字符串2");
        testList.add("字符串3");
        testList.add("字符串4");
        testList.add("字符串5");
        testLayout.setTextList(testList);

        ArrayList<String> testList1 = new ArrayList<>();
        testList1.add("字符串6");
        testList1.add("字符串7");
        testList1.add("字符串8");
        testList1.add("字符串9");
        testList1.add("字符串10");
        testLayout.setTextList(testList1);

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
        mTvApartmentRentPrice = (TextView) findViewById(R.id.tv_apartment_rent_price);
        mTvApartmentPricing = (TextView) findViewById(R.id.tv_apartment_pricing);

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

        mLl_housetag_container = (LinearLayout) findViewById(R.id.ll_housetag_container);

        SliderView sView = (SliderView) findViewById(R.id.sv_view);
        mImageLst = commonFun.getTestPicList(this);
        sView.setImages(mImageLst, mSvListener);

        Bundle bundle = getIntent().getExtras();
        mHouseId = bundle.getInt("houseId");

        mCmdMgr = new CommandManager(this, this, this);
        mCmdMgr.GetHouseInfo(mHouseId, false);
        kjsLogUtil.i("GetHouseInfo: " + mHouseId);

        mCmdMgr.GetBriefPublicHouseInfo(mHouseId);

//        小区物业信息，GetPropertyInfo
//        mCmdMgr.GetPropertyInfo(2);

//        代理员信息，GetUserInfo
//        mCmdMgr.GetUserInfo(1);

//        房屋设施 -> GetHouseFacilityList
        mCmdMgr.GetHouseFacilityList(mHouseId);

//        调用方式 GetHouseSeeAppointmentList(house_id, 0, 0)，返回的 total 就试预约人数
        mCmdMgr.GetHouseSeeAppointmentList(mHouseId, 0, 0);

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

            if (command == CMD_GET_BRIEF_PUBLIC_HOUSE_INFO) {
                // CMD_GET_BRIEF_PUBLIC_HOUSE_INFO, IApiResults.IHouseDigest & IApiResults.IResultList(IApiResults.IHouseTag)
                updateBriefHouseInfo((IApiResults.IHouseDigest)iResult);
            }

            if (command == CMD_GET_USER_INFO) {
                // CMD_GET_USER_INFO,       IApiResults.IGetUserInfo
                updateHouseUserInfo((IApiResults.IGetUserInfo)iResult);
            }

            if (command == CMD_APPOINT_HOUSE_SEE_LST) {
                // IApiResults.IHouseOrdertable
                updateHouseOrderTable(iResult);
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

        private void updateBriefHouseInfo(final IApiResults.IHouseDigest briefHouseInfo) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvApartmentName.setText(briefHouseInfo.GetProperty());         // name
                    mTvApartmentAddr.setText(briefHouseInfo.GetPropertyAddr());     // address
                    mTvApartmentRentPrice.setText(String.format("%d（元/月）", briefHouseInfo.GetRental() / 100));

                    int pricingValue = briefHouseInfo.GetPricing() / 100;
                    String pricing;
                    if (pricingValue > 0) {
                        pricing = String.format("升%d", pricingValue);
                    } else {
                        pricing = String.format("降%d", Math.abs(pricingValue));
                    }
                    mTvApartmentPricing.setText(pricing);

                    ArrayList<Object> houseTags = ((IApiResults.IResultList) briefHouseInfo).GetList();
                    ArrayList<ClassDefine.HouseTag> tagList = new ArrayList<>();
                    for (Object houseTagObj : houseTags) {
                        IApiResults.IHouseTag tag = (IApiResults.IHouseTag) houseTagObj;
                        tagList.add(new ClassDefine.HouseTag(tag.GetTagId(), tag.GetName()));
//                        tagList.add(new ClassDefine.HouseTag(tag.GetTagId(), tag.GetName()));
//                        tagList.add(new ClassDefine.HouseTag(tag.GetTagId(), tag.GetName()));
                        kjsLogUtil.i(String.format("tag_id: %d, tag_name:%s", tag.GetTagId(), tag.GetName()));
                    }

                    int totalTagUsed = 0;

                    while (totalTagUsed < tagList.size()) {
                        List<ClassDefine.HouseTag> tmpList = tagList.subList(totalTagUsed, tagList.size());
                        int count = addHouseTagToContainer(mLl_housetag_container, tmpList);
                        totalTagUsed += count;
                    }
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

    private void updateHouseOrderTable(final IApiResults.ICommon orderTable) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IApiResults.IResultList res = (IApiResults.IResultList) orderTable;
                mTvApartmentYuYue.setText(String.valueOf(res.GetTotalNumber()));
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

    private int addHouseTagToContainer(LinearLayout container, List<ClassDefine.HouseTag> houseTags) {
        if(houseTags == null || houseTags.size() == 0) {
            return 0;
        }

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        int totalWidth = 0;
        int count = 0;
        for(int i = 0; i < houseTags.size(); i ++) {
            ClassDefine.TextItem item = createTextItem(houseTags.get(i));
            if((totalWidth + item.mTextViewWidth <= (mActScreenWidth - 100)) || (i == 0 && item.mTextViewWidth > (mActScreenWidth - 100))) {
                layout.addView(item.mView);
                totalWidth += item.mTextViewWidth;
                count += 1;
            } else {
                break;
            }
        }

        container.addView(layout);
        return count;
    }

    private ClassDefine.TextItem createTextItem(ClassDefine.HouseTag tag) {
        int paddingLeft, paddingRight, paddingTop, paddingBottom;
        int marginLeft, marginRight, marginTop, marginBottom;
        paddingLeft = paddingRight = 25;
        paddingTop = paddingBottom = 18;
        marginLeft = marginRight = marginTop = marginBottom = 11;

        ClassDefine.TextItem item = new ClassDefine.TextItem();
        TextView textView = new TextView(this);

        commonFun.setHouseTagStyleById(textView, tag.tagName, tag.tagId);

        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        textView.setLayoutParams(layoutParams);

        float size = textView.getPaint().measureText(tag.tagName);
        item.mView = textView;
        item.mTextViewWidth = (int)size + paddingLeft + paddingRight + marginLeft + marginRight;
        return item;
    }

}
