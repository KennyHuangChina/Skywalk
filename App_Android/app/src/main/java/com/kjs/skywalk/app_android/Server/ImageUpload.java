package com.kjs.skywalk.app_android.Server;

import android.content.Context;
import android.text.style.UpdateLayout;

import com.kjs.skywalk.app_android.Activity_Weituoqueren;
import com.kjs.skywalk.app_android.ClassDefine;
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
import static com.kjs.skywalk.communicationlibrary.IApiArgs.*;

/**
 * Created by admin on 2017/9/30.
 */

public class ImageUpload implements CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{

    private Context         mContext    = null;
    private int             mUploadType = 0;
    private UploadFinished  mListener   = null;
    private int             mTimeout    = 60000;
    private boolean         mResultGot  = false;
    private boolean         mFailed     = false;

    private UploadResult    mUploadResult = null;

    public static final int UPLOAD_TYPE_IDCARD      = PIC_TYPE_MAJOR_User + PIC_TYPE_SUB_USER_IDCard,
                            UPLOAD_TYPE_OWNER_CERT  = PIC_TYPE_MAJOR_House + PIC_TYPE_SUB_HOUSE_OwnershipCert,
                            UPLOAD_TYPE_HUXING      = PIC_TYPE_MAJOR_House + PIC_TYPE_SUB_HOUSE_FLOOR_PLAN,
                            UPLOAD_TYPE_FANGJIAN    = PIC_TYPE_MAJOR_House + PIC_TYPE_SUB_HOUSE_RealMap,
                            UPLOAD_TYPE_JIAJU       = PIC_TYPE_MAJOR_House + PIC_TYPE_SUB_HOUSE_FURNITURE,
                            UPLOAD_TYPE_DIANQI      = PIC_TYPE_MAJOR_House + PIC_TYPE_SUB_HOUSE_APPLIANCE;

    public static final int UPLOAD_RESULT_OK            = 0,
                            UPLOAD_RESULT_FAIL          = 1,
                            UPLOAD_RESULT_INTERRUPT     = 2,
                            UPLOAD_RESULT_UPLOAD_START  = 3;

    private ArrayList<CmdExecRes> mCmdList = new ArrayList<CmdExecRes>();

    public class UploadResult {
        public int mResult = -1;
        public int mId = 0;
        public String mMD5 = "";
    }

    public interface UploadFinished {
        void onUploadStarted();
        void onUploadProgress(final int current, final int total, String image, UploadResult result);
        void onUploadEnd();
    }

    public static class UploadImageInfo{
        public String image = "";
        public int type = 0;
        public int houseId = 0;
        public int userId = 0;
        public String description = "无";
    }

    public ImageUpload(Context context, UploadFinished listener) {
        mContext = context;
        mListener = listener;

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

    private int getRefId(UploadImageInfo info) {
        if(info.type == UPLOAD_TYPE_IDCARD) {
            return info.userId;
        } else if(info.type == UPLOAD_TYPE_OWNER_CERT) {
            return info.houseId;
        } else if(info.type == UPLOAD_TYPE_HUXING) {
            return info.houseId;
        } else if(info.type == UPLOAD_TYPE_FANGJIAN) {
            return info.houseId;
        } else if(info.type == UPLOAD_TYPE_JIAJU) {
            return info.houseId;
        } else if(info.type == UPLOAD_TYPE_DIANQI) {
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

                    mUploadResult = new UploadResult();

                    int refId = getRefId(info);
                    if (refId > 0) {
                        CmdExecRes res = CommandManager.getCmdMgrInstance(mContext).AddPicture(info.houseId, info.type, refId, info.description, info.image);
                        if (res.mError != CE_ERROR_NO_ERROR) {
                            kjsLogUtil.e("Fail to send command AddPicture, error: " + res.mError);
                        } else {
                            commonFun.StoreCommand(mCmdList, res);
                        }
                    } else {
                        continue;
                    }

                    mUploadResult.mResult = UPLOAD_RESULT_UPLOAD_START;
                    mListener.onUploadProgress(index, imageList.size(), info.image, mUploadResult);

                    if(!waitResult(mTimeout)) {
                        if(mFailed) {
                            kjsLogUtil.e("upload failed, try next.");
                            mUploadResult.mResult = UPLOAD_RESULT_FAIL;
                            mListener.onUploadProgress(index, imageList.size(), info.image, mUploadResult);
                            continue;
                        }

                        kjsLogUtil.e("network is bad.");
                        mUploadResult.mResult = UPLOAD_RESULT_INTERRUPT;
                        mListener.onUploadProgress(index, imageList.size(), info.image, mUploadResult);
                        break;
                    }

                    mUploadResult.mResult = UPLOAD_RESULT_OK;
                    mListener.onUploadProgress(index, imageList.size(), info.image, mUploadResult);
                }

                mListener.onUploadEnd();
            }
        }.start();

        return 0;
    }

    @Override
    public void onCommandFinished(int command, int cmdSeq, IApiResults.ICommon iResult) {
        if (null == iResult) {
            kjsLogUtil.w("result is null");
            return;
        }
        CmdExecRes cmd = commonFun.RetrieveCommand(mCmdList, cmdSeq);
        if (null == cmd) {  // result is not we wanted
            return;
        }
        kjsLogUtil.i(String.format("command: %d(%s), seq: %d %s", command, CommunicationInterface.CmdID.GetCmdDesc(command), cmdSeq, iResult.DebugString()));

        int nErrCode = iResult.GetErrCode();
        if (CommunicationInterface.CmdRes.CMD_RES_NOERROR != nErrCode) {
            kjsLogUtil.e("Command:" + command + " finished with error: " + nErrCode);
            if (command == CMD_ADD_PICTURE) {
                mFailed = true;
                kjsLogUtil.e("Add picture failed with error: " + nErrCode);
            }
            return;
        }

        if (command == CMD_ADD_PICTURE) {
            IApiResults.IAddPic iAddResult = (IApiResults.IAddPic)iResult;
            mUploadResult.mId   = iAddResult.GetId();
            mUploadResult.mMD5  = iAddResult.GetPicChecksum();
            mResultGot = true;
        }
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
