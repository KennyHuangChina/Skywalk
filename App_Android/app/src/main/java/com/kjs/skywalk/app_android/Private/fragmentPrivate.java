package com.kjs.skywalk.app_android.Private;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kjs.skywalk.app_android.Activity_PasswordReset;
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
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_LOGIN_USER_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_USER_HOUSE_WATCH_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_HOUSE_LST_APPOINT_SEE;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentPrivate extends Fragment {
//    private CommandManager mCmdMgr;
    private boolean mIsLogin = false;
    private RelativeLayout mRlTitleBar;
    private RelativeLayout mRlUserNotLogin;
    private LinearLayout mLlUserLogin;
    private ImageView mIv_head_portrait;

    private EditText mEt_user_name;

    private LinearLayout mLlWatchListCount;         // 我的关注
    private LinearLayout mLl_browsing_history;      // 浏览记录
    private LinearLayout mLl_see_count;             // 看房记录
    private LinearLayout mLlAppointmentCount;       // 我的预约

    private TextView mTvWatchListCount;         // 我的关注
    private TextView mTv_browsing_history;      // 浏览记录
    private TextView mTv_see_count;             // 看房记录
    private TextView mTvAppointmentCount;       // 我的预约

    // 我的交易
    private RelativeLayout mRl_transaction;     // 我的交易
    private RelativeLayout mRl_rent_out;        // 我已租出
    private RelativeLayout mRl_house_rented;    // 我已租到
    private RelativeLayout mRl_lease;           // 我的租约

    private TextView mTv_transaction;
    private TextView mTv_rent_out;
    private TextView mTv_house_rented;
    private TextView mTv_lease;

    // 我代理的房源
    private RelativeLayout mRl_to_rent;
    private RelativeLayout mRl_rented;
    private RelativeLayout mRl_to_sale;
    private RelativeLayout mRl_month_turnoff;
    private RelativeLayout mRl_to_approve;


    private TextView mTv_agency_houses;
    private TextView mTvToRent;
    private TextView mTvRented;
    private TextView mTvToSale;
    private TextView mTv_month_turnoff;
    private TextView mTvToApprove;

    private String mUserName;
    private String mUserTelephoneNum;

    @Override
    public void onResume() {
        super.onResume();

        mIsLogin =  SKLocalSettings.UISettings_get(getActivity(), SKLocalSettings.UISettingsKey_LoginStatus, false);
        kjsLogUtil.i(String.format("mIsLogin is %b", mIsLogin));
        updateLayout(mIsLogin);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private, container, false);

        mRlTitleBar = (RelativeLayout) view.findViewById(R.id.rl_titlebar);
        mRlUserNotLogin = (RelativeLayout) view.findViewById(R.id.rl_user_not_login);
        mLlUserLogin = (LinearLayout) view.findViewById(R.id.ll_user_login);
        mIv_head_portrait = (ImageView) view.findViewById(R.id.iv_head_portrait);

        mEt_user_name = (EditText) view.findViewById(R.id.et_user_name);


        (view.findViewById(R.id.iv_setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettingsMenu(view);
            }
        });

//        mCmdMgr = CommandManager.getCmdMgrInstance(getActivity(), mCmdListener, mProgreessListener);

        mLlWatchListCount = (LinearLayout) view.findViewById(R.id.ll_watchlist_count);          // 我的关注
        mLl_browsing_history = (LinearLayout) view.findViewById(R.id.ll_browsing_history);      // 浏览记录
        mLl_see_count = (LinearLayout) view.findViewById(R.id.ll_see_count);                    // 看房记录
        mLlAppointmentCount = (LinearLayout) view.findViewById(R.id.ll_appointment_count);      // 我的预约

        mTvWatchListCount = (TextView) view.findViewById(R.id.tv_watchlist_count);              //        mCmdMgr.GetUserHouseWatchList(0, 0);
        mTv_browsing_history = (TextView) view.findViewById(R.id.tv_browsing_history);
        mTv_see_count = (TextView) view.findViewById(R.id.tv_see_count);
        mTvAppointmentCount = (TextView) view.findViewById(R.id.tv_appointment_count);          //        mCmdMgr.GetHouseList_AppointSee(0, 0);

        // 我的交易
        mRl_transaction  = (RelativeLayout) view.findViewById(R.id.rl_transaction);
        mRl_rent_out  = (RelativeLayout) view.findViewById(R.id.rl_rent_out);
        mRl_house_rented  = (RelativeLayout) view.findViewById(R.id.rl_house_rented);
        mRl_lease  = (RelativeLayout) view.findViewById(R.id.rl_lease);

        mTv_transaction = (TextView) view.findViewById(R.id.tv_transaction);
        mTv_rent_out = (TextView) view.findViewById(R.id.tv_rent_out);
        mTv_house_rented = (TextView) view.findViewById(R.id.tv_house_rented);
        mTv_lease = (TextView) view.findViewById(R.id.tv_lease);

        // 我代理的房源
        mRl_to_rent = (RelativeLayout) view.findViewById(R.id.rl_to_rent);
        mRl_rented = (RelativeLayout) view.findViewById(R.id.rl_rented);
        mRl_to_sale = (RelativeLayout) view.findViewById(R.id.rl_to_sale);
        mRl_month_turnoff = (RelativeLayout) view.findViewById(R.id.rl_month_turnoff);
        mRl_to_approve = (RelativeLayout) view.findViewById(R.id.rl_to_approve);

        mTv_agency_houses = (TextView) view.findViewById(R.id.tv_agency_houses);
        mTvToRent = (TextView) view.findViewById(R.id.tv_to_rent);
        mTvRented = (TextView) view.findViewById(R.id.tv_rented);
        mTvToSale = (TextView) view.findViewById(R.id.tv_to_sale);
        mTv_month_turnoff = (TextView) view.findViewById(R.id.tv_month_turnoff);
        mTvToApprove = (TextView) view.findViewById(R.id.tv_to_approve);

        ((RelativeLayout)view.findViewById(R.id.rl_to_rent)).setEnabled(false);

        // for emulator test
