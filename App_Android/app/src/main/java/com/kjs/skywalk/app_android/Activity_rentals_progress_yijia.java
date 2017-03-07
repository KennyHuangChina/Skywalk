package com.kjs.skywalk.app_android;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Activity_rentals_progress_yijia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentals_progress_yijia);
    }



    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title:
            {
                finish();
            }
            break;

            case R.id.tv_refuse:
            {
                finish();
            }
            break;

            case R.id.tv_accept:
            {
                showInputPwdDlg();
            }
            break;

            case R.id.tv_reoffer:
            {
                finish();
            }
            break;

            case R.id.tv_transform:
            {
                finish();
            }
            break;

        }
    }

    private AlertDialog mInputPwdDlg;
    private void showInputPwdDlg() {
        if (mInputPwdDlg == null) {
            mInputPwdDlg = new AlertDialog.Builder(this).create();
        }
        mInputPwdDlg.show();
        mInputPwdDlg.setContentView(R.layout.dialog_rentals_progress_password);

        TextView tvBack = (TextView) mInputPwdDlg.findViewById(R.id.tv_cancel);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputPwdDlg.dismiss();
            }
        });

        TextView tvConfirm = (TextView) mInputPwdDlg.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputPwdDlg.dismiss();
            }
        });
    }
}
