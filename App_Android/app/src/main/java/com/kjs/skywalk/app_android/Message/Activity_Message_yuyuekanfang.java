package com.kjs.skywalk.app_android.Message;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.SKBaseActivity;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_APPOINTMENT_INFO;

public class Activity_Message_yuyuekanfang extends SKBaseActivity {
    private int mApId = 0;  // 16
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__message_yuyuekanfang);

        findViewById(R.id.tv_change_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog_appointment_date_modify();
            }
        });

        mApId = getIntent().getIntExtra(ClassDefine.IntentExtraKeyValue.KEY_APID, 0);
        getAppointmentInfo();
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
//                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
//                    int nFetch = resultList.GetFetchedNumber();
//                    if (nFetch == -1) {
//                    }
                }
            }
        }, this).GetAppointmentInfo(mApId);
    }

    private void updateButtonGroup(int apId) {
        if ((int)(apId & 0x1) > 0) {
            // 取消
        }
        if ((int)(apId & 0x2) > 0) {
            // 改期
        }
        if ((int)(apId & 0x4) > 0) {
            // 确认
        }
        if ((int)(apId & 0x8) > 0) {
            // 完成
        }
        if ((int)(apId & 0x10) > 0) {
            // 指派经纪人
        }
    }

}
