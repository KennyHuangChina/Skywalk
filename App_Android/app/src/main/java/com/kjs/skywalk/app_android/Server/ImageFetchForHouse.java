package com.kjs.skywalk.app_android.Server;

import android.content.Context;
import android.util.Log;

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
    private String TAG = getClass().getSimpleName();

    private Context mContext = null;
    private HouseFetchFinished mListener = null;
    private int mTimeout = 10000;
    private boolean mResultGot = false;
    private boolean mFailed = false;

    ArrayList<ClassDefine.PictureInfo> mList = new ArrayList<>();

    public interface HouseFetchFinished {
        void onHouseImageFetched(ArrayList<ClassDefine.PictureInfo> list);
    }

    public ImageFetchForHouse(Context context, HouseFetchFinished listener) {
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
        String Fn = "fetch";
        mList.clear();
        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                String Fn = "onCommandFinished";
                kjsLogUtil.i(TAG, Fn, "fetchType: " + fetchType + ", listener: " + this + ", command: " + command + ", iResult: " + iResult);
                if (null == iResult) {
                    kjsLogUtil.i(TAG, Fn, "result is null");
                    mListener.onHouseImageFetched(mList);
                    return;
                }

                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.i("Error occurred during fetch picture from server");
                    mListener.onHouseImageFetched(mList);
                    return;
                }

                if (command == CMD_GET_HOUSE_PIC_LIST) {
                    kjsLogUtil.d(TAG, Fn, "iResult: " + iResult.DebugString());
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

                    kjsLogUtil.i("###### Picture list size: " + list.size() + " type: " + fetchType);
                    mListener.onHouseImageFetched(mList);
                }
            }
        };

        kjsLogUtil.d(TAG, Fn, "listener: " + listener + ", ImageFetchForHouse.this: " + ImageFetchForHouse.this);
        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(mContext, listener, ImageFetchForHouse.this);
        kjsLogUtil.d(TAG, Fn, "houseId: " + houseId + ", fetchType: " + fetchType + ", size: " + size);
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