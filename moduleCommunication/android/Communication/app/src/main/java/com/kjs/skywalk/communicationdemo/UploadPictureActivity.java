package com.kjs.skywalk.communicationdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kjs.skywalk.communicationdemo.FileSelectDialog.DialogConfirmCallback;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.HashMap;

public class UploadPictureActivity extends Activity implements DialogConfirmCallback,
                    CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{

    private String TAG              = getClass().getSimpleName();
    private String mResultString    = "";
    private final int SING_CHOICE_DIALOG = 1;

    private TextView mTextViewResult    = null;
    private EditText mEdtPicType        = null;
    private EditText mEdtPicHouse       = null;
    private EditText mEdtPicXId         = null;
    private EditText mEdtPicDesc        = null;
    private EditText mEdtPicPath        = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);

        mTextViewResult = (TextView)findViewById(R.id.textViewResult);
        mEdtPicType     = (EditText)findViewById(R.id.editPicType);
        mEdtPicHouse    = (EditText)findViewById(R.id.editPicHouse);
        mEdtPicXId      = (EditText)findViewById(R.id.editRefId);
        mEdtPicDesc     = (EditText)findViewById(R.id.editPicDesc);
        mEdtPicPath     = (EditText)findViewById(R.id.editPicPath);

        findViewById(R.id.btnOpenFileSelector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileSelectDialog openfileDlg = FileSelectDialog.newInstance("/sdcard");
                openfileDlg.show(getFragmentManager(), "Openfile dialog");
            }
        });

        findViewById(R.id.btnUploadPic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResultString = "";
                mTextViewResult.setText("");

                CommandManager CmdMgr = CommandManager.getCmdMgrInstance(UploadPictureActivity.this);

                String picType = mEdtPicType.getText().toString();
                int nPos = picType.indexOf(":");
                if (nPos > 0) {
                    picType = picType.substring(0, nPos);
                }
                CmdMgr.AddPicture(Integer.parseInt(mEdtPicHouse.getText().toString()),
                        Integer.parseInt(picType),
                        Integer.parseInt(mEdtPicXId.getText().toString()),
                        mEdtPicDesc.getText().toString(),
                        mEdtPicPath.getText().toString());
            }
        });

        findViewById(R.id.btnSelePicType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(SING_CHOICE_DIALOG);
            }
        });
    }

    @Override
    public void onDialogConfirm(DialogFragment dialog, Bundle arg) {
        if(dialog instanceof FileSelectDialog) {
            String path = arg.getString("file path");
            if(path != null) {
                EditText picPath = (EditText)findViewById(R.id.editPicPath);
                picPath.setText(path);
//                DataContainer.setFileTunerPath(this, path);
//                mTvFileInfo.setText(DataContainer.getFileTunerPath(this));
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch(id) {
            case SING_CHOICE_DIALOG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setIcon(R.drawable.basketball);
                builder.setTitle("体育爱好");
                final ChoiceOnClickListener choiceListener = new ChoiceOnClickListener();
                builder.setSingleChoiceItems(R.array.picType, 0, choiceListener);

                DialogInterface.OnClickListener btnListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                                int choiceWhich = choiceListener.getWhich();
                                String picType = String.format("%s: %s", getResources().getStringArray(R.array.picTypeId)[choiceWhich], getResources().getStringArray(R.array.picType)[choiceWhich]);
                                mEdtPicType.setText(picType);
                            }
                        };
                builder.setPositiveButton("确定", btnListener);
                dialog = builder.create();
                break;
        }
        return dialog;
    }
    @Override
    public void onCommandFinished(int cmdId, int cmdSeq, IApiResults.ICommon result) {
        if (null == result) {
            Log.w(TAG, "result is null");
            return;
        }
        mResultString = String.format("seqe: %d, command: %d\n%s", cmdSeq, cmdId, result.DebugString());

        if (CommunicationError.CE_ERROR_NO_ERROR != result.GetErrCode()) {
            Log.e(TAG, "Command:"+ cmdId + " finished with error: " + result.GetErrDesc());
//            showError(command, returnCode, description);
//            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextViewResult.setText(mResultString);
            }
        });
    }

    @Override
    public void onProgressChanged(int command, String percent, HashMap<String, String> map) {

    }

    private class ChoiceOnClickListener implements DialogInterface.OnClickListener {

        private int which = 0;
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            this.which = which;
        }

        public int getWhich() {
            return which;
        }
    }
}
