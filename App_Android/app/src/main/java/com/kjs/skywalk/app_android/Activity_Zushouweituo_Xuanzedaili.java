package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.HashMap;
import java.util.List;

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Zushouweituo_Xuanzedaili extends SKBaseActivity
        implements CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{

    private ListView mListViewAgents = null;
    private AdapterAgents mAdapter = null;

    private final int MSG_GET_AGENT_LIST = 0;

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

        mHandler.sendEmptyMessageDelayed(MSG_GET_AGENT_LIST, 100);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_GET_AGENT_LIST: {
                    fetchAgentList();
                    break;
                }
            }
        }
    };

    private void fetchAgentList() {
        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int i, IApiResults.ICommon iCommon) {
                if(iCommon.GetErrCode() == CE_ERROR_NO_ERROR) {
                    commonFun.showToast_info(getApplicationContext(), mListViewAgents, "添加成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } else {
                    commonFun.showToast_info(getApplicationContext(), mListViewAgents, iCommon.GetErrDesc());
                }
                hideWaiting();
            }
        };
        CommandManager manager = new CommandManager(this, listener, this);
        int res = manager.GetAgencyList(0, 0);
        if(res == CE_ERROR_NO_ERROR) {
            showWaiting(mListViewAgents);
        } else {
            commonFun.showToast_info(getApplicationContext(), mListViewAgents, "失败");
        }
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

    @Override
    public void onCommandFinished(int i, IApiResults.ICommon iCommon) {

    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
