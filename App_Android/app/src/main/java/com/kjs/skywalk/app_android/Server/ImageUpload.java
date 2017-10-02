package com.kjs.skywalk.app_android.Server;

import android.content.Context;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_ADD_PICTURE;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_MAJOR_House;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_MAJOR_User;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_SUB_HOUSE_OwnershipCert;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_SUB_USER_IDCard;

/**
 * Created by admin on 2017/9/30.
 */

public class ImageUpload implements CommunicationInterface.CIProgressListener{

    private Context mContext = null;
    private int mUploadType = 0;
    private UploadFinished mListener = null;
    private int mTimeout = 60000;
    private boolean mResultGot = false;
    private boolean mFailed = false;

    public static final int UPLOAD_TYPE_IDCARD = PIC_TYPE_MAJOR_User + PIC_TYPE_SUB_USER_IDCard;
    public static final int UPLOAD_TYPE_OWNER_CERT = PIC_TYPE_MAJOR_House + PIC_TYPE_SUB_HOUSE_OwnershipCert;

    public static final int UPLOAD_RESULT_OK = 0;
    public static final int UPLOAD_RESULT_FAIL = 1;
    public static final int UPLOAD_RESULT_INTERRUPT = 2;
    public static final int UPLOAD_RESULT_UPLOAD_START = 3;

    public interface UploadFinished {
        void onUploadStarted();
        void onUploadProgress(final int current, final int total, String image, int result);
        void onUploadEnd();
    }

    public static class UploadImageInfo{
        public String image = "";
        public int type = 0;
        public int houseId = 0;
        public int userId = 0;
    }

    public ImageUpload(Context context, UploadFinished listener) {
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

    private int getRefId(UploadImageInfo info) {
        if(info.type == UPLOAD_TYPE_IDCARD) {
            return info.userId;
        } else if(info.type == UPLOAD_TYPE_OWNER_CERT) {
            return info.houseId;
        }

        return 0;
    }

    public int upload(final ArrayList<UploadImageInfo> imageList) {
        if(imageList.size() == 0) {
            return -1;
        }

        new Thread(){
            public void run(){
                mListener.onUploadStarted();
                int index = 0;
                for(UploadImageInfo info : imageList) {
                    mResultGot = false;
                    index ++;

                    CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {

                        @Override
                        public void onCommandFinished(int i, IApiResults.ICommon iCommon) {
                            if(i == CMD_ADD_PICTURE) {
                                if(iCommon.GetErrCode() != CE_ERROR_NO_ERROR) {
                                    mFailed = true;
                                    kjsLogUtil.e("Add picture failed with error: " + iCommon.GetErrDesc());
                                } else {
                                    mResultGot = true;
                                }
                            }
                        }
                    };

                    CommandManager manager = CommandManager.getCmdMgrInstance(mContext, listener, ImageUpload.this);
                    int refId = getRefId(info);
                    if(refId > 0) {
                        manager.AddPicture(info.houseId, info.type, refId, "无", info.image);
                    } else {
                        continue;
                    }

                    mListener.onUploadProgress(index, imageList.size(), info.image, UPLOAD_RESULT_UPLOAD_START);

                    if(!waitResult(mTimeout)) {
                        if(mFailed) {
                            kjsLogUtil.e("upload failed, try next.");
                            mListener.onUploadProgress(index, imageList.size(), info.image, UPLOAD_RESULT_FAIL);
                            continue;
                        }

                        kjsLogUtil.e("network is bad.");
                        mListener.onUploadProgress(index, imageList.size(), info.image, UPLOAD_RESULT_INTERRUPT);
                        break;
                    }

                    mListener.onUploadProgress(index, imageList.size(), info.image, UPLOAD_RESULT_OK);
                }

                mListener.onUploadEnd();
            }
        }.start();

        return 0;
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
