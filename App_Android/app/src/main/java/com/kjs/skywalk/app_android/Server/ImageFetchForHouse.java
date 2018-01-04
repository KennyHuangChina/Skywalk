package com.kjs.skywalk.app_android.Server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.database.ImageCacheDBOperator;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CmdExecRes;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiArgs;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import me.iwf.photopicker.utils.FileUtils;

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

    private ImageCacheDBOperator mDB        = null;

    ArrayList<ClassDefine.PictureInfo>  mList       = new ArrayList<>();
    ArrayList<CmdExecRes>               mCmdList    = new ArrayList<CmdExecRes>();

    public ImageFetchForHouse(Context context) {
        mContext    = context;
        mDB = ImageCacheDBOperator.getOperator(mContext);
        // Register Listener
        CommandManager.getCmdMgrInstance(mContext).Register(this, this);
    }

    public void registerListener(HouseFetchFinished listener) {
        mListener = listener;
    }

    public  void close() {
        mListener = null;
        CommandManager.getCmdMgrInstance(mContext).Unregister(this, this);
    }

    @Override
    protected void finalize() throws Throwable {
        // 这里不会立刻被调用到，要等到 GC 的时候才会被调用，因此 Listener 不会被立刻 Unregister
        close();
        super.finalize();
    }

    @Override
    public void onCommandFinished(final int command, final int cmdSeq, IApiResults.ICommon iResult) {
        if (null == iResult) {
            kjsLogUtil.w("result is null");
            return;
        }
        // Filter out all other commands not send by us
        CmdExecRes cmd = commonFun.RetrieveCommand(mCmdList, cmdSeq);
        if (null == cmd) {
            return;
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
                    picInfo.mCheckSum       = info.GetChecksum();

                    mList.add(picInfo);
//                    picInfo.print();
                    download(picInfo.smallPicUrl);
                }
                if(mListener != null) {
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

    private void saveToFile(File source, File target) {
        try {
            int bytesum = 0;
            int byteread = 0;
            if (source.exists()) {
                InputStream inStream = new FileInputStream(source); //读入原文件
                FileOutputStream fs = new FileOutputStream(target);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            kjsLogUtil.e("save file failure.");
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void download(final String url) {
        new AsyncTask<Void, Integer, File>() {
            @Override
            protected File doInBackground(Void... params) {
                File targetFile = null;
                try {
                    FutureTarget<File> future = Glide
                            .with(mContext)
                            .load(url)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                    File file = future.get();
                    File cacheDir = new File("/sdcard/skywalk");//mContext.getCacheDir();
                    kjsLogUtil.i("cache dir: " + cacheDir);
                    File imageDir = new File(cacheDir,"images");
                    if (!imageDir.exists()) {
                        imageDir.mkdirs();
                    }
                    String fileName = url;
                    int index = fileName.lastIndexOf("/");
                    fileName = fileName.substring(index + 1);
                    kjsLogUtil.i("file Name: " + fileName);
                    targetFile = new File(imageDir, fileName);
                    saveToFile(file, targetFile);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                return targetFile;
            }

            @Override
            protected void onPostExecute(File file) {
                kjsLogUtil.i("onPostExecute: " + file.getPath());
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }
        }.execute();
    }

    public int fetch(int houseId, final int fetchType, int size) {
        mList.clear();

        kjsLogUtil.d("houseId: " + houseId + ", fetchType: " + fetchType + ", size: " + size);
        CmdExecRes result = CommandManager.getCmdMgrInstance(mContext).GetHousePics(houseId, fetchType, size);
        if (CE_ERROR_NO_ERROR != result.mError) {
            kjsLogUtil.e(String.format("Fail to send commnd GetHousePics, error: %d", result.mError));
            return -1;
        }
        kjsLogUtil.d(String.format("store command seq: %d", result.mCmdSeq));
        commonFun.StoreCommand(mCmdList, result);
        return 0;
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
