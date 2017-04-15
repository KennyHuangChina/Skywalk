package com.kjs.skywalk.app_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by sailor.zhou on 2017/1/15.
 */

public class listitem_adapter_household_appliance extends BaseAdapter {
    static class ApplianceItem {
        int mIcon;
        String mName;
        String mDesc;
        int mNum;

        public ApplianceItem(int icon, String name, String description, int number) {
            mIcon = icon;
            mName = name;
            mDesc = description;
            mNum = number;
        }
    }

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
            holder.tvContentName = (TextView) view.findViewById(R.id.title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(i == 1) {
            Drawable drawable = commonFun.getDrawableFromLocal(mContext, "/sdcard/skywalk/canju.png");
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tvContentName.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        return view;
    }
}
