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
 * Created by Jackie on 2017/3/24.
 */

public class AdapterHouseSearchResult extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<HouseData> mDataList;

    public AdapterHouseSearchResult(Context context) {
        super();
        mContext = context;
    }

    public class HouseData {
        String name;
        String location;
    }

    public void setDataList(ArrayList<HouseData> list) {
        mDataList = list;
    }

    @Override
    public int getCount() {
        return 5;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_housing, null);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
