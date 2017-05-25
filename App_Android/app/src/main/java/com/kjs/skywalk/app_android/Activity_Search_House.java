package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import com.kjs.skywalk.communicationlibrary.IApiResults.IPropertyInfo;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Search_House extends Activity implements
        CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{
    private ListView mListView = null;
    private SearchView mSearchView = null;
    private HouseSearchHistory mHistory = null;
    private LinearLayout mHistoryLayout = null;
    private LinearLayout mNoHistoryLayout = null;
    private ScrollView mAddHouseLayout = null;
    private AdapterHouseSearchHistory mAdapter = null;

    private int mPropertyCount = 0;
    private ArrayList<IPropertyInfo> mPropertyList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__search_house);

        CommandManager manager = new CommandManager(this, this, this);
        manager.GetPropertyListByName("", 0, 0);

        mHistory = new HouseSearchHistory(this);

        mHistoryLayout = (LinearLayout)findViewById(R.id.layoutHistory) ;
        mNoHistoryLayout = (LinearLayout)findViewById(R.id.noHistory) ;
        mAddHouseLayout = (ScrollView) findViewById(R.id.scrollViewAdd) ;

        mSearchView = (SearchView)findViewById(R.id.search_view);
        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) mSearchView.findViewById(id);
        textView.setTextSize(14);
        textView.setGravity(Gravity.BOTTOM);

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

        mListView = (ListView)findViewById(R.id.listViewContent);
        mListView.setFocusable(false);
        mAdapter = new AdapterHouseSearchHistory(this);
        mListView.setAdapter(mAdapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mHistory.addHistory(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")) {
                    mNoHistoryLayout.setVisibility(View.GONE);
                    mAddHouseLayout.setVisibility(View.GONE);
                    mHistoryLayout.setVisibility(View.VISIBLE);
                } else {
                    if(mHistory.searched(newText)) {
                        mNoHistoryLayout.setVisibility(View.GONE);
                        mAddHouseLayout.setVisibility(View.GONE);
                        mHistoryLayout.setVisibility(View.VISIBLE);
                    } else {
                        mAddHouseLayout.setVisibility(View.VISIBLE);
                        mNoHistoryLayout.setVisibility(View.GONE);
                        mHistoryLayout.setVisibility(View.GONE);
                    }
                }

                return true;
            }
        });
    }

    protected void onResume() {
        super.onResume();

        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        mSearchView.onActionViewExpanded();
        mSearchView.setIconifiedByDefault(false);

        if(mHistory.getCount() == 0) {
            mHistoryLayout.setVisibility(View.GONE);
            mAddHouseLayout.setVisibility(View.GONE);
            mNoHistoryLayout.setVisibility(View.VISIBLE);
        } else{
            mHistoryLayout.setVisibility(View.GONE);
            mAddHouseLayout.setVisibility(View.GONE);
            mHistoryLayout.setVisibility(View.VISIBLE);
        }
    }

    private void updateAdapter(String keywords) {
        mListView.removeAllViewsInLayout();
        ArrayList<String> newList = new ArrayList<>();
        for(IPropertyInfo info : mPropertyList) {
            if(info.GetName().contains(keywords)) {
                newList.add(info.GetName());
            }
        }
        mAdapter.setDataList(newList);
        mAdapter.notifyDataSetChanged();
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title: {
                finish();
                break;
            }
            case R.id.textViewAdd: {

                break;
            }
        }
    }

    private void fetchPropertyList() {
        CommandManager manager = new CommandManager(this, this, this);
        manager.GetPropertyListByName("", 0, mPropertyCount);
    }

    private final int MSG_FETCH_PROPERTY_LIST = 0;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_FETCH_PROPERTY_LIST: {
                    fetchPropertyList();
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
                    ArrayList<Object> array = list.GetList();
                    for(Object obj : array) {
                        IPropertyInfo info = (IPropertyInfo)obj;
                        mPropertyList.add(info);
                    }
                } else {
                    mHandler.sendEmptyMessageDelayed(MSG_FETCH_PROPERTY_LIST, 0);
                }
            }
        }
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
