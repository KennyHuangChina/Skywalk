package com.kjs.skywalk.app_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.Apartment.Activity_ApartmentDetail;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.List;

import static com.kjs.skywalk.app_android.ClassDefine.ServerError.SERVER_NEED_LOGIN;
import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_BEHALF_HOUSE_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_BRIEF_PUBLIC_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_USER_HOUSE_WATCH_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_HOUSE_LST_APPOINT_SEE;

public class Activity_ApartmentList extends SKBaseActivity {

    public static final int TYPE_TO_APPROVE = 0;
    public static final int TYPE_TO_SALE = 1;
    public static final int TYPE_TO_RENT = 2;
    public static final int TYPE_RENTED = 3;
    public static final int TYPE_ALL_AGENCY_HOUSES = 4;
    public static final int TYPE_WATCH_LIST = 5;
    public static final int TYPE_APPOINTMENT = 6;
    public static final int TYPE_BROWSING_HISTORY = 7;


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

        //loadDataFromServer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.reset();
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
            case TYPE_ALL_AGENCY_HOUSES: {
                getBehalfHousesList(0);
                break;
            }
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
            case TYPE_WATCH_LIST: {
                getBehalfHousesList(5);
                break;
            }
            case TYPE_APPOINTMENT: {
                getBehalfHousesList(6);
                break;
            }
            case TYPE_BROWSING_HISTORY: {
                getBrowingHistory();
                break;
            }
        }
    }

    private void updateTitle(int type) {
        TextView title = (TextView)findViewById(R.id.tv_title);
        switch(type){
            case TYPE_ALL_AGENCY_HOUSES: {
                title.setText("我代理的房源");
                break;
            }
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
            case TYPE_WATCH_LIST: {
                title.setText("我的关注");
                break;
            }
            case TYPE_APPOINTMENT: {
                title.setText("我的预约");
                break;
            }
            case TYPE_BROWSING_HISTORY: {
                title.setText("浏览记录");
                break;
            }
        }
    }

    private void updateList() {
        mAdapter.addData(mDataList, mDataList.size());
        mAdapter.notifyDataSetChanged();
    }

    private void getBrowingHistory() {
        CommandManager manager = CommandManager.getCmdMgrInstance(this);
        List<String> idLst = SKLocalSettings.browsing_history_read(this);
        kjsLogUtil.i("idLst:" + idLst);
        for (String houseId : idLst) {
            manager.GetBriefPublicHouseInfo(Integer.valueOf(houseId));
        }
    }

    private void fillHouseInfo(final IApiResults.IHouseDigest apiHouseDigest) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ClassDefine.HouseDigest houseDigest = new ClassDefine.HouseDigest();
                houseDigest.houseId = apiHouseDigest.GetHouseId();
                houseDigest.property = apiHouseDigest.GetProperty();
                houseDigest.addr = apiHouseDigest.GetPropertyAddr();
                houseDigest.Bedrooms = apiHouseDigest.GetBedrooms();
                houseDigest.Livingrooms = apiHouseDigest.GetLivingrooms();
                houseDigest.Bathrooms = apiHouseDigest.GetBathrooms();
                double acreage = (double) apiHouseDigest.GetAcreage() / 100.0;
                houseDigest.Acreage = Double.valueOf(String.format("%.02f", acreage));
                double rental = (double) apiHouseDigest.GetRental() / 100.0;
                houseDigest.Rental = Double.valueOf(String.format("%.02f", rental));
                double pricing = (double) apiHouseDigest.GetPricing() / 100.0;
                houseDigest.Pricing = Double.valueOf(String.format("%.02f", pricing));
                houseDigest.CoverImage = apiHouseDigest.GetCoverImage();
                houseDigest.CoverImageUrlS = apiHouseDigest.GetCoverImageUrlS();
                houseDigest.CoverImageUrlM = apiHouseDigest.GetCoverImageUrlM();
                houseDigest.includePropertyFee = apiHouseDigest.IsRentalIncPropFee();

                ArrayList<Object> houseTags = ((IApiResults.IResultList) apiHouseDigest).GetList();
                houseDigest.houseTags = new ArrayList<>();
                for (Object houseTagObj : houseTags) {
                    IApiResults.IHouseTag tag = (IApiResults.IHouseTag) houseTagObj;
                    ClassDefine.HouseTag houseTag = new ClassDefine.HouseTag(tag.GetTagId(), tag.GetName());
                    houseDigest.houseTags.add(houseTag);
                }

                ArrayList<ClassDefine.HouseDigest> dataList = new ArrayList<>();
                dataList.add(houseDigest);
                mAdapter.addData(dataList, dataList.size());
                mAdapter.notifyDataSetChanged();
            }
        });
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
            public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }
                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                    Activity_ApartmentList.super.onCommandFinished(command, cmdSeq, iResult);
                    return;
                }

                if (command == CMD_GET_BEHALF_HOUSE_LIST || command == CMD_HOUSE_LST_APPOINT_SEE || command == CMD_GET_USER_HOUSE_WATCH_LIST) {
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

        CommandManager manager = CommandManager.getCmdMgrInstance(this);
        switch (type) {
            case TYPE_TO_APPROVE:
            case TYPE_TO_SALE:
            case TYPE_TO_RENT:
            case TYPE_RENTED:
            case TYPE_ALL_AGENCY_HOUSES:
            {
                manager.GetBehalfHouses(type, 0, 0);
                break;
            }
            case TYPE_WATCH_LIST:
            {
                manager.GetUserHouseWatchList(0, 0);
                break;
            }
            case TYPE_APPOINTMENT:
            {
                manager.GetHouseList_AppointSee(0, 0);
                break;
            }
        }
    }

    private void gettBehalfHousesListDetail(int type, int total) {
        if(total <= 0){
            return;
        }
        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, final int cmdSeq, final IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }
                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                    Activity_ApartmentList.super.onCommandFinished(command, cmdSeq, iResult);
                    return;
                }

                if (command == CMD_GET_BEHALF_HOUSE_LIST || command == CMD_HOUSE_LST_APPOINT_SEE || command == CMD_GET_USER_HOUSE_WATCH_LIST) {
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

        CommandManager manager = CommandManager.getCmdMgrInstance(this);
        switch (type) {
            case TYPE_TO_APPROVE:
            case TYPE_TO_SALE:
            case TYPE_TO_RENT:
            case TYPE_RENTED:
            case TYPE_ALL_AGENCY_HOUSES:
            {
                manager.GetBehalfHouses(type, 0, total);
                break;
            }
            case TYPE_WATCH_LIST:
            {
                manager.GetUserHouseWatchList(0, total);
                break;
            }
            case TYPE_APPOINTMENT:
            {
                manager.GetHouseList_AppointSee(0, total);
                break;
            }
        }
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
                break;
            }
            default: {
                commonFun.startActivityWithHouseId(this, Activity_ApartmentDetail.class, digest.houseId);
                break;
            }
        }
    }

    @Override
    public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
        kjsLogUtil.i("Activity_ApartmentDetail::onCommandFinished");
        if (null == iResult) {
            kjsLogUtil.w("result is null");
            return;
        }
        kjsLogUtil.i(String.format("[command: %d] --- %s", command, iResult.DebugString()));
        if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
            kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
            return;
        }

        if (command == CMD_GET_BRIEF_PUBLIC_HOUSE_INFO) {
            // CMD_GET_BRIEF_PUBLIC_HOUSE_INFO, IApiResults.IHouseDigest & IApiResults.IResultList(IApiResults.IHouseTag)
            fillHouseInfo((IApiResults.IHouseDigest) iResult);
        }
    }
}
