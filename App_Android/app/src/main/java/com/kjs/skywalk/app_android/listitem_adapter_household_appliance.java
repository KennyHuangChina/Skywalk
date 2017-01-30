package com.kjs.skywalk.app_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by sailor.zhou on 2017/1/15.
 */

public class listitem_adapter_household_appliance extends BaseAdapter {
    private Context mContext;

    public listitem_adapter_household_appliance(Context context) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.listitem_household_appliance, null);

            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }
}
