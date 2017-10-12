package com.kjs.skywalk.app_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ArrayRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kjs.skywalk.app_android.Server.ImageUpload;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;

import static com.kjs.skywalk.app_android.Server.ImageUpload.UPLOAD_RESULT_INTERRUPT;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_PIC_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_MAJOR_House;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_MAJOR_User;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_SUB_HOUSE_OwnershipCert;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_SUB_USER_IDCard;

public class Activity_fangyuan_renzheng_customer extends SKBaseActivity implements ImageUpload.UploadFinished
{
    private int mPhotoPickerHostId;
    private ArrayList<String> mCertList = new ArrayList<>();
    private ArrayList<String> mIdList = new ArrayList<>();
    private PopupWindowWaitingUnclickable mWaitingWindow = null;
    private RelativeLayout mContainer = null;
    private int mLandlordId = 0;

    private ArrayList<Integer> mCertPictureIdList = new ArrayList<>();
    private ArrayList<Integer> mIdCardPictureIdList = new ArrayList<>();

    private final int MSG_UPLOAD_ALL_DONE = 0;
    private final int MSG_UPLOAD_FINISHED_WITH_ERROR = 1;
    private final int MSG_GET_CERT_PICTURES_ID_DONE = 2;
    private final int MSG_GET_IDCARD_PICTURES_ID_DONE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__fangyuan_renzheng_customer);

        mContainer = (RelativeLayout)findViewById(R.id.activity_container);

        getHouseInfo();

        getCertPicturesId();
        getIdCardPicturesId();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                onPhotoPickerReturn(photos);
            }
        }
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.textViewBack: {
                finish();
                break;
            }
            case R.id.tv_upload_pic: {
                upload();
                break;
            }
        }
    }

    private void upload() {
        ImageUpload imageUpload = new ImageUpload(this, this);
        ArrayList<ImageUpload.UploadImageInfo> list = new ArrayList<>();
        for(String path : mCertList) {
            ImageUpload.UploadImageInfo info = new ImageUpload.UploadImageInfo();
            info.type = ImageUpload.UPLOAD_TYPE_OWNER_CERT;
            info.image = path;
            info.houseId = mHouseId;
            list.add(info);
        }

        for(String path : mIdList) {
            ImageUpload.UploadImageInfo info = new ImageUpload.UploadImageInfo();
            info.type = ImageUpload.UPLOAD_TYPE_IDCARD;
            info.image = path;
            info.userId = mLandlordId;
            list.add(info);
        }

        if(imageUpload.upload(list) != 0) {
            commonFun.showToast_info(this, mContainer, "上传失败");
        } else {
            showWaiting(true);
        }
    }

    public void onPhotoPickerClicked(View v) {
        switch (v.getId()) {
            case R.id.iv_photopicker_hpc: {
                mCertList.clear();
                break;
            }
            case R.id.iv_photopicker_idcard: {
                mIdList.clear();
                break;
            }
            default:
                return;
        }

        startPhotoPickerActivity(v);
    }

    private void startPhotoPickerActivity(View host_view) {
        mPhotoPickerHostId = host_view.getId();
        PhotoPicker.builder().setPhotoCount(2).start(Activity_fangyuan_renzheng_customer.this);
    }

    private void onPhotoPickerReturn(List<String> photos) {
        switch (mPhotoPickerHostId) {
            case R.id.iv_photopicker_hpc:
            {
                ImageView ivZhengjian1 = (ImageView)findViewById(R.id.imageZhengjian1);
                ImageView ivZhengjian2 = (ImageView)findViewById(R.id.imageZhengjian2);
                ivZhengjian1.setVisibility(View.INVISIBLE);
                ivZhengjian2.setVisibility(View.INVISIBLE);

                int index = 0;
                for (String path : photos) {
                    if (index == 0) {
                        ivZhengjian1.setImageBitmap(commonFun.getScaleBitmapFromLocal(path, 320, 240));
                        ivZhengjian1.setVisibility(View.VISIBLE);
                    }
                    if (index == 1) {
                        ivZhengjian2.setImageBitmap(commonFun.getScaleBitmapFromLocal(path, 320, 240));
                        ivZhengjian2.setVisibility(View.VISIBLE);
                    }
                    index++;

                    mCertList.add(path);
                }

                break;
            }

            case R.id.iv_photopicker_idcard:
            {
                ImageView ivIdCard1 = (ImageView)findViewById(R.id.imageIdCard1);
                ImageView ivIdCard2 = (ImageView)findViewById(R.id.imageIdCard2);
                ivIdCard1.setVisibility(View.INVISIBLE);
                ivIdCard2.setVisibility(View.INVISIBLE);

                int index = 0;
                for (String path : photos) {
                    if (index == 0) {
                        ivIdCard1.setImageBitmap(commonFun.getScaleBitmapFromLocal(path, 320, 240));
                        ivIdCard1.setVisibility(View.VISIBLE);
                    }
                    if (index == 1) {
                        ivIdCard2.setImageBitmap(commonFun.getScaleBitmapFromLocal(path, 320, 240));
                        ivIdCard2.setVisibility(View.VISIBLE);
                    }
                    index++;

                    mIdList.add(path);
                }

                break;
            }
        }
    }

    private int getIdCardPicturesId() {
        mIdCardPictureIdList.clear();

        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }

                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    commonFun.showToast_info(getApplicationContext(), mContainer, "获取图片失败" + iResult.GetErrDesc());
                    return;
                }

                if (command == CMD_GET_HOUSE_PIC_LIST) {
                    IApiResults.IResultList res = (IApiResults.IResultList)iResult;
                    ArrayList<Object> list = res.GetList();
                    for(Object obj : list) {
                        IApiResults.IHousePicInfo info = (IApiResults.IHousePicInfo)obj;
                        mIdCardPictureIdList.add(info.GetId());
                    }
                    mHandler.sendEmptyMessageDelayed(MSG_GET_IDCARD_PICTURES_ID_DONE, 0);
                }
            }
        };

        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this, listener, this);
        int result = CmdMgr.GetHousePics(mHouseId, PIC_TYPE_SUB_HOUSE_OwnershipCert);
        if(result != CommunicationError.CE_ERROR_NO_ERROR) {
            commonFun.showToast_info(getApplicationContext(), mContainer, "获取图片失败");
            return -1;
        }

        return 0;
    }

    private int getCertPicturesId() {
        mCertPictureIdList.clear();

        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }

                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    commonFun.showToast_info(getApplicationContext(), mContainer, "获取图片失败" + iResult.GetErrDesc());
                    return;
                }

                if (command == CMD_GET_HOUSE_PIC_LIST) {
                    IApiResults.IResultList res = (IApiResults.IResultList)iResult;
                    ArrayList<Object> list = res.GetList();
                    for(Object obj : list) {
                        IApiResults.IHousePicInfo info = (IApiResults.IHousePicInfo)obj;
                        mCertPictureIdList.add(info.GetId());
                    }

                    mHandler.sendEmptyMessageDelayed(MSG_GET_CERT_PICTURES_ID_DONE, 0);
                }
            }
        };

        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this, listener, this);
        int result = CmdMgr.GetHousePics(mHouseId, PIC_TYPE_SUB_HOUSE_OwnershipCert);
        if(result != CommunicationError.CE_ERROR_NO_ERROR) {
            commonFun.showToast_info(getApplicationContext(), mContainer, "获取图片失败");
            return -1;
        }

        return 0;
    }

    private int getHouseInfo() {
        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }

                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    commonFun.showToast_info(getApplicationContext(), mContainer, "获取房屋信息失败:" + iResult.GetErrDesc());
                    return;
                }

                if (command == CMD_GET_HOUSE_INFO) {
                    IApiResults.IGetHouseInfo info = (IApiResults.IGetHouseInfo) iResult;
                    mLandlordId = info.Landlord();
                }
            }
        };

        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this, listener, this);
        int result = CmdMgr.GetHouseInfo(mHouseId, true);
        if(result != CommunicationError.CE_ERROR_NO_ERROR) {
            commonFun.showToast_info(getApplicationContext(), mContainer, "获取房屋信息失败");
            return -1;
        }

        return 0;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPLOAD_ALL_DONE:
                    showWaiting(false);
                    break;
                case MSG_GET_CERT_PICTURES_ID_DONE:
                    break;
                case MSG_GET_IDCARD_PICTURES_ID_DONE:
                    break;
            }

        }
    };

    private void showWaiting(final boolean show) {
        if (mWaitingWindow == null) {
            mWaitingWindow = new PopupWindowWaitingUnclickable(this, mActScreenWidth, mActScreenHeight);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    mWaitingWindow.show(mContainer);
                } else {
                    mWaitingWindow.hide();
                }
            }
        });
    }

    @Override
    public void onUploadStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mWaitingWindow != null) {
                    String text = "开始上传，请稍候...";
                    mWaitingWindow.updateProgressText(text);
                }
            }
        });
    }

    @Override
    public void onUploadProgress(final int current, final int total, String image, int result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mWaitingWindow != null) {
                    String text = "正在上传照片 ... " + current + "/" + total;
                    mWaitingWindow.updateProgressText(text);
                }
            }
        });
        if(result == UPLOAD_RESULT_INTERRUPT) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mWaitingWindow != null) {
                        String text = "上传失败, 请重新上传";
                        mWaitingWindow.updateProgressText(text);
                    }
                }
            });
            mHandler.sendEmptyMessageDelayed(MSG_UPLOAD_FINISHED_WITH_ERROR, 1000);
        }
    }

    @Override
    public void onUploadEnd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mWaitingWindow != null) {
                    String text = "上传完成";
                    mWaitingWindow.updateProgressText(text);
                    mHandler.sendEmptyMessageDelayed(MSG_UPLOAD_ALL_DONE, 1000);
                }
            }
        });
    }
}
