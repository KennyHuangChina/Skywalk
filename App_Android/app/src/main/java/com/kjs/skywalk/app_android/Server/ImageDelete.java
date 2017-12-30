package com.kjs.skywalk.app_android.Server;

import android.content.Context;

import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CmdExecRes;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_ADD_PICTURE;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_DEL_PICTURE;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdRes.CMD_RES_NOERROR;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.*;

/**
 * Created by admin on 2017/9/30.
 */

public class ImageDelete implements CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{

    private Context         mContext    = null;
    private DeleteFinished  mListener   = null;
    private int             mTimeout    = 10000;
    private boolean         mResultGot  = false;
    private boolean         mFailed     = false;

    public static final int DELETE_RESULT_OK            = 0,
                            DELETE_RESULT_FAIL          = 1,
                            DELETE_RESULT_INTERRUPT     = 2,
                            DELETE_RESULT_DELETE_START  = 3;

    private ArrayList<CmdExecRes> mCmdList = new ArrayList<CmdExecRes>();

    public interface DeleteFinished {
        void onDeleteStarted();
        void onDeleteProgress(final int current, final int total, int id, int result);
        void onDeleteEnd();
    }

    public ImageDelete(Context context, DeleteFinished listener) {
        mContext    = context;
        mListener   = listener;

        // Register the Listener of Server Agent Library
        CommandManager.getCmdMgrInstance(mContext).Register(this, this);
    }

    public void close() {
        // Unregister the Listener of Server Agent Library
        CommandManager.getCmdMgrInstance(mContext).Unregister(this, this);
    }

    private boolean waitResult(int nTimeoutMs) {
        if (nTimeoutMs < 100)
            return mResultGot;

        int wait_count = 0;
        while (wait_count < nTimeoutMs / 100) {
            if(mFailed) {
                return false;
            }
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

    public int delete(final ArrayList<Integer> imageList) {
        if(imageList.size() == 0) {
            return -1;
        }

        new Thread(){
            public void run(){
                mListener.onDeleteStarted();
                int index = 0;
                for(Integer id: imageList) {
                    mResultGot = false;
                    index ++;

                    CmdExecRes res = CommandManager.getCmdMgrInstance(mContext).DelePicture(id);
                    if (res.mError != CE_ERROR_NO_ERROR) {
                        kjsLogUtil.e("Fail to send command DelePicture, err:" + res.mError);
                    } else {
                        commonFun.StoreCommand(mCmdList, res);
                    }

                    mListener.onDeleteProgress(index, imageList.size(), id, DELETE_RESULT_DELETE_START);
                    if (!waitResult(mTimeout)) {
                        if(mFailed) {
                            kjsLogUtil.e("delete failed, try next.");
                            mListener.onDeleteProgress(index, imageList.size(), id, DELETE_RESULT_FAIL);
                            continue;
                        }

                        kjsLogUtil.e("network is bad.");
                        mListener.onDeleteProgress(index, imageList.size(), id, DELETE_RESULT_INTERRUPT);
                        break;
                    }
                    mListener.onDeleteProgress(index, imageList.size(), id, DELETE_RESULT_OK);
                }
                mListener.onDeleteEnd();
            }
        }.start();

        return 0;
    }

    @Override
    public void onCommandFinished(int command, int cmdSeq, IApiResults.ICommon iResult) {
        kjsLogUtil.d(String.format("command: %d(%s), seq: %d %s", command, CommunicationInterface.CmdID.GetCmdDesc(command), cmdSeq, iResult.DebugString()));
        if (null == iResult) {
            kjsLogUtil.w("result is null");
            return;
        }
        CmdExecRes cmd = commonFun.RetrieveCommand(mCmdList, cmdSeq);
        if (null == cmd) {  // result is not we wanted
            return;
        }
        kjsLogUtil.d("Picked");

        int nErrCode = iResult.GetErrCode();
        if (CMD_RES_NOERROR != nErrCode) {
            kjsLogUtil.e("Command:" + command + " finished with error: " + nErrCode);
            if (command == CMD_DEL_PICTURE) {
                mFailed = true;
                kjsLogUtil.e("Delete picture failed with error: " + iResult.GetErrDesc());
            }
            return;
        }

        if (command == CMD_DEL_PICTURE) {
            mResultGot = true;
        }
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
