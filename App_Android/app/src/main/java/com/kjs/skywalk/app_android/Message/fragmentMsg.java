package com.kjs.skywalk.app_android.Message;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.SKBaseActivity;
import com.kjs.skywalk.app_android.SKLocalSettings;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.database.ProfileDBOperator;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;
import com.kjs.skywalk.control.SwipeLoadMoreView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_BEHALF_HOUSE_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_SYSTEM_MSG_LST;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

// https://www.cnblogs.com/liushilin/p/5620072.html
public class fragmentMsg extends Fragment implements AbsListView.OnScrollListener {
    @Nullable
    private SwipeLoadMoreView mSrl_message_list;
    private ListView mLvMessage;
    private AdapterMessage mAdapterMsg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_msg, container, false);

        mLvMessage = (ListView) view.findViewById(R.id.lv_message);
        mAdapterMsg = new AdapterMessage(getActivity());
        mLvMessage.setAdapter(mAdapterMsg);
        mLvMessage.setOnScrollListener(this);

        mSrl_message_list = (SwipeLoadMoreView) view.findViewById(R.id.srl_message_list);
        mSrl_message_list.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSrl_message_list.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSrl_message_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                kjsLogUtil.i("onRefresh");
                setRefreshing(true);
                new ThreadLoadMessage().start();
            }
        });

        mSrl_message_list.setmItemCount(3);
        mSrl_message_list.measure(0, 0);
//        mSrl_message_list.setRefreshing(true);
        mSrl_message_list.setOnLoadMoreListener(new SwipeLoadMoreView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                new ThreadLoadMessage().start();
                loadMoreData();
            }
        });

        // test
        IApiResults.IGetUserInfo loginUserInfo = ((SKBaseActivity)getActivity()).mLoginUserInfo;
        if (loginUserInfo != null) {
            int user_id = loginUserInfo.GetUserId();
            kjsLogUtil.i("user_id: " + user_id);
        }

//        commonFun.TextDefine t = new commonFun.TextDefine("123", 12, R.color.colorFontNormal);
//
//        List<commonFun.TextDefine> textDefines = new ArrayList<commonFun.TextDefine>(
//            Arrays.asList(
//                    new commonFun.TextDefine("蓝色", 60, Color.BLUE),
//                    new commonFun.TextDefine("按钮高亮色", 45, ContextCompat.getColor(getActivity(), R.color.colorButtonTextHighlight)),
//                    new commonFun.TextDefine("青色", 60, Color.CYAN)
//                    )
//        );
//        TextView textV = (TextView) view.findViewById(R.id.textView);
//        textV.setText(commonFun.getSpannableString(textDefines));
        //

//        commonFun.displayImageWithMask(this.getActivity(), (ImageView) view.findViewById(R.id.iv_test), R.drawable.huxingtu1, R.drawable.head_portrait_mask);
//        commonFun.displayImageWithMask(this.getActivity(), (ImageView) view.findViewById(R.id.iv_test),
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502280826114&di=109e9c8df04726531b9a247a0e5744f8&imgtype=0&src=http%3A%2F%2Fwww.th7.cn%2Fd%2Ffile%2Fp%2F2016%2F11%2F17%2F7dc8d2aa0bd854e87295e6df73eaef19.jpg", R.drawable.head_portrait_mask);

        // test
//        List<String> idLst = new ArrayList<>();
//        SKLocalSettings.browsing_history_insert(getActivity(), "1");
//        SKLocalSettings.browsing_history_insert(getActivity(), "3");
//        SKLocalSettings.browsing_history_insert(getActivity(), "5");
//        SKLocalSettings.browsing_history_insert(getActivity(), "7");
//        SKLocalSettings.browsing_history_insert(getActivity(), "9");
//
//        idLst = SKLocalSettings.browsing_history_read(getActivity());
//        kjsLogUtil.i("idLst:" + idLst);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        kjsLogUtil.i("fragmentMsg --- onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        kjsLogUtil.i("fragmentMsg --- onResume");

        // get message count in db
