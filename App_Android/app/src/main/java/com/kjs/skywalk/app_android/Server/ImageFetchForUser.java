package com.kjs.skywalk.app_android.Server;

import android.content.Context;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CmdExecRes;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiArgs;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_PIC_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_USER_PIC_LIST;

/**
 * Created by admin on 2017/9/30.
 */

public class ImageFetchForUser implements CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{

    private Context             mContext    = null;
    private UserFetchFinished   mListener   = null;
    private int                 mTimeout    = 10000;
    private boolean             mResultGot  = false;
    private boolean             mFailed     = false;

    ArrayList<ClassDefine.PictureInfo>  mList       = new ArrayList<>();
    ArrayList<CmdExecRes>               mCmdList    = new ArrayList<CmdExecRes>();

    public interface UserFetchFinished {
        void onUserImageFetched(ArrayList<ClassDefine.PictureInfo> list);
    }

    public ImageFetchForUser(Context context, UserFetchFinished listener) {
        mContext    = context;
        mListener   = listener;

        // Register Listener
        CommandManager.getCmdMgrInstance(mContext).Register(this, this);
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public void close() {
        // Unregister Listener
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

    public int fetch(int userId, final int fetchType, int size) {
        mList.clear();

        CmdExecRes result = CommandManager.getCmdMgrInstance(mContext).GetUserPics(userId, fetchType, size);
        if (result.mError != CommunicationError.CE_ERROR_NO_ERROR) {
            kjsLogUtil.e("Fail to send command GetUserPics, err:" + result.mError);
            return -1;
        }

        kjsLogUtil.d(String.format("store command seq: %d", result.mCmdSeq));
        commonFun.StoreCommand(mCmdList, result);
        return 0;
    }

    @Override
    public void onCommandFinished(int command, int cmdSeq, IApiResults.ICommon iResult) {
        if (null == iResult) {
            mListener.onUserImageFetched(mList);
            return;
        }

        // Filter all other command out
        if (null == commonFun.RetrieveCommand(mCmdList, cmdSeq)) {
            return;
        }
        kjsLogUtil.d(String.format("command: %d(%s), seq: %d %s", command, CommunicationInterface.CmdID.GetCmdDesc(command), cmdSeq, iResult.DebugString()));

        if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
            kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrCode());
            mListener.onUserImageFetched(mList);
            return;
        }

        if (command == CMD_GET_USER_PIC_LIST) {
            IApiResults.IResultList     res         = (IApiResults.IResultList)iResult;
            IApiArgs.IArgsGetXPicLst    userPics    = (IApiArgs.IArgsGetXPicLst)iResult.GetArgs();

            ArrayList<Object> list = res.GetList();
            for(Object obj : list) {
                IApiResults.IPicInfo info = (IApiResults.IPicInfo)obj;
                ClassDefine.PictureInfo picInfo = new ClassDefine.PictureInfo();
                picInfo.mId = info.GetId();
                IApiResults.IPicUrls urls = (IApiResults.IPicUrls)obj;
                picInfo.largePicUrl = urls.GetLargePicture();
                picInfo.middlePicUrl= urls.GetMiddlePicture();
                picInfo.smallPicUrl = urls.GetSmallPicture();
                picInfo.mType       = userPics.getSubType(); // fetchType;
                mList.add(picInfo);
                picInfo.print();
            }

            mListener.onUserImageFetched(mList);
        }
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
