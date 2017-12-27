package com.kjs.skywalk.app_android;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.kjs.skywalk.communicationlibrary.CmdExecRes;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_DELIVERABLE_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_DELIVERABLES;

public class Activity_HouseholdDeliverables extends SKBaseActivity
        implements CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener {
    private AlertDialog mDeliverableEdtDlg;
    private ListView mLvDeliverables;
    private TextView mTvModifyFinish;
    private Boolean mIsModifyMode;
    private ImageButton mIbNew;
    AdapterDeliverables mDeliverablesAdapter;
    GetDeliverablesTask mGetDeliverablesTask;
    // test data


    private final static ArrayList<AdapterDeliverables.Deliverable> mHouseDeliverables = new ArrayList<AdapterDeliverables.Deliverable>(
            Arrays.asList(
                    new AdapterDeliverables.Deliverable(R.drawable.door_n, "大门钥匙", 1),
                    new AdapterDeliverables.Deliverable(R.drawable.card_n, "门禁卡", 2),
                    new AdapterDeliverables.Deliverable(R.drawable.card1_n, "水电卡/存折", 1),
                    new AdapterDeliverables.Deliverable(R.drawable.tv_n, "有线电视用户证", 1),
                    new AdapterDeliverables.Deliverable(R.drawable.water1_n, "水表箱钥匙", 1),
                    new AdapterDeliverables.Deliverable(R.drawable.power3_n, "电表箱钥匙", 3),
                    new AdapterDeliverables.Deliverable(R.drawable.mail_box_n, "信报箱钥匙", 1),
                    new AdapterDeliverables.Deliverable(R.drawable.coffer_n, "保险柜钥匙", 1),
                    new AdapterDeliverables.Deliverable(R.drawable.gas_n, "燃气卡", 3)
            )
    );
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_household_deliverables);

        SpannableString styleText = new SpannableString("世贸蝶湖湾 175栋2202室");
        int i = styleText.length();
        String s = "世贸蝶湖湾 175栋2202室";
        int l = s.length();
        styleText.setSpan(new TextAppearanceSpan(this, R.style.textstyle_large), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styleText.setSpan(new TextAppearanceSpan(this, R.style.textstyle_small), 5, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) findViewById(R.id.tv_apartment_name)).setText(styleText, TextView.BufferType.SPANNABLE);

        mIsModifyMode = false;
        loadUI();

        // add button
        mIbNew = (ImageButton) findViewById(R.id.ib_new);
        mIbNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeliverableNewDlg();
            }
        });


        // get deliverable list
//        mGetDeliverablesTask = new GetDeliverablesTask();
//        mGetDeliverablesTask.execute();

        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this);
        CmdMgr.GetHouseDeliverables(mHouseId);

    }

    // http://blog.csdn.net/gao_chun/article/details/46008651
    private void loadUI() {
        mTvModifyFinish = (TextView) findViewById(R.id.tv_modify_finish);

        mLvDeliverables = (ListView) findViewById(R.id.lv_deliverables);
        mDeliverablesAdapter = new AdapterDeliverables(this);
//        mDeliverablesAdapter.updateDeliverablesList(mHouseDeliverables);
        mLvDeliverables.setAdapter(mDeliverablesAdapter);


//        LinearLayout llDeliverables = (LinearLayout) findViewById(R.id.ll_deliverables);
//
//        int rows = mHouseDeliverables.size();
//        loadItems(llDeliverables, rows);
    }

    private void updateDeliverableList(final ArrayList<AdapterDeliverables.Deliverable> newList) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                kjsLogUtil.i("updateDeliverablesList");
                mDeliverablesAdapter.updateDeliverablesList(newList);
            }
        });
    }

//    private void loadItems(ViewGroup layout, int rows) {
//
//        for (int i = 0; i < rows; i++) {
//            // parent
//            final LinearLayout linearLayout = new LinearLayout(this);
////            View view = View.inflate(this, R.layout.listitem_household_deliverable, linearLayout);
//
//            // attachToRoot = false, return the view itself, otherwise return the root view
//            int index = i;
//            View view = LayoutInflater.from(this).inflate(R.layout.listitem_household_deliverable, linearLayout, false);
//            view.setId(index);
//            view.setOnClickListener(mDeliverableItemClickedListener);
//            ((ImageView)view.findViewById(R.id.iv_icon)).setImageResource(mHouseDeliverables.get(index).mIcon);
//            ((TextView)view.findViewById(R.id.tv_description)).setText(mHouseDeliverables.get(index).mDesc);
//            ((TextView)view.findViewById(R.id.tv_number)).setText("" + mHouseDeliverables.get(index).mNum);
//            linearLayout.addView(view);
//
//            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layout.addView(linearLayout, params);
//
//        }
//    }

