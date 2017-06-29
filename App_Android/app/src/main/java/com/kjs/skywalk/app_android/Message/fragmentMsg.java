package com.kjs.skywalk.app_android.Message;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentMsg extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_msg, container, false);

        // test
        commonFun.TextDefine t = new commonFun.TextDefine("123", 12, R.color.colorFontNormal);

        List<commonFun.TextDefine> textDefines = new ArrayList<commonFun.TextDefine>(
            Arrays.asList(
                    new commonFun.TextDefine("蓝色", 60, Color.BLUE),
                    new commonFun.TextDefine("按钮高亮色", 45, ContextCompat.getColor(getActivity(), R.color.colorButtonTextHighlight)),
                    new commonFun.TextDefine("青色", 60, Color.CYAN)
                    )
        );
        TextView textV = (TextView) view.findViewById(R.id.textView);
        textV.setText(commonFun.getSpannableString(textDefines));
        //

        return view;
    }
}
