package com.kjs.skywalk.app_android;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Activity_rentals_progress_yijia extends SKBaseActivity {

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
                showRefuseDlg();
            }
            break;

            case R.id.tv_accept:
            {
                showInputPwdDlg();
            }
            break;

            case R.id.tv_reoffer:
            {
                showReOfferDlg();
            }
            break;

            case R.id.tv_transform:
            {
                showTransformDlg();
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

    private AlertDialog mRefuseDlg;
    private void showRefuseDlg() {
        if (mRefuseDlg == null) {
            mRefuseDlg = new AlertDialog.Builder(this).create();
        }
        mRefuseDlg.show();
        mRefuseDlg.setContentView(R.layout.dialog_rentals_progress_refuse);

        TextView tvBack = (TextView) mRefuseDlg.findViewById(R.id.tv_cancel);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefuseDlg.dismiss();
            }
        });

        TextView tvRefuse = (TextView) mRefuseDlg.findViewById(R.id.tv_refuse);
        tvRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefuseDlg.dismiss();
            }
        });
    }

    private AlertDialog mReOfferDlg;
    private void showReOfferDlg() {
        if (mReOfferDlg == null) {
            mReOfferDlg = new AlertDialog.Builder(this).create();
        }
        mReOfferDlg.show();
        mReOfferDlg.setContentView(R.layout.dialog_rentals_progress_reoffer);

        TextView tvBack = (TextView) mReOfferDlg.findViewById(R.id.tv_cancel);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReOfferDlg.dismiss();
            }
        });

        TextView tvConfirm = (TextView) mReOfferDlg.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReOfferDlg.dismiss();
            }
        });
    }

    private AlertDialog mTransformDlg;
    private void showTransformDlg() {
        if (mTransformDlg == null) {
            mTransformDlg = new AlertDialog.Builder(this).create();
        }
        mTransformDlg.show();
        mTransformDlg.setContentView(R.layout.dialog_rentals_progress_transform);

        TextView tvBack = (TextView) mTransformDlg.findViewById(R.id.tv_cancel);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTransformDlg.dismiss();
            }
        });

        TextView tvConfirm = (TextView) mTransformDlg.findViewById(R.id.tv_transform);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTransformDlg.dismiss();
            }
        });
    }
}
