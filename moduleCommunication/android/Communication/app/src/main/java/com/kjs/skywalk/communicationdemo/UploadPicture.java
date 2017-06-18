package com.kjs.skywalk.communicationdemo;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import com.kjs.skywalk.communicationdemo.FileSelectDialog.DialogConfirmCallback;

public class UploadPicture extends Activity implements DialogConfirmCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);

        findViewById(R.id.btnOpenFileSelector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileSelectDialog openfileDlg = FileSelectDialog.newInstance("/sdcard");
                openfileDlg.show(getFragmentManager(), "Openfile dialog");
            }
        });
    }

    @Override
    public void onDialogConfirm(DialogFragment dialog, Bundle arg) {
        if(dialog instanceof FileSelectDialog) {
            String path = arg.getString("file path");
            if(path != null) {
//                DataContainer.setFileTunerPath(this, path);
//                mTvFileInfo.setText(DataContainer.getFileTunerPath(this));
            }
        }
    }
}
