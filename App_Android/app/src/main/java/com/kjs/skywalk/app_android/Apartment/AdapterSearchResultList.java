package com.kjs.skywalk.app_android.Apartment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.kjsLogUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by admin on 2017/2/8.
 */

class AdapterSearchResultList extends BaseAdapter {
    private Context mContext = null;

    private static final int DISPLAY_GRID = 0;
    private static final int DISPLAY_LIST= 1;
    private int mDisplayType = DISPLAY_LIST;

    ArrayList<ClassDefine.HouseDigest> mHouseList = new ArrayList<>();

    private int mTotal = 0;

    public void setDisplayType(int displayType) {
        if(displayType != mDisplayType) {
        }

        mDisplayType = displayType;
    }

    public void addData(ArrayList<ClassDefine.HouseDigest> list, int count) {
        mTotal = count;
        for(ClassDefine.HouseDigest digest : list) {
            mHouseList.add(digest);
            kjsLogUtil.i("Property Name: " + digest.property);
        }

        this.notifyDataSetChanged();
    }

    public AdapterSearchResultList(Context context) {
        super();
        mContext = context;
    }

    @Override
    public int getCount() {
        return mHouseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHouseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView propertyName;
        TextView briefInfo;
        TextView price;
        TextView propertyFee;
        ImageView flag;
        ImageView thumb;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            if(mDisplayType == DISPLAY_LIST) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_brief_house_info, null);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_brief_house_info_grid, null);
            }

            holder.flag = (ImageView)convertView.findViewById(R.id.imageViewFlag);
            holder.thumb = (ImageView)convertView.findViewById(R.id.iv_apartment_thumb);
            holder.propertyName = (TextView)convertView.findViewById(R.id.textViewPropertyName);
            holder.briefInfo = (TextView)convertView.findViewById(R.id.textViewBriefInfo);
            holder.price = (TextView)convertView.findViewById(R.id.textViewPrice);
            holder.propertyFee = (TextView)convertView.findViewById(R.id.textViewPropertyFee);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ClassDefine.HouseDigest digest = mHouseList.get(position);
        holder.propertyName.setText(digest.property);
        holder.flag.setVisibility(View.VISIBLE);

        return convertView;
    }
}
