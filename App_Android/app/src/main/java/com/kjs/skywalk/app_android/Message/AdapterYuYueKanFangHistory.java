package com.kjs.skywalk.app_android.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;

/**
 * Created by admin on 2017/2/8.
 */

class AdapterYuYueKanFangHistory extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<Object> mList = new ArrayList<>();

    public AdapterYuYueKanFangHistory(Context context) {
        super();
        mContext = context;
    }

    public void updateHistoryList(ArrayList<Object> list) {
        mList = list;
        notifyDataSetChanged();
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
        ImageView iv_touxiang;
        TextView tv_user_name;
        TextView tv_time;
        EditText et_description;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_yuyuekanfang_history, null);

            holder.iv_touxiang = ((ImageView)convertView.findViewById(R.id.iv_touxiang));
            holder.tv_user_name = ((TextView)convertView.findViewById(R.id.tv_user_name));
            holder.tv_time = ((TextView)convertView.findViewById(R.id.tv_time));
            holder.et_description = ((EditText)convertView.findViewById(R.id.et_description));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        IApiResults.IAppointmentAct appointmentAct = (IApiResults.IAppointmentAct) mList.get(position);
        holder.tv_user_name.setText(appointmentAct.Who());
        holder.tv_time.setText(appointmentAct.When().toString());
        holder.et_description.setText(appointmentAct.Comment());

        return convertView;
    }
}