//        int msgCount = ProfileDBOperator.getOperator(getActivity(), "test_user").getMessageCount();
//        kjsLogUtil.i("msgCount: " + msgCount);
//        ArrayList<ClassDefine.MessageInfo> msgList = ProfileDBOperator.getOperator(getActivity(), "test_user").getMessageListFromDB();
        //

        setRefreshing(true);
        new ThreadLoadMessage().start();
    }

    private void getMessageInfo() {
        CommunicationInterface.CICommandListener cl = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }
                kjsLogUtil.i(String.format("[command: %d(%s)] --- %s" , command, CommunicationInterface.CmdID.GetCmdDesc(command), iResult.DebugString()));
                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                    return;
                }

                if (command == CMD_GET_SYSTEM_MSG_LST) {
                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                    int nFetch = resultList.GetFetchedNumber();
                    if (nFetch == -1) {
                    }
                    updateMsgList(resultList.GetList());
                }
            }
        };
        CommandManager.getCmdMgrInstance(getActivity()/*, cl, mProgreessListener*/).GetSysMsgList(0, 100 , false, false);
    }

    boolean mIsCmdFinished = false;
    int mMsgCount = 0;
    private int getMessageCountSync(boolean ido, boolean nmo) {
        mIsCmdFinished = false;
        CommunicationInterface.CICommandListener cl = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    mIsCmdFinished = true;
                    return;
                }
                kjsLogUtil.i(String.format("[command: %d(%s)] --- %s" , command, CommunicationInterface.CmdID.GetCmdDesc(command), iResult.DebugString()));
                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                    mIsCmdFinished = true;
                    return;
                }

                if (command == CMD_GET_SYSTEM_MSG_LST) {
                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                    int nFetch = resultList.GetFetchedNumber();
                    kjsLogUtil.i("nFetch: " + nFetch);
                    if (nFetch == -1) {
                        mMsgCount = resultList.GetTotalNumber();
                        mIsCmdFinished = true;
                        kjsLogUtil.i("mIsCmdFinished: " + mIsCmdFinished);
                    }
                }
            }
        };
        CommandManager.getCmdMgrInstance(getActivity()/*, cl, mProgreessListener*/).GetSysMsgList(0, 0 , ido, nmo);

        kjsLogUtil.i("mIsCmdFinished: " + mIsCmdFinished);
        while (!mIsCmdFinished) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        kjsLogUtil.i("mMsgCount: " + mMsgCount);
        return mMsgCount;
    }

    CommunicationInterface.CIProgressListener mProgreessListener = new CommunicationInterface.CIProgressListener() {
        @Override
        public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {
        }
    };

    private void updateMsgList(final ArrayList<Object> list) {
        Activity activity = getActivity();
        if (activity != null) {
            IApiResults.IGetUserInfo loginUserInfo = ((SKBaseActivity)getActivity()).mLoginUserInfo;
            if (loginUserInfo != null) {
                int user_id = loginUserInfo.GetUserId();
                kjsLogUtil.i("user_id: " + user_id);
                ProfileDBOperator.getOperator(getActivity(), String.valueOf(user_id)).update(list);
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    ProfileDBOperator.getOperator(getActivity(), "test_user").update(list);
                    IApiResults.IGetUserInfo loginUserInfo = ((SKBaseActivity)getActivity()).mLoginUserInfo;
                    if (loginUserInfo != null) {
                        int user_id = loginUserInfo.GetUserId();
                        kjsLogUtil.i("user_id: " + user_id);
                        ArrayList<ClassDefine.MessageInfo> msgList = ProfileDBOperator.getOperator(getActivity(), "test_user").getMessageListFromDB();
                        mAdapterMsg.updateList(msgList);
                    }
                }
            });
        }
    }

    private void loadMessageList() {

    }

    private int mLastVisibleIndex = 0;
    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        kjsLogUtil.i("onScrollStateChanged --- scrollState: " + scrollState);
        if (mAdapterMsg.getCount() == mLastVisibleIndex && scrollState == SCROLL_STATE_IDLE) {

        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        kjsLogUtil.i(String.format("onScroll --- firstVisibleItem: %d visibleItemCount:%d", firstVisibleItem, visibleItemCount));
        mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
    }

    class ThreadLoadMessage extends Thread {
        private boolean mGetMsgFinished = false;
        @Override
        public void run() {
            kjsLogUtil.i(String.format("[ThreadLoadMessage] --- start"));
            boolean ido = false;
            boolean nmo = true; // new message only
            getMessageCountSync(ido, nmo);

            mGetMsgFinished = false;
            kjsLogUtil.i(String.format("[ThreadLoadMessage] --- mGetMsgFinished: " + mGetMsgFinished));
            CommunicationInterface.CICommandListener cl = new CommunicationInterface.CICommandListener() {
                @Override
                public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
                    if (null == iResult) {
                        kjsLogUtil.w("result is null");
                        mGetMsgFinished = true;
                        return;
                    }
                    kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
                    if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                        kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                        mGetMsgFinished = true;
                        return;
                    }

                    if (command == CMD_GET_SYSTEM_MSG_LST) {
                        IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                        int nFetch = resultList.GetFetchedNumber();
                        if (nFetch == -1) {
                        }
                        updateMsgList(resultList.GetList());
                        mGetMsgFinished = true;
                    }
                }
            };
            CommandManager.getCmdMgrInstance(getActivity()/*, cl, mProgreessListener*/).GetSysMsgList(0, 100 , ido, nmo);

            while (mGetMsgFinished == false) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            setRefreshing(false);

            kjsLogUtil.i(String.format("[ThreadLoadMessage] --- end"));
        }
    }

    public void setRefreshing(final boolean isRefresh) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSrl_message_list.setRefreshing(isRefresh);
            }
        });
    }

    private void loadMoreData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                kjsLogUtil.i("see if main thread");
                mSrl_message_list.setLoadingStatue(false);
            }
        }, 2000);
    }
}
