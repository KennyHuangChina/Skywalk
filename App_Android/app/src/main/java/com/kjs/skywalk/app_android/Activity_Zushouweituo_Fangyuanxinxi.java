package com.kjs.skywalk.app_android;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.kjs.skywalk.control.kjsNumberPicker;

public class Activity_Zushouweituo_Fangyuanxinxi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo__fangyuanxinxi);
        TextView titleText = (TextView)findViewById(R.id.textViewActivityTitle);
        titleText.setText("租售委托-房源信息");
        ImageView closeButton = (ImageView)findViewById(R.id.imageViewActivityClose);
        closeButton.setVisibility(View.INVISIBLE);
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
                break;
            }

            case R.id.tv_date_selector:
            {
                showDateSelectorDlg();
            }
            break;

            case R.id.tv_next:
            {
                startActivity(new Intent(Activity_Zushouweituo_Fangyuanxinxi.this, Activity_Zushouweituo_Jiagesheding.class));
            }
            break;
            case R.id.textViewSelectBlock: {
                startActivityForResult(new Intent(Activity_Zushouweituo_Fangyuanxinxi.this, Activity_Search_House.class), 0);
            }
            break;
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
