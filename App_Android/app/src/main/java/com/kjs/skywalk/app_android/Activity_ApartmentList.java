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

    private void loadDataFromServer() {
        switch(mType){
            case TYPE_TO_APPROVE: {
                getToApproveList();
                break;
            }
            case TYPE_TO_RENT: {
                break;
            }
            case TYPE_TO_SALE: {
                break;
            }
            case TYPE_RENTED: {
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
                break;
            }
            case TYPE_TO_SALE: {
                break;
            }
            case TYPE_RENTED: {
                break;
            }
        }
    }

    private void updateList() {
        mAdapter.addData(mDataList, mDataList.size());
    }

    private void getToApproveListDetail() {
        if(mTotal <= 0){
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
        manager.GetBehalfHouses(4, 0, mTotal);
    }


    private void getToApproveList() {
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
                        mTotal = ((IApiResults.IResultList) iResult).GetTotalNumber();
                        if (mTotal > 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getToApproveListDetail();
                                }
                            });
                        }
                    }
                }
            }
        };

        CommandManager manager = CommandManager.getCmdMgrInstance(this, listener, this);
        manager.GetBehalfHouses(4, 0, 0);
    }

    private void doSelectItem(ClassDefine.HouseDigest digest) {
        kjsLogUtil.i("Property Name: " + digest.property);
        kjsLogUtil.i("location: " + digest.addr);
    }
}
