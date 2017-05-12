package com.kjs.skywalk.app_android;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/8.
 */

class AdapterZhaoPianGroup extends BaseAdapter {

//    public static interface AdapterDeliverablesListener {
//        public void onItemChanged(int pos, Deliverable deliverable);
//    }

    private Context mContext = null;
    private ArrayList<ClassDefine.PicGroup> mPicGroupList;
    private boolean mIsEditMode = false;
    private boolean mIsNumberShow = true;

    public AdapterZhaoPianGroup(Context context) {
        super();
        mContext = context;
    }

    public void updateDeliverablesList(ArrayList<ClassDefine.PicGroup> list) {
        mPicGroupList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPicGroupList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        ImageView ivPhotoPicker;
        TextView tvName;
        TextView tvStatus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        ClassDefine.PicGroup picGroup = mPicGroupList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_fangyuanzhaopian_group, null);

            holder.ivPhotoPicker = ((ImageView)convertView.findViewById(R.id.iv_picgroup_photopicker));

            holder.tvName = ((TextView)convertView.findViewById(R.id.tv_picgroup_name));
            holder.tvName.setText(picGroup.mName);

            holder.tvStatus = ((TextView)convertView.findViewById(R.id.tv_picgroup_status));
            holder.tvStatus.setText("(" + mPicGroupList.size() + ")");

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        return convertView;
    }

    public void setEditMode(boolean isEditMode) {
        if (mIsEditMode != isEditMode) {
            notifyDataSetChanged();
        }
        mIsEditMode = isEditMode;
    }

    public void setNumberDisplay(boolean isShow) {
        if (mIsNumberShow != isShow) {
            notifyDataSetChanged();
        }
        mIsNumberShow = isShow;
    }

    class PicFragStatePageAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mLst;
        public PicFragStatePageAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            mLst = list;
        }

        @Override
        public Fragment getItem(int position) {
            return mLst.get(position);
        }

        @Override
        public int getCount() {
            return mLst.size();
        }

        public void updateSelectMode(boolean isSelectMode) {
            for (Fragment fragment : mLst) {
                if (fragment instanceof  fragmentFangYuanZhaoPianGroup) {
                    ((fragmentFangYuanZhaoPianGroup)fragment).updateSelectMode(isSelectMode);
                }
            }

        }
    }

    private void fillPicGroupInfo(ViewPager viewPager, ArrayList<fragmentFangYuanZhaoPianGroup.PicList> picLst) {
        List<Fragment> fragLst = new ArrayList<Fragment>();
        int lstCnt = picLst.size() % 2 == 0 ? picLst.size() / 2 : picLst.size() / 2 + 1;
        for (int i = 0; i <lstCnt; i++) {
            fragmentFangYuanZhaoPianGroup fragment = new fragmentFangYuanZhaoPianGroup();
            ArrayList<fragmentFangYuanZhaoPianGroup.PicList> list = new ArrayList();
            list.add(picLst.get(i * 2));
            list.add(picLst.get(i * 2  + 1));

            fragment.setPicList(list);
            fragLst.add(fragment);
        }
        //viewPager.setAdapter(new Activity_fangyuan_zhaopian.PicFragStatePageAdapter(getSupportFragmentManager(), fragLst));
        viewPager.setCurrentItem(0);
    }
}
