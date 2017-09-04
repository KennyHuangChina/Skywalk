package com.kjs.skywalk.app_android.Message;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.SKLocalSettings;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;

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

//        commonFun.displayImageWithMask(this.getActivity(), (ImageView) view.findViewById(R.id.iv_test), R.drawable.huxingtu1, R.drawable.head_portrait_mask);
        commonFun.displayImageWithMask(this.getActivity(), (ImageView) view.findViewById(R.id.iv_test),
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502280826114&di=109e9c8df04726531b9a247a0e5744f8&imgtype=0&src=http%3A%2F%2Fwww.th7.cn%2Fd%2Ffile%2Fp%2F2016%2F11%2F17%2F7dc8d2aa0bd854e87295e6df73eaef19.jpg", R.drawable.head_portrait_mask);

        // test
//        List<String> idLst = new ArrayList<>();
//        SKLocalSettings.browsing_history_insert(getActivity(), "1");
//        SKLocalSettings.browsing_history_insert(getActivity(), "3");
//        SKLocalSettings.browsing_history_insert(getActivity(), "5");
//        SKLocalSettings.browsing_history_insert(getActivity(), "7");
//        SKLocalSettings.browsing_history_insert(getActivity(), "9");
//
//        idLst = SKLocalSettings.browsing_history_read(getActivity());
//        kjsLogUtil.i("idLst:" + idLst);

        return view;
    }
}
