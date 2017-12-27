package com.kjs.skywalk.app_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kjs.skywalk.communicationlibrary.CmdExecRes;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_AGENCY_LIST;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Xuanzedaili extends SKBaseActivity {

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
        setContentView(R.layout.activity_xuanzedaili);
        TextView titleText = (TextView)findViewById(R.id.tv_title);
        titleText.setText("指派代理");

        mAgentListContainer = (ScrollView)findViewById(R.id.scrollViewAgentContainer);

        mListViewAgents = (ListView)findViewById(R.id.listViewContent);
        mListViewAgents.setFocusable(false);
        mAdapter = new AdapterAgents(this);
        mAdapter.setAutoSelect(false);
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
    }

    @Override
    protected void onResume() {
        super.onResume();

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
            public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
                if(command == CMD_GET_AGENCY_LIST) {
                    if (iResult.GetErrCode() == CE_ERROR_NO_ERROR) {
                        IApiResults.IResultList res = (IApiResults.IResultList) iResult;
                        mAgentCount = res.GetTotalNumber();
                        int nFetched = res.GetFetchedNumber();
                        if (nFetched > 0) {
                            ArrayList<Object> array = res.GetList();
                            for (Object obj : array) {
                                IApiResults.IAgencyInfo info = (IApiResults.IAgencyInfo) obj;
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
                                if (agent.mPortrait == null || agent.mPortrait.isEmpty()) {
                                    agent.mPortrait = "not set";
                                }

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
                        commonFun.showToast_info(getApplicationContext(), mListViewAgents, iResult.GetErrDesc());
                        Activity_Xuanzedaili.super.onCommandFinished(command, cmdSeq, iResult);
                    }
                } else {
                    Activity_Xuanzedaili.super.onCommandFinished(command, cmdSeq, iResult);
                }
                hideWaiting();
            }
        };
        CommandManager manager = CommandManager.getCmdMgrInstance(this);
        CmdExecRes res = manager.GetAgencyList(start, end);
        if (res.mError == CE_ERROR_NO_ERROR) {
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
            case R.id.textViewOK:
            {
                if(!collectData()) {
                    return;
                }
                Intent data = new Intent();
                data.putExtra("agentId", mCurrentAgentId);
                setResult(30, data);
                finish();
            }
            break;
        }
    }

    private boolean collectData() {
        if(!mAdapter.hasSelected()) {
            commonFun.showToast_info(this, mAgentListContainer, "请选择一个代理人");
            return false;
        }

        return true;
    }
}
