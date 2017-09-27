package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Search_Fangyuanliebiao extends SKBaseActivity {
    private ListView mListView = null;

    private TextView mDisplayType = null;
    private TextView mTextViewSort = null;
    private AdapterFangyuanliebiao mAdapter = null;

    private PopupWindowFangyuanliebiaoSort mPopSort = null;
    private PopupWindowFangyuanliebiaoFilter mPopFilter = null;

    private static final int DISPLAY_GRID = 0;
    private static final int DISPLAY_LIST= 1;
    private int mDisplay = DISPLAY_LIST;

    private TextView mViewPropertyName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangyuanliebiao);

        SearchView searchView = (SearchView)findViewById(R.id.search_view);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextSize(14);
        textView.setGravity(Gravity.BOTTOM);
        searchView.onActionViewCollapsed();

        mListView = (ListView)findViewById(R.id.listViewSearchResult);
        mAdapter = new AdapterFangyuanliebiao(this);
        mAdapter.setDisplayType(mDisplay);
        mListView.setAdapter(mAdapter);

        mDisplayType = (TextView)findViewById(R.id.textViewDisplayMode);
        mDisplayType.setOnClickListener(mClickListenerDisplayType);

        mTextViewSort = (TextView)findViewById(R.id.textViewSort);

        kjsLogUtil.i("property name: " + mPropertyName);
        kjsLogUtil.i("property id: " + mPropertyId);

        mViewPropertyName = (TextView)findViewById(R.id.textViewPropertyName);
    }

    public void onResume() {
        super.onResume();

        mViewPropertyName.setText(mPropertyName);
    }

    public void onClickResponse(View v) {
        //commonFun.showToast_resEntryName(this, v);

        switch (v.getId()) {
            case R.id.textViewAdvancedSearch:{
                startActivity(new Intent(Activity_Search_Fangyuanliebiao.this, Activity_Search.class));
                finish();
                break;
            }
            case R.id.textViewSort: {
                if(mPopSort == null) {
                    mPopSort = new PopupWindowFangyuanliebiaoSort(getBaseContext());
                    cleanSortItemSelection();
                    View v0 = (View)mPopSort.getView().findViewById(R.id.textSmart);
                    selectItem(v0);
                }

                LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayoutCondition);
                mPopSort.showAsDropDown(layout);
                break;
            }
            case R.id.textViewFilter: {
                if(mPopFilter == null) {
                    mPopFilter = new PopupWindowFangyuanliebiaoFilter(getBaseContext());
                    CheckBox button3 = (CheckBox)mPopFilter.getView().findViewById(R.id.radio3);
                    button3.setChecked(true);
                    CheckBox buttonTS5 = (CheckBox)mPopFilter.getView().findViewById(R.id.radioTS5);
                    buttonTS5.setChecked(true);
                }

                LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayoutCondition);
                mPopFilter.showAsDropDown(layout);
                break;
            }
            case R.id.textDistribution:
            case R.id.textHighToLow:
            case R.id.textLowToHigh:
            case R.id.textTotalHighToLow:
            case R.id.textTotalLowToHigh:
            case R.id.textSmart:
            case R.id.textBook:{
                cleanSortItemSelection();
                selectItem(v);
                break;
            }
        }
    }

    private View.OnClickListener mClickListenerDisplayType = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mDisplay == DISPLAY_LIST) {
                mDisplay = DISPLAY_GRID;
                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.show_list);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mDisplayType.setCompoundDrawables(drawable, null, null, null);
                mDisplayType.setText("小图模式");
            } else if(mDisplay == DISPLAY_GRID) {
                mDisplay = DISPLAY_LIST;
                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.show_grid);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mDisplayType.setCompoundDrawables(drawable, null, null, null);
                mDisplayType.setText("大图模式");
            }
            mAdapter.setDisplayType(mDisplay);
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    };

    private void unselectItem(View v) {
        TextView view = (TextView)v;
        view.setCompoundDrawables(null, null, null, null);
        view.setTextColor(Color.rgb(0, 0, 0));
        v.setSelected(false);
    }

    private void selectItem(View v) {
        TextView view = (TextView)v;
        Drawable drawable = ContextCompat.getDrawable(getBaseContext(), R.drawable.select4_check);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorFontSelected));
        view.setSelected(true);
    }

    private void cleanSortItemSelection() {
        View view = mPopSort.getView();
        View v0 = (View)view.findViewById(R.id.textSmart);
        View v1 = (View)view.findViewById(R.id.textDistribution);
        View v2 = (View)view.findViewById(R.id.textHighToLow);
        View v3 = (View)view.findViewById(R.id.textLowToHigh);
        View v4 = (View)view.findViewById(R.id.textTotalHighToLow);
        View v5 = (View)view.findViewById(R.id.textTotalLowToHigh);
        View v6 = (View)view.findViewById(R.id.textBook);

        unselectItem(v0);
        unselectItem(v1);
        unselectItem(v2);
        unselectItem(v3);
        unselectItem(v4);
        unselectItem(v5);
        unselectItem(v6);
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title:
            {
                finish();
            }
            break;
        }
    }
}
