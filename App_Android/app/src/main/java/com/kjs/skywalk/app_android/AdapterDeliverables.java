package com.kjs.skywalk.app_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by admin on 2017/2/8.
 */

class AdapterDeliverables extends BaseAdapter {
    static class Deliverable {
        int mIcon;
        String mDesc;
        int mNum;

        public Deliverable(int icon, String description, int number) {
            mIcon = icon;
            mDesc = description;
            mNum = number;
        }
    }

    private Context mContext = null;
    private ArrayList<Deliverable> mDeliverableLst;
    public AdapterDeliverables(Context context) {
        super();
        mContext = context;
    }

    public void updateDeliverablesList(ArrayList<Deliverable> list) {
        mDeliverableLst = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDeliverableLst.size();
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
        ImageView ivIcon;
        TextView tvDescription;
        TextView tvPlus;
        TextView tvNumber;
        TextView tvDecrease;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_household_deliverable, null);

            ((ImageView)convertView.findViewById(R.id.iv_icon)).setImageResource(mDeliverableLst.get(position).mIcon);
            ((TextView)convertView.findViewById(R.id.tv_description)).setText(mDeliverableLst.get(position).mDesc);
            ((TextView)convertView.findViewById(R.id.tv_number)).setText("" + mDeliverableLst.get(position).mNum);




//            ImageView flagView = (ImageView)convertView.findViewById(R.id.imageViewFlag);
//            flagView.setVisibility(View.VISIBLE);
//            ImageView thumbView = (ImageView)convertView.findViewById(R.id.iv_apartment_thumb);
//            thumbView.setImageResource(R.drawable.sample2);

            holder = new ViewHolder();
//            holder.tvContentName = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