//    private void showDeliverableEditDlg(final View vDeliverable) {
//        int itemIndex = vDeliverable.getId();
//        AdapterDeliverables.Deliverable deliverable = mHouseDeliverables.get(itemIndex);
//        int curValue = deliverable.mNum;
//        String name = deliverable.mDesc;
//
//        if (mDeliverableEdtDlg == null) {
//            mDeliverableEdtDlg = new AlertDialog.Builder(this).create();
//        }
//        mDeliverableEdtDlg.show();
//        mDeliverableEdtDlg.setContentView(R.layout.dialog_deliverable_editor);
//
//        TextView tvName = (TextView) mDeliverableEdtDlg.findViewById(R.id.tv_deliverable_name);
//        tvName.setText(name);
//
//        final NumberPicker npDeliverable = (NumberPicker) mDeliverableEdtDlg.findViewById(R.id.np_deliverable);
//        npDeliverable.setMinValue(1);
//        npDeliverable.setMaxValue(10);
//        npDeliverable.setValue(curValue);
//        npDeliverable.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
////                Toast.makeText(Activity_HouseholdDeliverables.this, oldVal + " to " + newVal, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        TextView tvBack = (TextView) mDeliverableEdtDlg.findViewById(R.id.tv_back);
//        tvBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDeliverableEdtDlg.dismiss();
//            }
//        });
//
//        TextView tvConfirm = (TextView) mDeliverableEdtDlg.findViewById(R.id.tv_confirm);
//        tvConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((TextView)vDeliverable.findViewById(R.id.tv_number)).setText("x" + npDeliverable.getValue());
//                mDeliverableEdtDlg.dismiss();
//            }
//        });
//
//    }

    private void showDeliverableNewDlg() {
        int itemIndex = 0;  // for test
        AdapterDeliverables.Deliverable deliverable = mHouseDeliverables.get(itemIndex);
        int curValue = deliverable.mNum;
        String name = deliverable.mDesc;

        if (mDeliverableEdtDlg == null) {
            mDeliverableEdtDlg = new AlertDialog.Builder(this).create();
        }
        mDeliverableEdtDlg.show();
        mDeliverableEdtDlg.setContentView(R.layout.dialog_deliverable_new);

        ListView lvDeliverables = (ListView) mDeliverableEdtDlg.findViewById(R.id.lv_deliverables);
        AdapterDeliverables deliverablesAdapter = new AdapterDeliverables(this);
        deliverablesAdapter.updateDeliverablesList(mHouseDeliverables);
        lvDeliverables.setAdapter(deliverablesAdapter);
        deliverablesAdapter.setNumberDisplay(false);

//        TextView tvName = (TextView) mDeliverableEdtDlg.findViewById(R.id.tv_deliverable_name);
//        tvName.setText(name);
//
//        final NumberPicker npDeliverable = (NumberPicker) mDeliverableEdtDlg.findViewById(R.id.np_deliverable);
//        npDeliverable.setMinValue(1);
//        npDeliverable.setMaxValue(10);
//        npDeliverable.setValue(curValue);
//        npDeliverable.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
////                Toast.makeText(Activity_HouseholdDeliverables.this, oldVal + " to " + newVal, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        TextView tvBack = (TextView) mDeliverableEdtDlg.findViewById(R.id.tv_back);
//        tvBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDeliverableEdtDlg.dismiss();
//            }
//        });
//
//        TextView tvConfirm = (TextView) mDeliverableEdtDlg.findViewById(R.id.tv_confirm);
//        tvConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDeliverableEdtDlg.dismiss();
//            }
//        });

    }

