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

    public static interface AdapterDeliverablesListener {
        public void onItemChanged(int pos, Deliverable deliverable);
    }

    private Context mContext = null;
    private ArrayList<Deliverable> mDeliverableLst = new ArrayList<>();
    private boolean mIsEditMode = false;
    private boolean mIsNumberShow = true;

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
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_household_deliverable, null);

            holder.ivIcon = ((ImageView)convertView.findViewById(R.id.iv_icon));
            holder.tvDescription = ((TextView)convertView.findViewById(R.id.tv_description));
            holder.tvNumber = ((TextView)convertView.findViewById(R.id.tv_number));
            holder.tvPlus = ((TextView)convertView.findViewById(R.id.tv_plus));
            holder.tvDecrease = ((TextView)convertView.findViewById(R.id.tv_decrease));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ivIcon.setImageResource(mDeliverableLst.get(position).mIcon);
        holder.tvDescription.setText(mDeliverableLst.get(position).mDesc);
        holder.tvNumber.setText("" + mDeliverableLst.get(position).mNum);

        if (mIsEditMode) {
            holder.tvPlus.setVisibility(View.VISIBLE);
            holder.tvDecrease.setVisibility(View.VISIBLE);
        } else {
            holder.tvPlus.setVisibility(View.INVISIBLE);
            holder.tvDecrease.setVisibility(View.INVISIBLE);
        }

        if (mIsNumberShow) {
            holder.tvNumber.setVisibility(View.VISIBLE);
        } else {
            holder.tvNumber.setVisibility(View.GONE);
        }

        final int pos = position;
        final TextView tvNumber = holder.tvNumber;
        holder.tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDeliverableLst.get(pos).mNum += 1;
                tvNumber.setText("" + mDeliverableLst.get(pos).mNum);
            }
        });

        holder.tvDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDeliverableLst.get(pos).mNum == 0)
                    return;

                mDeliverableLst.get(pos).mNum -= 1;
                tvNumber.setText("" + mDeliverableLst.get(pos).mNum);
            }
        });

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
}
