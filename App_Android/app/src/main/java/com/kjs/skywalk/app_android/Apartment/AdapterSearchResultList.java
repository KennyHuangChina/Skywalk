package com.kjs.skywalk.app_android.Apartment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;

/**
 * Created by admin on 2017/2/8.
 */

class AdapterSearchResultList extends BaseAdapter {
    private Context mContext = null;

    public AdapterSearchResultList(Context context) {
        super();
        mContext = context;
    }

    @Override
    public int getCount() {
        return 8;
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
        TextView tvContentName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_brief_house_info, null);
            ImageView flagView = (ImageView)convertView.findViewById(R.id.imageViewFlag);
            flagView.setVisibility(View.VISIBLE);
            ImageView thumbView = (ImageView)convertView.findViewById(R.id.iv_apartment_thumb);
            thumbView.setImageResource(R.drawable.sample2);

            holder = new ViewHolder();
            holder.tvContentName = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}