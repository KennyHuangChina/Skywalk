package com.kjs.skywalk.app_android.Server;

import android.content.Context;
import android.util.Log;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;

import com.kjs.skywalk.communicationlibrary.CmdExecRes;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.*;

/**
 * Created by Jackie on 2017/7/6.
 */

public class AddNewPropertyTask extends SKBaseAsyncTask {

    private TaskFinished    mTaskFinished           = null;
    private int             mNewPropertyId          = 0;
    private String          mPropertyName           = "";
    private String          mPropertyAddress        = "";
    private String          mPropertyDescription    = "";

    public AddNewPropertyTask(Context context, TaskFinished listener) {
        super(context);
        mTaskFinished = listener;
    }

    public interface TaskFinished {
        void onTaskFinished(int propertyId);
    }

    @Override
    protected  void onPostExecute(Integer result) {
        if(result != 0) {
            kjsLogUtil.e("task failed");
            mTaskFinished.onTaskFinished(0);

            return;
        }

        if(mNewPropertyId <= 0) {
            kjsLogUtil.e("task failed: " + "Invalid New Property ID: " + mNewPropertyId);
            mTaskFinished.onTaskFinished(0);
        }

        kjsLogUtil.d("task success: " + "New Property ID: " + mNewPropertyId);
        mTaskFinished.onTaskFinished(mNewPropertyId);
    }

    public void setPropertyAttributes(String name, String address, String description) {
        mPropertyName = name;
        mPropertyAddress = address;
        mPropertyDescription = description;
    }


    @Override
    protected Integer doInBackground(Integer... params) {
//        int result = 0;

        mResultGot = false;
        mNewPropertyId = 0;

        try {
            CmdExecRes result = CommandManager.getCmdMgrInstance(mContext).AddProperty(mPropertyName, mPropertyAddress, "");
            if (result.mError != CommunicationError.CE_ERROR_NO_ERROR) {
                kjsLogUtil.e("Fail to send command AddProperty, error: " + result.mError);
                return -1;
            }
            StoreCommand(result);

            if (!waitResult(1000)) {
                kjsLogUtil.w(String.format("Add Property timeout"));
                return -1;
            }
        } finally {
            mResultGot  = false;
            close();
        }

        return 0;
    }

    @Override
    protected void onCommandSuccess(int command, IApiResults.ICommon result) {
        if (command == CMD_ADD_PROPERTY) {  // AddProperty
            IApiResults.IAddRes res = (IApiResults.IAddRes)result;
            mNewPropertyId = res.GetId();
            kjsLogUtil.i("New Property ID: " + mNewPropertyId);
        }
        mResultGot = true;
    }
}
