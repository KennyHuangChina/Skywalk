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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fangyuanguanli_info1, container, false);
    }

    public void updateInfo(String info1, String info2) {
        ((TextView)getActivity().findViewById(R.id.tv_apartment_info1)).setText(info1);
        ((TextView)getActivity().findViewById(R.id.tv_apartment_info2)).setText(info2);
    }
}
