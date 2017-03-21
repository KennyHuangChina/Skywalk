package com.kjs.skywalk.app_android;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.kjs.skywalk.control.kjsNumberPicker;

public class Activity_Zushouweituo_Fangyuanxinxi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo__fangyuanxinxi);
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title:
            {
                finish();
            }
            break;

            case R.id.tv_huxing_selector:
            {
                showHuXingSelectorDlg();
            }
            break;

            case R.id.tv_date_selector:
            {
                showDateSelectorDlg();
            }
            break;
        }
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

    private AlertDialog mHuXingSelectorDlg;
    private void showHuXingSelectorDlg() {
        if (mHuXingSelectorDlg == null) {
            mHuXingSelectorDlg = new AlertDialog.Builder(this).create();
        }
        mHuXingSelectorDlg.show();
        mHuXingSelectorDlg.setContentView(R.layout.dialog_fangyuanxinxi_fangxing);

        kjsNumberPicker npShi = (kjsNumberPicker)mHuXingSelectorDlg.findViewById(R.id.np_unit_shi);
        kjsNumberPicker npTing = (kjsNumberPicker)mHuXingSelectorDlg.findViewById(R.id.np_unit_ting);
        kjsNumberPicker npWei = (kjsNumberPicker)mHuXingSelectorDlg.findViewById(R.id.np_unit_wei);
        String[] arrShi = {"1室", "2室", "3室", "4室", "5室"};
        String[] arrTing = {"1厅", "2厅", "3厅", "4厅", "5厅"};
        String[] arrWei = {"1卫", "2卫", "3卫", "4卫", "5卫"};

        npShi.setDisplayedValues(arrShi);
        npShi.setMinValue(0);
        npShi.setMaxValue(arrShi.length - 1);

        npTing.setDisplayedValues(arrTing);
        npTing.setMinValue(0);
        npTing.setMaxValue(arrTing.length - 1);

        npWei.setDisplayedValues(arrWei);
        npWei.setMinValue(0);
        npWei.setMaxValue(arrWei.length - 1);

        npWei.setValue(3);
        npWei.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
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
                mHuXingSelectorDlg.dismiss();
            }
        });
    }
}
