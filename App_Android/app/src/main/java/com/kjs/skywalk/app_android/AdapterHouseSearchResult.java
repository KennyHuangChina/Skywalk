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
    private ArrayList<ClassDefine.Garden> mDataList = new ArrayList<ClassDefine.Garden>();

    public AdapterHouseSearchResult(Context context) {
        super();
        mContext = context;
    }

    public void setDataList(ArrayList<ClassDefine.Garden> list) {
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
        TextView tvContentName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_housing, null);
            ClassDefine.Garden garden = mDataList.get(position);
            TextView name = (TextView)convertView.findViewById(R.id.textViewName);
            name.setText(garden.mName);
            TextView address = (TextView)convertView.findViewById(R.id.textViewLocation);
            address.setText(garden.mAddress);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
