package com.kjs.skywalk.app_android;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentFangYuanZhaoPianGroup extends Fragment {
//    private Group mGroup;
    private  ArrayList<PicList> mPicList;
    RelativeLayout mRlPicCheckFlag;
    RelativeLayout mRlPic1CheckFlag;
    CheckBox mCbPicCheckFlag;
    CheckBox mCbPic1CheckFlag;
    boolean mIsSelectMode = false;

    static class PicList {
        String mDesc;
        int    mDrawable;
        boolean mIsChecked;

        public PicList(String desc, int drawable, boolean isChecked) {
            mDesc = desc;
            mDrawable = drawable;
            mIsChecked = isChecked;
        }
    }

//    static class Group {
//        String mTitle;
//        ArrayList<PicList> mList;
//
//        public Group(String title,  ArrayList<PicList> list) {
//            mTitle = title;
//            mList = list;
//        }
//    }

//    public void setGroup(Group group) {
//        mGroup = group;
//    }

    public void setPicList(ArrayList<PicList> picLst) {
        mPicList = picLst;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fangyuanzhaopian_group, container, false);

        ImageView ivPic = (ImageView) view.findViewById(R.id.iv_pic);
        TextView tvPicDes = (TextView) view.findViewById(R.id.tv_pic_desc);
        ivPic.setImageResource(mPicList.get(0).mDrawable);
        tvPicDes.setText("照片说明：" + mPicList.get(0).mDesc);
        ivPic.setOnClickListener(mPicClicked);

        int visibility = View.INVISIBLE;
        if (mIsSelectMode)
            visibility = View.VISIBLE;
        mRlPicCheckFlag = (RelativeLayout) view.findViewById(R.id.rl_pic_checkflag);
        mRlPicCheckFlag.setOnClickListener(mRlPicCheckFlagClicked);
        mRlPicCheckFlag.setVisibility(visibility);
        mCbPicCheckFlag = (CheckBox) view.findViewById(R.id.cb_pic_checkflag);


        // pic1
        ImageView ivPic1 = (ImageView) view.findViewById(R.id.iv_pic1);
        TextView tvPicDes1 = (TextView) view.findViewById(R.id.tv_pic_desc1);
        ivPic1.setImageResource(mPicList.get(1).mDrawable);
        tvPicDes1.setText("照片说明：" + mPicList.get(1).mDesc);
        ivPic1.setOnClickListener(mPicClicked);

        visibility = View.INVISIBLE;
        if (mIsSelectMode)
            visibility = View.VISIBLE;
        mRlPic1CheckFlag = (RelativeLayout) view.findViewById(R.id.rl_pic1_checkflag);
        mRlPic1CheckFlag.setOnClickListener(mRlPicCheckFlagClicked);
        mRlPic1CheckFlag.setVisibility(visibility);
        mCbPic1CheckFlag = (CheckBox) view.findViewById(R.id.cb_pic1_checkflag);

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
                    mPicList.get(0).mIsChecked = bChecked;

                    break;
                }

                case R.id.rl_pic1_checkflag:
                {
                    boolean bChecked = mCbPic1CheckFlag.isChecked();
                    bChecked = !bChecked;
                    mCbPic1CheckFlag.setChecked(bChecked);
                    mPicList.get(1).mIsChecked = bChecked;

                    break;
                }
            }
        }
    };

    View.OnClickListener mPicClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.iv_pic:
                {

                    break;
                }

                case R.id.iv_pic1:
                {


                    break;
                }
            }
        }
    };
}
