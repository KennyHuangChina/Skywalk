package com.kjs.skywalk.app_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by sailor.zhou on 2017/1/15.
 */

public class listitem_adapter_household_appliance extends BaseAdapter {
    private ArrayList<ApplianceItem> mApplianceItemLst;

    static class ApplianceItem {
        Drawable mIcon;
        String mName;
        String mDesc;
        int mNum;

        public ApplianceItem(Drawable icon, String name, String description, int number) {
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
//        return 1;
        return mApplianceItemLst.size();
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
        TextView tvApplName;
        TextView tvApplNum;
        TextView tvApplDesc;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.listitem_household_appliance, null);

            holder = new ViewHolder();
            holder.tvApplName = (TextView) view.findViewById(R.id.tv_appl_name);
            holder.tvApplNum = (TextView) view.findViewById(R.id.tv_appl_num);
            holder.tvApplDesc = (TextView) view.findViewById(R.id.tv_appl_desc);



            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ApplianceItem item = mApplianceItemLst.get(i);
        holder.tvApplName.setText(item.mName);
        holder.tvApplNum.setText(String.valueOf(item.mNum));
        holder.tvApplDesc.setText(item.mDesc);

        Drawable drawable = item.mIcon;
        holder.tvApplName.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

        if(i == 0) {
            String testFile = mContext.getCacheDir().getAbsolutePath() + File.separator + "testPics" + File.separator + "sofa_n.png";
            File file = new File(testFile);
            if (file.exists()) {
                Drawable drawable_ex = commonFun.getDrawableFromLocal(mContext, testFile);
                holder.tvApplName.setCompoundDrawablesWithIntrinsicBounds(drawable_ex, null, null, null);
            }
        }

        return view;
    }

    public void updateApplianceItemList(ArrayList<ApplianceItem> list) {
        mApplianceItemLst = list;
        this.notifyDataSetChanged();
    }
}
