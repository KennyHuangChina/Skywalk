package com.kjs.skywalk.app_android.Server;

import android.content.Context;
import android.os.AsyncTask;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_BRIEF_PUBLIC_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_DIGEST_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_PUBLISH_TIME;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_PUBLISH_TIME_DESC;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_RENTAL;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_RENTAL_DESC;

/**
 * Created by Jackie on 2017/7/6.
 */

public class GetHouseInfo extends AsyncTask<Integer, Void, Integer> {

    private Context mContext;
    private TaskFinished mTaskFinished = null;
    private int mHouseId = -1;
    private boolean mBackEnd = false;
    private boolean mResultGot = false;

    public GetHouseInfo(Context context, TaskFinished listener) {
        mContext = context;
        mTaskFinished = listener;
    }

    public interface TaskFinished {
        void onTaskFinished(ClassDefine.HouseInfo houseInfo);
    }

    @Override
    protected  void onPostExecute(Integer result) {
        //mTaskFinished.onTaskFinished(mHouseList, result.intValue());
        //???????some times this function does not called....Why
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        int result = 0;
        mHouseId = params[0].intValue();
        if(params[1].intValue() == 0) {
            mBackEnd = false;
        } else {
            mBackEnd = true;
        }

        mResultGot = false;

        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(mContext, mCmdListener, mProgreessListener);
        result = CmdMgr.GetHouseInfo(mHouseId, mBackEnd);
        if(result != CommunicationError.CE_ERROR_NO_ERROR) {
            kjsLogUtil.e("Get House Info failed!");
            return -1;
        }

        if(!waitResult(1000)) {
            kjsLogUtil.i(String.format("[doInBackground] ------ get house info timeout"));
            return -1;
        }

        return 0;
    }

    CommunicationInterface.CICommandListener mCmdListener = new CommunicationInterface.CICommandListener() {
        @Override
        public void onCommandFinished(int command, IApiResults.ICommon iResult) {
            if (null == iResult) {
                kjsLogUtil.w("result is null");
                mResultGot = true;
                return;
            }
            kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
            if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                mResultGot = true;
                mTaskFinished.onTaskFinished(null);
                return;
            }

            if (command == CMD_GET_HOUSE_INFO) {
                IApiResults.IGetHouseInfo info = (IApiResults.IGetHouseInfo) iResult;
                ClassDefine.HouseInfo houseInfo = new ClassDefine.HouseInfo();
                mTaskFinished.onTaskFinished(houseInfo);
            }

            mResultGot = true;
        }
    };

    CommunicationInterface.CIProgressListener mProgreessListener = new CommunicationInterface.CIProgressListener() {
        @Override
        public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {
        }
    };

    boolean waitResult(int nTimeoutMs) {
        if (nTimeoutMs < 100)
            return mResultGot;

        int wait_count = 0;
        while (wait_count < nTimeoutMs / 100) {
            if (mResultGot)
                return true;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wait_count++;
        }

        return false;
    }
}
