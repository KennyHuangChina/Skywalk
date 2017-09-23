package com.kjs.skywalk.app_android.Message;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kjs.skywalk.app_android.R;

public class Activity_Message_yuyuekanfang extends AppCompatActivity {

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
}
