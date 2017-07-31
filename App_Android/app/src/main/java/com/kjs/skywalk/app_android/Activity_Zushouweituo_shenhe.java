package com.kjs.skywalk.app_android;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kjs.skywalk.app_android.Server.GetHouseInfo;

public class Activity_Zushouweituo_shenhe extends SKBaseActivity implements GetHouseInfo.TaskFinished{

    private boolean mModifyMode = false;
    private RelativeLayout mRootLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo_shenhe);
        mRootLayout = (RelativeLayout)findViewById(R.id.activity__zushouweituo_shenhe);

        GetHouseInfo houseInfo = new GetHouseInfo(this, this);
        houseInfo.execute(mHouseId, 1);
    }

    @Override
    public void onTaskFinished(ClassDefine.HouseInfo houseInfo) {
        if(houseInfo == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    commonFun.showToast_info(getApplicationContext(), mRootLayout, "获取房屋信息失败");
                }
            });
            return;
        }
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_back: {
                finish();
            }
            break;
            case R.id.textViewModify: {
                mModifyMode = !mModifyMode;
                TextView button = (TextView)v;
                TextView floor = (TextView)findViewById(R.id.textViewFloor);
                if(mModifyMode) {
                    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.right_n);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    floor.setCompoundDrawablePadding(15);
                    floor.setCompoundDrawables(null, null, drawable, null);

                    int color = ContextCompat.getColor(this, R.color.colorText);
                    floor.setTextColor(color);

                    button.setText("完成");
                } else {
                    floor.setCompoundDrawables(null, null, null, null);
                    int color = ContextCompat.getColor(this, R.color.colorTextNormal);
                    floor.setTextColor(color);
                    button.setText("修改");
                }

                break;
            }
        }
    }

}
