package com.kjs.skywalk.app_android.Homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;

import java.util.ArrayList;

/**
 * Created by sailor.zhou on 2017/1/15.
 */

public class homepage_apartment_listitem_adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ClassDefine.HouseDigest> mList;
    private String mTitle;
    private String mTotalCount;
    private ApartmentListCallback mCallback = null;

    public interface ApartmentListCallback {
        void onItemClicked(ClassDefine.HouseDigest houseDigest);
    }
    public void setApartmentListCallback(ApartmentListCallback callback) {
        this.mCallback = callback;
    }

    public homepage_apartment_listitem_adapter(Context context, String title) {
        super();
        mContext = context;
        mTitle = title;
        mTotalCount = String.format("查看全部 %d 套", 0);
        this.notifyDataSetChanged();
    }

    public void updateList(ArrayList<ClassDefine.HouseDigest> list, int totalCount) {
        mList = list;
        mTotalCount = String.format("查看全部 %d 套", totalCount);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mList != null)
            return 1;

        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    class ViewHolder {
        TextView tvGroupTitle;
        TextView tvGroupCount;
        RelativeLayout rlApartment1;
        ImageView ivApartment1_thumb;
        ImageView ivApartment1_flag;
        TextView tvApartment1_name;
        TextView tvApartment1_desc;
        LinearLayout llApartment1_tag;
        TextView tvApartment1_tag1;
        TextView tvApartment1_tag2;
        RelativeLayout rlApartment2;
        ImageView ivApartment2_thumb;
        ImageView ivApartment2_flag;
        TextView tvApartment2_name;
        TextView tvApartment2_desc;
        LinearLayout llApartment2_tag;
        TextView tvApartment2_tag1;
        TextView tvApartment2_tag2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.homepage_listitem, null);

            holder = new ViewHolder();
            holder.tvGroupTitle = (TextView) view.findViewById(R.id.tv_group_title);
            holder.tvGroupCount = (TextView) view.findViewById(R.id.tv_group_count);

            holder.rlApartment1 = (RelativeLayout) view.findViewById(R.id.rl_apartment1);
            holder.ivApartment1_thumb = (ImageView) view.findViewById(R.id.iv_apartment1_thumb);
            holder.ivApartment1_flag = (ImageView) view.findViewById(R.id.iv_apartment1_flag);
            holder.tvApartment1_name = (TextView) view.findViewById(R.id.tv_apartment1_name);
            holder.tvApartment1_desc = (TextView) view.findViewById(R.id.tv_apartment1_desc);
            holder.llApartment1_tag = (LinearLayout) view.findViewById(R.id.ll_apartment1_tag);
            holder.tvApartment1_tag1 = (TextView) view.findViewById(R.id.tv_apartment1_tag1);
            holder.tvApartment1_tag2 = (TextView) view.findViewById(R.id.tv_apartment1_tag2);

            holder.rlApartment2 = (RelativeLayout) view.findViewById(R.id.rl_apartment2);
            holder.ivApartment2_thumb = (ImageView) view.findViewById(R.id.iv_apartment2_thumb);
            holder.ivApartment2_flag = (ImageView) view.findViewById(R.id.iv_apartment2_flag);
            holder.tvApartment2_name = (TextView) view.findViewById(R.id.tv_apartment2_name);
            holder.tvApartment2_desc = (TextView) view.findViewById(R.id.tv_apartment2_desc);
            holder.llApartment2_tag = (LinearLayout) view.findViewById(R.id.ll_apartment2_tag);
            holder.tvApartment2_tag1 = (TextView) view.findViewById(R.id.tv_apartment2_tag1);
            holder.tvApartment2_tag2 = (TextView) view.findViewById(R.id.tv_apartment2_tag2);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvGroupTitle.setText(mTitle);
        holder.tvGroupCount.setText(mTotalCount);

        if (mList != null && mList.size() == 0) {
            holder.rlApartment1.setVisibility(View.INVISIBLE);
            holder.rlApartment2.setVisibility(View.INVISIBLE);
        } else {
            final ClassDefine.HouseDigest houseDigest1 = mList.get(0);
            holder.tvApartment1_name.setText(houseDigest1.property);
            commonFun.displayImageByURL(mContext, houseDigest1.CoverImageUrlS, holder.ivApartment1_thumb);
            holder.tvApartment1_desc.setText(String.format("%s/%.02f㎡", houseDigest1.addr, houseDigest1.Acreage));
            holder.rlApartment1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null)
                        mCallback.onItemClicked(houseDigest1);
                }
            });

            if (houseDigest1.houseTags != null) {
                int count = 0;
                for (ClassDefine.HouseTag houseTag : houseDigest1.houseTags) {
                    if (count == 0) {
                        commonFun.setHouseTagStyleById(holder.tvApartment1_tag1, houseTag.tagName, houseTag.tagId);
                    }
                    if (count == 1) {
                        commonFun.setHouseTagStyleById(holder.tvApartment1_tag2, houseTag.tagName, houseTag.tagId);
                    }
                    count++;
                }
            }

            if (mList.size() == 1) {
                holder.rlApartment1.setVisibility(View.VISIBLE);
                holder.rlApartment2.setVisibility(View.INVISIBLE);
            } else {
                holder.rlApartment1.setVisibility(View.VISIBLE);
                holder.rlApartment2.setVisibility(View.VISIBLE);

                final ClassDefine.HouseDigest houseDigest2 = mList.get(1);
                holder.rlApartment2.setVisibility(View.VISIBLE);
                holder.tvApartment2_name.setText(houseDigest2.property);
                commonFun.displayImageByURL(mContext, houseDigest2.CoverImageUrlS, holder.ivApartment2_thumb);
                holder.tvApartment2_desc.setText(String.format("%s/%.02f㎡", houseDigest2.addr, houseDigest2.Acreage));
//                holder.tvApartment2_desc.setText(String.format("%s%s/%d㎡", houseDigest2.addr,
//                        commonFun.getHouseTypeString(houseDigest2.Bedrooms, houseDigest2.Livingrooms, houseDigest2.Bathrooms), houseDigest2.Acreage));
                holder.rlApartment2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCallback != null)
                            mCallback.onItemClicked(houseDigest2);
                    }
                });

                if (houseDigest2.houseTags != null) {
                    int count = 0;
                    for (ClassDefine.HouseTag houseTag : houseDigest2.houseTags) {
                        if (count == 0) {
                            commonFun.setHouseTagStyleById(holder.tvApartment2_tag1, houseTag.tagName, houseTag.tagId);
                        }
                        if (count == 1) {
                            commonFun.setHouseTagStyleById(holder.tvApartment2_tag2, houseTag.tagName, houseTag.tagId);
                        }
                        count++;
                    }
                }
            }

        }

        return view;
    }
}
