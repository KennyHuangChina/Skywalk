package com.kjs.skywalk.app_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Activity_Zushouweituo_Jiagesheding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo__jiagesheding);
        TextView titleText = (TextView)findViewById(R.id.textViewActivityTitle);
        titleText.setText("租售委托-价格设定");

        ToggleButton button = (ToggleButton)findViewById(R.id.toggleButton);
        if(button.isChecked()) {
            EditText v = (EditText)findViewById(R.id.editTextPropertyFee);
            v.setEnabled(false);
            v.setHint("房租中已经包含物业费");
        }
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText v = (EditText)findViewById(R.id.editTextPropertyFee);
                if(isChecked) {
                    v.setEnabled(false);
                    v.setHint("房租中已经包含物业费");
                } else {
                    v.setEnabled(true);
                    v.setHint("请输入物业费");
                }
            }
        });
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_prev:
            {
                finish();
            }
            break;
            case R.id.tv_next:
            {
                if(!checkData()) {
                    return;
                }
                startActivity(new Intent(this, Activity_Zushouweituo_Xuanzedaili.class));
            }
            break;
            case R.id.imageViewActivityBack: {
                finish();
                break;
            }
            case R.id.imageViewActivityClose: {
                Intent intent =new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
                break;
            }
        }
    }

    private boolean checkData() {
        EditText viewRent = (EditText)findViewById(R.id.editTextRent);
        String rent = viewRent.getText().toString();
        EditText viewMinRent = (EditText)findViewById(R.id.editTextMinRent);
        String minRent = viewMinRent.getText().toString();

        int nRent;
        int nMinRent;
        try {
            nRent = Integer.valueOf(rent);
            if(nRent <= 0) {
                commonFun.showToast_info(this, viewRent, "租金输入不正确");
                return false;
            }
        } catch (NumberFormatException e) {
            commonFun.showToast_info(this, viewRent, "租金输入不正确");
            return false;
        }
        try {
            nMinRent = Integer.valueOf(minRent);
            if(nMinRent <= 0) {
                commonFun.showToast_info(this, viewRent, "最低租金输入不正确");
                return false;
            }
        } catch (NumberFormatException e) {
            commonFun.showToast_info(this, viewRent, "最低租金输入不正确");
            return false;
        }

        if(nRent < nMinRent){
            commonFun.showToast_info(this, viewRent, "租金应大于最小租金");
            return false;
        }

        ToggleButton button = (ToggleButton)findViewById(R.id.toggleButton);
        if(!button.isChecked()) {
            EditText viewFee = (EditText)findViewById(R.id.editTextPropertyFee);
            String fee = viewFee.getText().toString();
            int nFee;
            try {
                nFee = Integer.valueOf(fee);
                if(nFee <= 0) {
                    commonFun.showToast_info(this, viewFee, "物业费输入不正确");
                    return false;
                }
            } catch (NumberFormatException e) {
                commonFun.showToast_info(this, viewFee, "物业费输入不正确");
                return false;
            }
        }

        EditText viewPrice = (EditText)findViewById(R.id.editTextPrice);
        String price = viewPrice.getText().toString();
        if(!price.isEmpty()) {
            EditText viewMinPrice = (EditText)findViewById(R.id.editTextMinPrice);
            String minPrice = viewMinPrice.getText().toString();

            int nPrice;
            int nMinPrice;
            try {
                nPrice = Integer.valueOf(price);
                if(nPrice <= 0) {
                    commonFun.showToast_info(this, viewPrice, "售价输入不正确");
                    return false;
                }
            } catch (NumberFormatException e) {
                commonFun.showToast_info(this, viewPrice, "售价输入不正确");
                return false;
            }
            try {
                nMinPrice = Integer.valueOf(minPrice);
                if(nMinPrice <= 0) {
                    commonFun.showToast_info(this, viewPrice, "最低售价输入不正确");
                    return false;
                }
            } catch (NumberFormatException e) {
                commonFun.showToast_info(this, viewPrice, "最低售价输入不正确");
                return false;
            }

            if(nPrice < nMinPrice){
                commonFun.showToast_info(this, viewPrice, "售价应大于最低售价");
                return false;
            }
        }

        return true;
    }
}
