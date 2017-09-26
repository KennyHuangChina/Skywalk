package com.kjs.skywalk.app_android.Message;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;

/**
 * Created by admin on 2017/2/8.
 */

class AdapterMessage extends BaseAdapter {

    public static interface AdapterDeliverablesListener {
//        public void onItemChanged(int pos, Deliverable deliverable);
    }

    private Context mContext = null;
    private ArrayList<Object> mList = new ArrayList<>();


    public AdapterMessage(Context context) {
        super();
        mContext = context;
    }

    // IApiResults.ISysMsgInfo
    public void updateList(ArrayList<Object> list) {
        mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        ImageView iv_msg_icon;
        TextView tv_msg_time;
        TextView tv_msg_title;
        TextView tv_msg_result;
        TextView tv_msg_description;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_message, null);

            holder.iv_msg_icon = ((ImageView)convertView.findViewById(R.id.iv_msg_icon));
            holder.tv_msg_time = ((TextView)convertView.findViewById(R.id.tv_msg_time));
            holder.tv_msg_title = ((TextView)convertView.findViewById(R.id.tv_msg_title));
            holder.tv_msg_result = ((TextView)convertView.findViewById(R.id.tv_msg_result));
            holder.tv_msg_description = ((TextView)convertView.findViewById(R.id.tv_msg_description));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_msg_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position % 2 == 0)
                    mContext.startActivity(new Intent(mContext, Activity_Message_fangyuanshenhe.class));
                else
                    mContext.startActivity(new Intent(mContext, Activity_Message_yuyuekanfang.class));
            }
        });

        // IApiResults.ISysMsgInfo
        IApiResults.ISysMsgInfo msgInfo = (IApiResults.ISysMsgInfo) mList.get(position);
        holder.tv_msg_title.setText(msgInfo.MsgBody());

         return convertView;
    }
}
