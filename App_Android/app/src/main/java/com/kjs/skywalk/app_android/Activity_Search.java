package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Search extends Activity {
    private final String TAG = "Search";
    private ListView mListView = null;
    private LinearLayout mContainer = null;
    private LinearLayout mContainerEmpty = null;
    private ArrayList<String> mHistory = new ArrayList<String>();
    private ArrayList<String> mGuess = new ArrayList<String>();

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

        initHistory();

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

    private TextView createTextItem(String text) {
        int paddingLeft = 10;
        int paddingRight = 10;
        int paddingTop = 10;
        int paddingBottom = 10;
        int marginLeft = 15;
        int marginRight = 15;
        int marginTop = 15;
        int marginBottom = 15;

        TextView textView = new TextView(this);
        textView.setText(text);
        int color = ContextCompat.getColor(this, R.color.colorTextNormal);
        textView.setTextColor(color);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.rounded_rect_background_all_gray);
        textView.setBackground(drawable);
        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 15, 15, 15);
        textView.setLayoutParams(layoutParams);
        float size = textView.getPaint().measureText(text);
        Log.i(TAG, "with = " + size);
        return textView;
    }

    private void initGuess() {
        mHistory.add("中环时代");
        mHistory.add("新时代");
        mHistory.add("世茂蝶湖湾");
        mHistory.add("绿地21新城");
        mHistory.add("万科魅力花园MIXTOWN");
        mHistory.add("印象欧洲");
        mHistory.add("中冶昆廷");
        mHistory.add("香溢紫君");
        mHistory.add("两岸新天地");
        mHistory.add("中汇爱丁堡");


    }

    private void initHistory() {
        mHistory.add("世茂蝶湖湾");
        mHistory.add("印象欧洲");
        mHistory.add("中冶昆廷");
        mHistory.add("香溢紫君");
        mHistory.add("绿地21新城");
        mHistory.add("万科魅力花园MIXTOWN");
        mHistory.add("两岸新天地");
        mHistory.add("中汇爱丁堡");
        mHistory.add("中环时代");
        mHistory.add("新时代");

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout container = (LinearLayout)findViewById(R.id.layoutRecent);
        container.addView(layout);
//        for(int i = 0; i < mHistory.size(); i ++) {
        for(int i = 0; i < 3; i ++) {
            TextView view = createTextItem(mHistory.get(i));
            layout.addView(view);

            int width = view.getWidth();
            Log.i(TAG, "with = " + width);
        }
    }
}
