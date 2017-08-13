package com.kjs.skywalk.app_android;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/8.
 */

class AdapterForApartmentList extends BaseAdapter {
    private Context mContext = null;

    ArrayList<ClassDefine.HouseDigest> mHouseList = new ArrayList<>();

    private int mTotal = 0;

    public void reset() {
        mHouseList.clear();
        mTotal = 0;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<ClassDefine.HouseDigest> list, int count) {
        mTotal = count;
        for(ClassDefine.HouseDigest digest : list) {
            mHouseList.add(digest);
        }

        this.notifyDataSetChanged();
    }

    public AdapterForApartmentList(Context context) {
        super();
        mContext = context;
    }

    @Override
    public int getCount() {
        //return 8;
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
        TextView tag1;
        TextView tag2;
        TextView tag3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_brief_house_info, null);

            holder.flag = (ImageView)convertView.findViewById(R.id.imageViewFlag);
            holder.thumb = (ImageView)convertView.findViewById(R.id.iv_apartment_thumb);
            holder.propertyName = (TextView)convertView.findViewById(R.id.textViewPropertyName);
            holder.briefInfo = (TextView)convertView.findViewById(R.id.textViewBriefInfo);
            holder.price = (TextView)convertView.findViewById(R.id.textViewPrice);
            holder.propertyFee = (TextView)convertView.findViewById(R.id.textViewPropertyFee);
            holder.tag1 = (TextView)convertView.findViewById(R.id.textViewTag1);
            holder.tag2 = (TextView)convertView.findViewById(R.id.textViewTag2);
            holder.tag3 = (TextView)convertView.findViewById(R.id.textViewTag3);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ClassDefine.HouseDigest digest = mHouseList.get(position);

        //commonFun.displayImageByURL(mContext, digest.CoverImageUrlS, holder.thumb);
        commonFun.displayImageByURL(mContext, digest.CoverImageUrlS, holder.thumb, R.drawable.sample1, R.drawable.sample1);

        holder.propertyName.setText(digest.property);
        int bedRooms = digest.Bedrooms;
        int livingRooms = digest.Livingrooms;
        int bathRooms = digest.Bathrooms;
        String type = commonFun.getHouseTypeString(bedRooms, livingRooms, bathRooms);
        type = type + " " + digest.Acreage + "㎡";
        holder.briefInfo.setText(type);

        if (digest.houseTags != null) {
            int count = 0;
            for (ClassDefine.HouseTag houseTag : digest.houseTags) {
                if (count == 0) {
                    commonFun.setHouseTagStyleByIdWithoutSetting(holder.tag1, houseTag.tagName, houseTag.tagId);
                }
                if (count == 1) {
                    commonFun.setHouseTagStyleByIdWithoutSetting(holder.tag2, houseTag.tagName, houseTag.tagId);
                }
                if (count == 2) {
                    commonFun.setHouseTagStyleByIdWithoutSetting(holder.tag3, houseTag.tagName, houseTag.tagId);
                }
                count++;
            }
        }

        List<commonFun.TextDefine> list = new ArrayList<>();
        commonFun.TextDefine textDefine1 = new commonFun.TextDefine("￥", 32, Color.parseColor("#ff3d19"));
        list.add(textDefine1);
        commonFun.TextDefine textDefine2 = new commonFun.TextDefine(String.valueOf(digest.Rental), 45, Color.parseColor("#ff3d19"));
        list.add(textDefine2);
        commonFun.TextDefine textDefine3 = new commonFun.TextDefine("(元/月)", 32, Color.parseColor("#242224"));
        list.add(textDefine3);

        holder.price.setText(commonFun.getSpannableString(list));

        if(digest.includePropertyFee) {
            holder.propertyFee.setText("包含物业费");
        } else {
            holder.propertyFee.setText("不含物业费");
        }

        holder.flag.setVisibility(View.VISIBLE);

        return convertView;
    }
}
