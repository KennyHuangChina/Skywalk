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
import com.kjs.skywalk.communicationlibrary.IApiArgs;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_DEL_PICTURE;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_PIC_LIST;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.*;


/**
 * Created by admin on 2017/9/30.
 */

public class ImageFetchForHouse implements CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{
    private String TAG = getClass().getSimpleName();

    private Context             mContext    = null;
    private HouseFetchFinished  mListener   = null;
    private int                 mTimeout    = 10000;
    private boolean             mResultGot  = false;
    private boolean             mFailed     = false;
    private CmdExecRes          mCmd        = null;

    ArrayList<ClassDefine.PictureInfo> mList = new ArrayList<>();

    public ImageFetchForHouse(Context context, HouseFetchFinished listener) {
        mContext    = context;
        mListener   = listener;

        // Register Listener
        CommandManager.getCmdMgrInstance(mContext).Register(this, this);
    }

    @Override
    protected void finalize() throws Throwable {
        // TODO: Kenny, 这里不会立刻被调用到，要等到 GC 的时候才会被调用，因此 Listener 不会被立刻 Unregister
        // Unregister Listener
        CommandManager.getCmdMgrInstance(mContext).Unregister(this, this);
        super.finalize();
    }

    @Override
    public void onCommandFinished(final int command, final int cmdSeq, IApiResults.ICommon iResult) {
        if (null == iResult) {
            kjsLogUtil.w("result is null");
            return;
        }
        // Filter out all other commands not send by us
        synchronized (this) {
            if (null == mCmd || mCmd.mCmdSeq == command) {  // result is not we wanted
                return;
            }
        }
        kjsLogUtil.i(String.format("command: %d(%s), seq: %d %s", command, CommunicationInterface.CmdID.GetCmdDesc(command), cmdSeq, iResult.DebugString()));

        int nErrCode = iResult.GetErrCode();
        if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
            kjsLogUtil.e("Error occurred during fetch picture from server");
//            kjsLogUtil.e("Command:" + command + " finished with error: " + nErrCode);
            if (CommunicationInterface.CmdRes.CMD_RES_NOT_LOGIN == nErrCode || CommunicationError.IsNetworkError(nErrCode)) {
                kjsLogUtil.d("user not log in, reflash layout");
            }
            mListener.onHouseImageFetched(mList);
            return;
        }

        if (command == CMD_GET_HOUSE_PIC_LIST) {
            if (null != mCmd.mArgs && mCmd.mArgs.isEqual(iResult.GetArgs())) {
                IApiArgs.IArgsGetXPicLst args = (IApiArgs.IArgsGetXPicLst)iResult.GetArgs();
                IApiResults.IResultList  res  = (IApiResults.IResultList)iResult;

                ArrayList<Object> list = res.GetList();
                kjsLogUtil.d("###### Picture list size: " + list.size() + " type: " + args.getSubType());
                for(Object obj : list) {
                    IApiResults.IPicInfo info = (IApiResults.IPicInfo)obj;
                    IApiResults.IPicUrls urls = (IApiResults.IPicUrls)obj;

                    ClassDefine.PictureInfo picInfo = new ClassDefine.PictureInfo();
                    picInfo.mId             = info.GetId();
                    picInfo.largePicUrl     = urls.GetLargePicture();
                    picInfo.middlePicUrl    = urls.GetMiddlePicture();
                    picInfo.smallPicUrl     = urls.GetSmallPicture();
                    picInfo.mType           = args.getSubType(); // fetchType;

                    mList.add(picInfo);
                    picInfo.print();
                }
                mListener.onHouseImageFetched(mList);
            }
        }
    }

    public interface HouseFetchFinished {
        void onHouseImageFetched(ArrayList<ClassDefine.PictureInfo> list);
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

        kjsLogUtil.d("houseId: " + houseId + ", fetchType: " + fetchType + ", size: " + size);
        CmdExecRes result = CommandManager.getCmdMgrInstance(mContext).GetHousePics(houseId, fetchType, size);
        if (CE_ERROR_NO_ERROR != result.mError) {
            kjsLogUtil.e(String.format("Fail to send commnd GetHousePics, error: %d", result.mError));
            return -1;
        }

        synchronized (this) {
            mCmd = result;
        }
        return 0;
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
