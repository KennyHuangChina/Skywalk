package com.kjs.skywalk.app_android.Apartment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.PopupWindowWaitingUnclickable;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.Server.GetHouseListTask;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.kjs.skywalk.app_android.Server.GetHouseListTask.SORT_TYPE_APPOINT;
import static com.kjs.skywalk.app_android.Server.GetHouseListTask.SORT_TYPE_APPOINT_DESC;
import static com.kjs.skywalk.app_android.Server.GetHouseListTask.SORT_TYPE_PUBLIC_TIME;
import static com.kjs.skywalk.app_android.Server.GetHouseListTask.SORT_TYPE_PUBLIC_TIME_DESC;
import static com.kjs.skywalk.app_android.Server.GetHouseListTask.SORT_TYPE_RENTAL;
import static com.kjs.skywalk.app_android.Server.GetHouseListTask.SORT_TYPE_RENTAL_DESC;
import static com.kjs.skywalk.app_android.commonFun.getStatusBarHeight;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentApartment extends Fragment implements AbsListView.OnScrollListener {
    private String TAG = "FragmentApartment";
    private ListView mListViewSearchResult = null;
    private LinearLayout mSortContainer = null;
    private LinearLayout mLinearLayoutConditionContainer = null;
    private ListView mScrollViewSearchResult = null;

    private int mActScreenWidth = 1920;
    private int mActScreenHeight = 1080;

    private TextView mTextViewConditionPrice = null;
    private TextView mTextViewConditionHouseType = null;
    private TextView mTextViewConditionFilter = null;
    private RelativeLayout mSeperator = null;

    private TextView mTextViewSortByDate = null;
    private TextView mTextViewSortByRental = null;
    private TextView mTextViewSortByAppointment = null;
    private LinearLayout mLayoutWaiting = null;
    private RelativeLayout mRoot = null;
    private PopupWindowWaitingUnclickable mWaitingWindow = null;

    private PopupWindowSearchConditionFilter mPopSearchConditionFilter = null;
    private PopupWindowSearchConditionPrice mPopSearchConditionPrice = null;
    private PopupWindowSearchConditionHouseType mPopSearchConditionHouseType = null;

    private ImageView mDisplayType = null;
    private AdapterSearchResultList mAdapter = null;

    private static final int DISPLAY_GRID = 0;
    private static final int DISPLAY_LIST= 1;
    private int mDisplay = DISPLAY_LIST;

    private int mLastItemInList = 0;
    private int mTotalCount = 0;

    private int mSortType = SORT_TYPE_PUBLIC_TIME_DESC;
    private int mMinPrice = 0;
    private int mMaxPrice = 0;
    private ArrayList<Integer> mRoomList = new ArrayList<>();

    ArrayList<ClassDefine.HouseDigest> mHouseList = new ArrayList<>();
    @Nullable

    public void searchConditionFilterItemClicked(View view) {
        view.setSelected(!view.isSelected());
    }

    public void searchConditionHouseTypeItemClicked(View view) {
        mPopSearchConditionHouseType.onItemClicked(view);
    }

    public void searchConditionPriceItemClicked(View view) {
        mPopSearchConditionPrice.onItemClicked(view);
    }

    public boolean isBusy() {
        if(mWaitingWindow == null) {
            return false;
        }

        if(mWaitingWindow.isShowing()) {
            return true;
        }

        return false;
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

        Rect rc = commonFun.getScreenResolution(getActivity());
        mActScreenWidth = rc.right;
        mActScreenHeight = rc.bottom;

        mListViewSearchResult = (ListView) view.findViewById(R.id.listViewSearchResult);
        mListViewSearchResult.setFocusable(false);
        mListViewSearchResult.setOnScrollListener(this);

        mAdapter = new AdapterSearchResultList(getActivity());
        mAdapter.setDisplayType(mDisplay);
        mListViewSearchResult.setAdapter(mAdapter);

        mPopSearchConditionPrice = new PopupWindowSearchConditionPrice(getActivity().getBaseContext());
        mPopSearchConditionPrice.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mMinPrice = mPopSearchConditionPrice.mMinPrice;
                mMaxPrice = mPopSearchConditionPrice.mMaxPrice;

                loadMore(true);
            }
        });
        mPopSearchConditionFilter = new PopupWindowSearchConditionFilter(getActivity().getBaseContext());
        mPopSearchConditionFilter.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                loadMore(true);
            }
        });
        mPopSearchConditionHouseType = new PopupWindowSearchConditionHouseType(getActivity().getBaseContext());
        mPopSearchConditionHouseType.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mRoomList = mPopSearchConditionHouseType.getRoomSelection();
                loadMore(true);
            }
        });

        mSortContainer = (LinearLayout)view.findViewById(R.id.linearLayoutSortContainer);
        mTextViewConditionPrice = (TextView)view.findViewById(R.id.textViewSearchConditionPrice);
        mTextViewConditionPrice.setOnClickListener(mClickListenerConditionPrice);
        mScrollViewSearchResult = (ListView)view.findViewById(R.id.listViewSearchResult);
        mLinearLayoutConditionContainer = (LinearLayout)view.findViewById(R.id.linearLayoutConditionContainer);

