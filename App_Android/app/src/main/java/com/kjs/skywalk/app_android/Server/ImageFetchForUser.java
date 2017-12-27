package com.kjs.skywalk.app_android.Server;

import android.content.Context;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.communicationlibrary.CmdExecRes;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_PIC_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_USER_PIC_LIST;

/**
 * Created by admin on 2017/9/30.
 */

public class ImageFetchForUser implements CommunicationInterface.CIProgressListener{

    private Context mContext = null;
    private UserFetchFinished mListener = null;
    private int mTimeout = 10000;
    private boolean mResultGot = false;
    private boolean mFailed = false;

    ArrayList<ClassDefine.PictureInfo> mList = new ArrayList<>();

    public interface UserFetchFinished {
        void onUserImageFetched(ArrayList<ClassDefine.PictureInfo> list);
    }

    public ImageFetchForUser(Context context, UserFetchFinished listener) {
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

    public int fetch(int userId, final int fetchType, int size) {
        mList.clear();
        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    mListener.onUserImageFetched(mList);
                    return;
                }

                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    mListener.onUserImageFetched(mList);
                    return;
                }

                if (command == CMD_GET_USER_PIC_LIST) {
                    IApiResults.IResultList res = (IApiResults.IResultList)iResult;
                    ArrayList<Object> list = res.GetList();
                    for(Object obj : list) {
                        IApiResults.IPicInfo info = (IApiResults.IPicInfo)obj;
                        ClassDefine.PictureInfo picInfo = new ClassDefine.PictureInfo();
                        picInfo.mId = info.GetId();
                        IApiResults.IPicUrls urls = (IApiResults.IPicUrls)obj;
                        picInfo.largePicUrl = urls.GetLargePicture();
                        picInfo.middlePicUrl = urls.GetMiddlePicture();
                        picInfo.smallPicUrl = urls.GetSmallPicture();
                        picInfo.mType = fetchType;
                        mList.add(picInfo);
                        picInfo.print();
                    }

                    mListener.onUserImageFetched(mList);
                }
            }
        };

        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(mContext); //, listener, ImageFetchForUser.this);
        CmdExecRes result = CmdMgr.GetUserPics(userId, fetchType, size);
        if(result.mError != CommunicationError.CE_ERROR_NO_ERROR) {
            return -1;
        }

        return 0;
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
