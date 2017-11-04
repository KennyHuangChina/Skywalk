package com.kjs.skywalk.app_android;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentFangYuanGuanLiInfo3 extends Fragment {

    private View mView = null;
    private String mLocation = "";
    private String mHouseInfo = "";
    private String mPassTime = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fangyuanguanli_info3, container, false);

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

    public void setPassTime(String time) {
        mPassTime = time;
    }

    private void updateInfo() {
        TextView textView1 = (TextView)mView.findViewById(R.id.tv_apartment_info1);
        TextView textView2 = (TextView)mView.findViewById(R.id.tv_apartment_info2);
        TextView textViewTime = (TextView)mView.findViewById(R.id.textViewTime);

        textView1.setText(mLocation);
        textView2.setText(mHouseInfo);
        textViewTime.setText("审核通过：" + mPassTime);
    }
}
