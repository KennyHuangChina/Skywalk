package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Search extends Activity {
    private ListView mListView = null;
    private LinearLayout mContainer = null;
    private LinearLayout mContainerEmpty = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchView searchView = (SearchView)findViewById(R.id.search_view);

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextSize(14);
        textView.setGravity(Gravity.BOTTOM);

        if (searchView != null) {
            try {
                Class<?> argClass = searchView.getClass();
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                ownField.setAccessible(true);
                View mView = (View) ownField.get(searchView);
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mListView = (ListView)findViewById(R.id.listViewResult);
        AdapterHouseSearchResult adapter = new AdapterHouseSearchResult(this);
        mListView.setAdapter(adapter);

        mContainer = (LinearLayout)findViewById(R.id.searchResultContainer);
        mContainerEmpty = (LinearLayout)findViewById(R.id.searchResultEmptyContainer);

        mContainer.setVisibility(View.GONE);
        mContainerEmpty.setVisibility(View.VISIBLE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")) {
                    mContainer.setVisibility(View.GONE);
                    mContainerEmpty.setVisibility(View.VISIBLE);
                } else {
                    if(!mContainer.isShown()) {
                        mContainer.setVisibility(View.VISIBLE);
                        mContainerEmpty.setVisibility(View.GONE);
                    }
                }

                return true;
            }
        });
    }

    protected void onResume() {
        super.onResume();

        SearchView searchView = (SearchView)findViewById(R.id.search_view);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(false);
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title:
            {
                finish();
            }
            break;
        }
    }
}
