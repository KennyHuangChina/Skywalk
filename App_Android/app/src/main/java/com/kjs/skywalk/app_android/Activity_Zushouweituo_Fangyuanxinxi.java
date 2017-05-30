package com.kjs.skywalk.app_android;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kjs.skywalk.control.kjsNumberPicker;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Activity_Zushouweituo_Fangyuanxinxi extends AppCompatActivity {

    private PopupWindowZhuangxiuSelector mZhuangxiuSelector = null;
    private ScrollView mContainer = null;

    private String mRoomString = "";
    private String mLoungeString = "";
    private String mToiletString = "";
    private int mRoomIndex= -1;
    private int mLoungeIndex = -1;
    private int mToiletIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo__fangyuanxinxi);
        TextView titleText = (TextView)findViewById(R.id.textViewActivityTitle);
        titleText.setText("租售委托-房源信息");
        ImageView closeButton = (ImageView)findViewById(R.id.imageViewActivityClose);
        closeButton.setVisibility(View.INVISIBLE);
        mContainer = (ScrollView)findViewById(R.id.scrollViewContainer);
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.imageViewActivityBack:
            {
                finish();
            }
            break;
            case R.id.imageViewActivityClose:
            {
                Intent intent =new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
            }
            break;

            case R.id.tv_huxing_selector:
            {
                showHuXingSelectorDlg();
            }
            break;

            case R.id.tv_zhuangxiu_selector:
            {
                if(mZhuangxiuSelector == null) {
                    mZhuangxiuSelector = new PopupWindowZhuangxiuSelector(this);
                }

                mZhuangxiuSelector.showAtLocation(mContainer, Gravity.BOTTOM, 0, 0);
                break;
            }

            case R.id.tv_date_selector:
            {
                showDateSelectorDlg();
            }
            break;

            case R.id.tv_next:
            {
//                if(!checkData()){
//                    return;
//                }
                startActivity(new Intent(Activity_Zushouweituo_Fangyuanxinxi.this, Activity_Zushouweituo_Jiagesheding.class));
            }
            break;
            case R.id.textViewSelectBlock: {
                startActivityForResult(new Intent(Activity_Zushouweituo_Fangyuanxinxi.this, Activity_Search_House.class), 0);
                break;
            }
            case R.id.textViewMaopi:
            case R.id.textViewJianzhuang:
            case R.id.textViewZhongdeng:
            case R.id.textViewJingzhuang:
            case R.id.textViewHaohua:{
                TextView tmp = (TextView)v;
                TextView button = (TextView)findViewById(R.id.tv_zhuangxiu_selector);
                button.setText(tmp.getText().toString());
                mZhuangxiuSelector.dismiss();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0) { //request from Activity_Search_House
            if(resultCode != 0) {
                TextView view = (TextView)findViewById(R.id.textViewSelectBlock);
                String name = data.getStringExtra("name");
                view.setText(name);
            }
        }
    }

    private boolean checkData() {
        TextView viewPropertyName = (TextView)findViewById(R.id.textViewSelectBlock);
        String propertyName = viewPropertyName.getText().toString();
        if(propertyName.isEmpty()) {
            commonFun.showToast_info(this, viewPropertyName, "小区名称不能为空");
            return false;
        }

        EditText viewNumber = (EditText)findViewById(R.id.editTextPropertyNumber);
        String number = viewNumber.getText().toString();
        if(number.isEmpty()) {
            commonFun.showToast_info(this, viewPropertyName, "楼栋号不能为空");
            return false;
        }

        EditText viewFloor = (EditText)findViewById(R.id.editTextFloor);
        String floor = viewFloor.getText().toString();
        EditText viewFloorTotal = (EditText)findViewById(R.id.editTextTotalFloor);
        String floorTotal = viewFloorTotal.getText().toString();

        int nFloor;
        int nFloorTotal;
        try {
            nFloor = Integer.valueOf(floor);
            if(nFloor <= 0) {
                commonFun.showToast_info(this, viewPropertyName, "楼层输入不正确");
                return false;
            }
        } catch (NumberFormatException e) {
            commonFun.showToast_info(this, viewPropertyName, "楼层输入不正确");
            return false;
        }
        try {
            nFloorTotal = Integer.valueOf(floorTotal);
            if(nFloorTotal <= 0) {
                commonFun.showToast_info(this, viewPropertyName, "总楼层数输入不正确");
                return false;
            }
        } catch (NumberFormatException e) {
            commonFun.showToast_info(this, viewPropertyName, "总楼层数输入不正确");
            return false;
        }

        if(nFloor > nFloorTotal){
            commonFun.showToast_info(this, viewPropertyName, "楼层应小于总楼层数");
            return false;
        }

        EditText viewRoomNumber = (EditText)findViewById(R.id.editTextRoomNumber);
        String roomNumber = viewRoomNumber.getText().toString();
        if(roomNumber.isEmpty()) {
            commonFun.showToast_info(this, viewRoomNumber, "门牌号不能为空");
            return false;
        }

        EditText viewArea = (EditText)findViewById(R.id.editTextArea);
        String area = viewArea.getText().toString();
        double nArea;
        try {
            nArea = Double.valueOf(area);
            if(nArea <= 0) {
                commonFun.showToast_info(this, viewPropertyName, "面积输入不正确");
                return false;
            }
        } catch (NumberFormatException e) {
            commonFun.showToast_info(this, viewPropertyName, "面积输入不正确");
            return false;
        }

        TextView viewHuxing = (TextView)findViewById(R.id.tv_huxing_selector);
        String huxing = viewHuxing.getText().toString();
        if(huxing.isEmpty()) {
            commonFun.showToast_info(this, viewPropertyName, "请选择户型");
            return false;
        }

        TextView viewZhuangxiu = (TextView)findViewById(R.id.tv_zhuangxiu_selector);
        String zhuangxiu = viewZhuangxiu.getText().toString();
        if(zhuangxiu.isEmpty()) {
            commonFun.showToast_info(this, viewPropertyName, "请选择装修程度");
            return false;
        }

        TextView viewDate = (TextView)findViewById(R.id.tv_date_selector);
        String date = viewDate.getText().toString();
        if(date.isEmpty()) {
            commonFun.showToast_info(this, viewPropertyName, "请选择购买日期");
            return false;
        }
        return true;
    }

    private AlertDialog mDateSelectorDlg;
    private DatePicker mDatePicker;
    private void showDateSelectorDlg() {
        if (mDateSelectorDlg == null) {
            mDateSelectorDlg = new AlertDialog.Builder(this).create();
        }
        mDateSelectorDlg.show();
        mDateSelectorDlg.setContentView(R.layout.dialog_fangyuanxinxi_dateselector);

        mDatePicker = (DatePicker)mDateSelectorDlg.findViewById(R.id.datePicker2);

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
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String date = df.format(calendar.getTime());
                TextView tmp = (TextView)findViewById(R.id.tv_date_selector);
                tmp.setText(date);

                mDateSelectorDlg.dismiss();
            }
        });
    }

    private AlertDialog mHuXingSelectorDlg;
    private void showHuXingSelectorDlg() {
        if (mHuXingSelectorDlg == null) {
            mHuXingSelectorDlg = new AlertDialog.Builder(this).create();
        }
        mHuXingSelectorDlg.show();
        mHuXingSelectorDlg.setContentView(R.layout.dialog_fangyuanxinxi_fangxing);

        mRoomIndex = 0;
        mToiletIndex = 0;
        mLoungeIndex = 0;

        kjsNumberPicker npShi = (kjsNumberPicker)mHuXingSelectorDlg.findViewById(R.id.np_unit_shi);
        kjsNumberPicker npTing = (kjsNumberPicker)mHuXingSelectorDlg.findViewById(R.id.np_unit_ting);
        final kjsNumberPicker npWei = (kjsNumberPicker)mHuXingSelectorDlg.findViewById(R.id.np_unit_wei);
        final String[] arrShi = {"1室", "2室", "3室", "4室", "5室"};
        final String[] arrTing = {"1厅", "2厅", "3厅", "4厅", "5厅"};
        final String[] arrWei = {"1卫", "2卫", "3卫", "4卫", "5卫"};

        npShi.setDisplayedValues(arrShi);
        npShi.setMinValue(0);
        npShi.setMaxValue(arrShi.length - 1);
        npShi.setDividerColor();

        npTing.setDisplayedValues(arrTing);
        npTing.setMinValue(0);
        npTing.setMaxValue(arrTing.length - 1);
        npTing.setDividerColor();

        npWei.setDisplayedValues(arrWei);
        npWei.setMinValue(0);
        npWei.setMaxValue(arrWei.length - 1);
        npWei.setDividerColor();

        npWei.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mToiletIndex = newVal;
            }
        });

        npTing.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mLoungeIndex = newVal;
            }
        });

        npShi.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mRoomIndex = newVal;
            }
        });

        TextView tvBack = (TextView) mHuXingSelectorDlg.findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHuXingSelectorDlg.dismiss();
            }
        });

        TextView tvConfirm = (TextView) mHuXingSelectorDlg.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToiletString = arrWei[mToiletIndex];
                mRoomString = arrShi[mRoomIndex];
                mLoungeString = arrTing[mLoungeIndex];
                TextView tmp = (TextView)findViewById(R.id.tv_huxing_selector);
                tmp.setText(mRoomString + mLoungeString + mToiletString);
                mHuXingSelectorDlg.dismiss();
            }
        });
    }
}
