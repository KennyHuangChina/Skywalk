package com.kjs.skywalk.communicationdemo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jackie on 2017/1/23.
 */

public class LogInOutFragment extends Fragment {

    public LogInOutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_in_out, container, false);
    }
}
