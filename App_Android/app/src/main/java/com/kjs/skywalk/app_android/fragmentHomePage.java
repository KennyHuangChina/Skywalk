package com.kjs.skywalk.app_android;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentHomePage extends Fragment {
    public fragmentHomePage() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        ListView lvContent = (ListView) view.findViewById(R.id.lv_content);
        lvContent.setFocusable(false);

        homepage_apartment_listitem_adapter adapter = new homepage_apartment_listitem_adapter(getActivity());
        lvContent.setAdapter(adapter);
        return view;
    }
}