//        SKLocalSettings.UISettings_set(this.getActivity(), SKLocalSettings.UISettingsKey_LoginStatus, false);

        return view;
    }

    private void updateCount(final TextView textview, final int nCount) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textview.getId() == R.id.tv_agency_houses) {
                    // 我代理的房源
                    textview.setText(String.format("所有房源 ( %d )", nCount));
                } else {
                    textview.setText(String.valueOf(nCount));
                }
            }
        });
    }

    // 我代理的房源
    private void updateBehalfHouseCount() {
        // get behalf info
//        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(getActivity(), mCmdListener, mProgreessListener);
//        type    : list type. 0 - all; 1 - to rent; 2 - rented; 3 - to sale; 4 - to approve
//        1- 待租；2-已租； 3-待售； 4-待审核

        // all agency houses
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
                        updateCount(mTv_agency_houses, resultList.GetTotalNumber());
                    }
                }
            }
        }, mProgreessListener).GetBehalfHouses(0, 0 , 0);

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
    }

    private void updateLayout(boolean isLogin) {

        boolean bEnabled = false;
        if (isLogin) {
            mRlTitleBar.setBackgroundColor(Color.parseColor("#00AE63"));
            mRlUserNotLogin.setVisibility(View.GONE);
            mLlUserLogin.setVisibility(View.VISIBLE);
            commonFun.displayImageWithMask(this.getActivity(), mIv_head_portrait, R.drawable.touxiang, R.drawable.head_portrait_mask);


            CommandManager.getCmdMgrInstance(getActivity(), mCmdListener, mProgreessListener).GetUserHouseWatchList(0, 0);
            CommandManager.getCmdMgrInstance(getActivity(), mCmdListener, mProgreessListener).GetHouseList_AppointSee(0, 0);
            updateBehalfHouseCount();
            CommandManager.getCmdMgrInstance(getActivity(), mCmdListener, mProgreessListener).GetLoginUserInfo();

            bEnabled = true;

        } else {
            mRlTitleBar.setBackgroundColor(Color.parseColor("#E5E5E5"));
            mRlUserNotLogin.setVisibility(View.VISIBLE);
            mLlUserLogin.setVisibility(View.GONE);
            commonFun.displayImageWithMask(this.getActivity(), mIv_head_portrait, R.drawable.touxiang, R.drawable.head_portrait_mask);
        }
        mLlWatchListCount.setEnabled(bEnabled);
        mLl_browsing_history.setEnabled(bEnabled);
        mLl_see_count.setEnabled(bEnabled);
        mLlAppointmentCount.setEnabled(bEnabled);

        mRl_transaction.setEnabled(bEnabled);
        mRl_rent_out.setEnabled(bEnabled);
        mRl_house_rented.setEnabled(bEnabled);
        mRl_lease.setEnabled(bEnabled);

        mRl_to_rent.setEnabled(bEnabled);
        mRl_rented.setEnabled(bEnabled);
        mRl_to_sale.setEnabled(bEnabled);
        mRl_month_turnoff.setEnabled(bEnabled);
        mRl_to_approve.setEnabled(bEnabled);
    }

    private void updateUserInfo(final IApiResults.IGetUserInfo userInfo) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mUserName = userInfo.GetName();
                mUserTelephoneNum = userInfo.GetPhoneNo();
                mEt_user_name.setText(mUserName);
            }
        });
    }

    private void showPasswordResetActivity() {
        Intent intent = new Intent(getActivity(), Activity_PasswordReset.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("islogin", mIsLogin);
        bundle.putString("user_name", mUserName);
        bundle.putString("user_telephone_num", mUserTelephoneNum);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);

    }

    private void showSettingsMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this.getActivity(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_fragment__private, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_settings_password_reset:
                    {
                        showPasswordResetActivity();
                    }
                    break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    CommunicationInterface.CICommandListener mCmdListener = new CommunicationInterface.CICommandListener() {
        @Override
        public void onCommandFinished(int command, IApiResults.ICommon iResult) {
            if (null == iResult) {
                kjsLogUtil.w("result is null");
                return;
            }

            kjsLogUtil.i(String.format("[command: %d(%s)] --- %s", command, CommunicationInterface.CmdID.GetCmdDesc(command), iResult.DebugString()));

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
                    // 我的关注
                    updateCount(mTvWatchListCount, resultList.GetTotalNumber());
                }
            }

            if (command == CMD_GET_LOGIN_USER_INFO) {
                // IApiResults.IGetUserInfo
            IApiResults.IGetUserInfo userInfo = (IApiResults.IGetUserInfo)iResult;
                if (CommunicationError.CE_ERROR_NO_ERROR == iResult.GetErrCode()) {
                    updateUserInfo(userInfo);
//                    SKLocalSettings.UISettings_set(MainActivity.this, SKLocalSettings.UISettingsKey_LoginStatus, true);
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
