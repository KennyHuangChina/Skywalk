package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
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
    private ArrayList<ClassDefine.Agent> mAgentList = new ArrayList<>();
    private ScrollView mAgentListContainer = null;
    private int mAgentCount = 0;
    private final int MSG_GET_AGENT_LIST = 0;

    private String mCurrentAgentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo__xuanzedaili);
        TextView titleText = (TextView)findViewById(R.id.textViewActivityTitle);
        titleText.setText("租售委托-选择代理");

        mAgentListContainer = (ScrollView)findViewById(R.id.scrollViewAgentContainer);

        final CheckBox autoSelect = (CheckBox)findViewById(R.id.checkbox);
        autoSelect.setChecked(true);

        mListViewAgents = (ListView)findViewById(R.id.listViewContent);
        mListViewAgents.setFocusable(false);
        mAdapter = new AdapterAgents(this);
        mAdapter.setAutoSelect(autoSelect.isChecked());
        mListViewAgents.setAdapter(mAdapter);

        mListViewAgents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassDefine.Agent agent = (ClassDefine.Agent) parent.getAdapter().getItem(position);
                Log.i(getClass().getSimpleName().toString(), agent.mID);
                AdapterAgents agentAdapter = (AdapterAgents)parent.getAdapter();
                agentAdapter.setItemSelected(view, position);
                mCurrentAgentId = agent.mID;
            }
        });

        updateAgentListStatus(!autoSelect.isChecked());

        autoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)v;
                updateAgentListStatus(!checkBox.isChecked());
            }
        });
        mHandler.sendEmptyMessageDelayed(MSG_GET_AGENT_LIST, 100);
    }

    private void updateAgentListStatus(boolean enable) {
        if(enable) {
            mListViewAgents.setEnabled(true);
            mAgentListContainer.setBackgroundColor(0x000000);
        } else {
            mListViewAgents.setEnabled(false);
            mAdapter.unSelectCurrentItem();
            mAgentListContainer.setBackgroundColor(0x10666666);
        }

        mAdapter.unSelectCurrentItem();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_GET_AGENT_LIST: {
                    fetchAgentList(0, 50);
                    break;
                }
            }
        }
    };

    private void fetchAgentList(final int start, final int end) {
        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int i, IApiResults.ICommon iCommon) {
                if(iCommon.GetErrCode() == CE_ERROR_NO_ERROR) {
                    IApiResults.IResultList res = (IApiResults.IResultList) iCommon;
                    mAgentCount = res.GetTotalNumber();
                    int nFetched = res.GetFetchedNumber();
                    if (nFetched > 0) {
                        ArrayList<Object> array = res.GetList();
                        for(Object obj : array) {
                            IApiResults.IAgencyInfo info = (IApiResults.IAgencyInfo)obj;
                            ClassDefine.Agent agent = new ClassDefine.Agent();
                            double db = info.RankAtti() / 10.0;
                            agent.mAttitude = String.format("%.1f", db);
                            db = info.RankProf() / 10.0;
                            agent.mProfessional = String.format("%.1f", db);
                            agent.mIDCard = info.IdNo();
                            agent.mID = String.valueOf(info.Id());
                            agent.mName = info.Name();
                            agent.mSex = String.valueOf(info.Sex());
                            agent.mPortrait = info.Portrait();
                            agent.mYears = "3年";

                            mAgentList.add(agent);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateAgentList();
                        }
                    });
                } else {
                    commonFun.showToast_info(getApplicationContext(), mListViewAgents, iCommon.GetErrDesc());
                }
                hideWaiting();
            }
        };
        CommandManager manager = new CommandManager(this, listener, this);
        int res = manager.GetAgencyList(start, end);
        if(res == CE_ERROR_NO_ERROR) {
            showWaiting(mListViewAgents);
        } else {
            commonFun.showToast_info(getApplicationContext(), mListViewAgents, "失败");
        }
    }

    public void updateAgentList() {
        mListViewAgents.removeAllViewsInLayout();
        mListViewAgents.setAdapter(mAdapter);
        mAdapter.setDataList(mAgentList);
        mAdapter.notifyDataSetChanged();
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
                if(!collectData()) {
                    return;
                }
                startActivity(new Intent(this, Activity_Zushouweituo_SelectService.class));
            }
            break;
            case R.id.checkbox: {
                break;
            }
        }
    }

    private boolean collectData() {
        CheckBox autoSelect = (CheckBox)findViewById(R.id.checkbox);
        if(!autoSelect.isChecked()) {
            if(!mAdapter.hasSelected()) {
                commonFun.showToast_info(this, autoSelect, "请选择一个代理人或由系统自动为您分配");
                return false;
            } else {
                ClassDefine.HouseInfoForCommit.autoAgent = 0;
                ClassDefine.HouseInfoForCommit.agentId = mCurrentAgentId;
            }
        } else {
            ClassDefine.HouseInfoForCommit.autoAgent = 1;
            ClassDefine.HouseInfoForCommit.agentId = "0";
        }
        return true;
    }

    @Override
    public void onCommandFinished(int i, IApiResults.ICommon iCommon) {

    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
