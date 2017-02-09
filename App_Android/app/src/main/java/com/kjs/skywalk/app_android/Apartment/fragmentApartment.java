package com.kjs.skywalk.app_android.Apartment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentApartment extends Fragment {
    private String TAG = "FragmentApartment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apartment, container, false);
        SearchView searchView = (SearchView)view.findViewById(R.id.search_view);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextSize(14);
        textView.setHint(R.string.fragment_search_input_hint);
        textView.setGravity(Gravity.BOTTOM);
        textView.setVisibility(View.VISIBLE);

        ListView listViewSearchResult = (ListView) view.findViewById(R.id.listViewSearchResult);
        listViewSearchResult.setFocusable(false);

        AdapterSearchResultList adapter = new AdapterSearchResultList(getActivity());
        listViewSearchResult.setAdapter(adapter);

        return view;
    }
    
    public void onClickResponse(View view) {
        Log.i(TAG, "onClickResponse: ");
    }
}
