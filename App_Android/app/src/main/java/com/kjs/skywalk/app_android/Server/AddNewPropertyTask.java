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

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;

/**
 * Created by Jackie on 2017/7/6.
 */

public class AddNewPropertyTask extends SKBaseAsyncTask {

    private TaskFinished mTaskFinished = null;
    private int mNewPropertyId = 0;
    private String mPropertyName = "";
    private String mPropertyAddress = "";
    private String mPropertyDescription = "";


    public AddNewPropertyTask(Context context, TaskFinished listener) {
        mContext = context;
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
            kjsLogUtil.i("task failed: " + "Invalid New Property ID: " + mNewPropertyId);
            mTaskFinished.onTaskFinished(0);
        }

        kjsLogUtil.i("task success: " + "New Property ID: " + mNewPropertyId);
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
        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(mContext); //, mCmdListener, mProgressListener);
        CmdExecRes result = CmdMgr.AddProperty(mPropertyName, mPropertyAddress, "");
        if (result.mError != CommunicationError.CE_ERROR_NO_ERROR) {
            return -1;
        }

        if(!waitResult(1000)) {
            return -1;
        }

        return 0;
    }

    CommunicationInterface.CICommandListener mCmdListener = new CommunicationInterface.CICommandListener() {
        @Override
        public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
            AddNewPropertyTask.super.onCommandFinished(command, cmdSeq, iResult);
            if (null == iResult) {
                kjsLogUtil.w("result is null");
                mResultGot = true;
                return;
            }
            kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
            if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                mResultGot = true;
                return;
            }

            if(iResult.GetErrCode() == CE_ERROR_NO_ERROR) {
                IApiResults.IAddRes res = (IApiResults.IAddRes)iResult;
                mNewPropertyId = res.GetId();
                kjsLogUtil.i("New Property ID: " + mNewPropertyId);
            }
            mResultGot = true;
        }
    };
}
