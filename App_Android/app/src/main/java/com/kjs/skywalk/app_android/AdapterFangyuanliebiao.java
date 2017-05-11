package com.kjs.skywalk.app_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by admin on 2017/2/8.
 */

class AdapterFangyuanliebiao extends BaseAdapter {
    private Context mContext = null;

    private static final int DISPLAY_GRID = 0;
    private static final int DISPLAY_LIST= 1;
    private int mDisplayType = DISPLAY_LIST;

    public void setDisplayType(int displayType) {
        mDisplayType = displayType;
    }

    public AdapterFangyuanliebiao(Context context) {
        super();
        mContext = context;
    }

    @Override
    public int getCount() {
        return 24;
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
            if(mDisplayType == DISPLAY_LIST) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_brief_house_info, null);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_brief_house_info_grid, null);
            }
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
