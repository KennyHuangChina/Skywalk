package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Zushouweituo_Kanfangshijian extends SKBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo_kanfangshijian);

        TextView titleText = (TextView)findViewById(R.id.textViewActivityTitle);
        titleText.setText("世贸蝶湖湾17幢1608室");
        findViewById(R.id.imageViewActivityClose).setVisibility(View.INVISIBLE);

        findViewById(R.id.textViewWorkingDayEvening).setSelected(true);
        findViewById(R.id.textViewWorkingDayMorning).setSelected(true);
        findViewById(R.id.textViewWorkingDayNoon).setSelected(true);
        findViewById(R.id.textViewHolidayEvening).setSelected(true);
        findViewById(R.id.textViewHolidayMorning).setSelected(true);
        findViewById(R.id.textViewHolidayNoon).setSelected(true);

    }

    private boolean mWorkingDayMorming = true;
    private boolean mWorkingDayNoon = true;
    private boolean mWorkingDayEvening = true;
    private boolean mHolidayMorning = true;
    private boolean mHolidayNoon = true;
    private boolean mHolidayEvening = true;
    private String mTime = "";

    private boolean collectData(){
        mWorkingDayEvening = findViewById(R.id.textViewWorkingDayEvening).isSelected();
        mWorkingDayMorming = findViewById(R.id.textViewWorkingDayMorning).isSelected();
        mWorkingDayNoon = findViewById(R.id.textViewWorkingDayNoon).isSelected();
        mHolidayEvening = findViewById(R.id.textViewHolidayEvening).isSelected();
        mHolidayMorning = findViewById(R.id.textViewHolidayMorning).isSelected();
        mHolidayNoon = findViewById(R.id.textViewHolidayNoon).isSelected();
        EditText text = (EditText)findViewById(R.id.editText);
        mTime = text.getText().toString();

        if(!mWorkingDayEvening && !mWorkingDayMorming && !mWorkingDayNoon &&
                !mHolidayEvening && !mHolidayMorning && !mHolidayNoon && mTime.isEmpty()) {
            commonFun.showToast_info(this, text, "请至少选择一个看房时间或输入自定义看房时间");
            return false;
        }

        return true;
    }


    private void doSave() {
        if(!collectData()) {
            return;
        }

        //upload data to server
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title: {
                finish();
            }
            break;
            case R.id.textViewWorkingDayMorning:
            case R.id.textViewWorkingDayNoon:
            case R.id.textViewWorkingDayEvening:
            case R.id.textViewHolidayMorning:
            case R.id.textViewHolidayNoon:
            case R.id.textViewHolidayEvening:
                v.setSelected(!v.isSelected());
            break;
            case R.id.imageViewActivityBack:
                finish();
            break;
            case R.id.textViewSave:
                doSave();
                break;
        }
    }
}
