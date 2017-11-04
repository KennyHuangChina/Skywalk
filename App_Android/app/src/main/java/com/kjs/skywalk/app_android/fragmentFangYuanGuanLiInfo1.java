package com.kjs.skywalk.app_android;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentFangYuanGuanLiInfo1 extends Fragment {

    private View mView = null;
    private String mLocation = "";
    private String mHouseInfo = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fangyuanguanli_info1, container, false);

        return mView;
    }

    public void onResume() {
        super.onResume();

        updateInfo();
    }

    public void setHouseLocation(String location) {
        mLocation = location;
    }

    public void setHouseInfo(String info) {
        mHouseInfo = info;
    }

    private void updateInfo() {
        TextView textView1 = (TextView)mView.findViewById(R.id.tv_apartment_info1);
        TextView textView2 = (TextView)mView.findViewById(R.id.tv_apartment_info2);

        textView1.setText(mLocation);
        textView2.setText(mHouseInfo);
    }
}