//    private View.OnClickListener mDeliverableItemClickedListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            showDeliverableEditDlg(v);
//        }
//    };

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back: {
                finish();
            }
            break;
            case R.id.tv_modify_finish: {
                mIsModifyMode = !mIsModifyMode;
                if (mIsModifyMode) {
                    mTvModifyFinish.setText("完成");
                } else {
                    mTvModifyFinish.setText("编辑");
                }
                mDeliverablesAdapter.setEditMode(mIsModifyMode);
                mIbNew.setVisibility(mIsModifyMode ? View.GONE : View.VISIBLE);
            }
            break;

        }
    }

    @Override
    public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
        if (null == iResult) {
            kjsLogUtil.w("result is null");
            return;
        }
        kjsLogUtil.i(String.format("[command: %d] --- %s", command, iResult.DebugString()));
        if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
            kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
            return;
        }

        if (command == CMD_GET_HOUSE_DELIVERABLES) {
            IApiResults.IResultList list = (IApiResults.IResultList)iResult;
            ArrayList<AdapterDeliverables.Deliverable> deliverablesList = new ArrayList<AdapterDeliverables.Deliverable>();
            for (Object item : list.GetList()) {
                IApiResults.IDeliverableInfo info = (IApiResults.IDeliverableInfo)item;
                AdapterDeliverables.Deliverable deliverable = new AdapterDeliverables.Deliverable(R.drawable.gas_n, info.GetName(), info.GetQty());
                deliverablesList.add(deliverable);
                kjsLogUtil.i(String.format("[Deliverable:%d] name:%s qty:%d desc:%s", info.GetId(), info.GetName(), info.GetQty(), info.GetDesc()));
            }
            updateDeliverableList(deliverablesList);
        }
    }

    private void showErrorMessage(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                commonFun.showToast_info(Activity_HouseholdDeliverables.this, Activity_HouseholdDeliverables.this.getWindow().getDecorView(), msg);
            }
        });
    }

    public class GetDeliverablesTask extends AsyncTask<Void, Void, Boolean> {
        ArrayList<Object> mIds = null;
        boolean mGotHouseDeliverables = false;
        ArrayList<AdapterDeliverables.Deliverable> mDeliverablesList;
        @Override
        protected Boolean doInBackground(Void... voids) {
            CommandManager CmdMgr = CommandManager.getCmdMgrInstance(Activity_HouseholdDeliverables.this); //, mCmdListener, mProgreessListener);
            CmdExecRes result = CmdMgr.GetHouseDeliverables(mHouseId);
            if (result.mError != CommunicationError.CE_ERROR_NO_ERROR) {
                kjsLogUtil.e("Error to call GetHouseDeliverables");
                return false;
            }

            int wait_count = 0;
            while (wait_count < 10) {
                if (mGotHouseDeliverables)
                    break;

                wait_count++;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }

            if (!mGotHouseDeliverables) {
                kjsLogUtil.e("failed to GetHouseDeliverables");
                return false;
            }

            updateDeliverableList(mDeliverablesList);

//            for (Object id : mIds) {
//                CmdMgr = new CommandManager(Activity_HouseholdDeliverables.this, mCmdListener, mProgreessListener);
//                result = CmdMgr.GetHouseDeliverables((int)id);
//                if (result != CommunicationError.CE_ERROR_NO_ERROR) {
//                    kjsLogUtil.e("Error to call GetHouseDeliverables, id: " + (int)id);
//                    return false;
//                }
//            }

            return true;
        }

        CommunicationInterface.CICommandListener mCmdListener = new CommunicationInterface.CICommandListener() {

            @Override
            public void onCommandFinished(int i, final int cmdSeq, IApiResults.ICommon iCommon) {
                kjsLogUtil.i(String.format("[command: %d] ErrCode:%d(%s)", i, iCommon.GetErrCode(), iCommon.GetErrDesc()));
                if (iCommon.GetErrCode() != CommunicationError.CE_ERROR_NO_ERROR) {
                    showErrorMessage(iCommon.GetErrDesc());
                    return;
                }

                if (i == CMD_GET_HOUSE_DELIVERABLES && iCommon.GetErrCode() == CommunicationError.CE_ERROR_NO_ERROR) {
                    IApiResults.IResultList list = (IApiResults.IResultList)iCommon;
                    mDeliverablesList = new ArrayList<AdapterDeliverables.Deliverable>();
                    for (Object item : list.GetList()) {
                        IApiResults.IDeliverableInfo info = (IApiResults.IDeliverableInfo)item;
                        AdapterDeliverables.Deliverable deliverable = new AdapterDeliverables.Deliverable(R.drawable.gas_n, info.GetName(), info.GetQty());
                        mDeliverablesList.add(deliverable);
                        kjsLogUtil.i(String.format("[Deliverable:%d] name:%s qty:%d desc:%s", info.GetId(), info.GetName(), info.GetQty(), info.GetDesc()));
                    }
                  mGotHouseDeliverables = true;
                }
            }
        };

        CommunicationInterface.CIProgressListener mProgreessListener = new CommunicationInterface.CIProgressListener() {
            @Override
            public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

            }
        };
    }
}
