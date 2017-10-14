package com.kjs.skywalk.app_android.Server;

import android.content.Context;

import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_ADD_PICTURE;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_DEL_PICTURE;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_MAJOR_House;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_MAJOR_User;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_SUB_HOUSE_OwnershipCert;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_SUB_USER_IDCard;

/**
 * Created by admin on 2017/9/30.
 */

public class ImageDelete implements CommunicationInterface.CIProgressListener{

    private Context mContext = null;
    private DeleteFinished mListener = null;
    private int mTimeout = 10000;
    private boolean mResultGot = false;
    private boolean mFailed = false;

    public static final int DELETE_RESULT_OK = 0;
    public static final int DELETE_RESULT_FAIL = 1;
    public static final int DELETE_RESULT_INTERRUPT = 2;
    public static final int DELETE_RESULT_DELETE_START = 3;

    public interface DeleteFinished {
        void onDeleteStarted();
        void onDeleteProgress(final int current, final int total, int id, int result);
        void onDeleteEnd();
    }

    public ImageDelete(Context context, DeleteFinished listener) {
        mContext = context;
        mListener = listener;
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

                    CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {

                        @Override
                        public void onCommandFinished(int i, IApiResults.ICommon iCommon) {
                            if(i == CMD_DEL_PICTURE) {
                                if(iCommon.GetErrCode() != CE_ERROR_NO_ERROR) {
                                    mFailed = true;
                                    kjsLogUtil.e("Delete picture failed with error: " + iCommon.GetErrDesc());
                                } else {
                                    mResultGot = true;
                                }
                            }
                        }
                    };

                    CommandManager manager = CommandManager.getCmdMgrInstance(mContext, listener, ImageDelete.this);
                    manager.DelePicture(id);

                    mListener.onDeleteProgress(index, imageList.size(), id, DELETE_RESULT_DELETE_START);

                    if(!waitResult(mTimeout)) {
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
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
