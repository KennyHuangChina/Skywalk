package com.kjs.skywalk.app_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Activity_Zushouweituo_Jiagesheding extends SKBaseActivity {

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
                if(!collectData()) {
//                    return;
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

    private boolean collectData() {
        EditText viewRent = (EditText)findViewById(R.id.editTextRent);
        String rent = viewRent.getText().toString();
        EditText viewMinRent = (EditText)findViewById(R.id.editTextMinRent);
        String minRent = viewMinRent.getText().toString();
        EditText viewPrice = (EditText)findViewById(R.id.editTextPrice);
        String price = viewPrice.getText().toString();
        EditText viewMinPrice = (EditText)findViewById(R.id.editTextMinPrice);
        String minPrice = viewMinPrice.getText().toString();

        if(rent.isEmpty() && minRent.isEmpty() && price.isEmpty() && minPrice.isEmpty()) {
            commonFun.showToast_info(this, viewRent, "租金或售价至少填写一种");
            return false;
        }

        if(!rent.isEmpty()) {
            double nRent;
            double nMinRent;
            try {
                nRent = Double.valueOf(rent);
                if (nRent <= 0) {
                    commonFun.showToast_info(this, viewRent, "租金输入不正确");
                    return false;
                }
            } catch (NumberFormatException e) {
                commonFun.showToast_info(this, viewRent, "租金输入不正确");
                return false;
            }
            try {
                nMinRent = Double.valueOf(minRent);
                if (nMinRent <= 0) {
                    commonFun.showToast_info(this, viewRent, "最低租金输入不正确");
                    return false;
                }
            } catch (NumberFormatException e) {
                commonFun.showToast_info(this, viewRent, "最低租金输入不正确");
                return false;
            }

            if (nRent < nMinRent) {
                commonFun.showToast_info(this, viewRent, "租金应大于最小租金");
                return false;
            }

            ClassDefine.HouseInfoForCommit.rental = (int) (nRent * 100);
            ClassDefine.HouseInfoForCommit.minRental = (int) (nMinRent * 100);
        } else {
            ClassDefine.HouseInfoForCommit.rental = 0;
            ClassDefine.HouseInfoForCommit.minRental = 0;
        }

        ToggleButton button = (ToggleButton)findViewById(R.id.toggleButton);
        if(!button.isChecked()) {
            EditText viewFee = (EditText)findViewById(R.id.editTextPropertyFee);
            String fee = viewFee.getText().toString();
            double nFee;
            try {
                nFee = Double.valueOf(fee);
                if(nFee <= 0) {
                    commonFun.showToast_info(this, viewFee, "物业费输入不正确");
                    return false;
                }
            } catch (NumberFormatException e) {
                commonFun.showToast_info(this, viewFee, "物业费输入不正确");
                return false;
            }

            ClassDefine.HouseInfoForCommit.propertyFee = (int)(nFee * 100);
            ClassDefine.HouseInfoForCommit.includePropertyFee = 0;
        } else {
            ClassDefine.HouseInfoForCommit.includePropertyFee = 1;
            ClassDefine.HouseInfoForCommit.propertyFee = 0;
        }

        if(!price.isEmpty()) {
            double nPrice;
            double nMinPrice;
            try {
                nPrice = Double.valueOf(price);
                if(nPrice <= 0) {
                    commonFun.showToast_info(this, viewPrice, "售价输入不正确");
                    return false;
                }
            } catch (NumberFormatException e) {
                commonFun.showToast_info(this, viewPrice, "售价输入不正确");
                return false;
            }
            try {
                nMinPrice = Double.valueOf(minPrice);
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

            ClassDefine.HouseInfoForCommit.price = (int)(nPrice * 100);
            ClassDefine.HouseInfoForCommit.minPrice = (int)(nMinPrice * 100);
        } else {
            ClassDefine.HouseInfoForCommit.price = 0;
            ClassDefine.HouseInfoForCommit.minPrice = 0;
        }

        return true;
    }
}