//        mLayoutWaiting = (LinearLayout)view.findViewById(R.id.waitingWidget);
        mRoot = (RelativeLayout)view.findViewById(R.id.root_container);

        mTextViewConditionHouseType = (TextView)view.findViewById(R.id.textViewSearchConditionHouseType);
        mTextViewConditionHouseType.setOnClickListener(mClickListenerConditionHouseType);

        mTextViewConditionFilter = (TextView)view.findViewById(R.id.textViewSearchConditionFilter);
        mTextViewConditionFilter.setOnClickListener(mClickListenerConditionFilter);

        mSeperator = (RelativeLayout)view.findViewById(R.id.seperator_horizontal);

        mDisplayType = (ImageView)view.findViewById(R.id.imageViewDisplayMode);
        mDisplayType.setOnClickListener(mClickListenerDisplayType);

        mTextViewSortByDate = (TextView)view.findViewById(R.id.textViewSortDate);
        mTextViewSortByDate.setSelected(true);
        mTextViewSortByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSortType == SORT_TYPE_PUBLIC_TIME) {
                    mSortType = SORT_TYPE_PUBLIC_TIME_DESC;
                    commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByDate, R.drawable.sort_desc);
                } else if(mSortType == SORT_TYPE_PUBLIC_TIME_DESC) {
                    mSortType = SORT_TYPE_PUBLIC_TIME;
                    commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByDate, R.drawable.sort_aesc);
                } else {
                    mSortType = SORT_TYPE_PUBLIC_TIME;
                    commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByDate, R.drawable.sort_aesc);
                }

                commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByRental, R.drawable.sort_none);
                commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByAppointment, R.drawable.sort_none);

                mTextViewSortByAppointment.setSelected(false);
                mTextViewSortByDate.setSelected(true);
                mTextViewSortByRental.setSelected(false);

                loadMore(true);
            }
        });

        mTextViewSortByRental = (TextView)view.findViewById(R.id.textViewSortRental);
        mTextViewSortByRental.setSelected(false);
        mTextViewSortByRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSortType == SORT_TYPE_RENTAL) {
                    mSortType = SORT_TYPE_RENTAL_DESC;
                    commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByRental, R.drawable.sort_desc);
                } else if(mSortType == SORT_TYPE_RENTAL_DESC) {
                    mSortType = SORT_TYPE_RENTAL;
                    commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByRental, R.drawable.sort_aesc);
                } else {
                    mSortType = SORT_TYPE_RENTAL;
                    commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByRental, R.drawable.sort_aesc);
                }

                commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByDate, R.drawable.sort_none);
                commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByAppointment, R.drawable.sort_none);

                mTextViewSortByAppointment.setSelected(false);
                mTextViewSortByDate.setSelected(false);
                mTextViewSortByRental.setSelected(true);

                loadMore(true);
            }
        });

        mTextViewSortByAppointment = (TextView)view.findViewById(R.id.textViewSortAppointment);
        mTextViewSortByAppointment.setSelected(false);
        mTextViewSortByAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSortType == SORT_TYPE_APPOINT) {
                    mSortType = SORT_TYPE_APPOINT_DESC;
                    commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByAppointment, R.drawable.sort_desc);
                } else if(mSortType == SORT_TYPE_APPOINT_DESC) {
                    mSortType = SORT_TYPE_APPOINT;
                    commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByAppointment, R.drawable.sort_aesc);
                } else {
                    mSortType = SORT_TYPE_APPOINT;
                    commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByAppointment, R.drawable.sort_aesc);
                }
                commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByDate, R.drawable.sort_none);
                commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByRental, R.drawable.sort_none);

                mTextViewSortByAppointment.setSelected(true);
                mTextViewSortByDate.setSelected(false);
                mTextViewSortByRental.setSelected(false);

                loadMore(true);
            }
        });

        commonFun.setTextViewDrawableRight(getActivity().getApplicationContext(), mTextViewSortByDate, R.drawable.sort_desc);
        loadMore(true);

        return view;
    }

    private void showWaiting(boolean show) {
        if(mWaitingWindow == null) {
            mWaitingWindow = new PopupWindowWaitingUnclickable(getActivity(), mActScreenWidth, mActScreenHeight);
        }
        if(show) {
            mWaitingWindow.show(mRoot);
        } else {
            mWaitingWindow.hide();
        }
    }

    private void loadMore(boolean clean) {
        if(clean) {
            mAdapter.reset();
            mTotalCount = 0;
        }

        int countInList = mAdapter.getCount();
        if(countInList >= mTotalCount && mTotalCount != 0) {
            return;
        }

        showWaiting(true);

        GetHouseListTask task = new GetHouseListTask(getActivity(), new GetHouseListTask.TaskFinished() {
            @Override
            public void onTaskFinished(final ArrayList<ClassDefine.HouseDigest> houseList, final int totalCount) {
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTotalCount = totalCount;
                            mAdapter.addData(houseList, totalCount);
                            showWaiting(false);
                        }
                    });
                }
            }
        });

        task.addFilterRental(mMinPrice, mMaxPrice);
        task.addFilterBedRoom(mRoomList);
        task.setSort(mSortType);
        task.execute(GetHouseListTask.TYPE_ALL, countInList, 10);
    }

    private View.OnClickListener mClickListenerDisplayType = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mDisplay == DISPLAY_LIST) {
                mDisplay = DISPLAY_GRID;
                mDisplayType.setImageResource(R.drawable.show_list);
            } else if(mDisplay == DISPLAY_GRID) {
                mDisplay = DISPLAY_LIST;
                mDisplayType.setImageResource(R.drawable.show_grid);
            }
            mListViewSearchResult.removeAllViewsInLayout();
            mListViewSearchResult.setAdapter(mAdapter);
            mAdapter.setDisplayType(mDisplay);
            mAdapter.notifyDataSetChanged();
        }
    };

    private View.OnClickListener mClickListenerConditionPrice = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int nSortContainerHeight = mSortContainer.getHeight();
            int nResultHeight = mScrollViewSearchResult.getHeight();
            int nSeperatorHeight = mSeperator.getHeight() * 2;
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
            mPopSearchConditionFilter.setHeight(nSortContainerHeight + nResultHeight + nSeperatorHeight);
            mPopSearchConditionFilter.showAsDropDown(mLinearLayoutConditionContainer);
        }
    };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE && mLastItemInList == mAdapter.getCount()) {
            loadMore(false);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mLastItemInList = firstVisibleItem + visibleItemCount;
    }
}
