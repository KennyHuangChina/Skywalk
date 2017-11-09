package com.kjs.skywalk.app_android.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
        LinearLayout ll_time;
        TextView tv_subscribe_date;
        TextView tv_subscribe_am_pm;
        TextView tv_subscribe_time;
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
            holder.ll_time = (LinearLayout) convertView.findViewById(R.id.ll_time);
            holder.tv_subscribe_date = ((TextView)convertView.findViewById(R.id.tv_subscribe_date));
            holder.tv_subscribe_am_pm = ((TextView)convertView.findViewById(R.id.tv_subscribe_am_pm));
            holder.tv_subscribe_time = ((TextView)convertView.findViewById(R.id.tv_subscribe_time));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        IApiResults.IAppointmentAct appointmentAct = (IApiResults.IAppointmentAct) mList.get(position);
        holder.tv_user_name.setText(appointmentAct.Who());
        holder.tv_time.setText(commonFun.dateToStringFormat(appointmentAct.When()));
        holder.et_description.setText(appointmentAct.Comment());

        if (appointmentAct.PeriodBegin() != null && appointmentAct.PeriodEnd() != null){
            holder.ll_time.setVisibility(View.VISIBLE);
            String beginDate = new SimpleDateFormat("yyyy-MM-dd").format(appointmentAct.PeriodBegin());
            String beginTime = new SimpleDateFormat("hh:mm").format(appointmentAct.PeriodBegin());
            String endTime = new SimpleDateFormat("hh:mm").format(appointmentAct.PeriodEnd());
            Calendar cal = Calendar.getInstance();
            cal.setTime(appointmentAct.PeriodBegin());
            int am_pm = cal.get(Calendar.AM_PM);    // 0 -- am 1 -- pm
            String strAmPm = "上午";
            if (am_pm == 1)
                strAmPm = "下午";

            holder.tv_subscribe_date.setText(beginDate);
            holder.tv_subscribe_am_pm.setText(strAmPm);
            holder.tv_subscribe_time.setText(beginTime + " - " + endTime);
        } else {
            holder.ll_time.setVisibility(View.GONE);
            holder.tv_subscribe_date.setText("");
            holder.tv_subscribe_am_pm.setText("");
            holder.tv_subscribe_time.setText("");
        }

        return convertView;
    }
}
