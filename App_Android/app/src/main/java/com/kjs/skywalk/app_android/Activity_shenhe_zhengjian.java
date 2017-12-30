package com.kjs.skywalk.app_android;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kjs.skywalk.app_android.Server.ImageFetchForHouse;
import com.kjs.skywalk.app_android.Server.ImageFetchForUser;

import com.kjs.skywalk.communicationlibrary.CmdExecRes;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPreview;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_CERTIFY_HOUSE;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.*;

public class Activity_shenhe_zhengjian extends SKBaseActivity implements ImageFetchForHouse.HouseFetchFinished,
        ImageFetchForUser.UserFetchFinished {

    private ArrayList<ClassDefine.PictureInfo> mPictureList = new ArrayList<>();
    private RelativeLayout mContainer = null;
    private int mLandlordId = 0;

    private ImageView mCertImage1 = null;
    private ImageView mCertImage2 = null;
    private ImageView mIdCardImage1 = null;
    private ImageView mIdCardImage2 = null;

    private ClassDefine.ConfirmDialog mConfirmDialog = null;
    private boolean mPassed = false;

    private int mCurrentPreviewIndex = 0;

    private ImageFetchForHouse imageFetchHouse = null;

    private final int MSG_VERIFICATION_DONE = 0;
    private final int MSG_GET_PICTURES_DONE = 0x100;
    private final int MSG_GET_HOUSE_INFO_DONE = 0x300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__shenhe_zhengjian);

        mContainer = (RelativeLayout)findViewById(R.id.activity_container);
        mCertImage1 = (ImageView)findViewById(R.id.imageZhengjian1);
        mCertImage2 = (ImageView)findViewById(R.id.imageZhengjian2);

        mIdCardImage1 = (ImageView)findViewById(R.id.imageIdCard1);
        mIdCardImage2 = (ImageView)findViewById(R.id.imageIdCard2);

        getHouseInfo();
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title: {
                finish();
                break;
            }
            case R.id.tv_not_pass: {
                doPass(false);
                break;
            }
            case R.id.tv_pass: {
                doPass(true);
                break;
            }
            case R.id.imageZhengjian1:
            case R.id.imageZhengjian2:
            case R.id.imageIdCard1:
            case R.id.imageIdCard2:
                imagePreview(0);
                break;
        }
    }

    private void imagePreview(int current) {
        ArrayList<String> images = new ArrayList<>();
        for(ClassDefine.PictureInfo info : mPictureList) {
            images.add(info.smallPicUrl);
        }

        PhotoPreview.builder()
                .setPhotos(images)
                .setCurrentItem(0)
                .start(this);
    }

    private void doPass(boolean bPass) {
        EditText etAdvise = (EditText)findViewById(R.id.editTextAdvise);
        String textAdvise = etAdvise.getText().toString();
        if(textAdvise == null || textAdvise.isEmpty()) {
            commonFun.showToast_info(this, mContainer, "请填写审核意见");
            return;
        }

        commonFun.hideSoftKeyboard(this, mContainer);

        if(mConfirmDialog == null) {
            mConfirmDialog = new ClassDefine.ConfirmDialog(this);
        }

        mPassed = bPass;

        DialogInterface.OnDismissListener listener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mConfirmDialog.getConfirmed()) {
                    doCertificateConfirm();
                }
            }
        };
        if(!bPass) {
            mConfirmDialog.setConfirmString("是否确认未通过审核？");
        } else {
            mConfirmDialog.setConfirmString("是否确认通过审核？");
        }
        mConfirmDialog.setDismissListener(listener);
        mConfirmDialog.showDialog(true);
    }

    private void doCertificateConfirm() {
        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }

                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    commonFun.showToast_info(getApplicationContext(), mContainer, "提交房屋认证失败:" + iResult.GetErrDesc());
                    return;
                }

                if (command == CMD_CERTIFY_HOUSE) {
                    mHandler.sendEmptyMessageDelayed(MSG_VERIFICATION_DONE, 1000);
                }
            }
        };

        EditText etAdvise = (EditText)findViewById(R.id.editTextAdvise);
        String textAdvise = etAdvise.getText().toString();
        CmdExecRes result = CommandManager.getCmdMgrInstance(this/*, listener, this*/).CertificateHouse(mHouseId, mPassed, textAdvise);
        if (result.mError != CommunicationError.CE_ERROR_NO_ERROR) {
            commonFun.showToast_info(getApplicationContext(), mContainer, "提交房屋认证失败");
        }

        commonFun.showToast_info(getApplicationContext(), mContainer, "提交房屋认证成功");
    }

    private void refreshPicturesByType(int type) {
        int index = 0;
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.no_picture);
        if(type == PIC_TYPE_SUB_HOUSE_OwnershipCert) {
            mCertImage1.setImageDrawable(drawable);
            mCertImage1.setTag(null);
            mCertImage2.setImageDrawable(drawable);
            mCertImage2.setTag(null);

            for(ClassDefine.PictureInfo info : mPictureList) {
                if (info.mType == type) {
                    if (index == 0) {
                        commonFun.displayImageByURL(this, info.smallPicUrl, mCertImage1);
                        mCertImage1.setTag(info.mId);
                        index++;
                    } else if (index == 1) {
                        commonFun.displayImageByURL(this, info.smallPicUrl, mCertImage2);
                        mCertImage2.setTag(info.mId);
                        index = 0x9999;
                    }
                }
            }
        } else if(type == PIC_TYPE_SUB_USER_IDCard) {
            mIdCardImage1.setImageDrawable(drawable);
            mIdCardImage1.setTag(null);
            mIdCardImage2.setImageDrawable(drawable);
            mIdCardImage2.setTag(null);

            for(ClassDefine.PictureInfo info : mPictureList) {
                if (info.mType == type) {
                    if (index == 0) {
                        commonFun.displayImageByURL(this, info.smallPicUrl, mIdCardImage1);
                        mIdCardImage1.setTag(info.mId);
                        index++;
                    } else if (index == 1) {
                        commonFun.displayImageByURL(this, info.smallPicUrl, mIdCardImage2);
                        mIdCardImage2.setTag(info.mId);
                        index = 0x9999;
                    }
                }
            }
        }
    }

    private void showPictures() {
        refreshPicturesByType(PIC_TYPE_SUB_HOUSE_OwnershipCert);
        refreshPicturesByType(PIC_TYPE_SUB_USER_IDCard);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_PICTURES_DONE:
                    showPictures();
                    imageFetchHouse.close();
                    break;
                case MSG_GET_HOUSE_INFO_DONE:
                    getPictures();
                    break;
                case MSG_VERIFICATION_DONE:
                    finish();
                    break;
            }
        }
    };

    private void getPictures() {
        mPictureList.clear();

        imageFetchHouse = new ImageFetchForHouse(this);
        imageFetchHouse.registerListener(this);
        imageFetchHouse.fetch(mHouseId, PIC_TYPE_SUB_HOUSE_OwnershipCert, PIC_SIZE_ALL);

        ImageFetchForUser fetchForUser = new ImageFetchForUser(this, this);
        fetchForUser.fetch(mLandlordId, PIC_TYPE_SUB_USER_IDCard, PIC_SIZE_ALL);
    }

    private int getHouseInfo() {
        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
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
                    kjsLogUtil.i("User Id: " + mLandlordId);
                    if(mLandlordId != 0) { //取得landlord id以后才可以取身份证图片
                        mHandler.sendEmptyMessageDelayed(MSG_GET_HOUSE_INFO_DONE, 10);
                    }
                }
            }
        };

        CmdExecRes result = CommandManager.getCmdMgrInstance(this/*, listener, this*/).GetHouseInfo(mHouseId, true);
        if (result.mError != CommunicationError.CE_ERROR_NO_ERROR) {
            commonFun.showToast_info(getApplicationContext(), mContainer, "获取房屋信息失败");
            return -1;
        }

        return 0;
    }

    @Override
    public void onHouseImageFetched(ArrayList<ClassDefine.PictureInfo> list) {
        for(ClassDefine.PictureInfo info : list) {
            mPictureList.add(info);
        }
        mHandler.sendEmptyMessageDelayed(MSG_GET_PICTURES_DONE, 10);
    }

    @Override
    public void onUserImageFetched(ArrayList<ClassDefine.PictureInfo> list) {
        for(ClassDefine.PictureInfo info : list) {
            mPictureList.add(info);
        }
        mHandler.sendEmptyMessageDelayed(MSG_GET_PICTURES_DONE, 10);
    }
}
