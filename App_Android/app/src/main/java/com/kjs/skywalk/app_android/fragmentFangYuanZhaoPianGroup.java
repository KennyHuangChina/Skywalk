package com.kjs.skywalk.app_android;

import android.app.Fragment;
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
    private Group mGroup;

    static class PicList {
        String mDesc;
        int    mDrawable;

        public PicList(String desc, int drawable) {
            mDesc = desc;
            mDrawable = drawable;
        }
    }

    static class Group {
        String mTitle;
        ArrayList<PicList> mList;

        public Group(String title,  ArrayList<PicList> list) {
            mTitle = title;
            mList = list;
        }
    }

    public void setGroup(Group group) {
        mGroup = group;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fangyuanzhaopian_group, container, false);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(mGroup.mTitle);

        ImageView ivPic = (ImageView) view.findViewById(R.id.iv_pic);
        TextView tvPicDes = (TextView) view.findViewById(R.id.tv_pic_desc);
        ivPic.setImageResource(mGroup.mList.get(0).mDrawable);
        tvPicDes.setText("照片说明：" + mGroup.mList.get(0).mDesc);

        ImageView ivPic1 = (ImageView) view.findViewById(R.id.iv_pic1);
        TextView tvPicDes1 = (TextView) view.findViewById(R.id.tv_pic_desc1);
        ivPic1.setImageResource(mGroup.mList.get(1).mDrawable);
        tvPicDes1.setText("照片说明：" + mGroup.mList.get(1).mDesc);

        return view;
    }
}
