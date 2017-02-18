package com.kjs.skywalk.app_android.Homepage;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kjs.skywalk.app_android.Apartment.Activity_ApartmentDetail;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;

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

        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                commonFun.showToast_resId(getActivity().getApplicationContext(), view);
                Intent intent = new Intent(getActivity().getApplicationContext(), Activity_ApartmentDetail.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
