package com.kjs.skywalk.app_android.Private;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.SKLocalSettings;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_BEHALF_HOUSE_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_BRIEF_PUBLIC_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_DIGEST_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_USER_HOUSE_WATCH_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_HOUSE_LST_APPOINT_SEE;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentPrivate extends Fragment {
    private boolean mIsLogin = false;
    private RelativeLayout mRlTitleBar;
    private RelativeLayout mRlUserNotLogin;
    private LinearLayout mLlUserLogin;
    private ImageView mIv_portrait_mask;

    private TextView mTvWatchListCount;         // 我的关注
    private TextView mTvAppointmentCount;


    // 我代理的房源
    private TextView mTvToRent;
    private TextView mTvRented;
    private TextView mTvToSale;
    private TextView mTvToApprove;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private, container, false);

        mRlTitleBar = (RelativeLayout) view.findViewById(R.id.rl_titlebar);
        mRlUserNotLogin = (RelativeLayout) view.findViewById(R.id.rl_user_not_login);
        mLlUserLogin = (LinearLayout) view.findViewById(R.id.ll_user_login);
        mIv_portrait_mask = (ImageView) view.findViewById(R.id.iv_portrait_mask);

        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(getActivity(), mCmdListener, mProgreessListener);

        // 我的关注
        mTvWatchListCount = (TextView) view.findViewById(R.id.tv_watchlist_count);
        CmdMgr.GetUserHouseWatchList(0, 0);

        // 我的预约
        mTvAppointmentCount = (TextView) view.findViewById(R.id.tv_appointment_count);
        CmdMgr.GetHouseList_AppointSee(0, 0);

        // 我代理的房源
        mTvToRent = (TextView) view.findViewById(R.id.tv_to_rent);
        mTvRented = (TextView) view.findViewById(R.id.tv_rented);
        mTvToSale = (TextView) view.findViewById(R.id.tv_to_sale);
        mTvToApprove = (TextView) view.findViewById(R.id.tv_to_approve);


        mIsLogin =  SKLocalSettings.UISettings_get(getActivity(), SKLocalSettings.UISettingsKey_LoginStatus, false);
        kjsLogUtil.i(String.format("mIsLogin is %b", mIsLogin));
        updateLayout(mIsLogin);

        // get behalf info
//        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(getActivity(), mCmdListener, mProgreessListener);
//        type    : list type. 0 - all; 1 - to rent; 2 - rented; 3 - to sale; 4 - to approve
//        1- 待租；2-已租； 3-待售； 4-待审核

        // to rent
        CommandManager.getCmdMgrInstance(getActivity(), new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }
                kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                    return;
                }

                if (command == CMD_GET_BEHALF_HOUSE_LIST) {
                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                    int nFetch = resultList.GetFetchedNumber();
                    if (nFetch == -1) {
                        updateCount(mTvToRent, resultList.GetTotalNumber());
                    }
                }
            }
        }, mProgreessListener).GetBehalfHouses(1, 0 , 0);

        // rented
        CommandManager.getCmdMgrInstance(getActivity(), new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }
                kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                    return;
                }

                if (command == CMD_GET_BEHALF_HOUSE_LIST) {
                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                    int nFetch = resultList.GetFetchedNumber();
                    if (nFetch == -1) {
                        updateCount(mTvRented, resultList.GetTotalNumber());
                    }
                }
            }
        }, mProgreessListener).GetBehalfHouses(2, 0 , 0);

        // to sale
        CommandManager.getCmdMgrInstance(getActivity(), new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }
                kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                    return;
                }

                if (command == CMD_GET_BEHALF_HOUSE_LIST) {
                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                    int nFetch = resultList.GetFetchedNumber();
                    if (nFetch == -1) {
                        updateCount(mTvToSale, resultList.GetTotalNumber());
                    }
                }
            }
        }, mProgreessListener).GetBehalfHouses(3, 0 , 0);

        // to approve
        CommandManager.getCmdMgrInstance(getActivity(), new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }
                kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                    return;
                }

                if (command == CMD_GET_BEHALF_HOUSE_LIST) {
                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                    int nFetch = resultList.GetFetchedNumber();
                    if (nFetch == -1) {
                        updateCount(mTvToApprove, resultList.GetTotalNumber());
                    }
                }
            }
        }, mProgreessListener).GetBehalfHouses(4, 0 , 0);

        return view;
    }

    private void updateCount(final TextView textview, final int nCount) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textview.setText(String.valueOf(nCount));
            }
        });
    }

    private void updateLayout(boolean isLogin) {

        if (isLogin) {
            mRlTitleBar.setBackgroundColor(Color.parseColor("#00AE63"));
            mRlUserNotLogin.setVisibility(View.GONE);
            mLlUserLogin.setVisibility(View.VISIBLE);
//            mIv_portrait_mask.setImageResource(R.drawable.portrait_mask_login);
        } else {
            mRlTitleBar.setBackgroundColor(Color.parseColor("#E5E5E5"));
            mRlUserNotLogin.setVisibility(View.VISIBLE);
            mLlUserLogin.setVisibility(View.GONE);
//            mIv_portrait_mask.setImageResource(R.drawable.portrait_mask_notlogin);
        }
    }

    CommunicationInterface.CICommandListener mCmdListener = new CommunicationInterface.CICommandListener() {
        @Override
        public void onCommandFinished(int command, IApiResults.ICommon iResult) {
            if (null == iResult) {
                kjsLogUtil.w("result is null");
                return;
            }
            kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
            if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                return;
            }

            if (command == CMD_HOUSE_LST_APPOINT_SEE) {
                IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                int nFetch = resultList.GetFetchedNumber();
                if (nFetch == -1) {
                    // 我的预约
                    updateCount(mTvAppointmentCount, resultList.GetTotalNumber());
                }
            }

            if (command == CMD_GET_USER_HOUSE_WATCH_LIST) {
                IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                int nFetch = resultList.GetFetchedNumber();
                if (nFetch == -1) {
                    // 看房记录
                    updateCount(mTvWatchListCount, resultList.GetTotalNumber());
                }
            }

        }
    };

    CommunicationInterface.CIProgressListener mProgreessListener = new CommunicationInterface.CIProgressListener() {
        @Override
        public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {
        }
    };

}
