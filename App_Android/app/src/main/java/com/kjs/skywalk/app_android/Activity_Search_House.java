package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Search_House extends Activity {
    private ListView mListViewAgents = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__search_house);

        mListViewAgents = (ListView)findViewById(R.id.listViewContent);
        mListViewAgents.setFocusable(false);
        AdapterHouseSearchHistory adapter = new AdapterHouseSearchHistory(this);
        mListViewAgents.setAdapter(adapter);
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
