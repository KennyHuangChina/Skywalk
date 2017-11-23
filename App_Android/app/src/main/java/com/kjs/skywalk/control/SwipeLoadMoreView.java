package com.kjs.skywalk.control;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.kjs.skywalk.app_android.R;

/**
 * Created by sailor.zhou on 2017/11/23.
 */

public class SwipeLoadMoreView extends SwipeRefreshLayout {
    private final View mFooterView;
    private final int mScaledTouchSlop;
    private ListView mListView = null;

    private int mItemCount;
    private boolean mIsLoading;
    private OnLoadMoreListener mListener;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mListener = listener;
    }

    public SwipeLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mFooterView = View.inflate(context, R.layout.view_footer_load_more, null);
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private float mTouchDownY;
    private float mTouchUpY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                tryLoadData();
                break;
            case MotionEvent.ACTION_UP:
                mTouchUpY = ev.getY();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mListView == null) {
            if (getChildCount() > 0) {
                View firstChildView = getChildAt(0);
                if (firstChildView instanceof ListView) {
                    mListView = (ListView) firstChildView;
                    mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            tryLoadData();
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        }
                    });
                }
            }
        }
    }

    private boolean canLoadMore() {
        if (mListView == null || mListView.getAdapter() == null)
            return false;

        if (mIsLoading)
            return false;

        if (mTouchDownY - mTouchUpY < mScaledTouchSlop)     // 不是上拉状态
            return false;

        if (mItemCount > 0) {
            if (mListView.getAdapter().getCount() < mItemCount)
                return false;
        }

        if (mListView.getLastVisiblePosition() != (mListView.getAdapter().getCount() - 1))
            return false;

        return true;
    }

    private void loadData() {
        if (mListener != null) {
            setLoadingStatue(true);
            mListener.onLoadMore();
        }
    }

    private void tryLoadData() {
        if (canLoadMore()) {
            loadData();
        }
    }

    public void setmItemCount(int mItemCount) {
        this.mItemCount = mItemCount;
    }

    public void setLoadingStatue(boolean isLoading) {
        this.mIsLoading = isLoading;
        if (isLoading) {
            mListView.addFooterView(mFooterView);
        } else {
            mListView.removeFooterView(mFooterView);

            mTouchDownY = 0;
            mTouchUpY = 0;
        }
    }
}
