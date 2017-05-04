package com.kjs.skywalk.app_android;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentFangYuanZhaoPianGroup extends Fragment {
//    private Group mGroup;
    private  ArrayList<PicList> mPicList;
    ImageView mIvPicCheckFlag;
    ImageView mIvPic1CheckFlag;

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
        if (mPicList.get(0).mIsChecked)
            visibility = View.VISIBLE;
        mIvPicCheckFlag = (ImageView) view.findViewById(R.id.iv_pic_checkflag);
        mIvPicCheckFlag.setVisibility(visibility);


        ImageView ivPic1 = (ImageView) view.findViewById(R.id.iv_pic1);
        TextView tvPicDes1 = (TextView) view.findViewById(R.id.tv_pic_desc1);
        ivPic1.setImageResource(mPicList.get(1).mDrawable);
        tvPicDes1.setText("照片说明：" + mPicList.get(1).mDesc);
        ivPic1.setOnClickListener(mPicClicked);

        visibility = View.INVISIBLE;
        if (mPicList.get(1).mIsChecked)
            visibility = View.VISIBLE;
        mIvPic1CheckFlag = (ImageView) view.findViewById(R.id.iv_pic1_checkflag);
        mIvPic1CheckFlag.setVisibility(visibility);

        return view;
    }

    View.OnClickListener mPicClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.iv_pic:
                {
                    int visibility = View.VISIBLE;
                    if (mIvPicCheckFlag.getVisibility() == View.VISIBLE)
                        visibility = View.INVISIBLE;

                    mIvPicCheckFlag.setVisibility(visibility);
                    boolean bChecked = (visibility == View.VISIBLE);
                    mPicList.get(0).mIsChecked = bChecked;

                    break;
                }

                case R.id.iv_pic1:
                {
                    int visibility = View.VISIBLE;
                    if (mIvPic1CheckFlag.getVisibility() == View.VISIBLE)
                        visibility = View.INVISIBLE;

                    mIvPic1CheckFlag.setVisibility(visibility);
                    boolean bChecked = (visibility == View.VISIBLE);
                    mPicList.get(1).mIsChecked = bChecked;

                    break;
                }
            }
        }
    };
}
