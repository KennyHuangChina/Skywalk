package com.kjs.skywalk.app_android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jackie on 2017/3/24.
 */

public class AdapterAgents extends BaseAdapter {
    private Context mContext = null;

    public AdapterAgents(Context context) {
        super();
        mContext = context;
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
        TextView tvContentName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_agent, null);
            ImageView thumbView = (ImageView)convertView.findViewById(R.id.imageViewAgent);
            thumbView.setImageResource(R.drawable.ren);

            holder = new ViewHolder();
            holder.tvContentName = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);

            if(position == 0)
            {
                ImageView selectionView = (ImageView)convertView.findViewById(R.id.imageViewSelection);
                selectionView.setVisibility(View.VISIBLE);

                int color = 0xFFebfff6;

                convertView.setBackgroundColor(color);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
