package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Zushouweituo_SelectService extends SKBaseActivity {
    private RelativeLayout mContainer1 = null;
    private RelativeLayout mContainer2 = null;
    private int mService = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo__select_service);
        TextView titleText = (TextView)findViewById(R.id.textViewActivityTitle);
        titleText.setText("租售委托-选择服务");

        mContainer1 = (RelativeLayout)findViewById(R.id.container1);
        mContainer2 = (RelativeLayout)findViewById(R.id.container2);

        mContainer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectService1();
            }
        });
        mContainer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectService2();
            }
        });
    }

    private void selectService1() {
        ImageView view = (ImageView)findViewById(R.id.imageViewSelection1);
        view.setVisibility(View.VISIBLE);
        ImageView tmp = (ImageView)findViewById(R.id.imageViewSelection2);
        tmp.setVisibility(View.INVISIBLE);
        mContainer2.setBackground(null);

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.rounded_rect_stroke_background_select_service);
        mContainer1.setBackground(drawable);
        mService = 1;
    }

    private void selectService2() {
        ImageView view = (ImageView)findViewById(R.id.imageViewSelection2);
        view.setVisibility(View.VISIBLE);
        ImageView tmp = (ImageView)findViewById(R.id.imageViewSelection1);
        tmp.setVisibility(View.INVISIBLE);
        mContainer1.setBackground(null);

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.rounded_rect_stroke_background_select_service);
        mContainer2.setBackground(drawable);
        mService = 2;
    }

    private boolean collectData() {
        if(mService == -1) {
            commonFun.showToast_info(this, mContainer1, "请选择您需要的服务");
            return false;
        } else {
            ClassDefine.HouseInfoForCommit.service = mService;
        }
        return true;
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
                    return;
                }
                startActivity(new Intent(this, Activity_Weituoqueren.class));
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
}
