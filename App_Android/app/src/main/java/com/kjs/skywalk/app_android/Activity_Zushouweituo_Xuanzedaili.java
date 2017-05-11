package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Zushouweituo_Xuanzedaili extends Activity {

    private ListView mListViewAgents = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo__xuanzedaili);

        mListViewAgents = (ListView)findViewById(R.id.listViewContent);
        mListViewAgents.setFocusable(false);
        AdapterAgents adapter = new AdapterAgents(this);
        mListViewAgents.setAdapter(adapter);

    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
            {
                finish();
            }
            break;
            case R.id.tv_prev:
            {
                finish();
            }
            break;
            case R.id.tv_next:
            {
                startActivity(new Intent(this, Activity_Zushouweituo_SelectService.class));
            }
            break;
        }
    }
}
