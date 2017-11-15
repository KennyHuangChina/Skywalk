package com.kjs.skywalk.app_android.Message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.SKBaseActivity;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;

import static com.kjs.skywalk.app_android.commonFun.MAP_HOUSE_CERT_BUTTON;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_CERTIFY_HIST;

public class Activity_Message_fangyuanshenhe extends SKBaseActivity {
    private int mHouse_id = 0;  // house_id
    private String mHouseLocation;
    private ArrayList<TextView> mBtns = new ArrayList<>();
    private TextView mTvButton1;
    private TextView mTvButton2;
    private TextView mTvButton3;
    private AdapterShenHeXiaoXiHistory mAdapterHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__message_fangyuanshenhe);

        mTvButton1 = (TextView) findViewById(R.id.tv_button1);
        mTvButton2 = (TextView) findViewById(R.id.tv_button2);
        mTvButton3 = (TextView) findViewById(R.id.tv_button3);

        mBtns.add(mTvButton1);
        mBtns.add(mTvButton2);
        mBtns.add(mTvButton3);

        mHouse_id = getIntent().getIntExtra(ClassDefine.IntentExtraKeyValue.KEY_HOUSE_ID, 0);
        mHouseLocation = getIntent().getStringExtra(ClassDefine.IntentExtraKeyValue.KEY_HOUSE_LOCATION);

        kjsLogUtil.i("mHouse_id: " + mHouse_id);
        kjsLogUtil.i("mHouseLocation: " + mHouseLocation);
        getHouseCertification(mHouse_id);

        ((TextView) findViewById(R.id.tv_house_location)).setText(mHouseLocation);

        mAdapterHistory = new AdapterShenHeXiaoXiHistory(this);
        ((ListView)findViewById(R.id.lv_history)).setAdapter(mAdapterHistory);

//        updateButtonGroup(3);

    }

    private void getHouseCertification(int house_id) {
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

                if (command == CMD_GET_HOUSE_CERTIFY_HIST) {
//        *   Result: IApiResults.IResultList(IApiResults.IHouseCertInfo)), IApiResults.IHouseCertHist

                    IApiResults.IHouseCertHist houseCertHist = (IApiResults.IHouseCertHist) iResult;
                    int operations = houseCertHist.Operations();
                    updateButtonGroup(operations);

                    updateCertHistInfo((IApiResults.IResultList)iResult);
                }
            }
        }, this).GetHouseCertHist(house_id);
    }

    private void updateButtonGroup(final int operations) {
        kjsLogUtil.i("[updateButtonGroup] operations: " + operations);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> buttons = new ArrayList<>();
                if ((int)(operations & 0x1) > 0) {
                    // 审核
                    buttons.add("1");
                }
                if ((int)(operations & 0x2) > 0) {
                    // 重新提交
                    buttons.add("2");
                }
                if ((int)(operations & 0x4) > 0) {
                    // 撤销审核
                    buttons.add("4");
                }

                if (buttons.size() == 0) {
                    kjsLogUtil.e("result is null");
                    return;
                }

                int index = 0;
                for (String button : buttons) {
                    String btn_name = MAP_HOUSE_CERT_BUTTON.get(button);
                    kjsLogUtil.i("button: " + btn_name);

                    TextView btn = mBtns.get(index);
                    btn.setText(btn_name);
                    btn.setVisibility(View.VISIBLE);

                    index++;
                }
            }
        });
    }

    // IApiResults.IHouseCertInfo
    private void updateCertHistInfo(final IApiResults.IResultList list) {
        if (list == null)
            return;

        if (list.GetList().isEmpty())
            return;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapterHistory.updateHistoryList(list.GetList());
            }
        });
    }

}