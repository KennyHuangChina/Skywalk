package com.kjs.skywalk.app_android.Server;

import android.content.Context;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;

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

    public interface UploadFinished {
        void onUploadFinished(int current, int total, String image, boolean succeed);
    }

    public class UploadImageInfo{
        public final int UPLOAD_TYPE_IDCARD = PIC_TYPE_MAJOR_User + PIC_TYPE_SUB_USER_IDCard;
        public final int UPLOAD_TYPE_OWNER_CERT = PIC_TYPE_MAJOR_House + PIC_TYPE_SUB_HOUSE_OwnershipCert;

        public String image = "";
        public int type = 0;
    }

    ImageUpload(Context context, UploadFinished listener) {
        mContext = context;
        mListener = listener;
    }

    boolean waitResult(int nTimeoutMs) {
        if (nTimeoutMs < 100)
            return mResultGot;

        int wait_count = 0;
        while (wait_count < nTimeoutMs / 100) {
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

    public int upload(final ArrayList<UploadImageInfo> imageList) {
        if(imageList.size() == 0) {
            return -1;
        }

        new Thread(){
            public void run(){
                for(UploadImageInfo info : imageList) {
                    mResultGot = false;

                    CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {

                        @Override
                        public void onCommandFinished(int i, IApiResults.ICommon iCommon) {

                        }
                    };

                    CommandManager manager = CommandManager.getCmdMgrInstance(mContext, listener, ImageUpload.this);
                    waitResult(mTimeout);
                }
            }
        }.start();

        return 0;
    }

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
