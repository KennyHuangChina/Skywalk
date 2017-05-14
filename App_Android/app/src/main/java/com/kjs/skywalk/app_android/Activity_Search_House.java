package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Search_House extends Activity {
    private ListView mListViewAgents = null;
    private SearchView mSearchView = null;
    private HouseSearchHistory mHistory = null;
    private LinearLayout mHistoryLayout = null;
    private LinearLayout mNoHistoryLayout = null;
    private ScrollView mAddHouseLayout = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__search_house);

        mHistory = new HouseSearchHistory(this);

        mHistoryLayout = (LinearLayout)findViewById(R.id.layoutHistory) ;
        mNoHistoryLayout = (LinearLayout)findViewById(R.id.noHistory) ;
        mAddHouseLayout = (ScrollView) findViewById(R.id.scrollViewAdd) ;

        mSearchView = (SearchView)findViewById(R.id.search_view);
        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) mSearchView.findViewById(id);
        textView.setTextSize(14);
        textView.setGravity(Gravity.BOTTOM);

        if (mSearchView != null) {
            try {
                Class<?> argClass = mSearchView.getClass();
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                ownField.setAccessible(true);
                View mView = (View) ownField.get(mSearchView);
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mListViewAgents = (ListView)findViewById(R.id.listViewContent);
        mListViewAgents.setFocusable(false);
        AdapterHouseSearchHistory adapter = new AdapterHouseSearchHistory(this);
        mListViewAgents.setAdapter(adapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mHistory.addHistory(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")) {
                    mNoHistoryLayout.setVisibility(View.GONE);
                    mAddHouseLayout.setVisibility(View.GONE);
                    mHistoryLayout.setVisibility(View.VISIBLE);
                } else {
                    if(mHistory.searched(newText)) {
                        mNoHistoryLayout.setVisibility(View.GONE);
                        mAddHouseLayout.setVisibility(View.GONE);
                        mHistoryLayout.setVisibility(View.VISIBLE);
                    } else {
                        mAddHouseLayout.setVisibility(View.VISIBLE);
                        mNoHistoryLayout.setVisibility(View.GONE);
                        mHistoryLayout.setVisibility(View.GONE);
                    }
                }

                return true;
            }
        });
    }

    protected void onResume() {
        super.onResume();

        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        mSearchView.onActionViewExpanded();
        mSearchView.setIconifiedByDefault(false);

        if(mHistory.getCount() == 0) {
            mHistoryLayout.setVisibility(View.GONE);
            mAddHouseLayout.setVisibility(View.GONE);
            mNoHistoryLayout.setVisibility(View.VISIBLE);
        } else{
            mHistoryLayout.setVisibility(View.GONE);
            mAddHouseLayout.setVisibility(View.GONE);
            mHistoryLayout.setVisibility(View.VISIBLE);
        }
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
