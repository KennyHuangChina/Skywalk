package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Zushouweituo_Xuanzedaili extends SKBaseActivity {

    private ListView mListViewAgents = null;
    private AdapterAgents mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo__xuanzedaili);
        TextView titleText = (TextView)findViewById(R.id.textViewActivityTitle);
        titleText.setText("租售委托-选择代理");

        CheckBox autoSelect = (CheckBox)findViewById(R.id.checkbox);
        autoSelect.setChecked(true);

        mListViewAgents = (ListView)findViewById(R.id.listViewContent);
        mListViewAgents.setFocusable(false);
        mAdapter = new AdapterAgents(this);
        mAdapter.setAutoSelect(autoSelect.isChecked());
        mListViewAgents.setAdapter(mAdapter);

        mListViewAgents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.imageViewActivityBack: {
                finish();
                break;
            }
            case R.id.imageViewActivityClose: {
                Intent intent =new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
                break;
            }
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
            case R.id.checkbox: {
                break;
            }
        }
    }
}
