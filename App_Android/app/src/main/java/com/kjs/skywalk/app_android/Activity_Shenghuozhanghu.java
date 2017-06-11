package com.kjs.skywalk.app_android;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Activity_Shenghuozhanghu extends SKBaseActivity {
    private AlertDialog mInputDialog = null;
    private TextView mCurrent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenghuozhanghu);
    }

    private void showInputDialog(String strTitle) {
        if (mInputDialog == null) {
            mInputDialog = new AlertDialog.Builder(this).create();
        }
        mInputDialog.show();
        mInputDialog.setContentView(R.layout.dialog_input_account);
        mInputDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        TextView title = (TextView)mInputDialog.findViewById(R.id.textViewTitle);
        title.setText(strTitle);
        EditText edit = (EditText)mInputDialog.findViewById(R.id.editText);
        edit.setFocusable(true);
        edit.requestFocus();

        TextView tvBack = (TextView) mInputDialog.findViewById(R.id.textViewCannel);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputDialog.dismiss();
            }
        });

        TextView tvConfirm = (TextView) mInputDialog.findViewById(R.id.textViewOk);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputDialog.dismiss();
                EditText edit = (EditText)mInputDialog.findViewById(R.id.editText);
                mCurrent.setText(edit.getText());
            }
        });
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.textViewWater: {
                showInputDialog("请输入您的水表账户");
                mCurrent = (TextView)v;
                break;
            }
            case R.id.textViewPower: {
                showInputDialog("请输入您的电表账户");
                mCurrent = (TextView)v;
                break;
            }
            case R.id.textViewGas: {
                showInputDialog("请输入您的燃气表账户");
                mCurrent = (TextView)v;
                break;
            }
            case R.id.textViewNetwork: {
                showInputDialog("请输入您的网络宽带账户");
                mCurrent = (TextView)v;
                break;
            }
            case R.id.textViewCable: {
                showInputDialog("请输入您的有线电视账户");
                mCurrent = (TextView)v;
                break;
            }
        }
    }

}
