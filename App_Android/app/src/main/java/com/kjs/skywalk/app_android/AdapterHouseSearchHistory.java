package com.kjs.skywalk.app_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jackie on 2017/3/24.
 */

public class AdapterHouseSearchHistory extends BaseAdapter {
    private Context mContext = null;

    ArrayList<String> mList = new ArrayList<>();

    public AdapterHouseSearchHistory(Context context) {
        super();
        mContext = context;
    }

    public void setDataList(ArrayList<String> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_search_history, null);
            TextView view = (TextView)convertView.findViewById(R.id.textViewName);
            view.setText(mList.get(position));
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
