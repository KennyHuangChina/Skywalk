package com.kjs.skywalk.app_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.name;

/**
 * Created by Jackie on 2017/3/24.
 */

public class AdapterHouseSearchResult extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<ClassDefine.Property> mDataList = new ArrayList<ClassDefine.Property>();

    public AdapterHouseSearchResult(Context context) {
        super();
        mContext = context;
    }

    public void setDataList(ArrayList<ClassDefine.Property> list) {
        mDataList = list;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder {
        TextView textViewName;
        TextView textViewLocation;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_housing, null);

            holder.textViewName = (TextView)convertView.findViewById(R.id.textViewName);
            holder.textViewLocation = (TextView)convertView.findViewById(R.id.textViewLocation);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        ClassDefine.Property garden = mDataList.get(position);
        holder.textViewName.setText(garden.mName);
        holder.textViewLocation.setText(garden.mAddress);

        return convertView;
    }
}
