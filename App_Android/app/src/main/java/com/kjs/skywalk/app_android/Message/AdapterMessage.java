package com.kjs.skywalk.app_android.Message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.communicationlibrary.IApiResults;
import com.kjs.skywalk.control.BadgeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
//    public interface ISysMsgInfo extends IHouseTitleInfo {
//        int     MsgId();        // message id
//        int     MsgType();      // message type. 1 - House Certification. 2 - Planed House Watch
//        int     MsgPriority();  // message priority. 0 - info. 1 - Warning. 2 - Error
//        String  Receiver();     // people who the event send to
//        String  CreateTime();   // exact time when the event created
//        String  ReadTime();     // exact time when the event get readed
//        String  MsgBody();      // message text
//    }
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
        BadgeView bv_msg_new;
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

            holder.bv_msg_new = new BadgeView(mContext, holder.tv_msg_title);
            holder.bv_msg_new.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//            holder.bv_msg_new.setText(Integer.toString(0));
            holder.bv_msg_new.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8.0f);
            holder.bv_msg_new.setBackgroundResource(R.drawable.new_msg_icon);
//            holder.bv_msg_new.show(true);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // IApiResults.ISysMsgInfo
        final IApiResults.ISysMsgInfo msgInfo = (IApiResults.ISysMsgInfo) mList.get(position);
        holder.tv_msg_time.setText(msgInfo.CreateTime());

        int msgType = msgInfo.MsgType();
        if (msgType == 1) {
            // 1 - House Certification
            holder.iv_msg_icon.setImageResource(R.drawable.cert_house_icon);
            holder.tv_msg_title.setText("房源审核");
            holder.tv_msg_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                Intent intent = new Intent(mContext, Activity_Message_fangyuanshenhe.class);
                intent.putExtra(ClassDefine.IntentExtraKeyValue.KEY_REFID, msgInfo.RefId());
                mContext.startActivity(intent);
                }
            });
        } else if (msgType == 2) {
            // 2 - Planed House Watch
            holder.iv_msg_icon.setImageResource(R.drawable.see_house_icon);
            holder.tv_msg_title.setText("预约看房");
            holder.tv_msg_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, Activity_Message_yuyuekanfang.class);
                    intent.putExtra(ClassDefine.IntentExtraKeyValue.KEY_REFID, msgInfo.RefId());
                    mContext.startActivity(intent);
                }
            });
        } else {
            // undefined
        }

        holder.tv_msg_result.setText(msgInfo.MsgBody());

        List<commonFun.TextDefine> houseInfo = new ArrayList<commonFun.TextDefine>(
                Arrays.asList(
                        new commonFun.TextDefine("房源：" + msgInfo.Property(), 39, Color.parseColor("#000000")),
                        new commonFun.TextDefine( msgInfo.BuildingNo(), 36,  Color.parseColor("#FF6600")),
                        new commonFun.TextDefine( "栋", 36,  Color.parseColor("#000000")),
                        new commonFun.TextDefine( msgInfo.HouseNo(), 36,  Color.parseColor("#FF6600")),
                        new commonFun.TextDefine( "室", 36,  Color.parseColor("#000000"))
                       )
        );
        holder.tv_msg_description.setText(commonFun.getSpannableString(houseInfo));

        if(msgInfo.ReadTime().isEmpty()) {
            holder.bv_msg_new.show(true);
        }

        return convertView;
    }
}
