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

import java.util.ArrayList;

/**
 * Created by sailor.zhou on 2017/1/15.
 */

public class homepage_apartment_listitem_adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ClassDefine.HouseDigest> mList;

    public homepage_apartment_listitem_adapter(Context context) {
        super();
        mContext = context;
    }

    public void updateList(ArrayList<ClassDefine.HouseDigest> list) {
        mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 3;
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
        RelativeLayout rlApartment2;
        ImageView ivApartment2_thumb;
        ImageView ivApartment2_flag;
        TextView tvApartment2_name;
        TextView tvApartment2_desc;
        LinearLayout llApartment2_tag;
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

            holder.rlApartment2 = (RelativeLayout) view.findViewById(R.id.rl_apartment2);
            holder.ivApartment2_thumb = (ImageView) view.findViewById(R.id.iv_apartment2_thumb);
            holder.ivApartment2_flag = (ImageView) view.findViewById(R.id.iv_apartment2_flag);
            holder.tvApartment2_name = (TextView) view.findViewById(R.id.tv_apartment2_name);
            holder.tvApartment2_desc = (TextView) view.findViewById(R.id.tv_apartment2_desc);
            holder.llApartment2_tag = (LinearLayout) view.findViewById(R.id.ll_apartment2_tag);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }
}
