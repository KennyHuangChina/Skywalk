package com.kjs.skywalk.app_android.Apartment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.kjs.skywalk.app_android.SKLocalSettings;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdRes;
import com.kjs.skywalk.communicationlibrary.IApiResults;
import com.kjs.skywalk.control.ExpandedView;
import com.kjs.skywalk.control.LinearLayout_AdaptiveText;
import com.kjs.skywalk.control.SliderView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import me.iwf.photopicker.PhotoPreview;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID;

public class Activity_ApartmentDetail extends SKBaseActivity {
    private String TAG = getClass().getSimpleName();
    private ArrayList<String> mImageLst;
//    private int mHouseId = -1;

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
    private TextView mTvAapartment_rentalfee;

    private TextView mTvApartment_floor;
    private TextView mTvApartment_fangxing;
    private TextView mTvApartment_chanquan;
    private TextView mTvApartment_decoration;
    private TextView mTvApartment_jungong;
    private TextView mTvApartment_buydate;

    private TextView mTvApartment_username;
    private TextView mTvApartment_telephone;

    private LinearLayout mLl_housetag_container;
    private LinearLayout_AdaptiveText mLla_facilitylist;
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
        mTvApartmentRentPrice = (TextView) findViewById(R.id.tv_apartment_rent_price);
        mTvApartmentPricing = (TextView) findViewById(R.id.tv_apartment_pricing);
        mTvAapartment_rentalfee = (TextView) findViewById(R.id.tv_apartment_rentalfee);

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

        mLla_facilitylist = (LinearLayout_AdaptiveText) findViewById(R.id.lla_facilitylist);
        mLla_facilitylist.setLayoutPadding(0, 18, 0, 18);

        SliderView sView = (SliderView) findViewById(R.id.sv_view);
        mImageLst = commonFun.getTestPicList(this);
        sView.setImages(mImageLst, mSvListener);

        // mHouseId get in SKBaseActivity
//        Bundle bundle = getIntent().getExtras();
//        mHouseId = bundle.getInt("houseId");

        CommandManager.getCmdMgrInstance(this).GetHouseInfo(mHouseId, false);
        kjsLogUtil.i("GetHouseInfo: " + mHouseId);

        SKLocalSettings.browsing_history_insert(this, String.valueOf(mHouseId));
        kjsLogUtil.i("idLst:" + SKLocalSettings.browsing_history_read(this));

        CommandManager.getCmdMgrInstance(this).GetBriefPublicHouseInfo(mHouseId);

//        小区物业信息，GetPropertyInfo
//        CommandManager.getCmdMgrInstance(this).GetPropertyInfo(2);

//        代理员信息，GetUserInfo
//        CommandManager.getCmdMgrInstance(this).GetUserInfo(1);

//        房屋设施 -> GetHouseFacilityList
        CommandManager.getCmdMgrInstance(this).GetHouseFacilityList(mHouseId);

//        调用方式 GetHouseSeeAppointmentList(house_id, 0, 0)，返回的 total 就试预约人数
        CommandManager.getCmdMgrInstance(this).GetHouseSeeAppointmentList(mHouseId, 0, 0);

     }

    @Override
    public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
        kjsLogUtil.i("Activity_ApartmentDetail::onCommandFinished");
        if (null == iResult) {
            kjsLogUtil.w("result is null");
            return;
        }
        kjsLogUtil.i(String.format("[command: %d] --- %s", command, iResult.DebugString()));
        if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
            kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
            // Kenny: some commands need to process the return error
//            return;
        }

        switch (command) {
            case CmdID.CMD_GET_HOUSE_INFO: {
                updateHouseInfo((IApiResults.IGetHouseInfo) iResult);
                break;
            }
            case CmdID.CMD_GET_BRIEF_PUBLIC_HOUSE_INFO: {
                // CMD_GET_BRIEF_PUBLIC_HOUSE_INFO, IApiResults.IHouseDigest & IApiResults.IResultList(IApiResults.IHouseTag)
                updateBriefHouseInfo((IApiResults.IHouseDigest)iResult);
                break;
            }
            case CmdID.CMD_GET_USER_INFO: {
                // CMD_GET_USER_INFO,       IApiResults.IGetUserInfo
                updateHouseUserInfo((IApiResults.IGetUserInfo)iResult);
                break;
            }
            case CmdID.CMD_APPOINT_HOUSE_SEE_LST: {
                // IApiResults.IHouseOrdertable
                updateHouseOrderTable(iResult);
                break;
            }
            case CmdID.CMD_GET_HOUSEFACILITY_LIST: {
                // IApiResults.IResultList(IApiResults.IHouseFacilityInfo)
                updateHouseFacilityList(iResult);
                break;
            }
            case CmdID.CMD_APPOINT_SEE_HOUSE: {
                int nErr = iResult.GetErrCode();
                if (CmdRes.CMD_RES_NOERROR == nErr) {
                    commonFun.showToast_info(getApplicationContext(), mTvApartmentNo, "预约看房成功");
                } else if (CmdRes.CMD_RES_ALREADY_EXIST == nErr) {
                    commonFun.showToast_info(getApplicationContext(), mTvApartmentNo, "您已经预约看该房");
                } else {
                   commonFun.showToast_info(getApplicationContext(), mTvApartmentNo, String.format("预约看房失败(0x%x): %s", nErr, iResult.GetErrDesc()));
                }
                break;
            }
        }
    }

    private void updateHouseInfo(final IApiResults.IGetHouseInfo IHouseInfo) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvApartmentNo.setText("房屋编号： NO." + IHouseInfo.HouseId());
                mTvApartmentLastModifyDate.setText("最后更新： " + IHouseInfo.ModifyDate());
