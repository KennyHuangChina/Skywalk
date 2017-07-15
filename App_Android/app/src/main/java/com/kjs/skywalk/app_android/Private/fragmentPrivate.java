package com.kjs.skywalk.app_android.Private;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kjs.skywalk.app_android.R;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentPrivate extends Fragment {
    private boolean mIsLogin = false;
    private RelativeLayout mRlTitleBar;
    private RelativeLayout mRlUserNotLogin;
    private LinearLayout mLlUserLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private, container, false);

        mRlTitleBar = (RelativeLayout) view.findViewById(R.id.rl_titlebar);
        mRlUserNotLogin = (RelativeLayout) view.findViewById(R.id.rl_user_not_login);
        mLlUserLogin = (LinearLayout) view.findViewById(R.id.ll_user_login);


        updateLayout(mIsLogin);

        return view;
    }

    private void updateLayout(boolean isLogin) {

        if (isLogin) {
            mRlTitleBar.setBackgroundColor(Color.parseColor("#00AE63"));
            mRlUserNotLogin.setVisibility(View.GONE);
            mLlUserLogin.setVisibility(View.VISIBLE);
        } else {
            mRlTitleBar.setBackgroundColor(Color.parseColor("#E5E5E5"));
            mRlUserNotLogin.setVisibility(View.VISIBLE);
            mLlUserLogin.setVisibility(View.GONE);
        }
    }
}
