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
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_AGENCY_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;

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
                    // TODO: 这里不应该直接用 50，而是要先取经纪人的数量
                    fetchAgentList(0, 50);
                    break;
                }
            }
        }
    };

    private void fetchAgentList(final int start, final int end) {
        CmdExecRes res = CommandManager.getCmdMgrInstance(this).GetAgencyList(start, end);
        if (res.mError == CE_ERROR_NO_ERROR) {
            StoreCommand(res);
            showWaiting(mListViewAgents);
        } else {
            kjsLogUtil.e("Fail to send command GetAgencyList, err:" + res.mError);
            commonFun.showToast_info(getApplicationContext(), mListViewAgents, "失败");
        }
    }

    @Override
    public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
        if (null == iResult) {
            kjsLogUtil.w("result is null");
            return;
        }
        CmdExecRes cmd = RetrieveCommand(cmdSeq);
        if (null == cmd) {  // result is not we wanted
            return;
        }
        kjsLogUtil.i(String.format("[command: %d(%s)] --- %s", command, CommunicationInterface.CmdID.GetCmdDesc(command), iResult.DebugString()));

        int errCode = iResult.GetErrCode();
        if (CommunicationError.CE_ERROR_NO_ERROR != errCode) {
            kjsLogUtil.e("Command:" + command + " finished with error: " + errCode);
            super.onCommandFinished(command, cmdSeq, iResult);
            if (command == CMD_GET_AGENCY_LIST) {
                commonFun.showToast_info(getApplicationContext(), mListViewAgents, iResult.GetErrDesc());
            }
            return;
        }

        if (command == CMD_GET_AGENCY_LIST) {
            IApiResults.IResultList res = (IApiResults.IResultList) iResult;
            mAgentCount     = res.GetTotalNumber();
            int nFetched    = res.GetFetchedNumber();

            if (nFetched > 0) {
                ArrayList<Object> array = res.GetList();
                for (Object obj : array) {
                    IApiResults.IAgencyInfo info = (IApiResults.IAgencyInfo) obj;
                    ClassDefine.Agent agent = new ClassDefine.Agent();
                    agent.mAttitude     = String.format("%.1f", info.RankAtti() / 10.0);
                    agent.mProfessional = String.format("%.1f", info.RankProf() / 10.0);
                    agent.mIDCard       = info.IdNo();
                    agent.mID           = String.valueOf(info.Id());
                    agent.mName         = info.Name();
                    agent.mSex          = String.valueOf(info.Sex());
                    agent.mPortrait     = info.Portrait();
                    agent.mYears        = "3年";
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
            Activity_Xuanzedaili.super.onCommandFinished(command, cmdSeq, iResult);
        }
        hideWaiting();
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
