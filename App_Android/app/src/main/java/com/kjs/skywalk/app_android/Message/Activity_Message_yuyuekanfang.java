package com.kjs.skywalk.app_android.Message;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.Activity_Search_House;
import com.kjs.skywalk.app_android.Activity_Xuanzedaili;
import com.kjs.skywalk.app_android.Activity_Zushouweituo_Fangyuanxinxi;
import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.SKBaseActivity;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.kjs.skywalk.app_android.commonFun.MAP_AP_BUTTON;
import static com.kjs.skywalk.app_android.commonFun.MAP_HOUSE_CERT_BUTTON;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_APPOINTMENT_INFO;

public class Activity_Message_yuyuekanfang extends SKBaseActivity {
    private int mApId = 0;  // 16

    private TextView mTvButton1;
    private TextView mTvButton2;
    private TextView mTvButton3;
    private AdapterYuYueKanFangHistory mAdapterYuYueKanFang;

    private ArrayList<TextView> mBtns = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__message_yuyuekanfang);

        mTvButton1 = (TextView) findViewById(R.id.tv_button1);
        mTvButton2 = (TextView) findViewById(R.id.tv_button2);
        mTvButton3 = (TextView) findViewById(R.id.tv_button3);

        mBtns.add(mTvButton1);
        mBtns.add(mTvButton2);
        mBtns.add(mTvButton3);

        mTvButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog_appointment_date_modify();
                selectAgent();
            }
        });

        mApId = getIntent().getIntExtra(ClassDefine.IntentExtraKeyValue.KEY_REFID, 0);

        mAdapterYuYueKanFang = new AdapterYuYueKanFangHistory(this);
        ((ListView)findViewById(R.id.lv_history)).setAdapter(mAdapterYuYueKanFang);

        getAppointmentInfo();
    }

    private void selectAgent() {
        startActivityForResult(new Intent(Activity_Message_yuyuekanfang.this, Activity_Xuanzedaili.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0) { //request from Activity_Xuanzedaili
            if(resultCode != 0) {
                if(data != null) {
                    String agentId = data.getStringExtra("agentId");
                    kjsLogUtil.i("selected agent id: " + agentId);
                }
            }
        }
    }


    private AlertDialog mDateModifyDlg;
    private void showDialog_appointment_date_modify() {
        if (mDateModifyDlg == null) {
            mDateModifyDlg = new AlertDialog.Builder(this).create();
        }
        mDateModifyDlg.show();
        mDateModifyDlg.setContentView(R.layout.dialog_appointment_date_modify);

        TextView tvBack = (TextView) mDateModifyDlg.findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateModifyDlg.dismiss();
            }
        });

        TextView tvConfirm = (TextView) mDateModifyDlg.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateModifyDlg.dismiss();
            }
        });
    }

    private void getAppointmentInfo() {
        CommandManager.getCmdMgrInstance(this, new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }
                kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                    return;
                }

                if (command == CMD_GET_APPOINTMENT_INFO) {
//                    Result : IApiResults.IAppointmentInfo, IApiResults.IResultList(IApiResults.IAppointmentAct)

                    updateCertHistInfo((IApiResults.IResultList)iResult);

                    IApiResults.IAppointmentInfo appointmentInfo = (IApiResults.IAppointmentInfo) iResult;
                    updateMessageInfo(appointmentInfo);
                }
            }
        }, this).GetAppointmentInfo(mApId);
    }

    private void updateMessageInfo(final IApiResults.IAppointmentInfo appointmentInfo) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.tv_subscriber_name)).setText(appointmentInfo.Subscriber());
                ((TextView)findViewById(R.id.tv_subscriber_phone)).setText(appointmentInfo.SubscriberPhone());
                ((TextView)findViewById(R.id.tv_receptionist_name)).setText(appointmentInfo.Receptionist());
                ((TextView)findViewById(R.id.tv_receptionist_phone)).setText(appointmentInfo.ReceptionistPhone());

