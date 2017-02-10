package com.kjs.skywalk.control;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;

import java.util.ArrayList;

/**
 * Created by sailor.zhou on 2017/2/8.
 */

public class SliderView extends LinearLayout {
    private Context mCtx;
    private ViewPager mVpImages;
    private TextView mTvImageCount;
    private TextView mTvImageName;

    private float mDensity;
    private SliderViewAdapter mSvAdapter;
    private int mImageIndex = 0;

    public SliderView(Context context) {
        super(context);
    }

    public SliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCtx = context;
        mDensity = context.getResources().getDisplayMetrics().density;
        LayoutInflater.from(context).inflate(R.layout.slider_view, this);
        mVpImages = (ViewPager) findViewById(R.id.vp_image);
        mVpImages.setOnPageChangeListener(new SliderViewPageChangeListener());
        mVpImages.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        startSliderTask();
                        break;

                    default:
                        stopSliderTask();
                        break;
                }
                return false;
            }
        });

        mTvImageCount = (TextView) findViewById(R.id.tv_image_count);
        mTvImageName = (TextView) findViewById(R.id.tv_image_name);

    }

    public void setImages(ArrayList<String> imageList, SliderViewListener listener) {
        mSvAdapter = new SliderViewAdapter(mCtx, imageList, listener);
        mVpImages.setAdapter(mSvAdapter);
        startSliderTask();
    }

    private void startSliderTask() {
        stopSliderTask();
        mHandler.postDelayed(mSliderTimeTask, 3000);
    }

    private void stopSliderTask() {
        mHandler.removeCallbacks(mSliderTimeTask);
    }

    private Handler mHandler = new Handler();

    private Runnable mSliderTimeTask = new Runnable() {
        @Override
        public void run() {
            mImageIndex++;
            if(mImageIndex >= mSvAdapter.getCount()) {
                mImageIndex = 0;
            }
            mVpImages.setCurrentItem(mImageIndex);
        }
    };

    private final class SliderViewPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // set indicator
            mTvImageCount.setText(String.format("%d/%d", position + 1, mSvAdapter.getCount()));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                startSliderTask();
            }
        }
    }

    private class SliderViewAdapter extends PagerAdapter {

        private Context mCtx;
        private SliderViewListener mListener;
        private ArrayList<String> mImgList = new ArrayList<>();
        private ArrayList<ImageView> mIvCacheList;

        public SliderViewAdapter(Context context, ArrayList<String> imgList, SliderViewListener listener) {
            super();
            mCtx = context;
            mListener = listener;
            mImgList = imgList;
            mIvCacheList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return mImgList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView imgView = (ImageView)object;
            container.removeView(imgView);
            mIvCacheList.add(imgView);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            String imgUrl = mImgList.get(position);
            ImageView imageView = null;
            if (mIvCacheList.isEmpty()) {
                imageView = new ImageView(mCtx);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                imageView = mIvCacheList.remove(0);
            }

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onImageClick(position, v);
                }
            });

            imageView.setTag(imgUrl);
            container.addView(imageView);
            mListener.onImageDisplay(imgUrl, imageView);

            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public static interface SliderViewListener {
        public void onImageClick(int pos, View view);
        public void onImageDisplay(String imgUrl, ImageView imageView);
    }
}