//                    mTvApartmentHuXing.setText(String.format("%d室%d厅%d卫", IHouseInfo.Bedrooms(), IHouseInfo.Livingrooms(), IHouseInfo.Bathrooms()));
                mTvApartmentHuXing.setText(commonFun.getHouseTypeString(IHouseInfo.Bedrooms(), IHouseInfo.Livingrooms(), IHouseInfo.Bathrooms()));
                mTvApartmentAcreage.setText(String.format("%d 平米", IHouseInfo.Acreage() / 100));
                mTvApartmentYuYue.setText("");
                mTvApartmentStatus.setText(translateRentStatus(IHouseInfo.RentStat()));

                mTvApartment_floor.setText(String.format("%d/%d F", IHouseInfo.Floorthis(), IHouseInfo.FloorTotal()));
                mTvApartment_fangxing.setText(IHouseInfo.FloorDesc());
                mTvApartment_chanquan.setText("");
                mTvApartment_decoration.setText(IHouseInfo.DecorateDesc());
                mTvApartment_jungong.setText("");
                mTvApartment_buydate.setText(String.format("%s 年", IHouseInfo.BuyDate()));

                getPropertyInfoFromServer(IHouseInfo.ProId());
                getUserInfoFromServer(IHouseInfo.Agency());
            }
        });
    }

    private void getPropertyInfoFromServer(int nPropId) {
        CommandManager.getCmdMgrInstance(this).GetPropertyInfo(nPropId);
    }

    private void getUserInfoFromServer(int uid) {
        CommandManager.getCmdMgrInstance(this).GetUserInfo(uid);
    }

    private void updateBriefHouseInfo(final IApiResults.IHouseDigest briefHouseInfo) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvApartmentName.setText(briefHouseInfo.GetProperty());         // name
                    mTvApartmentAddr.setText(briefHouseInfo.GetPropertyAddr());     // address

                    List<commonFun.TextDefine> rentPriceDefines = new ArrayList<commonFun.TextDefine>(
                            Arrays.asList(
                                    new commonFun.TextDefine(String.valueOf(briefHouseInfo.GetRental() / 100), 45, Color.parseColor("#FF3F29")),
                                    new commonFun.TextDefine("（元/月）", 36, ContextCompat.getColor(Activity_ApartmentDetail.this, R.color.colorTextNormal))
                            )
                    );
                    mTvApartmentRentPrice.setText(commonFun.getSpannableString(rentPriceDefines));