//                kjsLogUtil.i("appointmentInfo.ScheduleBeginTime(): " + appointmentInfo.ScheduleBeginTime().toString());
//                kjsLogUtil.i("appointmentInfo.ScheduleEndTime(): " + appointmentInfo.ScheduleEndTime().toString());
//                kjsLogUtil.i("appointmentInfo.ScheduleDate(): " + appointmentInfo.ScheduleDate().toString());

                String beginDate = new SimpleDateFormat("yyyy-MM-dd").format(appointmentInfo.ScheduleBeginTime());
                String beginTime = new SimpleDateFormat("hh:mm").format(appointmentInfo.ScheduleBeginTime());
                String endTime = new SimpleDateFormat("hh:mm").format(appointmentInfo.ScheduleEndTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(appointmentInfo.ScheduleBeginTime());
                int am_pm = cal.get(Calendar.AM_PM);    // 0 -- am 1 -- pm
                String strAmPm = "上午";
                if (am_pm == 1)
                    strAmPm = "下午";

                kjsLogUtil.i("beginDate: " + beginDate);
                kjsLogUtil.i("beginTime: " + beginTime);
                kjsLogUtil.i("endTime: " + endTime);

                ((TextView)findViewById(R.id.tv_subscribe_date)).setText(beginDate);
                ((TextView)findViewById(R.id.tv_subscribe_am_pm)).setText(strAmPm);
                ((TextView)findViewById(R.id.tv_subscribe_time)).setText(beginTime + " - " + endTime);

                final List<commonFun.TextDefine> location = new ArrayList<commonFun.TextDefine>(
                        Arrays.asList(
                                new commonFun.TextDefine( appointmentInfo.Property(), 39, Color.parseColor("#000000")),
                                new commonFun.TextDefine( appointmentInfo.BuildingNo(), 36,  Color.parseColor("#FF6600")),
                                new commonFun.TextDefine( "栋", 36,  Color.parseColor("#000000")),
                                new commonFun.TextDefine( appointmentInfo.HouseNo(), 36,  Color.parseColor("#FF6600")),
                                new commonFun.TextDefine( "室", 36,  Color.parseColor("#000000"))
                        )
                );
                ((TextView)findViewById(R.id.tv_house_location)).setText(commonFun.getSpannableString(location));

                updateButtonGroup(appointmentInfo.Operations());
            }
        });
    }

    private void updateButtonGroup(final int operations) {
        kjsLogUtil.i("[updateButtonGroup] operations: " + operations);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> buttons = new ArrayList<>();
                if ((int)(operations & 0x1) > 0) {
                    // 取消
                    buttons.add("1");
                }
                if ((int)(operations & 0x2) > 0) {
                    // 改期
                    buttons.add("2");
                }
                if ((int)(operations & 0x4) > 0) {
                    // 确认
                    buttons.add("4");
                }
                if ((int)(operations & 0x8) > 0) {
                    // 完成
                    buttons.add("8");
                }
                if ((int)(operations & 0x10) > 0) {
                    // 指派经纪人
                    buttons.add("16");
                }

                if (buttons.size() == 0) {
                    kjsLogUtil.e("result is null");
                    return;
                }

                int index = 0;
                for (String button : buttons) {
                    String btn_name = MAP_AP_BUTTON.get(button);
                    kjsLogUtil.i("button: " + btn_name);

                    TextView btn = mBtns.get(index);
                    btn.setText(btn_name);
                    btn.setVisibility(View.VISIBLE);

                    index++;
                }
            }
        });
    }

    // IApiResults.IAppointmentAct
    private void updateCertHistInfo(final IApiResults.IResultList list) {
        if (list == null)
            return;

        if (list.GetList().isEmpty())
            return;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapterYuYueKanFang.updateHistoryList(list.GetList());
            }
        });
    }

}
