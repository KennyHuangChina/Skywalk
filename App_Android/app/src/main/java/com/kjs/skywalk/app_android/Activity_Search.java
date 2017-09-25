package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Search extends SKBaseActivity {
    private final String TAG = "Search";
    private ListView mListView = null;
    private LinearLayout mContainer = null;
    private LinearLayout mContainerEmpty = null;
    private PropertySearchHistory mHistory = null;
    private ArrayList<String> mGuess = new ArrayList<String>();
    private int mActScreenWidth = 1080;
    private Context mContext;
    AdapterHouseSearchResult mAdapter = null;

    private ArrayList<IApiResults.IPropertyInfo> mPropertyList = new ArrayList<>();
    private int mPropertyCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = getBaseContext();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mActScreenWidth = metric.widthPixels;
        mActScreenWidth -= 100;
        final SearchView searchView = (SearchView)findViewById(R.id.search_view);

        mHistory = new PropertySearchHistory(this);

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextSize(14);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(lp);

        if (searchView != null) {
            try {
                Class<?> argClass = searchView.getClass();
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                ownField.setAccessible(true);
                View mView = (View) ownField.get(searchView);
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mListView = (ListView)findViewById(R.id.listViewResult);
        mAdapter = new AdapterHouseSearchResult(this);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassDefine.Property property = (ClassDefine.Property)parent.getAdapter().getItem(position);
                int propertyId = getPropertyIdFromList(property.mName);
                if(propertyId >= 0) {
                    doSelected(property.mName, propertyId);
                }
            }
        });

        mContainer = (LinearLayout)findViewById(R.id.searchResultContainer);
        mContainerEmpty = (LinearLayout)findViewById(R.id.searchResultEmptyContainer);

        mContainer.setVisibility(View.GONE);
        mContainerEmpty.setVisibility(View.VISIBLE);

        initHistory();
        initGuess();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                commonFun.hideSoftKeyboard(mContext, searchView);
