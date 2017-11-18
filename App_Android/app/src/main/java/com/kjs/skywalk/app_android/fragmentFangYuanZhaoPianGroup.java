package com.kjs.skywalk.app_android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPreview;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentFangYuanZhaoPianGroup extends Fragment {
//    private Group mGroup;
    public ArrayList<ClassDefine.PicList> mPicList;
    RelativeLayout mRlPicCheckFlag;
    RelativeLayout mRlPic1CheckFlag;
    CheckBox mCbPicCheckFlag;
    CheckBox mCbPic1CheckFlag;
    boolean mIsSelectMode = false;
    ZhaoPianGroupCallback mCallback = null;
    private Context mContext = null;

    public interface ZhaoPianGroupCallback {
        void onPicSelectChanged();
        void onPicClicked(int hostId, int pos, String picPath);
    }
    public void setZhaoPianGroupCallback(ZhaoPianGroupCallback callback, Context context) {
        this.mCallback = callback;
        mContext = context;
    }

    public void setPicList(ArrayList<ClassDefine.PicList> picLst) {
        mPicList = picLst;
    }

    public static fragmentFangYuanZhaoPianGroup newInstance(int hostId) {
        fragmentFangYuanZhaoPianGroup fragGroup = new fragmentFangYuanZhaoPianGroup();
        Bundle bundle = new Bundle();
        bundle.putInt("host_id", hostId);
        fragGroup.setArguments(bundle);

        return fragGroup;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fangyuanzhaopian_group, container, false);
        if (mPicList.size() == 0) {
            commonFun.showToast_info(getActivity(), view, "found error, mPicList is null");
            return view;
        }

        // pic0
        ImageView ivPic = (ImageView) view.findViewById(R.id.iv_pic);
        TextView tvPicDes = (TextView) view.findViewById(R.id.tv_pic_desc);
        if (!mPicList.get(0).mPath.isEmpty()) {
            if(mPicList.get(0).mIsLocal) {
                ivPic.setImageBitmap(commonFun.getScaleBitmapFromLocal(mPicList.get(0).mPath, 320, 240));
            }
            else {
                commonFun.displayImageByURL(mContext, mPicList.get(0).mPath, ivPic);
            }
        } else {
            ivPic.setImageResource(mPicList.get(0).mDrawable);
        }
        tvPicDes.setText("照片说明：" + mPicList.get(0).mDesc);
        ivPic.setOnClickListener(mPicClicked);

        int visibility = View.INVISIBLE;
        if (mIsSelectMode)
            visibility = View.VISIBLE;
        mRlPicCheckFlag = (RelativeLayout) view.findViewById(R.id.rl_pic_checkflag);
        mRlPicCheckFlag.setOnClickListener(mRlPicCheckFlagClicked);
        mRlPicCheckFlag.setVisibility(visibility);
        mCbPicCheckFlag = (CheckBox) view.findViewById(R.id.cb_pic_checkflag);
        mCbPicCheckFlag.setChecked(mPicList.get(0).mIsChecked);
        mCbPicCheckFlag.setOnCheckedChangeListener(mCheckedChangeListener);

        // pic1
        if (mPicList.size() == 1) {
            LinearLayout llPicItem1 = (LinearLayout) view.findViewById(R.id.ll_picitem1);
            llPicItem1.setVisibility(View.INVISIBLE);
        } else {
            ImageView ivPic1 = (ImageView) view.findViewById(R.id.iv_pic1);
            TextView tvPicDes1 = (TextView) view.findViewById(R.id.tv_pic_desc1);
            if (!mPicList.get(1).mPath.isEmpty()) {
                if(mPicList.get(1).mIsLocal) {
                    ivPic1.setImageBitmap(commonFun.getScaleBitmapFromLocal(mPicList.get(1).mPath, 320, 240));
                }
                else {
                    commonFun.displayImageByURL(mContext, mPicList.get(1).mPath, ivPic1);
                }
            } else {
                ivPic1.setImageResource(mPicList.get(1).mDrawable);
            }
            tvPicDes1.setText("照片说明：" + mPicList.get(1).mDesc);
            ivPic1.setOnClickListener(mPicClicked);

            visibility = View.INVISIBLE;
            if (mIsSelectMode)
                visibility = View.VISIBLE;
            mRlPic1CheckFlag = (RelativeLayout) view.findViewById(R.id.rl_pic1_checkflag);
            mRlPic1CheckFlag.setOnClickListener(mRlPicCheckFlagClicked);
            mRlPic1CheckFlag.setVisibility(visibility);
            mCbPic1CheckFlag = (CheckBox) view.findViewById(R.id.cb_pic1_checkflag);
            mCbPicCheckFlag.setChecked(mPicList.get(1).mIsChecked);
            mCbPic1CheckFlag.setOnCheckedChangeListener(mCheckedChangeListener);
        }

        return view;
    }

    public void updateSelectMode(boolean isSelectMode) {
        mIsSelectMode = isSelectMode;
        int visibility = View.VISIBLE;
        if(mIsSelectMode == false) {
            visibility = View.INVISIBLE;
        }

        if(mRlPicCheckFlag != null)
            mRlPicCheckFlag.setVisibility(visibility);

        if(mRlPic1CheckFlag != null)
            mRlPic1CheckFlag.setVisibility(visibility);

        if (!mIsSelectMode) {
            // clear all check box select status
            if (mCbPicCheckFlag != null)
                mCbPicCheckFlag.setChecked(false);
            if (mCbPic1CheckFlag != null)
                mCbPic1CheckFlag.setChecked(false);

            for (ClassDefine.PicList picList : mPicList) {
                picList.mIsChecked = false;
            }
        }
    }

    public int getSelectCount() {
        if (!mIsSelectMode)
            return 0;

        int count = 0;
        for (ClassDefine.PicList picList : mPicList) {
            if (picList.mIsChecked)
                count++;
        }

        return count;
    }

    public void deleteSelectItem() {
        for (ClassDefine.PicList picList : mPicList) {
            if (picList.mIsChecked)
                mPicList.remove(picList);
        }
    }

    View.OnClickListener mRlPicCheckFlagClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.rl_pic_checkflag:
                {
                    boolean bChecked = mCbPicCheckFlag.isChecked();
                    bChecked = !bChecked;
                    mCbPicCheckFlag.setChecked(bChecked);

                    break;
                }

                case R.id.rl_pic1_checkflag:
                {
                    boolean bChecked = mCbPic1CheckFlag.isChecked();
                    bChecked = !bChecked;
                    mCbPic1CheckFlag.setChecked(bChecked);

                    break;
                }
            }
        }
    };

    View.OnClickListener mPicClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int pos = 0;
            switch (view.getId()) {
                case R.id.iv_pic:
                {
                    pos = 0;
                    break;
                }

                case R.id.iv_pic1:
                {
                    pos = 1;
                    break;
                }
            }

            if (mCallback != null) {
                int hostId = 0;
                if (getArguments() != null)
                    hostId = getArguments().getInt("host_id");
                mCallback.onPicClicked(hostId, pos, mPicList.get(pos).mPath);
            }
        }
    };

    CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.cb_pic_checkflag:
                {
                    mPicList.get(0).mIsChecked = isChecked;
                    break;
                }
                case R.id.cb_pic1_checkflag:
                {
                    mPicList.get(1).mIsChecked = isChecked;
                    break;
                }
            }
            if (mCallback != null)
                mCallback.onPicSelectChanged();
        }
    };
}
