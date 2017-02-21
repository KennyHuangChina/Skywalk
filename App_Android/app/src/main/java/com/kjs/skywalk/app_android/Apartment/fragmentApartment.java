package com.kjs.skywalk.app_android.Apartment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;

import org.w3c.dom.Text;

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
    @Nullable

    public void searchConditionFilterItemClicked(View view) {
        view.setSelected(!view.isSelected());
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
            PopupWindowSearchConditionPrice pop = new PopupWindowSearchConditionPrice(getActivity().getBaseContext());
            pop.setHeight(nSortContainerHeight + nResultHeight + nSeperatorHeight);
            pop.showAsDropDown(mLinearLayoutConditionContainer);
        }
    };

    private View.OnClickListener mClickListenerConditionHouseType = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int nSortContainerHeight = mSortContainer.getHeight();
            int nResultHeight = mScrollViewSearchResult.getHeight();
            int nSeperatorHeight = mSeperator.getHeight() * 2;
            PopupWindowSearchConditionHouseType pop = new PopupWindowSearchConditionHouseType(getActivity().getBaseContext());
            pop.setHeight(nSortContainerHeight + nResultHeight + nSeperatorHeight);
            pop.showAsDropDown(mLinearLayoutConditionContainer);
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
