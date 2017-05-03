package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Search extends Activity {
    private ListView mListView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mListView = (ListView)findViewById(R.id.listViewHint);
        AdapterHouseSearchResult adapter = new AdapterHouseSearchResult(this);
        mListView.setAdapter(adapter);
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
