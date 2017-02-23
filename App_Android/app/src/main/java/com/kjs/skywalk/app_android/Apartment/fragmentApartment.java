package com.kjs.skywalk.app_android.Apartment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentApartment extends Fragment {
    private String TAG = "FragmentApartment";
    private ListView mListViewSearchResult = null;
    private LinearLayout mSortContainer = null;
    private LinearLayout mLinearLayoutConditionContainer = null;
    private ScrollView mScrollViewSearchResult = null;

    private TextView mTextViewConditionPrice = null;
    private TextView mTextViewConditionHouseType = null;
    private TextView mTextViewConditionFilter = null;
    private RelativeLayout mSeperator = null;

    private PopupWindowSearchConditionFilter mPopSearchConditionFilter = null;
    private PopupWindowSearchConditionPrice mPopSearchConditionPrice = null;
    private PopupWindowSearchConditionHouseType mPopSearchConditionHouseType = null;
    @Nullable

    public void searchConditionFilterItemClicked(View view) {
        view.setSelected(!view.isSelected());
    }

    public void searchConditionHouseTypeItemClicked(View view) {
        mPopSearchConditionHouseType.onItemClicked(view);
    }

    public void searchConditionPriceItemClicked(View view) {
        ViewGroup parent = (ViewGroup)view.getParent();
        for(int i = 0; i < parent.getChildCount(); i ++) {
            View v = parent.getChildAt(i);
            if(v.getId() == view.getId()) {
                v.setSelected(true);
                //mPopSearchConditionPrice.setCurrentSelection(v);
            } else {
                v.setSelected(false);
            }
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPopSearchConditionPrice.dismiss();
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 200);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apartment, container, false);
        SearchView searchView = (SearchView)view.findViewById(R.id.search_view);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextSize(14);
        textView.setHint(R.string.fragment_search_input_hint);
        textView.setGravity(Gravity.BOTTOM);

        mListViewSearchResult = (ListView) view.findViewById(R.id.listViewSearchResult);
        mListViewSearchResult.setFocusable(false);

        AdapterSearchResultList adapter = new AdapterSearchResultList(getActivity());
        mListViewSearchResult.setAdapter(adapter);

        mSortContainer = (LinearLayout)view.findViewById(R.id.linearLayoutSortContainer);
        mTextViewConditionPrice = (TextView)view.findViewById(R.id.textViewSearchConditionPrice);
        mTextViewConditionPrice.setOnClickListener(mClickListenerConditionPrice);
        mScrollViewSearchResult = (ScrollView)view.findViewById(R.id.scrollViewResult);
        mLinearLayoutConditionContainer = (LinearLayout)view.findViewById(R.id.linearLayoutConditionContainer);

        mTextViewConditionHouseType = (TextView)view.findViewById(R.id.textViewSearchConditionHouseType);
        mTextViewConditionHouseType.setOnClickListener(mClickListenerConditionHouseType);

        mTextViewConditionFilter = (TextView)view.findViewById(R.id.textViewSearchConditionFilter);
        mTextViewConditionFilter.setOnClickListener(mClickListenerConditionFilter);

        mSeperator = (RelativeLayout)view.findViewById(R.id.seperator_horizontal);
        return view;
    }

    private View.OnClickListener mClickListenerConditionPrice = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int nSortContainerHeight = mSortContainer.getHeight();
            int nResultHeight = mScrollViewSearchResult.getHeight();
            int nSeperatorHeight = mSeperator.getHeight() * 2;
            if(mPopSearchConditionPrice ==null) {
                mPopSearchConditionPrice = new PopupWindowSearchConditionPrice(getActivity().getBaseContext());
            }
            mPopSearchConditionPrice.setHeight(nSortContainerHeight + nResultHeight + nSeperatorHeight);
            mPopSearchConditionPrice.showAsDropDown(mLinearLayoutConditionContainer);
        }
    };

    private View.OnClickListener mClickListenerConditionHouseType = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int nSortContainerHeight = mSortContainer.getHeight();
            int nResultHeight = mScrollViewSearchResult.getHeight();
            int nSeperatorHeight = mSeperator.getHeight() * 2;
            if(mPopSearchConditionHouseType == null) {
                mPopSearchConditionHouseType = new PopupWindowSearchConditionHouseType(getActivity().getBaseContext());
            }
            mPopSearchConditionHouseType.setHeight(nSortContainerHeight + nResultHeight + nSeperatorHeight);
            mPopSearchConditionHouseType.showAsDropDown(mLinearLayoutConditionContainer);
        }
    };

    private View.OnClickListener mClickListenerConditionFilter = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int nSortContainerHeight = mSortContainer.getHeight();
            int nResultHeight = mScrollViewSearchResult.getHeight();
            int nSeperatorHeight = mSeperator.getHeight() * 2;
            if(mPopSearchConditionFilter == null) {
                mPopSearchConditionFilter = new PopupWindowSearchConditionFilter(getActivity().getBaseContext());
            }
            mPopSearchConditionFilter.setHeight(nSortContainerHeight + nResultHeight + nSeperatorHeight);
            mPopSearchConditionFilter.showAsDropDown(mLinearLayoutConditionContainer);
        }
    };
}