//                startHouseListActivity();
//                finish();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")) {
                    mContainer.setVisibility(View.GONE);
                    mContainerEmpty.setVisibility(View.VISIBLE);
                } else {
                    updatePropertyList(newText);
                    if(!mContainer.isShown()) {
                        mContainer.setVisibility(View.VISIBLE);
                        mContainerEmpty.setVisibility(View.GONE);
                    }
                }

                return true;
            }
        });
    }

    private void doSelected(String property, int propertyId) {
        if(property == null || property.isEmpty()) {
            return;
        }
        commonFun.hideSoftKeyboard(getApplicationContext(), mListView);

        String history = property + "=" + propertyId;
        mHistory.addHistory(history);

        startHouseListActivity(property, propertyId);

        finish();
    }

    private int getPropertyIdFromList(String property) {
        int itemCount = mPropertyList.size();
        if(itemCount != 0) {
            for(IApiResults.IPropertyInfo info : mPropertyList) {
                if(info.GetName().equals(property)) {
                    return info.GetId();
                }
            }
        }

        return -1;
    }

    private void makePropertyList(ArrayList<Object> array) {
        for(Object obj : array) {
            IApiResults.IPropertyInfo info = (IApiResults.IPropertyInfo)obj;
            mPropertyList.add(info);
            Log.i(TAG, "Property ++ " + info.GetName());
        }
    }

    private void updateAdapterProperty() {
        ArrayList<ClassDefine.Property> newList = new ArrayList<>();
        for(IApiResults.IPropertyInfo info : mPropertyList) {
            ClassDefine.Property property = new ClassDefine.Property();
            property.mName = info.GetName();
            property.mAddress = info.GetAddress();
            newList.add(property);
        }

        mAdapter.setDataList(newList);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void updatePropertyList(final String keywords) {
        mListView.removeAllViewsInLayout();
        mPropertyList.clear();

        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int i, IApiResults.ICommon iCommon) {
                if(i == CommunicationInterface.CmdID.CMD_GET_PROPERTY_LIST) {
                    if(iCommon.GetErrCode() == CE_ERROR_NO_ERROR) {
                        IApiResults.IResultList list = (IApiResults.IResultList) iCommon;
                        mPropertyCount = list.GetTotalNumber();
                        int nFetchCount = list.GetFetchedNumber();
                        if(nFetchCount > 0) {
                            makePropertyList(list.GetList());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateAdapterProperty();
                                }
                            });
                        }
                    }
                }

                Activity_Search.super.onCommandFinished(i, iCommon);
            }
        };

        CommandManager manager = CommandManager.getCmdMgrInstance(this, listener, this);
        manager.GetPropertyListByName(keywords, 0, 0x7FFFFFF);
    }

    protected void onResume() {
        super.onResume();

        SearchView searchView = (SearchView)findViewById(R.id.search_view);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(false);

    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.imageViewBack: {
                finish();
                break;
            }
            case R.id.buttonCleanHistory: {
                mHistory.cleanHistory();
                initHistory();
                break;
            }
        }
    }

    class TextItem {
        public int mTextViewWidth;
        public TextView mView;
    }

    private TextItem createTextItem(String text) {
        int paddingLeft, paddingRight, paddingTop, paddingBottom;
        int marginLeft, marginRight, marginTop, marginBottom;
        paddingLeft = paddingRight = 25;
        paddingTop = paddingBottom = 18;
        marginLeft = marginRight = marginTop = marginBottom = 11;

        TextItem item = new TextItem();
        TextView textView = new TextView(this);
        textView.setText(text);
        int color = ContextCompat.getColor(this, R.color.colorTextNormal);
        textView.setTextColor(color);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.rounded_rect_background_f4f4f4);
        textView.setBackground(drawable);
        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        textView.setLayoutParams(layoutParams);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView v = (TextView)view;
                String textOnView = v.getText().toString();
                int id = mHistory.getPropertyIdByName(textOnView);
                startHouseListActivity(textOnView, id);
                finish();
            }
        });
        float size = textView.getPaint().measureText(text);
        item.mView = textView;
        item.mTextViewWidth = (int)size + paddingLeft + paddingRight + marginLeft + marginRight;
        return item;
    }

    private void startHouseListActivity(String propertyName, int propertyId) {
        Intent intent = new Intent(this, Activity_Search_Fangyuanliebiao.class);
        intent.putExtra(ClassDefine.IntentExtraKeyValue.KEY_PROPERTY_NAME, propertyName);
        intent.putExtra(ClassDefine.IntentExtraKeyValue.KEY_PROPERTY_ID, propertyId);
        startActivity(intent);
    }

    private void initGuess() {
        mGuess.add("中环时代");
        mGuess.add("中冶昆廷");
        mGuess.add("香溢紫君");
        mGuess.add("印象欧洲");
        mGuess.add("新时代");
        mGuess.add("世茂蝶湖湾");
        mGuess.add("绿地21新城");
        mGuess.add("万科魅力花园MIXTOWN");
        mGuess.add("两岸新天地");
        mGuess.add("中汇爱丁堡");

        ArrayList<String> list = mGuess;
        LinearLayout container = (LinearLayout)findViewById(R.id.layoutGuess);
        while(list.size() != 0) {
            int count = addToContainer(container, list);
            for(int i = 0; i < count; i ++) {
                list.remove(0);
            }
        }
    }

    private int addToContainer(LinearLayout container, ArrayList<String> list) {
        if(list.size() == 0) {
            return 0;
        }

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        int totalWidth = 0;
        int count = 0;
        for(int i = 0; i < list.size(); i ++) {
            TextItem item = createTextItem(list.get(i));
            if((totalWidth + item.mTextViewWidth <= mActScreenWidth) || (i == 0 && item.mTextViewWidth > mActScreenWidth)) {
                layout.addView(item.mView);
                totalWidth += item.mTextViewWidth;
                count += 1;
            } else {
                break;
            }
        }

        container.addView(layout);
        return count;
    }

    private void initHistory() {
        ArrayList<String> list = new ArrayList<>();
        int historyCount = mHistory.getCount();
        for(int i = 0; i < historyCount; i ++) {
            String property = mHistory.getPropertyNameByIndex(i);
            list.add(property);
        }

        LinearLayout container = (LinearLayout)findViewById(R.id.layoutRecent);
        container.removeAllViews();

        while(list.size() != 0) {
            int count = addToContainer(container, list);
            for(int i = 0; i < count; i ++) {
                list.remove(0);
            }
        }
    }

}
