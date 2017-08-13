package com.kjs.skywalk.app_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_BEHALF_HOUSE_LIST;

public class Activity_ApartmentList extends SKBaseActivity {

    public static final int TYPE_TO_APPROVE = 0;
    public static final int TYPE_TO_SALE = 1;
    public static final int TYPE_TO_RENT = 2;
    public static final int TYPE_RENTED = 3;

    private ListView mListView = null;
    private AdapterForApartmentList mAdapter = null;

    private int mType = -1;
    private int mTotal = 0;

    ArrayList<ClassDefine.HouseDigest> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__apartment_list);
        Intent i = getIntent();
        if(i != null) {
            mType = i.getIntExtra("type", -1);
        }
        updateTitle(mType);

        mListView = (ListView)findViewById(R.id.lv_apartment_list);
        mAdapter = new AdapterForApartmentList(this);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassDefine.HouseDigest item = (ClassDefine.HouseDigest)mAdapter.getItem(position);

                doSelectItem(item);
            }
        });

        loadDataFromServer();
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_title: {
                finish();
            }
            break;
        }
    }

    //        type    : list type. 0 - all; 1 - to rent; 2 - rented; 3 - to sale; 4 - to approve
    //        1- 待租；2-已租； 3-待售； 4-待审核
    private void loadDataFromServer() {
        switch(mType){
            case TYPE_TO_APPROVE: {
                getToApproveList();
                break;
            }
            case TYPE_TO_RENT: {
                getBehalfHousesList(1);
                break;
            }
            case TYPE_TO_SALE: {
                getBehalfHousesList(3);
                break;
            }
            case TYPE_RENTED: {
                getBehalfHousesList(2);
                break;
            }
        }
    }

    private void updateTitle(int type) {
        TextView title = (TextView)findViewById(R.id.tv_title);
        switch(type){
            case TYPE_TO_APPROVE: {
                title.setText("待审核房源");
                break;
            }
            case TYPE_TO_RENT: {
                title.setText("待租房源");
                break;
            }
            case TYPE_TO_SALE: {
                title.setText("待售房源");
                break;
            }
            case TYPE_RENTED: {
                title.setText("已租房源");
                break;
            }
        }
    }

    private void updateList() {
        mAdapter.addData(mDataList, mDataList.size());
    }

//    private void getToApproveListDetail() {
//        if(mTotal <= 0){
//            return;
//        }
//        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
//            @Override
//            public void onCommandFinished(int command, final IApiResults.ICommon iResult) {
//                if (null == iResult) {
//                    kjsLogUtil.w("result is null");
//                    return;
//                }
//                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
//                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
//                    return;
//                }
//
//                if (command == CMD_GET_BEHALF_HOUSE_LIST) {
//                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
//                    int nFetch = resultList.GetFetchedNumber();
//                    if (nFetch != -1) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mDataList = ClassDefine.ListConvert.IHouseDigestListToHouseDigestList(((IApiResults.IResultList) iResult).GetList());
//                                updateList();
//                            }
//                        });
//                    }
//                }
//            }
//        };
//
//        CommandManager manager = CommandManager.getCmdMgrInstance(this, listener, this);
//        manager.GetBehalfHouses(4, 0, mTotal);
//    }


    private void getToApproveList() {
        getBehalfHousesList(4);

//        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
//            @Override
//            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
//                if (null == iResult) {
//                    kjsLogUtil.w("result is null");
//                    return;
//                }
//                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
//                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
//                    return;
//                }
//
//                if (command == CMD_GET_BEHALF_HOUSE_LIST) {
//                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
//                    int nFetch = resultList.GetFetchedNumber();
//                    if (nFetch == -1) {
//                        mTotal = ((IApiResults.IResultList) iResult).GetTotalNumber();
//                        if (mTotal > 0) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    getToApproveListDetail();
//                                }
//                            });
//                        }
//                    }
//                }
//            }
//        };
//
//        CommandManager manager = CommandManager.getCmdMgrInstance(this, listener, this);
//        manager.GetBehalfHouses(4, 0, 0);
    }

    private void getBehalfHousesList(final int type) {
        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }
                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                    return;
                }

                if (command == CMD_GET_BEHALF_HOUSE_LIST) {
                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                    int nFetch = resultList.GetFetchedNumber();
                    if (nFetch == -1) {
                        final int total = ((IApiResults.IResultList) iResult).GetTotalNumber();
                        if (total > 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gettBehalfHousesListDetail(type, total);
                                }
                            });
                        }
                    }
                }
            }
        };

        CommandManager manager = CommandManager.getCmdMgrInstance(this, listener, this);
        manager.GetBehalfHouses(type, 0, 0);
    }

    private void gettBehalfHousesListDetail(int type, int total) {
        if(total <= 0){
            return;
        }
        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, final IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }
                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                    return;
                }

                if (command == CMD_GET_BEHALF_HOUSE_LIST) {
                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                    int nFetch = resultList.GetFetchedNumber();
                    if (nFetch != -1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDataList = ClassDefine.ListConvert.IHouseDigestListToHouseDigestList(((IApiResults.IResultList) iResult).GetList());
                                updateList();
                            }
                        });
                    }
                }
            }
        };

        CommandManager manager = CommandManager.getCmdMgrInstance(this, listener, this);
        manager.GetBehalfHouses(type, 0, total);
    }

    private void startApproveActivity(ClassDefine.HouseDigest digest) {
        Intent intent = new Intent(Activity_ApartmentList.this, Activity_Zushouweituo_shenhe.class);
        intent.putExtra(ClassDefine.IntentExtraKeyValue.KEY_HOUSE_ID, digest.houseId);
        intent.putExtra(ClassDefine.IntentExtraKeyValue.KEY_PROPERTY_NAME, digest.property);
        startActivity(intent);
    }

    private void doSelectItem(ClassDefine.HouseDigest digest) {
        switch (mType) {
            case TYPE_TO_APPROVE: {
                startApproveActivity(digest);
            }
        }
    }
}