//                    mTvApartmentRentPrice.setText(String.format("%d（元/月）", briefHouseInfo.GetRental() / 100));

                    List<commonFun.TextDefine> pricingDefines = new ArrayList<>();
                    int pricingValue = briefHouseInfo.GetPricing() / 100;
                    String pricing;
                    if (pricingValue > 0) {
                        pricing = String.format("升%d", pricingValue);
                        pricingDefines.add(new commonFun.TextDefine(pricing, 36, Color.parseColor("#FF3F29")));
                    } else {
                        pricing = String.format("降%d", Math.abs(pricingValue));
                        pricingDefines.add(new commonFun.TextDefine(pricing, 36, Color.parseColor("#32BE84")));
                    }
                    mTvApartmentPricing.setText(commonFun.getSpannableString(pricingDefines));

                    // 是否包含物业费
                    boolean isIncludeRentalFee = briefHouseInfo.IsRentalIncPropFee();
                    if (isIncludeRentalFee) {
                        mTvAapartment_rentalfee.setText("含物业费");
                    } else {
                        mTvAapartment_rentalfee.setText("不含物业费");
                    }

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

    private void updateHouseFacilityList(final IApiResults.ICommon facilityList) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Object> list = ((IApiResults.IResultList) facilityList).GetList();
                ArrayList<String> nameList = new ArrayList<String>();
                for (Object item : list) {
                    IApiResults.IHouseFacilityInfo facilityInfo = (IApiResults.IHouseFacilityInfo) item;
                    if (facilityInfo.GetQty() > 0) {
                        nameList.add(facilityInfo.GetName());
                    }
                }
                mLla_facilitylist.setTextList(nameList);
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

    @Override
    protected void onResume() {
        super.onResume();

        // Disable the bottom tool bar if user not login in
        findViewById(R.id.rl_bottom_tools).setVisibility(IsLogined() ? View.VISIBLE : View.GONE);
    }

    /**
     *  Get offset date based on today
     *  @param  off_date > 0 : date after today, < 0 : date before today
     *  @return
     */
    private String getOffDay(int off_date) {
        String offDay = "";

        Date today = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(calendar.DATE, off_date);
        Date date = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    private AlertDialog mOrderDlg;
    private void showOrderDlg() {
        if (mOrderDlg == null) {
            mOrderDlg = new AlertDialog.Builder(this).create();
        }
        mOrderDlg.show();
        mOrderDlg.setContentView(R.layout.dialog_apartment_order);
        mOrderDlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        final TextView tvDateSelector = (TextView) mOrderDlg.findViewById(R.id.tv_date_selector);
//        tvDateSelector.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDateSelectorDlg();
//            }
//        });

        final TextView tvOtherDate = (TextView) mOrderDlg.findViewById(R.id.rb_otherdays);
        tvOtherDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateSelectorDlg();
            }
        });

        ((RadioButton)mOrderDlg.findViewById(R.id.rb_tomorrow)).setChecked(true);
        tvDateSelector.setText(getOffDay(1));
        // set date selector check status
        Boolean isOtherDaysChecked = ((RadioButton)mOrderDlg.findViewById(R.id.rb_otherdays)).isChecked();
        tvDateSelector.setEnabled(isOtherDaysChecked);

        final RadioGroup radioGroup = (RadioGroup) mOrderDlg.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                // log
                if (radioGroup.findViewById(i) instanceof RadioButton) {
                    RadioButton radBtn = (RadioButton) radioGroup.findViewById(i);
//                    commonFun.showToast_info(Activity_ApartmentDetail.this, radioGroup, "selected : " + radBtn.getText());
                } else {
//                    commonFun.showToast_info(Activity_ApartmentDetail.this, radioGroup, "selected id: " + i);
                }

                switch (i) {
                    case R.id.rb_today:
                        tvDateSelector.setEnabled(false);
                        tvDateSelector.setText(getOffDay(0));
                        break;
                    case R.id.rb_tomorrow:
                        tvDateSelector.setEnabled(false);
                        tvDateSelector.setText(getOffDay(1));
                        break;
                    case R.id.rb_aftertomorrow:
                        tvDateSelector.setEnabled(false);
                        tvDateSelector.setText(getOffDay(2));
                        break;
                    case R.id.rb_otherdays:
                        tvDateSelector.setEnabled(true);
                        break;
                }
            }
        });

        // initialize Bandwidth spinner
        ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(this, R.layout.spinner_item);
        adapterTime.add("8:00 - 9:00");
        adapterTime.add("9:00 - 10:00");
        adapterTime.add("10:00 - 11:00");
        adapterTime.add("11:00 - 12:00");
        adapterTime.add("12:00 - 13:00");
        adapterTime.add("13:00 - 14:00");
        adapterTime.add("14:00 - 15:00");
        adapterTime.add("15:00 - 16:00");
        adapterTime.add("16:00 - 17:00");
        adapterTime.add("17:00 - 18:00");
        adapterTime.add("18:00 - 19:00");
        adapterTime.add("19:00 - 20:00");
        adapterTime.add("20:00 - 21:00");
        adapterTime.setDropDownViewResource(R.layout.spinner_dropdown_item);

        final String[] timePeriod = {""};
        final Spinner spTimeSelector = (Spinner)mOrderDlg.findViewById(R.id.sp_timeselector);
        spTimeSelector.setAdapter(adapterTime);
        spTimeSelector.setSelection(2);
        spTimeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kjsLogUtil.i("position:" + position + ", id:" + id);
                timePeriod[0] = parent.getItemAtPosition(position).toString();
//                commonFun.showToast_info(Activity_ApartmentDetail.this, spTimeSelector, "selected: " + timePeriod[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final EditText tvAppointDesc = (EditText) mOrderDlg.findViewById(R.id.appointDesc);


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
                String appointDate = (String) tvDateSelector.getText();

                String[] times = timePeriod[0].split(" - ");
                // appointment begin time
                String timeBegin = appointDate + " " + times[0];
                // appointment end time
                String timeEnd = appointDate + " " + times[1];
                // appoint description
                String appointDesc = tvAppointDesc.getText().toString();

                // check
                if (appointDesc.isEmpty()) {
                    tvAppointDesc.setHintTextColor(Color.rgb(255,0,0));
                    tvAppointDesc.requestFocus();
                    return;
                }

                // make an appointment for house seeing
                CommandManager.getCmdMgrInstance(Activity_ApartmentDetail.this/*, Activity_ApartmentDetail.this,
                        Activity_ApartmentDetail.this*/).MakeAppointment_SeeHouse(mHouseId, "", timeBegin, timeEnd, appointDesc);
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
        final DatePicker mDatePicker = (DatePicker)mDateSelectorDlg.findViewById(R.id.datePicker2);

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
                Calendar calendar = Calendar.getInstance();
                calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String date = df.format(calendar.getTime());
                TextView tmp = (TextView)mOrderDlg.findViewById(R.id.tv_date_selector);
                tmp.setText(date);

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
