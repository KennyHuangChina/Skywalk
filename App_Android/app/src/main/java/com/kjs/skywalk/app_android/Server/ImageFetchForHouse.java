package com.kjs.skywalk.app_android.Server;

import android.content.Context;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_DEL_PICTURE;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_PIC_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_SIZE_ALL;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_SUB_USER_IDCard;

/**
 * Created by admin on 2017/9/30.
 */

public class ImageFetchForHouse implements CommunicationInterface.CIProgressListener{

    private Context mContext = null;
    private FetchFinished mListener = null;
    private int mTimeout = 10000;
    private boolean mResultGot = false;
    private boolean mFailed = false;

    ArrayList<ClassDefine.PictureInfo> mList = new ArrayList<>();

    public interface FetchFinished {
        void onImageFetched(ArrayList<ClassDefine.PictureInfo> list);
    }

    public ImageFetchForHouse(Context context, FetchFinished listener) {
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

    public int fetch(int houseId, final int fetchType, int size) {
        mList.clear();
        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    mListener.onImageFetched(mList);
                    return;
                }

                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    mListener.onImageFetched(mList);
                    return;
                }

                if (command == CMD_GET_HOUSE_PIC_LIST) {
                    IApiResults.IResultList res = (IApiResults.IResultList)iResult;
                    ArrayList<Object> list = res.GetList();
                    for(Object obj : list) {
                        IApiResults.IHousePicInfo info = (IApiResults.IHousePicInfo)obj;
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

                    mListener.onImageFetched(mList);
                }
            }
        };

        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(mContext, listener, ImageFetchForHouse.this);
        int result = CmdMgr.GetHousePics(houseId, fetchType, size);
        if(result != CommunicationError.CE_ERROR_NO_ERROR) {
            return -1;
        }

        return 0;
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
