package com.kjs.skywalk.app_android.Apartment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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
        TextView tag1;
        TextView tag2;
        TextView tag3;
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
            holder.tag1 = (TextView)convertView.findViewById(R.id.textViewTag1);
            holder.tag2 = (TextView)convertView.findViewById(R.id.textViewTag2);
            holder.tag3 = (TextView)convertView.findViewById(R.id.textViewTag3);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ClassDefine.HouseDigest digest = mHouseList.get(position);

        commonFun.displayImageByURL(mContext, digest.CoverImageUrlS, holder.thumb);

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
                    commonFun.setHouseTagStyleById(holder.tag1, houseTag.tagName, houseTag.tagId);
                }
                if (count == 1) {
                    commonFun.setHouseTagStyleById(holder.tag2, houseTag.tagName, houseTag.tagId);
                }
                if (count == 2) {
                    commonFun.setHouseTagStyleById(holder.tag3, houseTag.tagName, houseTag.tagId);
                }
                count++;
            }
        }

        List<commonFun.TextDefine> list = new ArrayList<>();
        commonFun.TextDefine textDefine1 = new commonFun.TextDefine("￥", 32, Color.parseColor("#ff3d19"));
        list.add(textDefine1);
        commonFun.TextDefine textDefine2 = new commonFun.TextDefine(String.valueOf(digest.Rental), 45, Color.parseColor("#ff3d19"));
        list.add(textDefine2);
        commonFun.TextDefine textDefine3 = new commonFun.TextDefine("（元/月）", 32, Color.parseColor("#242224"));
        list.add(textDefine3);

        holder.price.setText(commonFun.getSpannableString(list));

        list.clear();
        commonFun.TextDefine textDefine4 = new commonFun.TextDefine("物业", 32, Color.parseColor("#242224"));
        list.add(textDefine4);
        commonFun.TextDefine textDefine5 = new commonFun.TextDefine(String.valueOf(200), 45, Color.parseColor("#ff3d19"));
        list.add(textDefine5);
        commonFun.TextDefine textDefine6 = new commonFun.TextDefine("（月）", 32, Color.parseColor("#242224"));
        list.add(textDefine6);
        holder.propertyFee.setText(commonFun.getSpannableString(list));

        holder.flag.setVisibility(View.VISIBLE);

        return convertView;
    }
}
