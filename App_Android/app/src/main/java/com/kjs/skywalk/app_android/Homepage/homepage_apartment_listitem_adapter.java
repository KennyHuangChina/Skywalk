package com.kjs.skywalk.app_android.Homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;

/**
 * Created by sailor.zhou on 2017/1/15.
 */

public class homepage_apartment_listitem_adapter extends BaseAdapter {
    private Context mContext;

    public homepage_apartment_listitem_adapter(Context context) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.homepage_listitem, null);

            holder = new ViewHolder();
            holder.tvContentName = (TextView) view.findViewById(R.id.tv_info_title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }
}
