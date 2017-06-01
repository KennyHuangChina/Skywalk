package com.kjs.skywalk.app_android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Jackie on 2017/3/24.
 */

public class AdapterAgents extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<ClassDefine.Agent> mDataList = new ArrayList<>();
    private boolean mAutoSelect = true;

    public AdapterAgents(Context context) {
        super();
        mContext = context;
    }

    public void setAutoSelect(boolean b) {
        mAutoSelect = b;
    }

    public void setDataList(ArrayList<ClassDefine.Agent> list) {
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
        ImageView mImageViewPhoto;
        TextView mName;
        TextView mIDCard;
        TextView mYears;
        TextView mAttitude;
        TextView mProfessional;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_agent, null);
            ImageView thumbView = (ImageView)convertView.findViewById(R.id.imageViewAgent);
            thumbView.setImageResource(R.drawable.ren);

            holder = new ViewHolder();
            holder.mImageViewPhoto = thumbView;
            holder.mName = (TextView)convertView.findViewById(R.id.textViewAgentName);
            holder.mIDCard = (TextView)convertView.findViewById(R.id.textViewAgentIDCard);
            holder.mYears = (TextView)convertView.findViewById(R.id.textViewAgentConyezigeYear);
            holder.mAttitude = (TextView)convertView.findViewById(R.id.textViewAttitude);
            holder.mProfessional = (TextView)convertView.findViewById(R.id.textViewProfessional);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ClassDefine.Agent agent = mDataList.get(position);
        holder.mName.setText(agent.mName);
        holder.mIDCard.setText("身份证: " + agent.mIDCard);
        holder.mYears.setText("从业资格: " + agent.mYears);
        holder.mAttitude.setText(agent.mAttitude);
        holder.mProfessional.setText(agent.mProfessional);

        if(!mAutoSelect)
        {
            ImageView selectionView = (ImageView)convertView.findViewById(R.id.imageViewSelection);
            selectionView.setVisibility(View.VISIBLE);

            int color = 0xFFebfff6;

            convertView.setBackgroundColor(color);
        }

        return convertView;
    }
}
