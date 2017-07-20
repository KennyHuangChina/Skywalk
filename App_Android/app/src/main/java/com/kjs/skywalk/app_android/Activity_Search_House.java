package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import com.kjs.skywalk.communicationlibrary.IApiResults.IPropertyInfo;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Search_House extends SKBaseActivity implements
        CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{
    private final String TAG = getClass().getSimpleName().toString();
    private ListView mListViewHistory = null;
    private ListView mListViewProperty = null;
    private SearchView mSearchView = null;
    private HouseSearchHistory mHistory = null;
    private LinearLayout mHistoryLayout = null;
    private LinearLayout mNoHistoryLayout = null;
    private ScrollView mAddHouseLayout = null;
    private AdapterSelectPropertyHistory mAdapterHistory = null;
    private AdapterHouseSearchResult mAdapterProperty = null;
    private int mPropertyCount = 0;
    private ArrayList<IPropertyInfo> mPropertyList = new ArrayList<>();

    private final int MSG_FETCH_PROPERTY_LIST = 0;
    private final int MSG_PROPERTY_LIST_DONW = 1;
    private final int MSG_HIDE_WAITING = 2;

    private int mPropertyId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__search_house);

        mHistory = new HouseSearchHistory(this);

        mHistoryLayout = (LinearLayout)findViewById(R.id.layoutHistory) ;
        mNoHistoryLayout = (LinearLayout)findViewById(R.id.noHistory) ;
        mAddHouseLayout = (ScrollView) findViewById(R.id.scrollViewAdd) ;

        mSearchView = (SearchView)findViewById(R.id.search_view);
        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) mSearchView.findViewById(id);
        textView.setTextSize(14);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(lp);

        if (mSearchView != null) {
            try {
                Class<?> argClass = mSearchView.getClass();
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                ownField.setAccessible(true);
                View mView = (View) ownField.get(mSearchView);
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mListViewHistory = (ListView)findViewById(R.id.listViewHistory);
        mListViewHistory.setFocusable(false);
        mAdapterHistory = new AdapterSelectPropertyHistory(this);
        mListViewHistory.setAdapter(mAdapterHistory);

        mListViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String property = (String)parent.getAdapter().getItem(position);
                int propertyId = mHistory.getPropertyIdByName(property);
                if(propertyId >= 0) {
                    doSelected(property, propertyId);
                }
            }
        });

        mListViewProperty = (ListView)findViewById(R.id.listViewAdd);
        mListViewProperty.setFocusable(false);
        mAdapterProperty = new AdapterHouseSearchResult(this);
        mListViewProperty.setAdapter(mAdapterProperty);
        mListViewProperty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassDefine.Property property = (ClassDefine.Property)parent.getAdapter().getItem(position);
                int propertyId = getPropertyIdFromList(property.mName);
                if(propertyId >= 0) {
                    doSelected(property.mName, propertyId);
                }
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                commonFun.hideSoftKeyboard(getApplicationContext(), mSearchView);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")) {
                    updateAdapterHistory();
                } else {
                    showPropertyList();
                    updatePropertyList(newText);
                }
                return true;
            }
        });
    }

    private void doSelected(String property, int propertyId) {
        if(property == null || property.isEmpty()) {
            return;
        }

        String history = property + "=" + propertyId;
        mHistory.addHistory(history);
        
        Intent data = new Intent();
        data.putExtra("name", property);
        data.putExtra("id", propertyId);
        setResult(30, data);
        finish();
    }

    protected void onResume() {
        super.onResume();

        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        mSearchView.onActionViewExpanded();
        mSearchView.setIconifiedByDefault(false);
    }

    private void showPropertyList() {
        mHistoryLayout.setVisibility(View.GONE);
        mNoHistoryLayout.setVisibility(View.GONE);
        mAddHouseLayout.setVisibility(View.VISIBLE);
    }

    private void makePropertyList(ArrayList<Object> array) {
        for(Object obj : array) {
            IPropertyInfo info = (IPropertyInfo)obj;
            mPropertyList.add(info);
            Log.i(TAG, "Property ++ " + info.GetName());
        }
    }

    private void updateAdapterProperty() {
        ArrayList<ClassDefine.Property> newList = new ArrayList<>();
        for(IPropertyInfo info : mPropertyList) {
            ClassDefine.Property property = new ClassDefine.Property();
            property.mName = info.GetName();
            property.mAddress = info.GetAddress();
            newList.add(property);
        }

        mAdapterProperty.setDataList(newList);
        mListViewProperty.setAdapter(mAdapterProperty);
        mAdapterProperty.notifyDataSetChanged();
    }

    private void updatePropertyList(final String keywords) {
        mListViewProperty.removeAllViewsInLayout();
        mPropertyList.clear();

        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int i, IApiResults.ICommon iCommon) {
                if(i == CommunicationInterface.CmdID.CMD_GET_PROPERTY_LIST) {
                    if(iCommon.GetErrCode() == CE_ERROR_NO_ERROR) {
                        IApiResults.IResultList list = (IApiResults.IResultList) iCommon;
                        mPropertyCount = list.GetTotalNumber();
                        int nFetchCount = list.GetFetchedNumber();
                        if(nFetchCount == 0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fetchPropertyList(keywords);
                                }
                            });
                        }
                    }
                }

                Activity_Search_House.super.onCommandFinished(i, iCommon);
            }
        };

        CommandManager manager = new CommandManager(this, listener, this);
        manager.GetPropertyListByName(keywords, 0, 0);
    }

    private void updateAdapterHistory() {
        if(mHistory.getCount() > 0) {
            mNoHistoryLayout.setVisibility(View.GONE);
            mAddHouseLayout.setVisibility(View.GONE);
            mHistoryLayout.setVisibility(View.VISIBLE);
        } else {
            mNoHistoryLayout.setVisibility(View.VISIBLE);
            mAddHouseLayout.setVisibility(View.GONE);
            mHistoryLayout.setVisibility(View.GONE);
            return;
        }

        mListViewHistory.removeAllViewsInLayout();
        ArrayList<String> newList = new ArrayList<>();
        for(int i = 0; i < mHistory.getCount(); i ++){
            String tmp = mHistory.get(i);
            String tmpList[] = tmp.split("=");
            newList.add(tmpList[0]);
        }
        mAdapterHistory.setDataList(newList);
        mListViewHistory.setAdapter(mAdapterHistory);
        mAdapterHistory.notifyDataSetChanged();
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title: {
                finish();
                break;
            }
            case R.id.textViewAdd: {
                commonFun.hideSoftKeyboard(getApplicationContext(), v);
                mSearchView.clearFocus();
                addProperty();
                break;
            }
            case R.id.cleanHistory: {
                mHistory.cleanHistory();
                updateAdapterHistory();
                break;
            }
        }
    }

    private int getPropertyIdFromList(String property) {
        int itemCount = mPropertyList.size();
        if(itemCount != 0) {
            for(IPropertyInfo info : mPropertyList) {
                if(info.GetName().equals(property)) {
                    return info.GetId();
                }
            }
        }

        return -1;
    }

    private void fetchPropertyList(String keywords) {
        CommandManager manager = new CommandManager(this, this, this);
        manager.GetPropertyListByName(keywords, 0, mPropertyCount);
    }

    private void addProperty() {
        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int i, IApiResults.ICommon iCommon) {
                if(iCommon.GetErrCode() == CE_ERROR_NO_ERROR) {
                    commonFun.showToast_info(getApplicationContext(), mSearchView, "添加成功");
                    IApiResults.IAddRes res = (IApiResults.IAddRes)iCommon;
                    mPropertyId = res.GetId();
                    Log.i(TAG, "New Property ID: " + mPropertyId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String text = commonFun.getTextOnSearchView(mSearchView);
                            doSelected(text, mPropertyId);
                            finish();
                        }
                    });
                } else {
                    commonFun.showToast_info(getApplicationContext(), mSearchView, iCommon.GetErrDesc());
                }

                hideWaiting();

                Activity_Search_House.super.onCommandFinished(i, iCommon);
            }
        };

        int res = 0;
        CommandManager manager = new CommandManager(this, listener, this);
        String text = commonFun.getTextOnSearchView(mSearchView);
        res = manager.AddProperty(text, "地址：未填写", "");
        if(res == CE_ERROR_NO_ERROR) {
            showWaiting(mSearchView);
        } else {
            commonFun.showToast_info(getApplicationContext(), mSearchView, "失败");
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_FETCH_PROPERTY_LIST: {
                    break;
                }
                case MSG_PROPERTY_LIST_DONW: {
                    updateAdapterProperty();
                    break;
                }
            }
        }
    };

    @Override
    public void onCommandFinished(int i, IApiResults.ICommon iCommon) {
        if(i == CommunicationInterface.CmdID.CMD_GET_PROPERTY_LIST) {
            if(iCommon.GetErrCode() == CE_ERROR_NO_ERROR) {
                IApiResults.IResultList list = (IApiResults.IResultList) iCommon;
                mPropertyCount = list.GetTotalNumber();
                int nFetchCount = list.GetFetchedNumber();
                if(nFetchCount > 0) {
                    makePropertyList(list.GetList());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateAdapterProperty();
                    }
                });
            }
        }
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
